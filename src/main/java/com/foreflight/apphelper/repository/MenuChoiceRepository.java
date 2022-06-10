package com.foreflight.apphelper.repository;

import com.foreflight.apphelper.domain.MenuChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

}
