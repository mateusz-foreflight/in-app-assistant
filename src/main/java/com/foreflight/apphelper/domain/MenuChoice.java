package com.foreflight.apphelper.domain;

import lombok.*;
import javax.persistence.*;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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
    @ToString.Exclude
    private List<Resource> resources = new ArrayList<>();
}