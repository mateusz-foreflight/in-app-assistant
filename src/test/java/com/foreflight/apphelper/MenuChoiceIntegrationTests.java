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


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class MenuChoiceIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuChoiceRepository menuChoiceRepository;

    @Autowired
    private ResourceRepository resourceRepository;



    @Test
    void contextLoads() {
    }

//    @Test
//    @Transactional
//    void menuChoiceRepositoryWorks(){
//        MenuChoice choice1 = new MenuChoice("test", null, null);
//        MenuChoice choice2 = new MenuChoice("test2", choice1, null);
//
//        menuChoiceRepository.save(choice1);
//        menuChoiceRepository.save(choice2);
//
//        for(MenuChoice mc : menuChoiceRepository.findAll()){
//            System.out.println(mc.getName());
//        }
//
//        List<MenuChoice> foundChoices = menuChoiceRepository.findAll();
//        assertThat(foundChoices).contains(choice1, choice2);
//    }
//
//    @Test
//    @Transactional
//    void resourceRepositoryWorks() throws Exception {
//        Resource resource1 = new Resource("test", "testLink");
//        Resource resource2 = new Resource("test2", "testLink2");
//
//        resourceRepository.save(resource1);
//        resourceRepository.save(resource2);
//
//        List<Resource> foundResources = resourceRepository.findAll();
//        assertThat(foundResources).contains(resource1, resource2);
//
//        mockMvc.perform(get("/api/v1/resources"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[?(@.name == \"test\" && @.link == \"testLink\")]").exists());
//    }

    @Test
    @Transactional
    void getAllChoices() throws Exception{
        // Given
        Resource resource1 = new Resource("res1", "link");
        Resource resource2 = new Resource("res2", "link");
        List<Resource> resources = Arrays.asList(resource1, resource2);
        resourceRepository.saveAll(resources);

        MenuChoice choice1 = new MenuChoice("choice1", null, Collections.emptyList());
        MenuChoice choice2 = new MenuChoice("choice2", choice1, Collections.emptyList());
        MenuChoice choice3 = new MenuChoice("choice3", choice2, resources);
        List<MenuChoice> choices = Arrays.asList(choice1, choice2, choice3);
        menuChoiceRepository.saveAll(choices);

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[?(@.name == \"choice1\" && @.parent == null && @.resources == [])]").exists())
                .andExpect(jsonPath("$.[?(@.name == \"choice2\" && @.parent.name == \"choice1\"  && @.resources == [])]").exists())
                .andExpect(jsonPath("$.[?(@.name == \"choice3\" && @.parent.name == \"choice2\" && [\"res2\", \"res1\"] subsetof @.resources[*].name && @.resources.length() == 2)]").exists());
    }

    @Test
    @Transactional
    void getChoiceById() throws Exception{
        // Given
        MenuChoice choice1 = new MenuChoice("choice1", null, null);

        Long id = menuChoiceRepository.save(choice1).getId();

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath(String.format("$.[?(@.name == \"choice1\" && @.id == %d)]", id)).exists());
    }

    @Test
    @Transactional
    void getTopLevelChoices() throws Exception{
        // Given
        MenuChoice choice1 = new MenuChoice("choice1", null, Collections.emptyList());
        MenuChoice choice2 = new MenuChoice("choice2", choice1, Collections.emptyList());
        MenuChoice choice3 = new MenuChoice("choice3", choice2, Collections.emptyList());
        MenuChoice choice4 = new MenuChoice("choice4", null, Collections.emptyList());
        List<MenuChoice> choices = Arrays.asList(choice1, choice2, choice3, choice4);
        menuChoiceRepository.saveAll(choices);

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices/toplevel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[?(@.name == \"choice1\" && @.parent == null && @.resources == [])]").exists())
                .andExpect(jsonPath("$.[?(@.name == \"choice4\" && @.parent == null && @.resources == [])]").exists());
    }

    @Test
    @Transactional
    void getChildrenById_withValidId() throws Exception{
        // Given
        MenuChoice choice1 = new MenuChoice("choice1", null, Collections.emptyList());
        MenuChoice choice2 = new MenuChoice("choice2", choice1, Collections.emptyList());
        MenuChoice choice3 = new MenuChoice("choice3", choice1, Collections.emptyList());
        MenuChoice choice4 = new MenuChoice("choice4", choice3, Collections.emptyList());
        List<MenuChoice> choices = Arrays.asList(choice1, choice2, choice3, choice4);
        menuChoiceRepository.saveAll(choices);

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices/" + choice1.getId() + "/children"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[?(@.name == \"choice2\" && @.parent.name == \"choice1\" && @.resources == [])]").exists())
                .andExpect(jsonPath("$.[?(@.name == \"choice3\" && @.parent.name == \"choice1\" && @.resources == [])]").exists());
    }

}
