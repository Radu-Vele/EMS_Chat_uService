package com.chatus.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.security.Principal;

@AllArgsConstructor
public class User implements Principal {
    private final String name;
    @Getter
    private final UserRole role;
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", role=" + role +
                '}';
    }
}
