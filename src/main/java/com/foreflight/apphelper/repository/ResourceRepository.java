package com.foreflight.apphelper.repository;

import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.domain.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Optional<Resource> findResourceById(Long id);

    Optional<Resource> findResourceByName(String name);

    List<Resource> findResourcesBySource(Source source);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM menuchoice_resource WHERE resource_id = ?1",
            nativeQuery = true)
    void deleteRelationsToMenuChoiceById(Long id);
}
