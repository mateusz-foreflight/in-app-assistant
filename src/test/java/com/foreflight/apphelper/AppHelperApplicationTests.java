package com.foreflight.apphelper;

import com.foreflight.apphelper.domain.MenuChoice;
import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.repository.MenuChoiceRepository;
import com.foreflight.apphelper.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AppHelperApplicationTests {

    @Autowired
    private MenuChoiceRepository menuChoiceRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    void menuChoiceRepositoryWorks(){
        MenuChoice choice1 = new MenuChoice("test", null, null);
        MenuChoice choice2 = new MenuChoice("test2", choice1, null);

        menuChoiceRepository.save(choice1);
        menuChoiceRepository.save(choice2);

        List<MenuChoice> foundChoices = menuChoiceRepository.findAll();
        assertThat(foundChoices).contains(choice1, choice2);
    }

    @Test
    @Transactional
    void resourceRepositoryWorks(){
        Resource resource1 = new Resource("test", "testLink");
        Resource resource2 = new Resource("test2", "testLink2");

        resourceRepository.save(resource1);
        resourceRepository.save(resource2);

        List<Resource> foundResources = resourceRepository.findAll();
        assertThat(foundResources).contains(resource1, resource2);
    }

}
