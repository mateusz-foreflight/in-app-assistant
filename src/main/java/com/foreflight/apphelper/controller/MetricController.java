package com.foreflight.apphelper.controller;

import com.foreflight.apphelper.domain.Metric;
import com.foreflight.apphelper.domain.MetricCreateDTO;
import com.foreflight.apphelper.domain.MetricDTO;
import com.foreflight.apphelper.service.MetricService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/metrics")
public class MetricController {

    private final MetricService metricService;

    public MetricController(MetricService metricService){
        this.metricService = metricService;
    }

    @GetMapping
    public List<MetricDTO> getAllMetrics() {
        return metricService.getAllMetrics();
    }

    @GetMapping(path = "{metricId}")
    public Optional<MetricDTO> getMetricById(@PathVariable("metricId") Long id){
        return metricService.getMetricById(id);
    }

    @PostMapping
    public MetricDTO createMetric(@RequestBody MetricCreateDTO metric){
        return metricService.addMetric(metric);
    }

    @PutMapping(path = "{metricId}")
    public MetricDTO updateMetric(@RequestBody MetricCreateDTO metric, @PathVariable("metricId") Long id){
        return metricService.updateMetric(metric, id);
    }

    @DeleteMapping(path = "{metricId}")
    public void deleteMetric(@PathVariable("metricId") Long id){
        metricService.deleteMetric(id);
    }
}
