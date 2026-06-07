package com.example.tennisreservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.tennisreservation.entity.Customer;
import com.example.tennisreservation.utils.CustomerTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(CustomerDao.class)
@ActiveProfiles("test")
class CustomerDaoTest {

    @Autowired
    private CustomerDao dao;
    @Autowired
    private TestEntityManager tem;

    @Test
    void save_newEntity_persistsAndAssignsId() {
        Customer saved = dao.save(CustomerTestDataFactory.customer());

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void save_existingEntity_updatesIt() {
        Customer saved = dao.save(CustomerTestDataFactory.customer());
        flushAndClear();

        saved.setName("Alice Smith");
        dao.save(saved);
        flushAndClear();

        assertThat(dao.findById(saved.getId()).orElseThrow().getName()).isEqualTo("Alice Smith");
    }

    @Test
    void findById_existingEntity_returnsIt() {
        Long id = dao.save(CustomerTestDataFactory.customer("+420700111222", "Alice")).getId();
        flushAndClear();

        Customer loaded = dao.findById(id).orElseThrow();
        assertThat(loaded.getPhoneNumber()).isEqualTo("+420700111222");
        assertThat(loaded.getName()).isEqualTo("Alice");
    }

    @Test
    void findById_missingEntity_returnsEmpty() {
        assertThat(dao.findById(999L)).isEmpty();
    }

    @Test
    void findAll_withSavedEntity_returnsIt() {
        Long id = dao.save(CustomerTestDataFactory.customer()).getId();
        flushAndClear();

        assertThat(dao.findAll()).extracting(Customer::getId).containsExactly(id);
    }

    @Test
    void deleteById_existingEntity_softDeletesAndHidesFromQueries() {
        Long id = dao.save(CustomerTestDataFactory.customer()).getId();
        flushAndClear();

        assertThat(dao.deleteById(id)).isTrue();
        flushAndClear();

        assertThat(dao.findById(id)).isEmpty();
        assertThat(dao.findAll()).isEmpty();
    }

    @Test
    void deleteById_missingEntity_returnsFalse() {
        assertThat(dao.deleteById(999L)).isFalse();
    }

    @Test
    void delete_detachedEntity_softDeletesIt() {
        Customer saved = dao.save(CustomerTestDataFactory.customer());
        flushAndClear();

        dao.delete(saved);
        flushAndClear();

        assertThat(dao.findById(saved.getId())).isEmpty();
    }

    @Test
    void findByPhoneNumber_existingPhone_returnsMatch() {
        dao.save(CustomerTestDataFactory.customer("+420700111222", "Alice"));
        flushAndClear();

        assertThat(dao.findByPhoneNumber("+420700111222"))
                .get()
                .extracting(Customer::getName)
                .isEqualTo("Alice");
    }

    @Test
    void findByPhoneNumber_missingPhone_returnsEmpty() {
        assertThat(dao.findByPhoneNumber("+420700000000")).isEmpty();
    }

    private void flushAndClear() {
        tem.flush();
        tem.clear();
    }
}
