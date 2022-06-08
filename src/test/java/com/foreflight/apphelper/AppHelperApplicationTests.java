package com.foreflight.apphelper;

import com.foreflight.apphelper.domain.MenuChoice;
import com.foreflight.apphelper.repository.MenuChoiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AppHelperApplicationTests {

    @Autowired
    private MenuChoiceRepository menuChoiceRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testDb(){
        MenuChoice choice = new MenuChoice("test", null, null);

        menuChoiceRepository.save(choice);

        for(MenuChoice foundChoice : menuChoiceRepository.findAll()){
            System.out.println(foundChoice.getName());
        }
    }

    @Test
    void testDb2(){
        MenuChoice choice = new MenuChoice("test2", null, null);

        menuChoiceRepository.save(choice);

        for(MenuChoice foundChoice : menuChoiceRepository.findAll()){
            System.out.println(foundChoice.getName());
        }
    }


}
