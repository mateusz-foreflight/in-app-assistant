package com.foreflight.apphelper.integrationtests;

import org.checkerframework.checker.units.qual.C;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class DatabaseCleaner {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void clean(){
        Query query = entityManager.createNativeQuery("DELETE FROM metric_menuchoice;" +
                "DELETE FROM metric_resource;" +
                "DELETE FROM metric;" +
                "DELETE FROM menuchoice_resource;" +
                "DELETE FROM menuchoice;" +
                "DELETE FROM resource;" +
                "DELETE FROM source;");
        query.executeUpdate();
    }
}
