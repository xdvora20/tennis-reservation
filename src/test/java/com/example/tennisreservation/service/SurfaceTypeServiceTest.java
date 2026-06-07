package com.example.tennisreservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tennisreservation.dao.SurfaceTypeDao;
import com.example.tennisreservation.entity.SurfaceType;
import com.example.tennisreservation.exception.NotFoundException;
import com.example.tennisreservation.utils.TestData;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SurfaceTypeServiceTest {

    @Mock private SurfaceTypeDao surfaceTypeDao;
    @InjectMocks private SurfaceTypeService service;

    @Test
    void getById_existingEntity_returnsIt() {
        SurfaceType surface = TestData.surfaceType();
        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.of(surface));

        assertThat(service.getById(1L)).isSameAs(surface);
    }

    @Test
    void getById_missingEntity_throwsNotFound() {
        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(1L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getAll_returnsAllFromDao() {
        List<SurfaceType> all = List.of(TestData.surfaceType());
        when(surfaceTypeDao.findAll()).thenReturn(all);

        assertThat(service.getAll()).isEqualTo(all);
    }

    @Test
    void create_savesAndReturnsEntity() {
        SurfaceType surface = TestData.surfaceType();
        when(surfaceTypeDao.save(surface)).thenReturn(surface);

        assertThat(service.create(surface)).isSameAs(surface);
        verify(surfaceTypeDao).save(surface);
    }

    @Test
    void update_savesAndReturnsEntity() {
        SurfaceType surface = TestData.surfaceType();
        when(surfaceTypeDao.save(surface)).thenReturn(surface);

        assertThat(service.update(surface)).isSameAs(surface);
        verify(surfaceTypeDao).save(surface);
    }

    @Test
    void delete_existingEntity_deletes() {
        when(surfaceTypeDao.deleteById(1L)).thenReturn(true);

        service.delete(1L);

        verify(surfaceTypeDao).deleteById(1L);
    }

    @Test
    void delete_missingEntity_throwsNotFound() {
        when(surfaceTypeDao.deleteById(1L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(1L)).isInstanceOf(NotFoundException.class);
    }
}
