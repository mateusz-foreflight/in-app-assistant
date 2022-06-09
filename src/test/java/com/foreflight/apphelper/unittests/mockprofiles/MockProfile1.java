package com.foreflight.apphelper.unittests.mockprofiles;

import com.foreflight.apphelper.domain.MenuChoice;
import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.repository.MenuChoiceRepository;
import com.foreflight.apphelper.service.ResourceService;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

// Mocks a menu choice repository with the following MenuChoice objects in it:
//      id: 1, name: choice1, parent: null, resources: []
//      id: 2, name: choice2, parent: choice1, resources: []
//      id: 3, name: choice3, parent: choice1, resources: [resource1]
// And a resource service with the following resources:
//      id: 1, name: resource1, link: link1
public class MockProfile1{
    public Resource resource1;
    public List<Resource> resources;

    public MenuChoice choice1, choice2, choice3;
    public List<MenuChoice> choices;

    public MockProfile1(MenuChoiceRepository menuChoiceRepository, ResourceService resourceService){
        // SET UP DATA
        // Set up resources
        this.resource1 = new Resource("resource1", "link1");
        this.resource1.setId(1L);
        this.resources = new ArrayList<>();
        resources.add(resource1);

        // Set up menu choices
        this.choice1 = new MenuChoice("choice1", null, new ArrayList<>());
        this.choice1.setId(1L);
        this.choice2 = new MenuChoice("choice2", choice1, new ArrayList<>());
        this.choice2.setId(2L);
        this.choice3 = new MenuChoice("choice3", choice1, resources);
        this.choice3.setId(3L);
        this.choices = Arrays.asList(choice1, choice2, choice3);

        // SET UP METHOD MOCKS
        // menuChoiceRepository - save, findAll, findMenuChoiceById, findMenuChoiceByName, existsById
        when(menuChoiceRepository.save(Mockito.any(MenuChoice.class))).then(returnsFirstArg());

        when(menuChoiceRepository.findAll()).thenReturn(choices);

        // Take advantage of overriding stubbing as described here:
        // https://stackoverflow.com/questions/34171082/how-to-handle-any-other-value-with-mockito
        when(menuChoiceRepository.findMenuChoiceById(anyLong())).thenReturn(Optional.empty());
        doReturn(Optional.of(choice1)).when(menuChoiceRepository).findMenuChoiceById(1L);
        doReturn(Optional.of(choice2)).when(menuChoiceRepository).findMenuChoiceById(2L);
        doReturn(Optional.of(choice3)).when(menuChoiceRepository).findMenuChoiceById(3L);

        when(menuChoiceRepository.findMenuChoiceByName(anyString())).thenReturn(Optional.empty());
        doReturn(Optional.of(choice1)).when(menuChoiceRepository).findMenuChoiceByName("choice1");
        doReturn(Optional.of(choice2)).when(menuChoiceRepository).findMenuChoiceByName("choice2");
        doReturn(Optional.of(choice3)).when(menuChoiceRepository).findMenuChoiceByName("choice3");

        when(menuChoiceRepository.existsById(anyLong())).thenReturn(false);
        doReturn(true).when(menuChoiceRepository).existsById(1L);
        doReturn(true).when(menuChoiceRepository).existsById(2L);
        doReturn(true).when(menuChoiceRepository).existsById(3L);

        // resourceService - getResourceByName
        when(resourceService.getResourceByName(anyString())).thenReturn(Optional.empty());
        doReturn(Optional.of(resource1)).when(resourceService).getResourceByName("resource1");
    }
}
