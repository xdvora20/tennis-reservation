package com.example.tennisreservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tennisreservation.dao.CourtDao;
import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.exception.NotFoundException;
import com.example.tennisreservation.utils.CourtTestDataFactory;
import com.example.tennisreservation.utils.SurfaceTypeTestDataFactory;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourtServiceTest {

    @Mock private CourtDao courtDao;
    @InjectMocks private CourtService service;

    @Test
    void getById_existingEntity_returnsIt() {
        Court court = CourtTestDataFactory.court(1, SurfaceTypeTestDataFactory.surfaceType());
        when(courtDao.findById(1L)).thenReturn(Optional.of(court));

        assertThat(service.getById(1L)).isSameAs(court);
    }

    @Test
    void getById_missingEntity_throwsNotFound() {
        when(courtDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(1L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getByNumber_existingCourt_returnsIt() {
        Court court = CourtTestDataFactory.court(7, SurfaceTypeTestDataFactory.surfaceType());
        when(courtDao.findByCourtNumber(7)).thenReturn(Optional.of(court));

        assertThat(service.getByNumber(7)).isSameAs(court);
    }

    @Test
    void getByNumber_missingCourt_throwsNotFound() {
        when(courtDao.findByCourtNumber(7)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByNumber(7)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getAll_returnsAllFromDao() {
        List<Court> all = List.of(CourtTestDataFactory.court(1, SurfaceTypeTestDataFactory.surfaceType()));
        when(courtDao.findAll()).thenReturn(all);

        assertThat(service.getAll()).isEqualTo(all);
    }

    @Test
    void create_savesAndReturnsEntity() {
        Court court = CourtTestDataFactory.court(1, SurfaceTypeTestDataFactory.surfaceType());
        when(courtDao.save(court)).thenReturn(court);

        assertThat(service.create(court)).isSameAs(court);
        verify(courtDao).save(court);
    }

    @Test
    void update_savesAndReturnsEntity() {
        Court court = CourtTestDataFactory.court(1, SurfaceTypeTestDataFactory.surfaceType());
        when(courtDao.save(court)).thenReturn(court);

        assertThat(service.update(court)).isSameAs(court);
        verify(courtDao).save(court);
    }

    @Test
    void delete_existingEntity_deletes() {
        when(courtDao.deleteById(1L)).thenReturn(true);

        service.delete(1L);

        verify(courtDao).deleteById(1L);
    }

    @Test
    void delete_missingEntity_throwsNotFound() {
        when(courtDao.deleteById(1L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(1L)).isInstanceOf(NotFoundException.class);
    }
}
