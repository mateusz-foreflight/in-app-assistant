package com.foreflight.apphelper.integrationtests;

import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.domain.Source;
import com.foreflight.apphelper.repository.ResourceRepository;
import com.foreflight.apphelper.repository.SourceRepository;
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

    @Autowired
    private SourceRepository sourceRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    void getAllResources() throws Exception{
        // Given
        Source source1 = new Source("source1", "sourcelink1");
        Source source2 = new Source("source2", "sourcelink2");
        List<Source> sources = Arrays.asList(source1, source2);
        sourceRepository.saveAll(sources);

        Resource resource1 = new Resource("resource1", "link1", source1);
        Resource resource2 = new Resource("resource2", "link1", source1);
        Resource resource3 = new Resource("resource3", "link2", source2);
        List<Resource> resources = Arrays.asList(resource1, resource2, resource3);
        resourceRepository.saveAll(resources);

        // When, Then
        mockMvc.perform(get("/api/v1/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[?(@.name == \"resource1\" && @.link == \"link1\" && @.source.name == \"source1\" && @.source.link == \"sourcelink1\")]").exists())
                .andExpect(jsonPath("$.[?(@.name == \"resource2\" && @.link == \"link1\" && @.source.name == \"source1\" && @.source.link == \"sourcelink1\")]").exists())
                .andExpect(jsonPath("$.[?(@.name == \"resource3\" && @.link == \"link2\" && @.source.name == \"source2\" && @.source.link == \"sourcelink2\")]").exists());
    }

    @Test
    @Transactional
    void getResourceById() throws Exception{
        // Given
        Source source1 = new Source("source1", "sourcelink1");
        sourceRepository.save(source1);

        Resource resource1 = new Resource("resource1", "link1", source1);
        Long id = resourceRepository.save(resource1).getId();

        // When, Then
        mockMvc.perform(get("/api/v1/resources/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath(String.format("$.[?(@.name == \"resource1\" && @.link == \"link1\" && @.id == %d && @.source.name == \"source1\" && @.source.link == \"sourcelink1\")]", id)).exists());
    }

    @Test
    @Transactional
    void createResource_withValidResource() throws Exception{
        // Given
        Source source1 = new Source("source1", "sourcelink1");
        sourceRepository.save(source1);

        String inputJson = "{\"name\": \"new\", \"link\": \"newLink\", \"source\": \"source1\"}";

        // When, Then
        mockMvc.perform(post("/api/v1/resources/").content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.name == \"new\" && @.link == \"newLink\" && @.source.name == \"source1\" && @.source.link == \"sourcelink1\")]").exists());

        Optional<Resource> newResource = resourceRepository.findResourceByName("new");
        assertThat(newResource.isPresent()).isTrue();
        assertThat(newResource.get().getLink()).isEqualTo("newLink");
        assertThat(newResource.get().getSource().getName()).isEqualTo("source1");
        assertThat(newResource.get().getSource().getLink()).isEqualTo("sourcelink1");
    }

    @Test
    @Transactional
    void updateResource_withPresentId() throws Exception{
        // Given
        Source source1 = new Source("source1", "sourcelink1");
        Source source2 = new Source("source2", "sourcelink2");
        List<Source> sources = Arrays.asList(source1, source2);
        sourceRepository.saveAll(sources);

        Resource resource1 = new Resource("resource1", "link1", source1);
        Long id = resourceRepository.save(resource1).getId();
        String inputJson = "{\"name\": \"new\", \"link\": \"newLink\", \"source\": \"source2\"}";

        // When, Then
        mockMvc.perform(put("/api/v1/resources/{id}", id).content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.name == \"new\" && @.link == \"newLink\" && @.source.name == \"source2\" && @.source.link == \"sourcelink2\")]").exists());

        Optional<Resource> updatedResource = resourceRepository.findResourceById(id);
        assertThat(updatedResource.isPresent()).isTrue();
        assertThat(updatedResource.get().getName()).isEqualTo("new");
        assertThat(updatedResource.get().getLink()).isEqualTo("newLink");
        assertThat(updatedResource.get().getSource().getName()).isEqualTo("source2");
        assertThat(updatedResource.get().getSource().getLink()).isEqualTo("sourcelink2");
        Optional<Resource> oldResourceNotPresent = resourceRepository.findResourceByName("resource1");
        assertThat(oldResourceNotPresent.isPresent()).isFalse();
    }

    @Test
    @Transactional
    void deleteResource_withValidId() throws Exception{
        // Given
        Source source1 = new Source("source1", "sourcelink1");
        sourceRepository.save(source1);

        Resource resource1 = new Resource("resource1", "link1", source1);

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
