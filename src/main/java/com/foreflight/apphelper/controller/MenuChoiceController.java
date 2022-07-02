package com.foreflight.apphelper.controller;

import com.foreflight.apphelper.domain.MenuChoice;
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
    public List<MenuChoice> getAllChoices() {return menuChoiceService.getAllChoices(); }

    // Get single menu choice by id
    @GetMapping(path = "{choiceId}")
    public Optional<MenuChoice> getChoiceById(@PathVariable("choiceId") Long id) {
        return menuChoiceService.getChoiceById(id);
    }

    // Get all menu choices that don't have a parent
    @GetMapping(path = "toplevel")
    public List<MenuChoice> getTopLevelChoices() {return menuChoiceService.getTopLevelChoices(); }

    // Get all child menu choices of a given menu choice specified by id
    @GetMapping(path = "{choiceId}/children")
    public List<MenuChoice> getChildrenById(@PathVariable("choiceId") Long id) {
        return menuChoiceService.getChildrenById(id);
    }

    // Add a new menu choice
    @PostMapping
    public MenuChoice createChoice(@RequestBody MenuChoiceDTO choice) {
        return menuChoiceService.addChoice(choice);
    }

    // Update an existing menu choice with the given id, or create a new one if one doesn't exist already
    // NOTE: No guarantee that the newly created menu choice will have the provided id
    @PutMapping(path = "{choiceId}")
    public MenuChoice updateChoice(@RequestBody MenuChoiceDTO choice, @PathVariable("choiceId") Long id){
        return menuChoiceService.updateChoice(choice, id);
    }

    // Delete menu choice by id
    @DeleteMapping(path = "{choiceId}")
    public void deleteChoice(@PathVariable("choiceId") Long id, @RequestParam(defaultValue = "false") boolean force) {
        menuChoiceService.deleteChoice(id, force);
    }

}
