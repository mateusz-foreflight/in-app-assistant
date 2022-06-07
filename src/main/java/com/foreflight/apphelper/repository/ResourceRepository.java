package com.foreflight.apphelper.repository;

import com.foreflight.apphelper.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Optional<Resource> findResourceById(Long id);

    Optional<Resource> findResourceByName(String name);
}
