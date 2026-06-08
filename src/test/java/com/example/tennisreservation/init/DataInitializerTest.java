package com.example.tennisreservation.init;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tennisreservation.dao.CourtDao;
import com.example.tennisreservation.dao.SurfaceTypeDao;
import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.entity.Role;
import com.example.tennisreservation.entity.SurfaceType;
import com.example.tennisreservation.service.UserService;
import com.example.tennisreservation.utils.SurfaceTypeTestDataFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private SurfaceTypeDao surfaceTypeDao;
    @Mock
    private CourtDao courtDao;
    @Mock
    private UserService userService;
    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void run_emptyDatabase_seedsTwoSurfaceTypesAndFourCourts() {
        when(surfaceTypeDao.findAll()).thenReturn(List.of());
        when(surfaceTypeDao.save(any(SurfaceType.class))).thenAnswer(call -> call.getArgument(0));

        dataInitializer.run();

        verify(surfaceTypeDao, times(2)).save(any(SurfaceType.class));
        verify(courtDao, times(4)).save(any(Court.class));
        verify(userService).create("admin", "admin123", Role.ADMIN);
        verify(userService).create("user", "user123", Role.USER);
    }

    @Test
    void run_databaseAlreadyPopulated_seedsNothing() {
        when(surfaceTypeDao.findAll()).thenReturn(List.of(SurfaceTypeTestDataFactory.surfaceType()));

        dataInitializer.run();

        verify(surfaceTypeDao, never()).save(any());
        verify(courtDao, never()).save(any());
    }
}
