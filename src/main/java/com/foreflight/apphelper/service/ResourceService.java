package com.foreflight.apphelper.service;

import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.domain.ResourceDTO;
import com.foreflight.apphelper.repository.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    // Get a list of all resources
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    // Get a single resource by id
    public Optional<Resource> getResourceById(Long id) { return resourceRepository.findResourceById(id); }

    // Get a single resource by name
    public Optional<Resource> getResourceByName(String name) { return resourceRepository.findResourceByName(name); }

    // Add a new resource
    public Resource addResource(ResourceDTO resource) {
        Resource newResource = unpackDTO(resource);
        return resourceRepository.save(newResource);
    }

    // Update an existing resource with the given id, or create a new one if one doesn't exist already
    public Resource updateResource(ResourceDTO resource, Long id) {
        Resource newResource = unpackDTO(resource);
        return resourceRepository.findResourceById(id)
                .map(foundResource -> {
                    foundResource.setName(newResource.getName());
                    foundResource.setLink(newResource.getLink());
                    return resourceRepository.save(foundResource);
                })
                .orElseGet(() -> {
                    newResource.setId(id);
                    return resourceRepository.save(newResource);
                });
    }

    // Delete a resource by id
    public void deleteResource(Long id) {
        if(!resourceRepository.existsById(id)){
            throw new IllegalStateException("Resource with the id " + id + " does not exist.");
        }
        resourceRepository.deleteById(id);
    }

    // Create a Resource object using a data transfer object
    private Resource unpackDTO(ResourceDTO dto){
        Resource resource = new Resource();

        if(dto.getName() == null){
            throw new IllegalStateException("Resource name must not be null");
        }

        resource.setName(dto.getName());
        resource.setLink(dto.getLink());

        return resource;
    }
}
