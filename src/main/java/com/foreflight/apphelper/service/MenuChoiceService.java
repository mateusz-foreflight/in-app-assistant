package com.foreflight.apphelper.service;

import com.foreflight.apphelper.domain.MenuChoice;
import com.foreflight.apphelper.domain.MenuChoiceDTO;
import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.repository.MenuChoiceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MenuChoiceService {

    private final MenuChoiceRepository menuChoiceRepository;
    private final ResourceService resourceService;

    public MenuChoiceService(MenuChoiceRepository menuChoiceRepository, ResourceService resourceService) {
        this.menuChoiceRepository = menuChoiceRepository;
        this.resourceService = resourceService;
    }

    // Get list of all available menu choices
    public List<MenuChoice> getAllChoices() {
        return menuChoiceRepository.findAll();
    }

    // Get menu choice by id
    public Optional<MenuChoice> getChoiceById(Long id) { return menuChoiceRepository.findMenuChoiceById(id); }

    // Get all menu choices that don't have a parent
    public List<MenuChoice> getTopLevelChoices(){
        List<MenuChoice> topLevelChoices = new ArrayList<>();
        for(MenuChoice menuChoice : menuChoiceRepository.findAll()){
            if(menuChoice.getParent() == null){
                topLevelChoices.add(menuChoice);
            }
        }
        return topLevelChoices;
    }

    // Get all child menu choices of a given menu choice specified by id
    public List<MenuChoice> getChildrenById(Long id){
        if(!menuChoiceRepository.existsById(id)){
            throw new IllegalStateException("Menu choice with the id " + id + " does not exist.");
        }

        List<MenuChoice> childChoices = new ArrayList<>();
        for(MenuChoice menuChoice : menuChoiceRepository.findAll()){
            if(menuChoice.getParent() != null && menuChoice.getParent().getId() == id){
                childChoices.add(menuChoice);
            }
        }
        return childChoices;
    }

    // Add new menu choice
    public MenuChoice addChoice(MenuChoiceDTO choice) {
        MenuChoice newChoice = unpackDTO(choice);
        return menuChoiceRepository.save(newChoice);
    }

    // Update an existing menu choice with the given id, or create a new one if one doesn't exist already
    public MenuChoice updateChoice(MenuChoiceDTO choice, Long id) {
        MenuChoice newChoice = unpackDTO(choice);

        return menuChoiceRepository.findMenuChoiceById(id)
                .map(foundChoice ->{
                    foundChoice.setName(newChoice.getName());
                    foundChoice.setParent(newChoice.getParent());
                    foundChoice.setResources(newChoice.getResources());
                    return menuChoiceRepository.save(foundChoice);
                })
                .orElseGet(() -> {
                    newChoice.setId(id);
                    return menuChoiceRepository.save(newChoice);
                });
    }

    // Delete menu choice by id
    public void deleteChoice(Long id) {
        if(!menuChoiceRepository.existsById(id)){
            throw new IllegalStateException("Menu choice with the id " + id + " does not exist.");
        }
        menuChoiceRepository.deleteById(id);
    }

    // Create a MenuChoice object using a data transfer object
    private MenuChoice unpackDTO(MenuChoiceDTO dto){
        MenuChoice choice = new MenuChoice();

        if(dto.getChoiceName() == null){
            throw new IllegalStateException("Menu choice name must not be null");
        }
        choice.setName(dto.getChoiceName());

        if(dto.getParentName() != null) {
            Optional<MenuChoice> newParent = menuChoiceRepository.findMenuChoiceByName(dto.getParentName());
            if (!newParent.isPresent()) {
                throw new IllegalStateException("No menu choice with the provided parent name " + dto.getParentName() + " exists");
            }
            choice.setParent(newParent.get());
        }

        List<Resource> newResources = new ArrayList<>();
        for(String resourceName : dto.getResourceNames()){
            Optional<Resource> newResource = resourceService.getResourceByName(resourceName);
            if (!newResource.isPresent()){
                throw new IllegalStateException("No resource with the provided resource name" + resourceName + " exists");
            }
            newResources.add(newResource.get());
        }
        choice.setResources(newResources);

        return choice;
    }
}
