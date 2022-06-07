package com.foreflight.apphelper.service;

import com.foreflight.apphelper.domain.Resource;
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

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public Optional<Resource> getResourceById(Long id) { return resourceRepository.findResourceById(id); }

    public Optional<Resource> getResourceByName(String name) { return resourceRepository.findResourceByName(name); }
}
