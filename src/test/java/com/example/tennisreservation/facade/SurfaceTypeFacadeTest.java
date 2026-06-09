package com.example.tennisreservation.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tennisreservation.dto.SurfaceTypeRequest;
import com.example.tennisreservation.dto.SurfaceTypeResponse;
import com.example.tennisreservation.entity.SurfaceType;
import com.example.tennisreservation.exception.ConflictException;
import com.example.tennisreservation.mapper.SurfaceTypeMapper;
import com.example.tennisreservation.service.CourtService;
import com.example.tennisreservation.service.SurfaceTypeService;
import com.example.tennisreservation.utils.SurfaceTypeTestDataFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SurfaceTypeFacadeTest {

    @Mock
    private SurfaceTypeService surfaceTypeService;
    @Mock
    private SurfaceTypeMapper surfaceTypeMapper;
    @Mock
    private CourtService courtService;
    @InjectMocks
    private SurfaceTypeFacade facade;

    private final SurfaceTypeRequest request = SurfaceTypeTestDataFactory.surfaceTypeRequest();
    private final SurfaceTypeResponse response = SurfaceTypeTestDataFactory.surfaceTypeResponse();

    @Test
    void create_mapsRequestCreatesAndReturnsResponse() {
        SurfaceType entity = SurfaceTypeTestDataFactory.surfaceType();
        SurfaceType created = SurfaceTypeTestDataFactory.surfaceType();
        when(surfaceTypeMapper.toEntity(request)).thenReturn(entity);
        when(surfaceTypeService.create(entity)).thenReturn(created);
        when(surfaceTypeMapper.toResponse(created)).thenReturn(response);

        assertThat(facade.create(request)).isSameAs(response);
    }

    @Test
    void update_loadsAppliesChangesAndReturnsResponse() {
        SurfaceType existing = SurfaceTypeTestDataFactory.surfaceType();
        SurfaceType updated = SurfaceTypeTestDataFactory.surfaceType();
        when(surfaceTypeService.getById(1L)).thenReturn(existing);
        when(surfaceTypeService.update(existing)).thenReturn(updated);
        when(surfaceTypeMapper.toResponse(updated)).thenReturn(response);

        assertThat(facade.update(1L, request)).isSameAs(response);
        verify(surfaceTypeMapper).updateEntity(existing, request);
    }

    @Test
    void getById_returnsMappedResponse() {
        SurfaceType entity = SurfaceTypeTestDataFactory.surfaceType();
        when(surfaceTypeService.getById(1L)).thenReturn(entity);
        when(surfaceTypeMapper.toResponse(entity)).thenReturn(response);

        assertThat(facade.getById(1L)).isSameAs(response);
    }

    @Test
    void getAll_returnsMappedResponses() {
        List<SurfaceType> entities = List.of(SurfaceTypeTestDataFactory.surfaceType());
        List<SurfaceTypeResponse> responses = List.of(response);
        when(surfaceTypeService.getAll()).thenReturn(entities);
        when(surfaceTypeMapper.toResponseList(entities)).thenReturn(responses);

        assertThat(facade.getAll()).isEqualTo(responses);
    }

    @Test
    void delete_delegatesToService() {
        facade.delete(1L);

        verify(surfaceTypeService).delete(1L);
    }

    @Test
    void delete_surfaceTypeUsedByCourt_throwsConflict() {
        when(courtService.existsForSurfaceType(1L)).thenReturn(true);

        assertThatThrownBy(() -> facade.delete(1L)).isInstanceOf(ConflictException.class);
        verify(surfaceTypeService, never()).delete(any());
    }
}
