package com.foreflight.apphelper;

import com.foreflight.apphelper.domain.MenuChoice;
import com.foreflight.apphelper.domain.MenuChoiceDTO;
import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.repository.MenuChoiceRepository;
import com.foreflight.apphelper.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

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
        mockMvc.perform(get("/api/v1/menuchoices/{id}", id))
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
        mockMvc.perform(get("/api/v1/menuchoices/{id}/children", choice1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[?(@.name == \"choice2\" && @.parent.name == \"choice1\" && @.resources == [])]").exists())
                .andExpect(jsonPath("$.[?(@.name == \"choice3\" && @.parent.name == \"choice1\" && @.resources == [])]").exists());
    }

    @Test
    @Transactional
    void getChildrenById_withInvalidId() throws Exception{
        // Given
        MenuChoice choice1 = new MenuChoice("choice1", null, Collections.emptyList());
        MenuChoice choice2 = new MenuChoice("choice2", choice1, Collections.emptyList());
        MenuChoice choice3 = new MenuChoice("choice3", choice1, Collections.emptyList());
        MenuChoice choice4 = new MenuChoice("choice4", choice3, Collections.emptyList());
        List<MenuChoice> choices = Arrays.asList(choice1, choice2, choice3, choice4);
        menuChoiceRepository.saveAll(choices);

        Long invalidId = choice1.getId() + choice2.getId() + choice3.getId() + choice4.getId();

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices/{id}/children", invalidId))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("id")))
                .andExpect(content().string(containsString(String.valueOf(invalidId))));
    }

    @Test
    @Transactional
    void createChoice_withValidChoice() throws Exception{
        // Given
        Resource resource1 = new Resource("resource1", "link");
        MenuChoice choice1 = new MenuChoice("choice1", null, null);
        menuChoiceRepository.save(choice1);
        resourceRepository.save(resource1);
        String inputJson = "{\"choiceName\": \"new\", \"parentName\": \"choice1\", \"resourceNames\": [\"resource1\"]}";

        // When, Then
        mockMvc.perform(post("/api/v1/menuchoices/").content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.name == \"new\" && @.parent.name == \"choice1\" && @.resources[0].name == \"resource1\")]").exists());

        Optional<MenuChoice> newChoice = menuChoiceRepository.findMenuChoiceByName("new");
        assertThat(newChoice.isPresent()).isTrue();
        assertThat(newChoice.get().getParent().getName()).isEqualTo("choice1");
        assertThat(newChoice.get().getResources().get(0).getName()).isEqualTo("resource1");
    }

    @Test
    @Transactional
    void createChoice_withInvalidChoice_withInvalidParent() throws Exception{
        // Given
        String inputJson = "{\"choiceName\": \"new\", \"parentName\": \"choice1\", \"resourceNames\": []}";

        // When, Then
        mockMvc.perform(post("/api/v1/menuchoices/").content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("parent")))
                .andExpect(content().string(containsString("choice1")));

        Optional<MenuChoice> notPresentChoice = menuChoiceRepository.findMenuChoiceByName("new");
        assertThat(notPresentChoice.isPresent()).isFalse();
    }

    @Test
    @Transactional
    void updateChoice_withValidChoice_withPresentId() throws Exception{
        // Given
        Resource resource1 = new Resource("resource1", "link");
        MenuChoice choice1 = new MenuChoice("choice1", null, Collections.emptyList());
        resourceRepository.save(resource1);
        menuChoiceRepository.save(choice1);
        Long id = choice1.getId();
        String inputJson = "{\"choiceName\": \"new\", \"parentName\": null, \"resourceNames\": [\"resource1\"]}";

        // When, Then
        mockMvc.perform(put("/api/v1/menuchoices/{id}", id).content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.name == \"new\" && @.parent == null && @.resources[0].name == \"resource1\")]").exists());

        Optional<MenuChoice> updatedChoice = menuChoiceRepository.findMenuChoiceById(id);
        assertThat(updatedChoice.isPresent()).isTrue();
        assertThat(updatedChoice.get().getName()).isEqualTo("new");
        assertThat(updatedChoice.get().getResources().get(0).getName()).isEqualTo("resource1");
        Optional<MenuChoice> oldChoiceNotPresent = menuChoiceRepository.findMenuChoiceByName("choice1");
        assertThat(oldChoiceNotPresent.isPresent()).isFalse();
    }

    @Test
    @Transactional
    void updateChoice_withValidChoice_withNotPresentId() throws Exception{
        // Given
        Resource resource1 = new Resource("resource1", "link");
        MenuChoice choice1 = new MenuChoice("choice1", null, Collections.emptyList());
        resourceRepository.save(resource1);
        menuChoiceRepository.save(choice1);
        Long id = choice1.getId() + 1;
        String inputJson = "{\"choiceName\": \"new\", \"parentName\": null, \"resourceNames\": [\"resource1\"]}";

        // When, Then
        mockMvc.perform(put("/api/v1/menuchoices/{id}", id).content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.name == \"new\" && @.parent == null && @.resources[0].name == \"resource1\")]").exists());

        Optional<MenuChoice> updatedChoice = menuChoiceRepository.findMenuChoiceById(id);
        assertThat(updatedChoice.isPresent()).isTrue();
        assertThat(updatedChoice.get().getName()).isEqualTo("new");
        assertThat(updatedChoice.get().getResources().get(0).getName()).isEqualTo("resource1");
        Optional<MenuChoice> oldChoicePresent = menuChoiceRepository.findMenuChoiceByName("choice1");
        assertThat(oldChoicePresent.isPresent()).isTrue();
    }

    @Test
    @Transactional
    void deleteChoice_withValidId() throws Exception{
        // Given
        MenuChoice choice1 = new MenuChoice("choice1", null, null);

        Long id = menuChoiceRepository.save(choice1).getId();

        // When, Then
        mockMvc.perform(delete("/api/v1/menuchoices/{id}", id))
                .andExpect(status().isOk());

        Optional<MenuChoice> deletedChoice = menuChoiceRepository.findMenuChoiceById(id);
        assertThat(deletedChoice.isPresent()).isFalse();
        deletedChoice = menuChoiceRepository.findMenuChoiceByName("choice1");
        assertThat(deletedChoice.isPresent()).isFalse();
    }

}
