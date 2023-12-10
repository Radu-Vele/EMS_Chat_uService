package com.chatus.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.Principal;

@AllArgsConstructor
public class User implements Principal {
    private final String name;

    @Override
    public String getName() {
        return this.name;
    }
}
