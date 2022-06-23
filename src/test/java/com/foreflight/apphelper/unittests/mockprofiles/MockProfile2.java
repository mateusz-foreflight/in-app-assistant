package com.foreflight.apphelper.unittests.mockprofiles;

import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.domain.Source;
import com.foreflight.apphelper.repository.ResourceRepository;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

// Mocks a resource repository with the following Resource objects in it:
//      id: 1, name: resource1, link: link1
//      id: 2, name: resource2, link: null
public class MockProfile2 {
    public Resource resource1;
    public Resource resource2;
    public List<Resource> resources;


    public MockProfile2(ResourceRepository resourceRepository){
        // SET UP DATA
        // Set up resources
        this.resource1 = new Resource("resource1", "link2", new Source("source", "sourcelink"));
        this.resource1.setId(1L);
        this.resource2 = new Resource("resource2", null, new Source("source", "sourcelink"));
        this.resource2.setId(2L);
        this.resources = Arrays.asList(resource1, resource2);


        // SET UP METHOD MOCKS
        // resourceRepository - save, findAll, findResourceById, findResourceByName, existsById
        when(resourceRepository.save(Mockito.any(Resource.class))).then(returnsFirstArg());

        when(resourceRepository.findAll()).thenReturn(resources);

        // Take advantage of overriding stubbing as described here:
        // https://stackoverflow.com/questions/34171082/how-to-handle-any-other-value-with-mockito
        when(resourceRepository.findResourceById(anyLong())).thenReturn(Optional.empty());
        doReturn(Optional.of(resource1)).when(resourceRepository).findResourceById(1L);
        doReturn(Optional.of(resource1)).when(resourceRepository).findResourceById(2L);

        when(resourceRepository.findResourceByName(anyString())).thenReturn(Optional.empty());
        doReturn(Optional.of(resource1)).when(resourceRepository).findResourceByName("resource1");
        doReturn(Optional.of(resource2)).when(resourceRepository).findResourceByName("resource2");

        when(resourceRepository.existsById(anyLong())).thenReturn(false);
        doReturn(true).when(resourceRepository).existsById(1L);
        doReturn(true).when(resourceRepository).existsById(2L);

    }
}
