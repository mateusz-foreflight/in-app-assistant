package com.foreflight.apphelper.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MenuChoiceDTO {
    private String choiceName;
    private String parentName = null;
    private List<String> resourceNames = new ArrayList<>();
}
