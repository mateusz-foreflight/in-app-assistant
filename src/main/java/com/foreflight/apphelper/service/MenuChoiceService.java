package com.foreflight.apphelper.service;

import com.foreflight.apphelper.domain.*;
import com.foreflight.apphelper.repository.MenuChoiceRepository;
import com.foreflight.apphelper.repository.ResourceRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuChoiceService {

    private final MenuChoiceRepository menuChoiceRepository;
    private final ResourceService resourceService;

    public MenuChoiceService(MenuChoiceRepository menuChoiceRepository, ResourceService resourceService) {
        this.menuChoiceRepository = menuChoiceRepository;
        this.resourceService = resourceService;
    }

    // Get list of all available menu choices
    public List<MenuChoiceDTO> getAllChoices() {
        return menuChoiceRepository.findAll().stream().map(MenuChoiceDTO::assemble).collect(Collectors.toList());
    }

    // Get menu choice by id
    public Optional<MenuChoiceDTO> getChoiceById(Long id) {
        return menuChoiceRepository.findMenuChoiceById(id).map(MenuChoiceDTO::assemble);
    }

    // Get all menu choices that don't have a parent
    public List<MenuChoiceDTO> getTopLevelChoices(){
        return menuChoiceRepository.findAllTopLevelMenuChoices().stream().map(MenuChoiceDTO::assemble)
                .collect(Collectors.toList());
    }

    // Get all child menu choices of a given menu choice specified by id
    public List<MenuChoiceDTO> getChildrenById(Long id){
        if(!menuChoiceRepository.existsById(id)){
            throw new IllegalStateException("Menu choice with the id " + id + " does not exist.");
        }

        return menuChoiceRepository.findChildMenuChoicesById(id).stream().map(MenuChoiceDTO::assemble)
                .collect(Collectors.toList());
    }

    // Add new menu choice
    public MenuChoiceDTO addChoice(MenuChoiceCreateDTO choice) {
        MenuChoice newChoice = unpackDTO(choice);
        if(menuChoiceRepository.findMenuChoiceByName(newChoice.getName()).isPresent()){
            throw new IllegalStateException("Menu choice with the name " + newChoice.getName() + " already exists.");
        }
        return MenuChoiceDTO.assemble(menuChoiceRepository.save(newChoice));
    }

    // Update an existing menu choice with the given id, or create a new one if one doesn't exist already
    public MenuChoiceDTO updateChoice(MenuChoiceCreateDTO choice, Long id) {
        MenuChoice newChoice = unpackDTO(choice);

        return menuChoiceRepository.findMenuChoiceById(id)
                .map(foundChoice ->{
                    if(newChoice.getParent() != null && checkDescendent(newChoice.getParent(), foundChoice)){
                        throw new IllegalStateException("Cannot update menu choice's parent name to " +
                                newChoice.getParent().getName() + " because that would result in a circular " +
                                "parent-child relationship.");
                    }

                    foundChoice.setName(newChoice.getName());
                    foundChoice.setParent(newChoice.getParent());
                    foundChoice.setResources(newChoice.getResources());
                    return MenuChoiceDTO.assemble(menuChoiceRepository.save(foundChoice));
                })
                .orElseGet(() -> {
                    newChoice.setId(id);
                    return MenuChoiceDTO.assemble(menuChoiceRepository.save(newChoice));
                });
    }

    // Delete menu choice by id
    public void deleteChoice(Long id, boolean force) {
        if(!menuChoiceRepository.existsById(id)){
            throw new IllegalStateException("Menu choice with the id " + id + " does not exist.");
        }

        menuChoiceRepository.deleteRelationsToMetricById(id);

        if(force){
            cascadingDelete(id);
            return;
        }

        try{
            menuChoiceRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex){
            throw new IllegalStateException("Cannot delete Menu Choice with id " + id + " because the choice has " +
                    "children. Try using a force delete.");
        }
    }

    public List<MenuChoice> namesToMenuChoices(List<String> menuchoiceNames){
        List<MenuChoice> newChoices = new ArrayList<>();
        for(String menuchoiceName : menuchoiceNames){
            Optional<MenuChoice> newMenuchoice = menuChoiceRepository.findMenuChoiceByName(menuchoiceName);
            if (!newMenuchoice.isPresent()){
                throw new IllegalStateException("No menu choice with the provided name" + menuchoiceName + " exists");
            }
            newChoices.add(newMenuchoice.get());
        }

        return newChoices;
    }

    // Create a MenuChoice object using a data transfer object
    private MenuChoice unpackDTO(MenuChoiceCreateDTO dto){
        MenuChoice choice = new MenuChoice();

        if(dto.getName() == null){
            throw new IllegalStateException("Menu choice name must not be null");
        }
        choice.setName(dto.getName());

        if(dto.getParentId() != null) {
            Optional<MenuChoice> newParent = menuChoiceRepository.findMenuChoiceById(dto.getParentId());
            if (!newParent.isPresent()) {
                throw new IllegalStateException("No menu choice with the provided parent id " + dto.getParentId() + " exists");
            }
            choice.setParent(newParent.get());
        }

        choice.setResources(resourceService.getUniqueResourcesFromIdList(dto.getResourceIds()));

        return choice;
    }

    // Check if choiceA is a descendent of choiceB
    private boolean checkDescendent(MenuChoice choiceA, MenuChoice choiceB){
        List<MenuChoice> descendents = menuChoiceRepository.findDescendentMenuChoicesById(choiceB.getId());

        for(MenuChoice descendent : descendents){
            if(descendent.getId().equals(choiceA.getId())){
                return true;
            }
        }

        return false;
    }

    private void cascadingDelete(Long targetId){
        List<MenuChoice> children = menuChoiceRepository.findChildMenuChoicesById(targetId);

        if(children.size() <= 0){
            menuChoiceRepository.deleteById(targetId);
            return;
        }

        for(MenuChoice child : children){
            cascadingDelete(child.getId());
        }

        menuChoiceRepository.deleteById(targetId);
    }

    public List<MenuChoice> getUniqueMenuChoicesFromIdList(List<Long> ids){
        // Remove duplicate ids
        List<Long> choiceIdsNoDupes = new ArrayList<>(new HashSet<>(ids));
        List<MenuChoice> choices = new ArrayList<>();

        for(Long choiceId : choiceIdsNoDupes){
            Optional<MenuChoice> foundChoice = menuChoiceRepository.findMenuChoiceById(choiceId);
            if(!foundChoice.isPresent()){
                throw new IllegalStateException("No menu choice with the id " + choiceId + " exists");
            }
            choices.add(foundChoice.get());
        }

        return choices;
    }
}
