package com.castsoftware.restapi.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reference {
    public String href;
    public String name;
    public String shortName;
    public String key;
    public String gradeAggregators;
}
