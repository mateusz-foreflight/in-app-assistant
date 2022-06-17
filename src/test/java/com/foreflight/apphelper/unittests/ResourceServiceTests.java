package com.foreflight.apphelper.unittests;

import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.domain.ResourceDTO;
import com.foreflight.apphelper.domain.Source;
import com.foreflight.apphelper.repository.ResourceRepository;
import com.foreflight.apphelper.service.ResourceService;
import com.foreflight.apphelper.unittests.mockprofiles.MockProfile2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ResourceServiceTests {

    @Mock
    private ResourceRepository resourceRepository;

    private ResourceService resourceService;

    @BeforeEach
    void init(){
        resourceRepository = mock(ResourceRepository.class);

        resourceService = new ResourceService(resourceRepository);
    }

    @Test
    void getAllResources_works(){
        // Given
        MockProfile2 mockProfile = new MockProfile2(resourceRepository);

        // When
        List<Resource> actual = resourceService.getAllResources();

        // Then
        List<Resource> expected = mockProfile.resources;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getResourceById_withValidId(){
        // Given
        MockProfile2 mockProfile = new MockProfile2(resourceRepository);

        // When
        Optional<Resource> actual = resourceService.getResourceById(1L);

        // Then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(mockProfile.resource1);
    }

    @Test
    void getResourceById_withInvalidId(){
        // Given
        MockProfile2 mockProfile = new MockProfile2(resourceRepository);

        // When
        Optional<Resource> actual = resourceService.getResourceById(15L);

        // Then
        assertThat(actual.isPresent()).isFalse();
    }

    @Test
    void getResourceByName_withValidName(){
        // Given
        MockProfile2 mockProfile = new MockProfile2(resourceRepository);

        // When
        Optional<Resource> actual = resourceService.getResourceByName("resource1");

        // Then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(mockProfile.resource1);
    }

    @Test
    void getResourceByName_withInvalidName(){
        // Given
        MockProfile2 mockProfile = new MockProfile2(resourceRepository);

        // When
        Optional<Resource> actual = resourceService.getResourceByName("doesNotExist");

        // Then
        assertThat(actual.isPresent()).isFalse();
    }

    @Test
    void addResource_withValidResource(){
        // Given
        MockProfile2 mockProfile = new MockProfile2(resourceRepository);
        ResourceDTO dto = new ResourceDTO("new", "newLink", "PilotGuide");

        // When
        Resource actual = resourceService.addResource(dto);
        actual.setId(5L);

        // Then
        Resource expected = new Resource("new", "newLink", Source.PilotGuide);
        expected.setId(5L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addResource_withInvalidResource_withInvalidName(){
        // Given
        MockProfile2 mockProfile = new MockProfile2(resourceRepository);
        ResourceDTO dto = new ResourceDTO(null, "newLink", "PilotGuide");

        // When, Then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> resourceService.addResource(dto))
                .withMessageContaining("name")
                .withMessageContaining("null");
    }

    @Test
    void updateResource_withPresentId(){
        // Given
        MockProfile2 mockProfile = new MockProfile2(resourceRepository);
        ResourceDTO dto = new ResourceDTO("new", "newLink", "PilotGuide");

        // When
        Resource actual = resourceService.updateResource(dto, 1L);

        // Then
        Resource expected = new Resource("new", "newLink", Source.PilotGuide);
        expected.setId(1L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateResource_withNotPresentId(){
        // Given
        MockProfile2 mockProfile = new MockProfile2(resourceRepository);
        ResourceDTO dto = new ResourceDTO("new", "newLink", "PilotGuide");

        // When
        Resource actual = resourceService.updateResource(dto, 15L);

        // Then
        Resource expected = new Resource("new", "newLink", Source.PilotGuide);
        expected.setId(15L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteResource_withValidId(){
        //Given
        MockProfile2 mockProfile = new MockProfile2(resourceRepository);

        // When
        resourceService.deleteResource(1L, false);

        // Then
        verify(resourceRepository).deleteById(1L);
    }

    @Test
    void deleteChoice_withInvalidId(){
        //Given
        MockProfile2 mockProfile = new MockProfile2(resourceRepository);

        // When, Then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> resourceService.deleteResource(15L, false))
                .withMessageContaining("id")
                .withMessageContaining(String.valueOf(15));
    }
}
