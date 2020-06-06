package com.techstack.batch.job.processor.validate;

import com.techstack.batch.domain.Customer;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

public class CustomerValidator implements Validator<Customer> {

    /**
     * In this business use case, if the Customer FirstName starts with "A"
     * we would throw an exception.
     */
    @Override
    public void validate(Customer value) throws ValidationException {
        if(value.getFirstName().startsWith("A")) {
            throw new ValidationException("First names that begin with A are invalid: " + value);
        }
    }
}
