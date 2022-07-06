package com.foreflight.apphelper.service;

import com.foreflight.apphelper.domain.*;
import com.foreflight.apphelper.repository.ResourceRepository;
import com.foreflight.apphelper.repository.SourceRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<SourceDTO> getAllSources() {
        return sourceRepository.findAll().stream().map(SourceDTO::assemble).collect(Collectors.toList());
    }

    public Optional<SourceDTO> getSourceById(Long id) {
        return sourceRepository.findSourceById(id).map(SourceDTO::assemble);
    }

    public SourceDTO addSource(SourceCreateDTO source) {
        Source newSource = unpackDTO(source);
        return SourceDTO.assemble(sourceRepository.save(newSource));
    }

    public SourceDTO updateSource(SourceCreateDTO source, Long id){
        Source newSource = unpackDTO(source);
        return sourceRepository.findSourceById(id)
                .map(foundSource -> {
                    foundSource.setName(newSource.getName());
                    foundSource.setLink(newSource.getLink());
                    return SourceDTO.assemble(sourceRepository.save(foundSource));
                })
                .orElseGet(() -> {
                    newSource.setId(id);
                    return SourceDTO.assemble(sourceRepository.save(newSource));
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

    private Source unpackDTO(SourceCreateDTO dto){
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
