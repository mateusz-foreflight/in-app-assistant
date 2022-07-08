package com.foreflight.apphelper.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class MetricDTO {
    private Long id;
    private Boolean answerFound;
    private OffsetDateTime timestamp;
    private String ticketLink;
    private String userName;
    private String userFeedback;
    private List<Long> menuchoiceIds = new ArrayList<>();
    private List<Long> resourceIds = new ArrayList<>();


    public static MetricDTO assemble(Metric metric){
        MetricDTO dto = new MetricDTO();

        dto.setId(metric.getId());
        dto.setAnswerFound(metric.isAnswerFound());
        dto.setTimestamp(metric.getTimestamp());
        dto.setTicketLink(metric.getTicketLink());
        dto.setUserName(metric.getUserName());
        dto.setUserFeedback(metric.getUserFeedback());
        for(MenuChoice choice : metric.getMenuChoices()){
            dto.getMenuchoiceIds().add(choice.getId());
        }
        for(Resource resource : metric.getResources()){
            dto.getResourceIds().add(resource.getId());
        }

        return dto;
    }


    public MetricDTO(){}

    public MetricDTO(Long id, Boolean answerFound, OffsetDateTime timestamp, String ticketLink, String userName, String userFeedback, List<Long> menuchoiceIds, List<Long> resourceIds) {
        this.id = id;
        this.answerFound = answerFound;
        this.timestamp = timestamp;
        this.ticketLink = ticketLink;
        this.userName = userName;
        this.userFeedback = userFeedback;
        this.menuchoiceIds = menuchoiceIds;
        this.resourceIds = resourceIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAnswerFound() {
        return answerFound;
    }

    public void setAnswerFound(Boolean answerFound) {
        this.answerFound = answerFound;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
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

    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }

    public List<Long> getMenuchoiceIds() {
        return menuchoiceIds;
    }

    public void setMenuchoiceIds(List<Long> menuchoiceIds) {
        this.menuchoiceIds = menuchoiceIds;
    }

    public List<Long> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<Long> resourceIds) {
        this.resourceIds = resourceIds;
    }
}
