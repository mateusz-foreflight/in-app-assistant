package com.foreflight.apphelper.domain;

public class ResourceCreateDTO {
    private String name;
    private String link;
    private Long sourceId;

    public ResourceCreateDTO(){

    }

    public ResourceCreateDTO(String name, String link, Long sourceId) {
        this.name = name;
        this.link = link;
        this.sourceId = sourceId;
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
