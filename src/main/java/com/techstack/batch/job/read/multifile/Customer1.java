package com.techstack.batch.job.read.multifile;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

import java.util.Date;

@RequiredArgsConstructor
public class Customer1 implements ResourceAware {

    private final long id;

    private final String firstName;

    private final String lastName;

    private final Date birthdate;

    private Resource resource;

    /**
     * Implementing this ResourceAware interface will helpful to know
     * from which file we are reading the record.
     */
    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthdate=" + birthdate +
                ", from " + resource.getDescription() +
                '}';
    }
}
