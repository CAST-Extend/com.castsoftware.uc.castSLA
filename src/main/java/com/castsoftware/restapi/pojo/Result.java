package com.castsoftware.restapi.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
    private Double value;
    private Double grade;
    private EvolutionSummary evolutionSummary;
    private String boundaries;
}
