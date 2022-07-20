package com.foreflight.apphelper.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceCreateDTO {
    private String name;
    private String link;
    private Long sourceId;
    private Boolean isPublic;

    public ResourceCreateDTO(){

    }

    public ResourceCreateDTO(String name, String link, Long sourceId, Boolean isPublic) {
        this.name = name;
        this.link = link;
        this.sourceId = sourceId;
        this.isPublic = isPublic;
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

    @JsonProperty("isPublic")
    public Boolean getPublic() {
        return isPublic;
    }

    @JsonProperty("isPublic")
    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }
}
