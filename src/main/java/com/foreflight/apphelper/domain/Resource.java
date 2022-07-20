package com.foreflight.apphelper.domain;


import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "resource")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String name;

    @Column
    private String link;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "source_id",
            nullable = false)
    private Source source;

    @Column(name = "public")
    private Boolean isPublic;

    public Resource() {
    }

    public Resource(String name, String link, Source source) {
        this.name = name;
        this.link = link;
        this.source = source;
    }

    public Resource(String name, String link, Source source, Boolean isPublic) {
        this.name = name;
        this.link = link;
        this.source = source;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Source getSource() {
        return source;
    }
    public void setSource(Source source) {
        this.source = source;
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
        Resource resource = (Resource) o;
        return id.equals(resource.id) && name.equals(resource.name) && Objects.equals(link, resource.link) && source.equals(resource.source) && isPublic.equals(resource.isPublic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, link, source, isPublic);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Resource{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", link='").append(link).append('\'');
        sb.append(", source=").append(source);
        sb.append(", isPublic=").append(isPublic);
        sb.append('}');
        return sb.toString();
    }
}
