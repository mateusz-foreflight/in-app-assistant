package com.foreflight.apphelper.repository;

import com.foreflight.apphelper.domain.MenuChoice;
import com.foreflight.apphelper.domain.Metric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetricRepository extends JpaRepository<Metric, Long> {
    Optional<Metric> findMetricById(Long id);
}
