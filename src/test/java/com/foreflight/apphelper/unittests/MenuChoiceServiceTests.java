package com.foreflight.apphelper.unittests;


import com.foreflight.apphelper.domain.MenuChoice;
import com.foreflight.apphelper.domain.MenuChoiceDTO;
import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.repository.MenuChoiceRepository;
import com.foreflight.apphelper.service.MenuChoiceService;
import com.foreflight.apphelper.service.ResourceService;
import com.foreflight.apphelper.unittests.mockprofiles.MockProfile1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MenuChoiceServiceTests {

    @Mock
    private MenuChoiceRepository menuChoiceRepository;
    @Mock
    private ResourceService resourceService;
    private MenuChoiceService menuChoiceService;

    @BeforeEach
    void init(){
        menuChoiceRepository = mock(MenuChoiceRepository.class);
        resourceService = mock(ResourceService.class);

        menuChoiceService = new MenuChoiceService(menuChoiceRepository, resourceService);
    }

    @Test
    void getAllChoices_works(){
        // Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);

        // When
        List<MenuChoice> actual = menuChoiceService.getAllChoices();

        // Then
        assertThat(actual).isEqualTo(mockProfile.choices);
    }

    @Test
    void getChoiceById_withPresentId(){
        // Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);

        // When
        Optional<MenuChoice> actual = menuChoiceService.getChoiceById(1L);

        // Then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(mockProfile.choice1);
    }

    @Test
    void getChoiceById_withNotPresentId(){
        // Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);

        // When
        Optional<MenuChoice> actual = menuChoiceService.getChoiceById(15L);

        // Then
        assertThat(actual.isPresent()).isFalse();
    }

    @Test
    void getTopLevelChoices_works(){
        // Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);

        // When
        List<MenuChoice> actual = menuChoiceService.getTopLevelChoices();

        //Then
        List<MenuChoice> expected = Collections.singletonList(mockProfile.choice1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getChildrenById_withPresentId_withPresentChildren(){
        // Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);

        // When
        List<MenuChoice> actual = menuChoiceService.getChildrenById(1L);

        //Then
        List<MenuChoice> expected = Arrays.asList(mockProfile.choice2, mockProfile.choice3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getChildrenById_withPresentId_withNotPresentChildren(){
        // Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);

        // When
        List<MenuChoice> actual = menuChoiceService.getChildrenById(2L);

        //Then
        List<MenuChoice> expected = Collections.emptyList();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getChildrenById_withNotPresentId(){
        // Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);

        // When, Then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> menuChoiceService.getChildrenById(15L));
    }

    @Test
    void addChoice_withValidChoice_withNoParent_withNoResources(){
        // Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);
        MenuChoiceDTO dto = new MenuChoiceDTO("new", null, Collections.emptyList());

        // When
        MenuChoice actual = menuChoiceService.addChoice(dto);
        actual.setId(5L);

        //Then
        MenuChoice expected = new MenuChoice("new", null, Collections.emptyList());
        expected.setId(5L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addChoice_withValidChoice_withParent_withResources(){
        // Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);
        MenuChoiceDTO dto = new MenuChoiceDTO("new", "choice2", Collections.singletonList("resource1"));

        // When
        MenuChoice actual = menuChoiceService.addChoice(dto);
        actual.setId(5L);

        //Then
        MenuChoice expected = new MenuChoice("new", mockProfile.choice2, Collections.singletonList(mockProfile.resource1));
        expected.setId(5L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addChoice_withInvalidChoice_withInvalidName(){
        // Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);
        MenuChoiceDTO dto = new MenuChoiceDTO(null, null, Collections.emptyList());

        // When, Then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> menuChoiceService.addChoice(dto))
                .withMessageContaining("name")
                .withMessageContaining("null");
    }

    @Test
    void addChoice_withInvalidChoice_withInvalidParent(){
        // Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);
        MenuChoiceDTO dto = new MenuChoiceDTO("new", "doesNotExist", Collections.emptyList());

        // When, Then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> menuChoiceService.addChoice(dto))
                .withMessageContaining("parent")
                .withMessageContaining(dto.getParentName());
    }

    @Test
    void addChoice_withInvalidChoice_withInvalidResource(){
        // Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);
        MenuChoiceDTO dto = new MenuChoiceDTO("new", null, Collections.singletonList("doesNotExist"));

        // When, Then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> menuChoiceService.addChoice(dto))
                .withMessageContaining("resource")
                .withMessageContaining(dto.getResourceNames().get(0));
    }

    @Test
    void updateChoice_withValidChoice_withPresentId(){
        // Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);
        MenuChoiceDTO dto = new MenuChoiceDTO("newChoice3", "choice2", Collections.singletonList("resource1"));

        // When
        MenuChoice actual = menuChoiceService.updateChoice(dto, 3L);

        //Then
        MenuChoice expected = new MenuChoice("newChoice3", mockProfile.choice2, Collections.singletonList(mockProfile.resource1));
        expected.setId(3L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateChoice_withValidChoice_withNotPresentId(){
        // Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);
        MenuChoiceDTO dto = new MenuChoiceDTO("new", "choice2", Collections.singletonList("resource1"));

        // When
        MenuChoice actual = menuChoiceService.updateChoice(dto, 15L);

        //Then
        MenuChoice expected = new MenuChoice("new", mockProfile.choice2, Collections.singletonList(mockProfile.resource1));
        expected.setId(15L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteChoice_withPresentId(){
        //Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);

        // When
        menuChoiceService.deleteChoice(1L, false);

        // Then
        verify(menuChoiceRepository).deleteById(1L);
    }

    @Test
    void deleteChoice_withNotPresentId(){
        //Given
        MockProfile1 mockProfile = new MockProfile1(menuChoiceRepository, resourceService);

        // When, Then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> menuChoiceService.deleteChoice(15L, false))
                .withMessageContaining("id")
                .withMessageContaining(String.valueOf(15));
    }
}
