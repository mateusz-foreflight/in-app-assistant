package com.foreflight.apphelper.controller;

import com.foreflight.apphelper.domain.Source;
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
    public List<Source> getAllSources(){
        return sourceService.getAllSources();
    }

    @GetMapping(path="{sourceId}")
    public Optional<Source> getSourceById(@PathVariable("sourceId") Long id){
        return sourceService.getSourceById(id);
    }

    @PostMapping
    public Source createSource(@RequestBody SourceDTO source){
        return sourceService.addSource(source);
    }

    @PutMapping(path = "{sourceId}")
    public Source updateSource(@RequestBody SourceDTO source, @PathVariable("sourceId") Long id){
        return sourceService.updateSource(source, id);
    }

    @DeleteMapping(path="{sourceId}")
    public void deleteSource(@PathVariable("sourceId") Long id){
        sourceService.deleteSource(id);
    }
}
