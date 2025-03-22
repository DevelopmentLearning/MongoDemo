package com.culmanu.mongo.dtos;

import lombok.Data;

@Data
public class CountrySummary {
    private String id;
    private Double avgBalance;
    private Integer count;
}


