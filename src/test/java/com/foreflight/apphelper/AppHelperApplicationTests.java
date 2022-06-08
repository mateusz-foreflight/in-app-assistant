package com.foreflight.apphelper;

import com.foreflight.apphelper.domain.MenuChoice;
import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.repository.MenuChoiceRepository;
import com.foreflight.apphelper.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class AppHelperApplicationTests {

    @Autowired
    private MockMvc mockMvc;

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

        for(MenuChoice mc : menuChoiceRepository.findAll()){
            System.out.println(mc.getName());
        }

        List<MenuChoice> foundChoices = menuChoiceRepository.findAll();
        assertThat(foundChoices).contains(choice1, choice2);
    }

    @Test
    @Transactional
    void resourceRepositoryWorks() throws Exception {
        Resource resource1 = new Resource("test", "testLink");
        Resource resource2 = new Resource("test2", "testLink2");

        resourceRepository.save(resource1);
        resourceRepository.save(resource2);

        List<Resource> foundResources = resourceRepository.findAll();
        assertThat(foundResources).contains(resource1, resource2);

        mockMvc.perform(get("/api/v1/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.name == \"test\" && @.link == \"testLink\")]").exists());


    }

    @Test
    @Transactional
    void getAllMenuChoices() throws Exception{
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource("res1", "link"));
        resources.add(new Resource("res2", "link"));
        resourceRepository.saveAll(resources);

        List<MenuChoice> choices = new ArrayList<>();
        choices.add(new MenuChoice("test1", null, new ArrayList<>()));
        choices.add(new MenuChoice("test2", choices.get(0), new ArrayList<>()));
        choices.add(new MenuChoice("test3", choices.get(1), resources));
        menuChoiceRepository.saveAll(choices);

        mockMvc.perform(get("/api/v1/menuchoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.name == \"test1\" && @.parent == null && @.resources == [])]").exists())
                .andExpect(jsonPath("$.[?(@.name == \"test2\" && @.parent.name == \"test1\"  && @.resources == [])]").exists())
                .andExpect(jsonPath("$.[?(@.name == \"test3\" && @.parent.name == \"test2\" && [\"res2\", \"res1\"] subsetof @.resources[*].name && @.resources.length() == 2)]").exists());
    }

    @Test
    @Transactional
    void getMenuChoiceById() throws Exception{
        MenuChoice choice1 = new MenuChoice("test1", null, null);

        Long id = menuChoiceRepository.save(choice1).getId();

        mockMvc.perform(get("/api/v1/menuchoices/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath(String.format("$.[?(@.name == \"test1\" && @.id == %d)]", id)).exists());
    }

}
