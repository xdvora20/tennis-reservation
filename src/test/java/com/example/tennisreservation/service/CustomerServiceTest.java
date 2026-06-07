package com.example.tennisreservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tennisreservation.dao.CustomerDao;
import com.example.tennisreservation.entity.Customer;
import com.example.tennisreservation.utils.CustomerTestDataFactory;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock private CustomerDao customerDao;
    @InjectMocks private CustomerService service;

    @Test
    void getOrCreate_existingPhone_returnsExistingKeepingStoredName() {
        Customer existing = CustomerTestDataFactory.customer("+420700111222", "Alice");
        when(customerDao.findByPhoneNumber("+420700111222")).thenReturn(Optional.of(existing));

        Customer result = service.getOrCreate("+420700111222", "Different Name");

        assertThat(result).isSameAs(existing);
        assertThat(result.getName()).isEqualTo("Alice");
        verify(customerDao, never()).save(any());
    }

    @Test
    void getOrCreate_newPhone_createsWithProvidedName() {
        when(customerDao.findByPhoneNumber("+420700999888")).thenReturn(Optional.empty());
        when(customerDao.save(any(Customer.class))).thenAnswer(call -> call.getArgument(0));

        Customer result = service.getOrCreate("+420700999888", "Bob");

        assertThat(result.getPhoneNumber()).isEqualTo("+420700999888");
        assertThat(result.getName()).isEqualTo("Bob");
        verify(customerDao).save(any(Customer.class));
    }
}
