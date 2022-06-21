package com.foreflight.apphelper.service;

import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.domain.Source;
import com.foreflight.apphelper.domain.SourceDTO;
import com.foreflight.apphelper.repository.ResourceRepository;
import com.foreflight.apphelper.repository.SourceRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SourceService {
    private final SourceRepository sourceRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceService resourceService;

    public SourceService(SourceRepository sourceRepository, ResourceService resourceService,
                         ResourceRepository resourceRepository){
        this.sourceRepository = sourceRepository;
        this.resourceService = resourceService;
        this.resourceRepository = resourceRepository;
    }

    public List<Source> getAllSources() {
        return sourceRepository.findAll();
    }

    public Optional<Source> getSourceById(Long id) {
        return sourceRepository.findSourceById(id);
    }

    public Optional<Source> getSourceByName(String name) {
        return sourceRepository.findSourceByName(name);
    }

    public Source addSource(SourceDTO source) {
        Source newSource = unpackDTO(source);
        return sourceRepository.save(newSource);
    }

    public Source updateSource(SourceDTO source, Long id){
        Source newSource = unpackDTO(source);
        return sourceRepository.findSourceById(id)
                .map(foundSource -> {
                    foundSource.setName(newSource.getName());
                    foundSource.setLink(newSource.getLink());
                    return sourceRepository.save(foundSource);
                })
                .orElseGet(() -> {
                    newSource.setId(id);
                    return sourceRepository.save(newSource);
                });
    }

    public void deleteSource(Long id, boolean force){
        if(!sourceRepository.existsById(id)){
            throw new IllegalStateException("Source with the id " + id + " does not exist.");
        }

        if(force){
            // Get all resources that have this source and delete them
            Optional<Source> source = sourceRepository.findSourceById(id);
            assert(source.isPresent());
            List<Resource> resources = resourceRepository.findResourcesBySource(source.get());
            for(Resource resource : resources){
                resourceService.deleteResource(resource.getId(), true);
            }
        }

        try{
            sourceRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex){
            throw new IllegalStateException("Cannot delete Source with id " + id + " because the source has " +
                    "resources that rely on it. Try using a force delete.");
        }
    }

    private Source unpackDTO(SourceDTO dto){
        Source source = new Source();

        if(dto.getName() == null){
            throw new IllegalStateException("Source name must not be null");
        }
        if(dto.getLink() == null){
            throw new IllegalStateException("Source link must not be null");
        }

        source.setName(dto.getName());
        source.setLink(dto.getLink());

        return source;
    }
}
