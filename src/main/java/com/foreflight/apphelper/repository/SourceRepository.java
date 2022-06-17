package com.foreflight.apphelper.repository;

import com.foreflight.apphelper.domain.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SourceRepository extends JpaRepository<Source, Long> {
    Optional<Source> findSourceById(Long id);

    Optional<Source> findSourceByName(String name);
}
