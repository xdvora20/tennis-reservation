package com.example.tennisreservation.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tennisreservation.dto.CourtRequest;
import com.example.tennisreservation.dto.CourtResponse;
import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.entity.SurfaceType;
import com.example.tennisreservation.mapper.CourtMapper;
import com.example.tennisreservation.service.CourtService;
import com.example.tennisreservation.service.SurfaceTypeService;
import com.example.tennisreservation.utils.TestData;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourtFacadeTest {

    @Mock private CourtService courtService;
    @Mock private SurfaceTypeService surfaceTypeService;
    @Mock private CourtMapper courtMapper;
    @InjectMocks private CourtFacade facade;

    private final CourtRequest request = TestData.courtRequest();
    private final CourtResponse response = TestData.courtResponse();

    @Test
    void create_resolvesSurfaceSetsItAndReturnsResponse() {
        SurfaceType surface = TestData.surfaceType();
        Court court = TestData.court(1, null);
        Court created = TestData.court(1, surface);
        when(courtMapper.toEntity(request)).thenReturn(court);
        when(surfaceTypeService.getById(request.surfaceTypeId())).thenReturn(surface);
        when(courtService.create(court)).thenReturn(created);
        when(courtMapper.toResponse(created)).thenReturn(response);

        assertThat(facade.create(request)).isSameAs(response);
        assertThat(court.getSurfaceType()).isSameAs(surface);
    }

    @Test
    void update_loadsAppliesChangesResolvesSurfaceAndReturnsResponse() {
        SurfaceType surface = TestData.surfaceType();
        Court existing = TestData.court(1, null);
        Court updated = TestData.court(1, surface);
        when(courtService.getById(1L)).thenReturn(existing);
        when(surfaceTypeService.getById(request.surfaceTypeId())).thenReturn(surface);
        when(courtService.update(existing)).thenReturn(updated);
        when(courtMapper.toResponse(updated)).thenReturn(response);

        assertThat(facade.update(1L, request)).isSameAs(response);
        verify(courtMapper).updateEntity(existing, request);
        assertThat(existing.getSurfaceType()).isSameAs(surface);
    }

    @Test
    void getById_returnsMappedResponse() {
        Court court = TestData.court(1, TestData.surfaceType());
        when(courtService.getById(1L)).thenReturn(court);
        when(courtMapper.toResponse(court)).thenReturn(response);

        assertThat(facade.getById(1L)).isSameAs(response);
    }

    @Test
    void getAll_returnsMappedResponses() {
        List<Court> courts = List.of(TestData.court(1, TestData.surfaceType()));
        List<CourtResponse> responses = List.of(response);
        when(courtService.getAll()).thenReturn(courts);
        when(courtMapper.toResponseList(courts)).thenReturn(responses);

        assertThat(facade.getAll()).isEqualTo(responses);
    }

    @Test
    void delete_delegatesToService() {
        facade.delete(1L);

        verify(courtService).delete(1L);
    }
}
