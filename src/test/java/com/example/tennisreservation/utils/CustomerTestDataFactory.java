package com.example.tennisreservation.utils;

import com.example.tennisreservation.entity.Customer;

public final class CustomerTestDataFactory {

    public static final String PHONE = "+420700111222";
    public static final String NAME = "Alice";

    private CustomerTestDataFactory() {}

    public static Customer customer() {
        return customer(PHONE, NAME);
    }

    public static Customer customer(String phoneNumber, String name) {
        Customer customer = new Customer();
        customer.setPhoneNumber(phoneNumber);
        customer.setName(name);
        return customer;
    }
}
