package com.foreflight.apphelper.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "source")
public class Source{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String name;

    @Column
    private String link;

    public Source(){}

    public Source(String name, String link) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Source source = (Source) o;
        return getId().equals(source.getId()) && getName().equals(source.getName()) && getLink().equals(source.getLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLink());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Source{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", link='").append(link).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
