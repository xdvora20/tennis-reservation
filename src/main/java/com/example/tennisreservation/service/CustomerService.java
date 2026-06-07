package com.example.tennisreservation.service;

import com.example.tennisreservation.dao.CustomerDao;
import com.example.tennisreservation.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerDao customerDao;

    public Customer getOrCreate(String phoneNumber, String name) {
        return customerDao
                .findByPhoneNumber(phoneNumber)
                .orElseGet(
                        () -> {
                            Customer customer = new Customer();
                            customer.setPhoneNumber(phoneNumber);
                            customer.setName(name);
                            return customerDao.save(customer);
                        });
    }
}
