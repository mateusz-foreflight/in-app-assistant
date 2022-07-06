package com.foreflight.apphelper.controller;

import com.foreflight.apphelper.domain.*;
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
    public List<ResourceDTO> getAllResources() {
        return resourceService.getAllResources();
    }

    // Get a single resource by id
    @GetMapping(path = "{resourceId}")
    public Optional<ResourceDTO> getResourceById(@PathVariable("resourceId") Long id){
        return resourceService.getResourceById(id);
    }

    // Add a new resource
    @PostMapping
    public ResourceDTO createResource(@RequestBody ResourceCreateDTO resource) {
        return resourceService.addResource(resource);
    }

    // Update an existing resource with the given id, or create a new one if one doesn't exist already
    @PutMapping(path = "{resourceId}")
    public ResourceDTO updateResource(@RequestBody ResourceCreateDTO resource, @PathVariable("resourceId") Long id){
        return resourceService.updateResource(resource, id);
    }

    // Delete a resource by id
    @DeleteMapping(path = "{resourceId}")
    public void deleteResource(@PathVariable("resourceId") Long id, @RequestParam(defaultValue = "false") boolean force) {
        resourceService.deleteResource(id, force);
    }

}
