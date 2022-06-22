package com.foreflight.apphelper.service;

import com.foreflight.apphelper.domain.MenuChoice;
import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.domain.ResourceDTO;
import com.foreflight.apphelper.domain.Source;
import com.foreflight.apphelper.repository.ResourceRepository;
import com.foreflight.apphelper.repository.SourceRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final SourceRepository sourceRepository;

    public ResourceService(ResourceRepository resourceRepository, SourceRepository sourceRepository) {
        this.resourceRepository = resourceRepository;
        this.sourceRepository = sourceRepository;
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

    public List<Resource> namesToResources(List<String> resourceNames){
        List<Resource> newResources = new ArrayList<>();

        for(String resourceName : resourceNames){
            Optional<Resource> newResource = this.getResourceByName(resourceName);
            if (!newResource.isPresent()){
                throw new IllegalStateException("No resource with the provided resource name" + resourceName + " exists");
            }
            newResources.add(newResource.get());
        }

        return newResources;
    }

    // Create a Resource object using a data transfer object
    private Resource unpackDTO(ResourceDTO dto){
        Resource resource = new Resource();

        if(dto.getName() == null){
            throw new IllegalStateException("Resource name must not be null");
        }
        if(dto.getLink() == null){
            throw new IllegalStateException("Resource link must not be null");
        }
        if(dto.getSource() == null){
            throw new IllegalStateException("Resource source must not be null");
        }

        resource.setName(dto.getName());
        resource.setLink(dto.getLink());

        Optional<Source> newSource = sourceRepository.findSourceByName(dto.getSource());
        if (!newSource.isPresent()) {
            throw new IllegalStateException("No source with the provided name " + dto.getSource() + " exists");
        }
        resource.setSource(newSource.get());

        return resource;
    }
}
