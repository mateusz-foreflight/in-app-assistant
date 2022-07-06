package com.foreflight.apphelper.service;

import com.foreflight.apphelper.domain.*;
import com.foreflight.apphelper.repository.MetricRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MetricService {

    private final MetricRepository metricRepository;
    private final ResourceService resourceService;
    private final MenuChoiceService menuChoiceService;

    public MetricService(MetricRepository metricRepository, ResourceService resourceService, MenuChoiceService menuChoiceService){
        this.metricRepository = metricRepository;
        this.resourceService = resourceService;
        this.menuChoiceService = menuChoiceService;
    }

    public List<MetricDTO> getAllMetrics() {
        return metricRepository.findAll().stream().map(MetricDTO::assemble).collect(Collectors.toList());
    }

    public Optional<MetricDTO> getMetricById(Long id) {
        return metricRepository.findMetricById(id).map(MetricDTO::assemble);
    }

    public MetricDTO addMetric(MetricCreateDTO metric) {
        Metric newMetric = unpackDTO(metric);
        return MetricDTO.assemble(metricRepository.save(newMetric));
    }

    public MetricDTO updateMetric(MetricCreateDTO metric, Long id) {
        Metric newMetric = unpackDTO(metric);

        return metricRepository.findMetricById(id)
                .map(foundMetric -> {
                    foundMetric.setAnswerFound(newMetric.isAnswerFound());
                    foundMetric.setTimestamp(newMetric.getTimestamp());
                    foundMetric.setTicketLink(newMetric.getTicketLink());
                    foundMetric.setUserName(newMetric.getUserName());
                    foundMetric.setMenuChoices(newMetric.getMenuChoices());
                    foundMetric.setResources(newMetric.getResources());

                    return MetricDTO.assemble(metricRepository.save(foundMetric));
                })
                .orElseGet(() -> {
                    newMetric.setId(id);
                    return MetricDTO.assemble(metricRepository.save(newMetric));
                });
    }

    public void deleteMetric(Long id) {
        if(!metricRepository.existsById(id)){
            throw new IllegalStateException("Metric with the id " + id + " does not exist.");
        }

        metricRepository.deleteById(id);
    }

    private Metric unpackDTO(MetricCreateDTO dto){
        Metric metric = new Metric();

        if(dto.getAnswerFound() == null){
            throw new IllegalStateException("Metric answer found field must not be null");
        }
        metric.setAnswerFound(dto.getAnswerFound());

        metric.setTimestamp(dto.getTimestamp());

        metric.setTicketLink(dto.getTicketLink());

        metric.setUserName(dto.getUserName());

        metric.setUserFeedback(dto.getUserFeedback());

        if(dto.getResourceIds() != null) {
            metric.setResources(resourceService.getUniqueResourcesFromIdList(dto.getResourceIds()));
        }

        if(dto.getMenuchoiceIds() != null) {
            metric.setMenuChoices(menuChoiceService.getUniqueMenuChoicesFromIdList(dto.getMenuchoiceIds()));
        }

        return metric;
    }
}
