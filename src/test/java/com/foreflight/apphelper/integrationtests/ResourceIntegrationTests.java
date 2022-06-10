package com.foreflight.apphelper.integrationtests;

import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.domain.Source;
import com.foreflight.apphelper.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ResourceIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceRepository resourceRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    void getAllResources() throws Exception{
        // Given
        Resource resource1 = new Resource("resource1", "link1", Source.PilotGuide);
        Resource resource2 = new Resource("resource2", "link1", Source.PilotGuide);
        Resource resource3 = new Resource("resource3", "link2", Source.PilotGuide);
        List<Resource> resources = Arrays.asList(resource1, resource2, resource3);
        resourceRepository.saveAll(resources);

        // When, Then
        mockMvc.perform(get("/api/v1/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[?(@.name == \"resource1\" && @.link == \"link1\" && @.source == \"PilotGuide\")]").exists())
                .andExpect(jsonPath("$.[?(@.name == \"resource2\" && @.link == \"link1\" && @.source == \"PilotGuide\")]").exists())
                .andExpect(jsonPath("$.[?(@.name == \"resource3\" && @.link == \"link2\" && @.source == \"PilotGuide\")]").exists());
    }

    @Test
    @Transactional
    void getResourceById() throws Exception{
        // Given
        Resource resource1 = new Resource("resource1", "link1", Source.PilotGuide);
        Long id = resourceRepository.save(resource1).getId();

        // When, Then
        mockMvc.perform(get("/api/v1/resources/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath(String.format("$.[?(@.name == \"resource1\" && @.link == \"link1\" && @.id == %d)]", id)).exists());
    }

    @Test
    @Transactional
    void createResource_withValidResource() throws Exception{
        // Given
        String inputJson = "{\"name\": \"new\", \"link\": \"newLink\", \"source\": \"PilotGuide\"}";

        // When, Then
        mockMvc.perform(post("/api/v1/resources/").content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.name == \"new\" && @.link == \"newLink\" && @.source == \"PilotGuide\")]").exists());

        Optional<Resource> newResource = resourceRepository.findResourceByName("new");
        assertThat(newResource.isPresent()).isTrue();
        assertThat(newResource.get().getLink()).isEqualTo("newLink");
    }

    @Test
    @Transactional
    void updateResource_withPresentId() throws Exception{
        // Given
        Resource resource1 = new Resource("resource1", "link1", Source.PilotGuide);
        Long id = resourceRepository.save(resource1).getId();
        String inputJson = "{\"name\": \"new\", \"link\": \"newLink\", \"source\": \"PilotGuide\"}";

        // When, Then
        mockMvc.perform(put("/api/v1/resources/{id}", id).content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.name == \"new\" && @.link == \"newLink\" && @.source == \"PilotGuide\")]").exists());

        Optional<Resource> updatedResource = resourceRepository.findResourceById(id);
        assertThat(updatedResource.isPresent()).isTrue();
        assertThat(updatedResource.get().getName()).isEqualTo("new");
        assertThat(updatedResource.get().getLink()).isEqualTo("newLink");
        Optional<Resource> oldResourceNotPresent = resourceRepository.findResourceByName("resource1");
        assertThat(oldResourceNotPresent.isPresent()).isFalse();
    }

    @Test
    @Transactional
    void deleteResource_withValidId() throws Exception{
        // Given
        Resource resource1 = new Resource("resource1", "link1", Source.PilotGuide);

        Long id = resourceRepository.save(resource1).getId();

        // When, Then
        mockMvc.perform(delete("/api/v1/resources/{id}", id))
                .andExpect(status().isOk());

        Optional<Resource> deletedResource = resourceRepository.findResourceById(id);
        assertThat(deletedResource.isPresent()).isFalse();
        deletedResource = resourceRepository.findResourceByName("resource1");
        assertThat(deletedResource.isPresent()).isFalse();
    }
}
