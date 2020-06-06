package com.techstack.batch.job.processor.filter;

import com.techstack.batch.domain.Customer;
import org.springframework.batch.item.ItemProcessor;

public class FilteringItemProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer item) throws Exception {

        if(item.getId() % 2 == 0) { //skip EVEN ID items
            return null;
        }
        else {
            return item;
        }
    }
}
