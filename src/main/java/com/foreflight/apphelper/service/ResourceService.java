package com.foreflight.apphelper.service;

import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.domain.ResourceDTO;
import com.foreflight.apphelper.domain.Source;
import com.foreflight.apphelper.repository.ResourceRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                    foundResource.setSource(newResource.getSource());
                    return resourceRepository.save(foundResource);
                })
                .orElseGet(() -> {
                    newResource.setId(id);
                    return resourceRepository.save(newResource);
                });
    }

    // Delete a resource by id
    public void deleteResource(Long id, boolean force) {
        if(!resourceRepository.existsById(id)){
            throw new IllegalStateException("Resource with the id " + id + " does not exist.");
        }

        if(force) {
            resourceRepository.deleteRelationsToMenuChoiceById(id);
        }
        try {
            resourceRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex){
            throw new IllegalStateException("Cannot delete Resource with id " + id + " because the Resource is " +
                    "referenced by other entries. Try using a force delete.");
        }
    }

    // Create a Resource object using a data transfer object
    private Resource unpackDTO(ResourceDTO dto){
        Resource resource = new Resource();

        if(dto.getName() == null){
            throw new IllegalStateException("Resource name must not be null");
        }

        resource.setName(dto.getName());
        resource.setLink(dto.getLink());

        try {
            resource.setSource(Source.valueOf(dto.getSource()));
        } catch (IllegalArgumentException ex){
            List<String> sourceNames = Stream.of(Source.values()).map(Source::name).collect(Collectors.toList());
            throw new IllegalArgumentException("Invalid source name: \"" + dto.getSource() +
                    "\". Name must be one of the following: " + sourceNames);
        } catch (NullPointerException ex){
            throw new IllegalArgumentException("Source name cannot be null.");
        }


        return resource;
    }
}
