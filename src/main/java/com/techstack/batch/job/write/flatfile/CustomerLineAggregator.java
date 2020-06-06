package com.techstack.batch.job.write.flatfile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techstack.batch.domain.Customer;
import org.springframework.batch.item.file.transform.LineAggregator;

public class CustomerLineAggregator implements LineAggregator<Customer> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String aggregate(Customer item) {
        try {
            return objectMapper.writeValueAsString(item);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize Customer", e);
        }
    }
}
