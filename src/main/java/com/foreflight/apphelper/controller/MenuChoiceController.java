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

    @GetMapping
    public List<MenuChoice> getAllChoices() {return menuChoiceService.getAllChoices(); }

    @GetMapping(path = "{choiceId}")
    public Optional<MenuChoice> getChoiceById(@PathVariable("choiceId") Long id) {
        return menuChoiceService.getChoiceById(id);
    }

    @GetMapping(path = "toplevel")
    public List<MenuChoice> getTopLevelChoices() {return menuChoiceService.getTopLevelChoices(); }

    @GetMapping(path = "{choiceId}/children")
    public List<MenuChoice> getChildrenById(@PathVariable("choiceId") Long id) {
        return menuChoiceService.getChildrenById(id);
    }

    @PostMapping
    public MenuChoice createChoice(@RequestBody MenuChoiceDTO choice) {
        return menuChoiceService.addChoice(choice);
    }

    @PutMapping(path = "{choiceId}")
    public MenuChoice updateChoice(@RequestBody MenuChoiceDTO choice, @PathVariable("choiceId") Long id){
        return menuChoiceService.updateChoice(choice, id);
    }

    @DeleteMapping(path = "{choiceId}")
    public void deleteChoice(@PathVariable("choiceId") Long id) {
        menuChoiceService.deleteChoice(id);
    }

}
