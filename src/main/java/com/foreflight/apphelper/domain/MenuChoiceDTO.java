package com.foreflight.apphelper.domain;



import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


public class MenuChoiceDTO {
    private Long id;
    private String name;
    private Long parentId = null;
    private List<Long> resourceIds = new ArrayList<>();
    private Boolean isPublic = false;

    public static MenuChoiceDTO assemble(MenuChoice choice){
        MenuChoiceDTO dto = new MenuChoiceDTO();

        dto.setId(choice.getId());
        dto.setName(choice.getName());
        dto.setParentId(choice.getParent() == null ? null : choice.getParent().getId());
        for(Resource resource : choice.getResources()){
            dto.getResourceIds().add(resource.getId());
        }
        dto.setPublic(choice.getPublic());

        return dto;
    }

    public MenuChoiceDTO(){}

    public MenuChoiceDTO(Long id, String name, Long parentId, List<Long> resourceIds, Boolean isPublic) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.resourceIds = resourceIds;
        this.isPublic = isPublic;
    }

    public MenuChoiceDTO(Long id, String name, Long parentId, List<Long> resourceIds) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.resourceIds = resourceIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @JsonProperty("isPublic")
    public Boolean getPublic() {
        return isPublic;
    }

    @JsonProperty("isPublic")
    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }
}
