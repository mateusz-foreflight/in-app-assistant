package com.foreflight.apphelper.integrationtests;

import com.foreflight.apphelper.domain.MenuChoice;
import com.foreflight.apphelper.domain.MenuChoiceDTO;
import com.foreflight.apphelper.domain.Metric;
import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.repository.MenuChoiceRepository;
import com.foreflight.apphelper.repository.MetricRepository;
import com.foreflight.apphelper.repository.ResourceRepository;
import com.foreflight.apphelper.repository.SourceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


import static org.hamcrest.core.StringContains.containsString;
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
    @Autowired
    private SourceRepository sourceRepository;
    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private DatabaseCleaner dbCleaner;

    private final ObjectMapper mapper = new ObjectMapper();

    private final MockDatabaseEntries entries = new MockDatabaseEntries();

    @BeforeEach
    void init(){
        entries.initEntries();

        sourceRepository.save(entries.source1);
        resourceRepository.save(entries.resource1);
        resourceRepository.save(entries.resource2);
    }

    @AfterEach
    void cleanup(){
        dbCleaner.clean();
    }


    // Generate a jsonPath string  from a given menu choice
    // The string will look for a json object with the given choice's name, the choice's parent's name, and the
    // choice's resource's names
    private String menuChoiceToJsonPathStr(MenuChoice choice){
        String nameStr = "@.name == \"" + choice.getName() +"\"";

        String parentStr = choice.getParent() == null ? "@.parent == null" : "@.parent.name == \"" + choice.getParent().getName() + "\"";

        String resourceStr;
        if(choice.getResources().size() == 0){
            resourceStr = "@.resources == []";
        }
        else{
            // If resources are present, check if each of choice's resources can be found in the json resources list
            // and if that list has the same length as choice's resource list.
            resourceStr = "[";
            for(Resource resource : choice.getResources()){
                resourceStr += "\"" + resource.getName() + "\",";
            }
            resourceStr = resourceStr.substring(0, resourceStr.length() - 1);
            resourceStr += "] subsetof @.resources[*].name && @.resources.length() ==" + choice.getResources().size();
        }

        return String.format("$.[?(%1$s && %2$s && %3$s)]", nameStr, parentStr, resourceStr);
    }

    @Test    
    void contextLoads() {
    }

    @Test    
    void getAllChoices_withOneChoice() throws Exception{
        // Given
        menuChoiceRepository.save(entries.menuChoice1);

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath(menuChoiceToJsonPathStr(entries.menuChoice1)).exists());
    }

    @Test    
    void getAllChoices_withMultipleChoices() throws Exception{
        // Given
        List<MenuChoice> choices = Arrays.asList(entries.menuChoice1, entries.menuChoice2, entries.menuChoice3);
        menuChoiceRepository.saveAll(choices);

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath(menuChoiceToJsonPathStr(entries.menuChoice1)).exists())
                .andExpect(jsonPath(menuChoiceToJsonPathStr(entries.menuChoice2)).exists())
                .andExpect(jsonPath(menuChoiceToJsonPathStr(entries.menuChoice3)).exists());
    }

    @Test    
    void getAllChoices_withNoChoices() throws Exception{
        // Given

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }


    @Test    
    void getChoiceById_withValidId() throws Exception{
        // Given
        Long id = menuChoiceRepository.save(entries.menuChoice1).getId();

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath(String.format("$.[?(@.name == \"%1$s\" && @.id == %2$d)]", entries.menuChoice1.getName(), id)).exists())
                .andExpect(jsonPath(menuChoiceToJsonPathStr(entries.menuChoice1)).exists());
    }

    @Test    
    void getChoiceById_withInvalidId() throws Exception{
        // Given
        Long id = menuChoiceRepository.save(entries.menuChoice1).getId();

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices/{id}", id * 100 + 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath(String.format("$.[?(@.name == \"choice1\" && @.id == %d)]", id)).doesNotExist())
                .andExpect(jsonPath(menuChoiceToJsonPathStr(entries.menuChoice1)).doesNotExist());
    }

    @Test    
    void getTopLevelChoices_withNoTopLevel() throws Exception{
        // Given

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices/toplevel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test    
    void getTopLevelChoices_withOneTopLevel() throws Exception{
        // Given
        menuChoiceRepository.save(entries.menuChoice1);

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices/toplevel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath(menuChoiceToJsonPathStr(entries.menuChoice1)).exists());
    }

    @Test    
    void getTopLevelChoices_withMultipleTopLevel_withMultipleDescendents() throws Exception{
        // Given
        menuChoiceRepository.save(entries.menuChoice1); // Top level
        menuChoiceRepository.save(entries.menuChoice3); // Top level
        menuChoiceRepository.save(entries.menuChoice2); // Second level
        menuChoiceRepository.save(entries.menuChoice4); // Third level

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices/toplevel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath(menuChoiceToJsonPathStr(entries.menuChoice1)).exists())
                .andExpect(jsonPath(menuChoiceToJsonPathStr(entries.menuChoice3)).exists())
                .andExpect(jsonPath(menuChoiceToJsonPathStr(entries.menuChoice2)).doesNotExist())
                .andExpect(jsonPath(menuChoiceToJsonPathStr(entries.menuChoice4)).doesNotExist());
    }

    @Test    
    void getChildrenById_withInvalidId() throws Exception{
        // Given
        Long id = menuChoiceRepository.save(entries.menuChoice1).getId();
        Long childId = menuChoiceRepository.save(entries.menuChoice2).getId();
        Long invalidId = id + childId;

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices/{id}/children", invalidId))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("id")))
                .andExpect(content().string(containsString(String.valueOf(invalidId))));
    }

    @Test    
    void getChildrenById_withNoChildren() throws Exception{
        // Given
        Long id = menuChoiceRepository.save(entries.menuChoice1).getId();
        menuChoiceRepository.save(entries.menuChoice3);

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices/{id}/children", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test    
    void getChildrenById_withOneChild() throws Exception{
        // Given
        Long id = menuChoiceRepository.save(entries.menuChoice1).getId();
        menuChoiceRepository.save(entries.menuChoice2); // child of 1
        menuChoiceRepository.save(entries.menuChoice3);

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices/{id}/children", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath(menuChoiceToJsonPathStr(entries.menuChoice2)).exists());
    }

    @Test    
    void getChildrenById_withMultipleChildren_withDescendents() throws Exception{
        // Given
        Long id = menuChoiceRepository.save(entries.menuChoice1).getId();
        menuChoiceRepository.save(entries.menuChoice5); // child of 1
        menuChoiceRepository.save(entries.menuChoice2); // child of 1
        menuChoiceRepository.save(entries.menuChoice4); // child of 2
        menuChoiceRepository.save(entries.menuChoice3);

        // When, Then
        mockMvc.perform(get("/api/v1/menuchoices/{id}/children", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath(menuChoiceToJsonPathStr(entries.menuChoice2)).exists())
                .andExpect(jsonPath(menuChoiceToJsonPathStr(entries.menuChoice5)).exists());
    }

    @Test    
    void createChoice_withBlankParentName_withNoResources() throws Exception{
        // Given
        MenuChoiceDTO dto = new MenuChoiceDTO("new", null, Collections.emptyList());
        MenuChoice choice = new MenuChoice("new", null, Collections.emptyList());
        String inputJson = mapper.writeValueAsString(dto);

        // When, Then
        mockMvc.perform(post("/api/v1/menuchoices/").content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(menuChoiceToJsonPathStr(choice)).exists());

        Optional<MenuChoice> newChoice = menuChoiceRepository.findMenuChoiceByName("new");
        assertThat(newChoice.isPresent()).isTrue();
        choice.setId(newChoice.get().getId());
        assertThat(newChoice.get()).isEqualTo(choice);
    }

    @Test    
    void createChoice_withPresentParentName_withMultipleResources() throws Exception{
        // Given
        menuChoiceRepository.save(entries.menuChoice1);
        MenuChoiceDTO dto = new MenuChoiceDTO("new", entries.menuChoice1.getName(), Arrays.asList(entries.resource1.getName(), entries.resource2.getName()));
        MenuChoice choice = new MenuChoice("new", entries.menuChoice1, Arrays.asList(entries.resource1, entries.resource2));
        String inputJson = mapper.writeValueAsString(dto);

        // When, Then
        mockMvc.perform(post("/api/v1/menuchoices/").content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(menuChoiceToJsonPathStr(choice)).exists());

        Optional<MenuChoice> newChoice = menuChoiceRepository.findMenuChoiceByName("new");
        assertThat(newChoice.isPresent()).isTrue();
        choice.setId(newChoice.get().getId());
        assertThat(choice).isEqualTo(newChoice.get());
    }

    @Test    
    void createChoice_withDuplicateResources() throws Exception{
        // Given
        menuChoiceRepository.save(entries.menuChoice1);
        MenuChoiceDTO dto = new MenuChoiceDTO("new", entries.menuChoice1.getName(), Arrays.asList(entries.resource1.getName(), entries.resource1.getName()));
        MenuChoice choice = new MenuChoice("new", entries.menuChoice1, Collections.singletonList(entries.resource1));
        String inputJson = mapper.writeValueAsString(dto);

        // When, Then
        mockMvc.perform(post("/api/v1/menuchoices/").content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(menuChoiceToJsonPathStr(choice)).exists());

        Optional<MenuChoice> newChoice = menuChoiceRepository.findMenuChoiceByName("new");
        assertThat(newChoice.isPresent()).isTrue();
        choice.setId(newChoice.get().getId());
        assertThat(choice).isEqualTo(newChoice.get());
    }

    @Test    
    void createChoice_withEmptyName() throws Exception{
        // Given
        MenuChoiceDTO dto = new MenuChoiceDTO(null, null, Collections.emptyList());
        String inputJson = mapper.writeValueAsString(dto);

        // When, Then
        mockMvc.perform(post("/api/v1/menuchoices/").content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("name")));
    }

    @Test    
    void createChoice_withDuplicateName() throws Exception{
        // Given
        menuChoiceRepository.save(entries.menuChoice1);
        MenuChoiceDTO dto = new MenuChoiceDTO(entries.menuChoice1.getName(), null, Collections.emptyList());
        String inputJson = mapper.writeValueAsString(dto);

        // When, Then
        mockMvc.perform(post("/api/v1/menuchoices/").content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Optional<MenuChoice> notPresentChoice = menuChoiceRepository.findMenuChoiceByName("new");
        assertThat(notPresentChoice.isPresent()).isFalse();
    }

    @Test    
    void createChoice_withNotPresentParentName() throws Exception{
        // Given
        menuChoiceRepository.save(entries.menuChoice1);
        MenuChoiceDTO dto = new MenuChoiceDTO("new", "doesNotExist", Collections.emptyList());
        String inputJson = mapper.writeValueAsString(dto);

        // When, Then
        mockMvc.perform(post("/api/v1/menuchoices/").content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Optional<MenuChoice> notPresentChoice = menuChoiceRepository.findMenuChoiceByName("new");
        assertThat(notPresentChoice.isPresent()).isFalse();
    }

    @Test    
    void createChoice_withNotPresentResourceName() throws Exception{
        // Given
        menuChoiceRepository.save(entries.menuChoice1);
        MenuChoiceDTO dto = new MenuChoiceDTO("new", null, Collections.singletonList("doesNotExist"));
        String inputJson = mapper.writeValueAsString(dto);

        // When, Then
        mockMvc.perform(post("/api/v1/menuchoices/").content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Optional<MenuChoice> notPresentChoice = menuChoiceRepository.findMenuChoiceByName("new");
        assertThat(notPresentChoice.isPresent()).isFalse();
    }

    @Test    
    void createChoice_withSelfParentName() throws Exception{
        // Given
        menuChoiceRepository.save(entries.menuChoice1);
        MenuChoiceDTO dto = new MenuChoiceDTO("new", "new", Collections.emptyList());
        String inputJson = mapper.writeValueAsString(dto);

        // When, Then
        mockMvc.perform(post("/api/v1/menuchoices/").content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Optional<MenuChoice> notPresentChoice = menuChoiceRepository.findMenuChoiceByName("new");
        assertThat(notPresentChoice.isPresent()).isFalse();
    }

    @Test    
    void updateChoice_withNotPresentId() throws Exception{
        // Given
        MenuChoice choice = new MenuChoice("new", null, Collections.emptyList());
        MenuChoiceDTO dto = new MenuChoiceDTO("new", null, Collections.emptyList());
        String inputJson = mapper.writeValueAsString(dto);
        Long id = 1L;
        for(MenuChoice c : menuChoiceRepository.findAll()){
            id += c.getId();
        }


        // When, Then
        mockMvc.perform(put("/api/v1/menuchoices/{id}", id).content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(menuChoiceToJsonPathStr(choice)).exists());

        System.out.println(id);
        for(MenuChoice c : menuChoiceRepository.findAll()){
            System.out.println(c);
        }
        Optional<MenuChoice> updatedChoice = menuChoiceRepository.findMenuChoiceByName("new");
        assertThat(updatedChoice.isPresent()).isTrue();
        choice.setId(updatedChoice.get().getId());
        assertThat(updatedChoice.get()).isEqualTo(choice);
    }

    @Test    
    void updateChoice_withPresentId() throws Exception{
        // Given
        menuChoiceRepository.save(entries.menuChoice1);
        String oldName = entries.menuChoice1.getName();
        MenuChoice choice = new MenuChoice("new", null, Collections.emptyList());
        MenuChoiceDTO dto = new MenuChoiceDTO("new", null, Collections.emptyList());
        String inputJson = mapper.writeValueAsString(dto);
        Long id = entries.menuChoice1.getId();
        choice.setId(id);

        // When, Then
        mockMvc.perform(put("/api/v1/menuchoices/{id}", id).content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(menuChoiceToJsonPathStr(choice)).exists());

        Optional<MenuChoice> updatedChoice = menuChoiceRepository.findMenuChoiceById(id);
        assertThat(updatedChoice.isPresent()).isTrue();
        assertThat(updatedChoice.get()).isEqualTo(choice);
        Optional<MenuChoice> oldChoiceNotPresent = menuChoiceRepository.findMenuChoiceByName(oldName);
        assertThat(oldChoiceNotPresent.isPresent()).isFalse();
    }

    @Test    
    void updateChoice_withPresentId_withSelfParentName() throws Exception{
        // Given
        menuChoiceRepository.save(entries.menuChoice1);
        String oldName = entries.menuChoice1.getName();
        MenuChoiceDTO dto = new MenuChoiceDTO("new", "new", Collections.emptyList());
        String inputJson = mapper.writeValueAsString(dto);
        Long id = entries.menuChoice1.getId();

        // When, Then
        mockMvc.perform(put("/api/v1/menuchoices/{id}", id).content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Optional<MenuChoice> updatedChoiceNotPresent = menuChoiceRepository.findMenuChoiceByName("new");
        assertThat(updatedChoiceNotPresent.isPresent()).isFalse();
        Optional<MenuChoice> oldChoice = menuChoiceRepository.findMenuChoiceById(id);
        assertThat(oldChoice.isPresent()).isTrue();
        assertThat(oldChoice.get().getName()).isEqualTo(oldName);
    }

    @Test    
    void updateChoice_withPresentId_withChildParentName() throws Exception{
        // Given
        menuChoiceRepository.save(entries.menuChoice1);
        menuChoiceRepository.save(entries.menuChoice2); // child
        String oldName = entries.menuChoice1.getName();
        MenuChoiceDTO dto = new MenuChoiceDTO("new", entries.menuChoice2.getName(), Collections.emptyList());
        String inputJson = mapper.writeValueAsString(dto);
        Long id = entries.menuChoice1.getId();

        // When, Then
        mockMvc.perform(put("/api/v1/menuchoices/{id}", id).content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Optional<MenuChoice> updatedChoiceNotPresent = menuChoiceRepository.findMenuChoiceByName("new");
        assertThat(updatedChoiceNotPresent.isPresent()).isFalse();
        Optional<MenuChoice> oldChoice = menuChoiceRepository.findMenuChoiceById(id);
        assertThat(oldChoice.isPresent()).isTrue();
        assertThat(oldChoice.get().getName()).isEqualTo(oldName);
    }

    @Test    
    void updateChoice_withPresentId_withDistantDescendentParentName() throws Exception{
        // Given
        menuChoiceRepository.save(entries.menuChoice1);
        menuChoiceRepository.save(entries.menuChoice2); // child
        menuChoiceRepository.save(entries.menuChoice4); // descendent
        String oldName = entries.menuChoice1.getName();
        MenuChoiceDTO dto = new MenuChoiceDTO("new", entries.menuChoice4.getName(), Collections.emptyList());
        String inputJson = mapper.writeValueAsString(dto);
        Long id = entries.menuChoice1.getId();

        // When, Then
        mockMvc.perform(put("/api/v1/menuchoices/{id}", id).content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Optional<MenuChoice> updatedChoiceNotPresent = menuChoiceRepository.findMenuChoiceByName("new");
        assertThat(updatedChoiceNotPresent.isPresent()).isFalse();
        Optional<MenuChoice> oldChoice = menuChoiceRepository.findMenuChoiceById(id);
        assertThat(oldChoice.isPresent()).isTrue();
        assertThat(oldChoice.get().getName()).isEqualTo(oldName);
    }

    @Test    
    void deleteChoice_withValidId() throws Exception{
        // Given
        Long id = menuChoiceRepository.save(entries.menuChoice1).getId();

        // When, Then
        mockMvc.perform(delete("/api/v1/menuchoices/{id}", id))
                .andExpect(status().isOk());

        Optional<MenuChoice> deletedChoice = menuChoiceRepository.findMenuChoiceById(id);
        assertThat(deletedChoice.isPresent()).isFalse();
        deletedChoice = menuChoiceRepository.findMenuChoiceByName(entries.menuChoice1.getName());
        assertThat(deletedChoice.isPresent()).isFalse();
    }

    @Test    
    void deleteChoice_withResource() throws Exception{
        // Given
        Long id = menuChoiceRepository.save(entries.menuChoice6).getId();

        // When, Then
        mockMvc.perform(delete("/api/v1/menuchoices/{id}", id))
                .andExpect(status().isOk());

        Optional<MenuChoice> deletedChoice = menuChoiceRepository.findMenuChoiceById(id);
        assertThat(deletedChoice.isPresent()).isFalse();
        deletedChoice = menuChoiceRepository.findMenuChoiceByName(entries.menuChoice1.getName());
        assertThat(deletedChoice.isPresent()).isFalse();
    }


    @Test
    void deleteChoice_withMetrics() throws Exception{
        // Given
        Long id = menuChoiceRepository.save(entries.menuChoice1).getId();
        metricRepository.save(entries.metric1);


        // When, Then
        mockMvc.perform(delete("/api/v1/menuchoices/{id}", id))
                .andExpect(status().isOk())
                .andReturn();


        Optional<MenuChoice> deletedChoice = menuChoiceRepository.findMenuChoiceById(id);
        assertThat(deletedChoice.isPresent()).isFalse();
        deletedChoice = menuChoiceRepository.findMenuChoiceByName(entries.menuChoice1.getName());
        assertThat(deletedChoice.isPresent()).isFalse();
        Optional<Metric> relatedMetric = metricRepository.findById(entries.metric1.getId());
        assertThat(relatedMetric.isPresent()).isTrue();
        assertThat(relatedMetric.get().getMenuChoices().size()).isEqualTo(0);
    }

    @Test
    void deleteChoice_withParent() throws Exception{
        // Given
        menuChoiceRepository.save(entries.menuChoice1);
        Long id = menuChoiceRepository.save(entries.menuChoice2).getId();

        // When, Then
        mockMvc.perform(delete("/api/v1/menuchoices/{id}", id))
                .andExpect(status().isOk())
                .andReturn();

        Optional<MenuChoice> deletedChoice = menuChoiceRepository.findMenuChoiceById(id);
        assertThat(deletedChoice.isPresent()).isFalse();
        deletedChoice = menuChoiceRepository.findMenuChoiceByName(entries.menuChoice2.getName());
        assertThat(deletedChoice.isPresent()).isFalse();
        Optional<MenuChoice> parentChoice = menuChoiceRepository.findMenuChoiceById(entries.menuChoice1.getId());
        assertThat(parentChoice.isPresent()).isTrue();
    }

    @Test
    void deleteChoice_withForce_withChildren() throws Exception{
        // Given
        Long id = menuChoiceRepository.save(entries.menuChoice1).getId();
        menuChoiceRepository.save(entries.menuChoice2); // child

        // When, Then
        mockMvc.perform(delete("/api/v1/menuchoices/{id}?force=true", id))
                .andExpect(status().isOk())
                .andReturn();

        Optional<MenuChoice> deletedChoice = menuChoiceRepository.findMenuChoiceById(id);
        assertThat(deletedChoice.isPresent()).isFalse();
        deletedChoice = menuChoiceRepository.findMenuChoiceByName(entries.menuChoice1.getName());
        assertThat(deletedChoice.isPresent()).isFalse();
        Optional<MenuChoice> deletedChildChoice = menuChoiceRepository.findMenuChoiceById(entries.menuChoice2.getId());
        assertThat(deletedChildChoice.isPresent()).isFalse();
    }

    @Test
    void deleteChoice_withForce_withNoChildren() throws Exception{
        // Given
        Long id = menuChoiceRepository.save(entries.menuChoice1).getId();

        // When, Then
        mockMvc.perform(delete("/api/v1/menuchoices/{id}?force=true", id))
                .andExpect(status().isOk())
                .andReturn();

        Optional<MenuChoice> deletedChoice = menuChoiceRepository.findMenuChoiceById(id);
        assertThat(deletedChoice.isPresent()).isFalse();
        deletedChoice = menuChoiceRepository.findMenuChoiceByName(entries.menuChoice1.getName());
        assertThat(deletedChoice.isPresent()).isFalse();
    }

    @Test
    void deleteChoice_withNotPresentId() throws Exception{
        // Given
        Long id = 1L;
        for(MenuChoice c : menuChoiceRepository.findAll()){
            id += c.getId();
        }

        // When, Then
        mockMvc.perform(delete("/api/v1/menuchoices/{id}", id))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteChoice_withNoForce_withChildren() throws Exception{
        // Given
        Long id = menuChoiceRepository.save(entries.menuChoice1).getId();
        menuChoiceRepository.save(entries.menuChoice2); // child

        // When, Then
        mockMvc.perform(delete("/api/v1/menuchoices/{id}", id))
                .andExpect(status().is4xxClientError());
    }

}
