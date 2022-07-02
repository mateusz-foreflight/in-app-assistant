package com.foreflight.apphelper.repository;

import com.foreflight.apphelper.domain.MenuChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MenuChoiceRepository extends JpaRepository<MenuChoice, Long> {
    Optional<MenuChoice> findMenuChoiceById(Long id);

    Optional<MenuChoice> findMenuChoiceByName(String name);

    @Query(
            value = "SELECT * FROM menuchoice WHERE parent_id is null",
            nativeQuery = true)
    List<MenuChoice> findAllTopLevelMenuChoices();

    @Query(
            value = "SELECT * FROM menuchoice WHERE parent_id = ?1",
            nativeQuery = true)
    List<MenuChoice> findChildMenuChoicesById(Long id);


    @Query(
            value = "WITH RECURSIVE " +
                    "    q AS " +
                    "    ( " +
                    "        SELECT  id " +
                    "        FROM    menuchoice mc_p " +
                    "        WHERE   id = ?1 " +
                    "        UNION ALL " +
                    "        SELECT  mc_c.id " +
                    "        FROM    q " +
                    "        JOIN    menuchoice mc_c " +
                    "        ON      mc_c.parent_id = q.id " +
                    "    ) " +
                    "SELECT * " +
                    "FROM q",
            nativeQuery = true
    )
    List<MenuChoice> findDescendentMenuChoicesById(Long id);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM metric_menuchoice WHERE menuchoice_id = ?1",
            nativeQuery = true)
    void deleteRelationsToMetricById(Long id);
}
