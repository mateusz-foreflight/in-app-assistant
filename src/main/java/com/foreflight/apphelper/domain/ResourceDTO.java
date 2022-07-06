package com.foreflight.apphelper.domain;

public class ResourceDTO {
    private Long id;
    private String name;
    private String link;
    private Long sourceId;

    public static ResourceDTO assemble(Resource resource){
        ResourceDTO dto = new ResourceDTO();

        dto.setId(resource.getId());
        dto.setName(resource.getName());
        dto.setLink(resource.getLink());
        dto.setSourceId(resource.getSource().getId());

        return dto;
    }

    public ResourceDTO() {
    }

    public ResourceDTO(Long id, String name, String link, Long sourceId) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.sourceId = sourceId;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
}
