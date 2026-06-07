package com.example.tennisreservation.dao;

import com.example.tennisreservation.entity.Customer;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDao extends BaseDao<Customer> {

    public CustomerDao() {
        super(Customer.class);
    }

    public Optional<Customer> findByPhoneNumber(String phoneNumber) {
        return em.createQuery(
                        "SELECT c FROM Customer c WHERE c.phoneNumber = :phone", Customer.class)
                .setParameter("phone", phoneNumber)
                .getResultList()
                .stream()
                .findFirst();
    }
}
