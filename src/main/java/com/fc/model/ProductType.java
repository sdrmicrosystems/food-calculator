package com.fc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  ProductType {

    SOFT("SOFT"),
    HARD("HARD")
    ;

    private final String name;
}
