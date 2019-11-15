package com.castsoftware.restapi.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reference {
    private String href;
    private String name;
    private String shortName;
    private String key;
    private String gradeAggregators;
}
