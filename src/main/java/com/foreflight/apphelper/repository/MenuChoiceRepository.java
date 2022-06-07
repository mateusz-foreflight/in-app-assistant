package com.foreflight.apphelper.repository;

import com.foreflight.apphelper.domain.MenuChoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuChoiceRepository extends JpaRepository<MenuChoice, Long> {
    Optional<MenuChoice> findMenuChoiceById(Long id);

    Optional<MenuChoice> findMenuChoiceByName(String name);

}
