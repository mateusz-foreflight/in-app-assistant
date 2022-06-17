package com.foreflight.apphelper.repository;

import com.foreflight.apphelper.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Optional<Resource> findResourceById(Long id);

    Optional<Resource> findResourceByName(String name);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM menuchoice_resource WHERE resource_id = ?1",
            nativeQuery = true)
    void deleteRelationsToMenuChoiceById(Long id);
}
