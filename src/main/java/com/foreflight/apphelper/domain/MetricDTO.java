package com.foreflight.apphelper.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public class MetricDTO {
    private Boolean answerFound;

    private OffsetDateTime timestamp;

    private String ticketLink;

    private String userName;

    private String userFeedback;

    private List<String> menuchoiceNames;

    private List<String> resourceNames;



    public MetricDTO(){}

    public MetricDTO(Boolean answerFound, OffsetDateTime timestamp, String ticketLink, String userName, String userFeedback, List<String> menuchoiceNames, List<String> resourceNames) {
        this.answerFound = answerFound;
        this.timestamp = timestamp;
        this.ticketLink = ticketLink;
        this.userName = userName;
        this.userFeedback = userFeedback;
        this.menuchoiceNames = menuchoiceNames;
        this.resourceNames = resourceNames;
    }

    public Boolean isAnswerFound() {
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

    public List<String> getMenuchoiceNames() {
        return menuchoiceNames;
    }

    public void setMenuchoiceNames(List<String> menuchoiceNames) {
        this.menuchoiceNames = menuchoiceNames;
    }

    public List<String> getResourceNames() {
        return resourceNames;
    }

    public void setResourceNames(List<String> resourceNames) {
        this.resourceNames = resourceNames;
    }
}
