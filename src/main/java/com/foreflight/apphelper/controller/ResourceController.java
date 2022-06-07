package com.foreflight.apphelper.controller;

import com.foreflight.apphelper.domain.MenuChoice;
import com.foreflight.apphelper.domain.MenuChoiceDTO;
import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.domain.ResourceDTO;
import com.foreflight.apphelper.service.ResourceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/resources")
public class ResourceController {
    private final ResourceService resourceService;

    public ResourceController(ResourceService service){
        resourceService = service;
    }

    @GetMapping
    public List<Resource> getAllResources() {
        return resourceService.getAllResources();
    }

    @GetMapping(path = "{resourceId}")
    public Optional<Resource> getResourceById(@PathVariable("resourceId") Long id){
        return resourceService.getResourceById(id);
    }

    @PostMapping
    public Resource createResource(@RequestBody ResourceDTO resource) {
        return resourceService.addResource(resource);
    }

    @PutMapping(path = "{resourceId}")
    public Resource updateResource(@RequestBody ResourceDTO resource, @PathVariable("resourceId") Long id){
        return resourceService.updateResource(resource, id);
    }

    @DeleteMapping(path = "{resourceId}")
    public void deleteResource(@PathVariable("resourceId") Long id) {
        resourceService.deleteResource(id);
    }


}
