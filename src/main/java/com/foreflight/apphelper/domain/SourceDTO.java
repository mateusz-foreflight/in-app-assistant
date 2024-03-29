package com.foreflight.apphelper.domain;

public class SourceDTO {
    private Long id;
    private String name;
    private String link;

    public static SourceDTO assemble(Source source){
        return new SourceDTO(source.getId(), source.getName(), source.getLink());
    }

    public SourceDTO(){}

    public SourceDTO(Long id, String name, String link) {
        this.id = id;
        this.name = name;
        this.link = link;
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
}
