package com.techstack.batch.domain;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ToString
@RequiredArgsConstructor
public class Customer {

    private final long id;

    private final String firstName;

    private final String lastName;

    private final Date birthdate;
}
