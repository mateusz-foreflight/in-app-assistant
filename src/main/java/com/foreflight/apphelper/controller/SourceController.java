package com.foreflight.apphelper.controller;

import com.foreflight.apphelper.domain.Source;
import com.foreflight.apphelper.domain.SourceCreateDTO;
import com.foreflight.apphelper.domain.SourceDTO;
import com.foreflight.apphelper.service.SourceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/sources")
public class SourceController {
    private final SourceService sourceService;

    public SourceController(SourceService service){
        sourceService = service;
    }

    @GetMapping
    public List<SourceDTO> getAllSources(){
        return sourceService.getAllSources();
    }

    @GetMapping(path="{sourceId}")
    public Optional<SourceDTO> getSourceById(@PathVariable("sourceId") Long id){
        return sourceService.getSourceById(id);
    }

    @PostMapping
    public SourceDTO createSource(@RequestBody SourceCreateDTO source){
        return sourceService.addSource(source);
    }

    @PutMapping(path = "{sourceId}")
    public SourceDTO updateSource(@RequestBody SourceCreateDTO source, @PathVariable("sourceId") Long id){
        return sourceService.updateSource(source, id);
    }

    @DeleteMapping(path="{sourceId}")
    public void deleteSource(@PathVariable("sourceId") Long id, @RequestParam(defaultValue = "false") boolean force){
        sourceService.deleteSource(id, force);
    }
}
