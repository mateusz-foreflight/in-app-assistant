package com.foreflight.apphelper.domain;

import javax.persistence.*;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menuchoice")
public class MenuChoice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "parent_id",
            table = "menuchoice")
    private MenuChoice parent;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "menuchoice_resource",
            joinColumns = @JoinColumn(name = "menuchoice_id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id"))
    private List<Resource> resources = new ArrayList<>();

    @Column(name="public")
    private Boolean isPublic;

    public MenuChoice(){}

    public MenuChoice(String name, MenuChoice parent, List<Resource> resources) {
        this.name = name;
        this.parent = parent;
        this.resources = resources;
    }

    public MenuChoice(String name, MenuChoice parent, List<Resource> resources, Boolean isPublic) {
        this.name = name;
        this.parent = parent;
        this.resources = resources;
        this.isPublic = isPublic;
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

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuChoice choice = (MenuChoice) o;

        boolean resourcesEquality = true;
        for(Resource resource : resources){
            boolean found = false;
            for(Resource otherResource: choice.resources){
                if(resource.equals(otherResource)){
                    found = true;
                    break;
                }
            }
            if(!found){
                resourcesEquality = false;
                break;
            }
        }

        return id.equals(choice.id) && name.equals(choice.name) && Objects.equals(parent, choice.parent)
                && isPublic.equals(choice.isPublic) && resourcesEquality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parent, resources, isPublic);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MenuChoice{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", parent=").append(parent);
        sb.append(", resources=").append(resources);
        sb.append(", public=").append(isPublic);
        sb.append('}');
        return sb.toString();
    }

}