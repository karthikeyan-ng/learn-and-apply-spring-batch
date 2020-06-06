package com.techstack.batch.job.write.multifiles;

import com.techstack.batch.domain.Customer;
import com.techstack.batch.job.read.db.CustomerRowMapper;
import com.techstack.batch.job.write.flatfile.CustomerLineAggregator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JobConfigurationToWriteContentToMultipleFileFormat {

    public final StepBuilderFactory stepBuilderFactory;

    public final JobBuilderFactory jobBuilderFactory;

    public final DataSource dataSource;

    @Bean
    public JdbcPagingItemReader<Customer> pagingItemReader() {
        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(this.dataSource);
        reader.setFetchSize(10);
        reader.setRowMapper(new CustomerRowMapper());

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, firstName, lastName, birthdate");
        queryProvider.setFromClause("from customer");

        Map<String, Order> sortKeys = new HashMap<>(1);

        sortKeys.put("id", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);

        return reader;
    }

    @Bean
    public FlatFileItemWriter<Customer> jsonItemWriter() throws Exception {
        FlatFileItemWriter<Customer> itemWriter = new FlatFileItemWriter<>();

        itemWriter.setLineAggregator(new CustomerLineAggregator());
        String customerOutputPath = File.createTempFile("customerOutput", ".out").getAbsolutePath();
        System.out.println(">> Output Path: " + customerOutputPath);
        itemWriter.setResource(new FileSystemResource(customerOutputPath));
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    @Bean
    public StaxEventItemWriter<Customer> xmlItemWriter() throws Exception {

        XStreamMarshaller marshaller = new XStreamMarshaller();

        Map<String, Class> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);

        marshaller.setAliases(aliases);

        StaxEventItemWriter<Customer> itemWriter = new StaxEventItemWriter<>();

        itemWriter.setRootTagName("customers");
        itemWriter.setMarshaller(marshaller);
        String customerOutputPath = File.createTempFile("customerOutput", ".xml").getAbsolutePath();
        System.out.println(">> Output Path: " + customerOutputPath);
        itemWriter.setResource(new FileSystemResource(customerOutputPath));

        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    /**
     * Scenario 1: In this case, we have configure that each Item should be
     * written in both XML and JSON format.
     *
     * For this we are using CompositeItemWrite which takes list of ItemWrite of
     * different implementations
     */
	@Bean
	public CompositeItemWriter<Customer> itemWriter() throws Exception {
		List<ItemWriter<? super Customer>> writers = new ArrayList<>(2);

		writers.add(xmlItemWriter());
		writers.add(jsonItemWriter());

		CompositeItemWriter<Customer> itemWriter = new CompositeItemWriter<>();

		itemWriter.setDelegates(writers);
		itemWriter.afterPropertiesSet();

		return itemWriter;
	}

    /**
     * Scenario 2: In this case, we have write content based on classification style.
     * For this we are using ClassifierCompositeItemWriter which uses Classifier object
     * to write content based on classification logic.
     */
//    @Bean
//    public ClassifierCompositeItemWriter<Customer> itemWriter() throws Exception {
//        ClassifierCompositeItemWriter<Customer> itemWriter = new ClassifierCompositeItemWriter<>();
//
//        itemWriter.setClassifier(new CustomerClassifier(xmlItemWriter(), jsonItemWriter()));
//
//        return itemWriter;
//    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .<Customer, Customer>chunk(10)
                .reader(pagingItemReader())
                .writer(itemWriter())

                //When executing Scenario 2, use this below two stream
//                .stream(xmlItemWriter())
//                .stream(jsonItemWriter())
                .build();
    }

    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("job")
                .start(step1())
                .build();
    }
}
