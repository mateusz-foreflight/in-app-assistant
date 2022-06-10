package com.foreflight.apphelper.domain;

public class ResourceDTO {
    private String name;
    private String link;
    private String source;

    public ResourceDTO() {
    }

    public ResourceDTO(String name, String link, String source) {
        this.name = name;
        this.link = link;
        this.source = source;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
