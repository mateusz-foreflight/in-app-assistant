package com.foreflight.apphelper.controller;

import com.foreflight.apphelper.domain.MenuChoice;
import com.foreflight.apphelper.domain.MenuChoiceCreateDTO;
import com.foreflight.apphelper.domain.MenuChoiceDTO;
import com.foreflight.apphelper.service.MenuChoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/menuchoices")
public class MenuChoiceController {
    private final MenuChoiceService menuChoiceService;

    public MenuChoiceController(MenuChoiceService service){
        menuChoiceService = service;
    }

    // Get list of all possible menu choices
    @GetMapping
    public List<MenuChoiceDTO> getAllChoices() {return menuChoiceService.getAllChoices(); }

    // Get single menu choice by id
    @GetMapping(path = "{choiceId}")
    public Optional<MenuChoiceDTO> getChoiceById(@PathVariable("choiceId") Long id) {
        return menuChoiceService.getChoiceById(id);
    }

    // Get all menu choices that don't have a parent
    @GetMapping(path = "toplevel")
    public List<MenuChoiceDTO> getTopLevelChoices() {return menuChoiceService.getTopLevelChoices(); }

    // Get all child menu choices of a given menu choice specified by id
    @GetMapping(path = "{choiceId}/children")
    public List<MenuChoiceDTO> getChildrenById(@PathVariable("choiceId") Long id) {
        return menuChoiceService.getChildrenById(id);
    }

    // Add a new menu choice
    @PostMapping
    public MenuChoiceDTO createChoice(@RequestBody MenuChoiceCreateDTO choice) {
        return menuChoiceService.addChoice(choice);
    }

    // Update an existing menu choice with the given id, or create a new one if one doesn't exist already
    // NOTE: No guarantee that the newly created menu choice will have the provided id
    @PutMapping(path = "{choiceId}")
    public MenuChoiceDTO updateChoice(@RequestBody MenuChoiceCreateDTO choice, @PathVariable("choiceId") Long id){
        return menuChoiceService.updateChoice(choice, id);
    }

    // Delete menu choice by id
    @DeleteMapping(path = "{choiceId}")
    public void deleteChoice(@PathVariable("choiceId") Long id, @RequestParam(defaultValue = "false") boolean force) {
        menuChoiceService.deleteChoice(id, force);
    }

}
