package com.foreflight.apphelper.domain;

public class ResourceDTO {
    private String name;
    private String link;

    public ResourceDTO() {
    }

    public ResourceDTO(String name, String link) {
        this.name = name;
        this.link = link;
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
}
