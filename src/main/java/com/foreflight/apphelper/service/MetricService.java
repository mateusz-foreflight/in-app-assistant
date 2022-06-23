package com.foreflight.apphelper.service;

import com.foreflight.apphelper.domain.MenuChoice;
import com.foreflight.apphelper.domain.Metric;
import com.foreflight.apphelper.domain.MetricDTO;
import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.repository.MetricRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Metric> getAllMetrics() {
        return metricRepository.findAll();
    }

    public Optional<Metric> getMetricById(Long id) {
        return metricRepository.findMetricById(id);
    }

    public Metric addMetric(MetricDTO metric) {
        Metric newMetric = unpackDTO(metric);
        return metricRepository.save(newMetric);
    }

    public Metric updateMetric(MetricDTO metric, Long id) {
        Metric newMetric = unpackDTO(metric);

        return metricRepository.findMetricById(id)
                .map(foundMetric -> {
                    foundMetric.setAnswerFound(newMetric.isAnswerFound());
                    foundMetric.setTimestamp(newMetric.getTimestamp());
                    foundMetric.setTicketLink(newMetric.getTicketLink());
                    foundMetric.setUserName(newMetric.getUserName());
                    foundMetric.setMenuChoices(newMetric.getMenuChoices());
                    foundMetric.setResources(newMetric.getResources());

                    return metricRepository.save(foundMetric);
                })
                .orElseGet(() -> {
                    newMetric.setId(id);
                    return metricRepository.save(newMetric);
                });
    }

    public void deleteMetric(Long id) {
        if(!metricRepository.existsById(id)){
            throw new IllegalStateException("Metric with the id " + id + " does not exist.");
        }

        metricRepository.deleteById(id);
    }

    private Metric unpackDTO(MetricDTO dto){
        Metric metric = new Metric();

        if(dto.isAnswerFound() == null){
            throw new IllegalStateException("Metric answer found field must not be null");
        }
        metric.setAnswerFound(dto.isAnswerFound());

        System.out.println(dto.getTimestamp());
        metric.setTimestamp(dto.getTimestamp());

        metric.setTicketLink(dto.getTicketLink());

        metric.setUserName(dto.getUserName());

        if(dto.getResourceNames() != null) {
            metric.setResources(resourceService.namesToResources(dto.getResourceNames()));
        }

        if(dto.getMenuchoiceNames() != null) {
            metric.setMenuChoices(menuChoiceService.namesToMenuChoices(dto.getMenuchoiceNames()));
        }

        return metric;
    }
}
