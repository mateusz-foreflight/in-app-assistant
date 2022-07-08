package com.foreflight.apphelper.domain;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class MetricCreateDTO {
    private Boolean answerFound;
    private OffsetDateTime timestamp;
    private String ticketLink;
    private String userName;
    private String userFeedback;
    private List<Long> menuchoiceIds = new ArrayList<>();
    private List<Long> resourceIds = new ArrayList<>();

    public MetricCreateDTO(){}

    public MetricCreateDTO(Boolean answerFound, OffsetDateTime timestamp, String ticketLink, String userName, String userFeedback, List<Long> menuchoiceIds, List<Long> resourceIds) {
        this.answerFound = answerFound;
        this.timestamp = timestamp;
        this.ticketLink = ticketLink;
        this.userName = userName;
        this.userFeedback = userFeedback;
        this.menuchoiceIds = menuchoiceIds;
        this.resourceIds = resourceIds;
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
