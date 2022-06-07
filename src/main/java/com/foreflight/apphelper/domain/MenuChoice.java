package com.foreflight.apphelper.domain;

import javax.persistence.*;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menuchoice")
public class MenuChoice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "parent_id",
            table = "menuchoice")
    private MenuChoice parent;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "menuchoice_resource",
            joinColumns = @JoinColumn(name = "menuchoice_id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id"))
    private List<Resource> resources = new ArrayList<>();

    public MenuChoice(){}

    public MenuChoice(String name, MenuChoice parent, List<Resource> resources){
        this.name = name;
        this.parent = parent;
        this.resources = resources;
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

    public MenuChoice getParent() {
        return parent;
    }

    public void setParent(MenuChoice parent) {
        this.parent = parent;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MenuChoice{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", parent=").append(parent);
        sb.append(", resources=").append(resources);
        sb.append('}');
        return sb.toString();
    }

}