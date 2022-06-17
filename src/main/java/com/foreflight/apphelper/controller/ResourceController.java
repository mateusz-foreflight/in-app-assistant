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

    // Get a list of all resources
    @GetMapping
    public List<Resource> getAllResources() {
        return resourceService.getAllResources();
    }

    // Get a single resource by id
    @GetMapping(path = "{resourceId}")
    public Optional<Resource> getResourceById(@PathVariable("resourceId") Long id){
        return resourceService.getResourceById(id);
    }

    // Add a new resource
    @PostMapping
    public Resource createResource(@RequestBody ResourceDTO resource) {
        return resourceService.addResource(resource);
    }

    // Update an existing resource with the given id, or create a new one if one doesn't exist already
    @PutMapping(path = "{resourceId}")
    public Resource updateResource(@RequestBody ResourceDTO resource, @PathVariable("resourceId") Long id){
        return resourceService.updateResource(resource, id);
    }

    // Delete a resource by id
    @DeleteMapping(path = "{resourceId}")
    public void deleteResource(@PathVariable("resourceId") Long id, @RequestParam(defaultValue = "false") boolean force) {
        resourceService.deleteResource(id, force);
    }

}
