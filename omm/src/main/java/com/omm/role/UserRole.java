package com.omm.role;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    USER("ROLE_USER");

    UserRole(String value){
        this.value = value;
    }

    private String value;
}
