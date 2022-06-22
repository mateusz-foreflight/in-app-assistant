package com.foreflight.apphelper.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "metric")
public class Metric implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "answer_found")
    private Boolean answerFound;

    @Column(name = "timestamp")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @Column(name = "ticket_link")
    private String ticketLink;

    @Column(name = "user_name")
    private String userName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "metric_menuchoice",
            joinColumns = @JoinColumn(name = "metric_id"),
            inverseJoinColumns = @JoinColumn(name = "menuchoice_id"))
    private List<MenuChoice> menuChoices = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "metric_resource",
            joinColumns = @JoinColumn(name = "metric_id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id"))
    private List<Resource> resources = new ArrayList<>();



    public Metric(Boolean answerFound, LocalDateTime timestamp, String ticketLink, String userName, List<MenuChoice> menuChoices, List<Resource> resources) {
        this.answerFound = answerFound;
        this.timestamp = timestamp;
        this.ticketLink = ticketLink;
        this.userName = userName;
        this.menuChoices = menuChoices;
        this.resources = resources;
    }

    public Metric() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isAnswerFound() {
        return answerFound;
    }

    public void setAnswerFound(Boolean answerFound) {
        this.answerFound = answerFound;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getTicketLink() {
        return ticketLink;
    }

    public void setTicketLink(String ticketLink) {
        this.ticketLink = ticketLink;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<MenuChoice> getMenuChoices() {
        return menuChoices;
    }

    public void setMenuChoices(List<MenuChoice> menuChoices) {
        this.menuChoices = menuChoices;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metric metric = (Metric) o;
        return isAnswerFound() == metric.isAnswerFound() && getId().equals(metric.getId()) && Objects.equals(getTimestamp(), metric.getTimestamp()) && Objects.equals(getTicketLink(), metric.getTicketLink()) && Objects.equals(getUserName(), metric.getUserName()) && Objects.equals(getMenuChoices(), metric.getMenuChoices()) && Objects.equals(getResources(), metric.getResources());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), isAnswerFound(), getTimestamp(), getTicketLink(), getUserName(), getMenuChoices(), getResources());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Metric{");
        sb.append("id=").append(id);
        sb.append(", answerFound=").append(answerFound);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", ticketLink='").append(ticketLink).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", menuChoices=").append(menuChoices);
        sb.append(", resources=").append(resources);
        sb.append('}');
        return sb.toString();
    }
}
