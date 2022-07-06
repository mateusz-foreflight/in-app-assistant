package com.foreflight.apphelper.domain;



import java.util.ArrayList;
import java.util.List;


public class MenuChoiceCreateDTO {
    private String name;
    private Long parentId = null;
    private List<Long> resourceIds = new ArrayList<>();

    public MenuChoiceCreateDTO(){}

    public MenuChoiceCreateDTO(String name, Long parentId, List<Long> resourceIds) {
        this.name = name;
        this.parentId = parentId;
        this.resourceIds = resourceIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<Long> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<Long> resourceIds) {
        this.resourceIds = resourceIds;
    }
}
