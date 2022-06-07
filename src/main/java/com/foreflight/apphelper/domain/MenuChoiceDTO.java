package com.foreflight.apphelper.domain;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MenuChoiceDTO {
    private String choiceName;
    private String parentName = null;
    private List<String> resourceNames = new ArrayList<>();

    public MenuChoiceDTO(){}

    public MenuChoiceDTO(String choiceName, String parentName, List<String> resourceNames) {
        this.choiceName = choiceName;
        this.parentName = parentName;
        this.resourceNames = resourceNames;
    }

    public String getChoiceName() {
        return choiceName;
    }

    public void setChoiceName(String choiceName) {
        this.choiceName = choiceName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<String> getResourceNames() {
        return resourceNames;
    }

    public void setResourceNames(List<String> resourceNames) {
        this.resourceNames = resourceNames;
    }
}
