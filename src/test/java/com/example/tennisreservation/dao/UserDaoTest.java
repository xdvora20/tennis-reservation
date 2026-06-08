package com.example.tennisreservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.tennisreservation.entity.Role;
import com.example.tennisreservation.entity.User;
import com.example.tennisreservation.utils.UserTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(UserDao.class)
@ActiveProfiles("test")
class UserDaoTest {

    @Autowired
    private UserDao dao;
    @Autowired
    private TestEntityManager tem;

    @Test
    void save_newEntity_persistsAndAssignsId() {
        User saved = dao.save(UserTestDataFactory.user());

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void save_existingEntity_updatesIt() {
        User saved = dao.save(UserTestDataFactory.user());
        flushAndClear();

        saved.setRole(Role.ADMIN);
        dao.save(saved);
        flushAndClear();

        assertThat(dao.findById(saved.getId()).orElseThrow().getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void findById_existingEntity_returnsIt() {
        Long id = dao.save(UserTestDataFactory.user("alice", "hash", Role.ADMIN)).getId();
        flushAndClear();

        User loaded = dao.findById(id).orElseThrow();
        assertThat(loaded.getUsername()).isEqualTo("alice");
        assertThat(loaded.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void findById_missingEntity_returnsEmpty() {
        assertThat(dao.findById(999L)).isEmpty();
    }

    @Test
    void findAll_withSavedEntity_returnsIt() {
        Long id = dao.save(UserTestDataFactory.user()).getId();
        flushAndClear();

        assertThat(dao.findAll()).extracting(User::getId).containsExactly(id);
    }

    @Test
    void deleteById_existingEntity_softDeletesAndHidesFromQueries() {
        Long id = dao.save(UserTestDataFactory.user()).getId();
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
        User saved = dao.save(UserTestDataFactory.user());
        flushAndClear();

        dao.delete(saved);
        flushAndClear();

        assertThat(dao.findById(saved.getId())).isEmpty();
    }

    @Test
    void findByUsername_existingUsername_returnsMatch() {
        dao.save(UserTestDataFactory.user("alice", "hash", Role.ADMIN));
        flushAndClear();

        assertThat(dao.findByUsername("alice"))
                .get()
                .extracting(User::getRole)
                .isEqualTo(Role.ADMIN);
    }

    @Test
    void findByUsername_missingUsername_returnsEmpty() {
        assertThat(dao.findByUsername("ghost")).isEmpty();
    }

    private void flushAndClear() {
        tem.flush();
        tem.clear();
    }
}
