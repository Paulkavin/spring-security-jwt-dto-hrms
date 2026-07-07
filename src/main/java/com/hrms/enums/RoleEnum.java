package com.hrms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {

    EMPLOYEE("Employee", "Default employee role"),

    HR("HR", "Human Resource role"),

    ADMIN("Admin", "System administrator");

    private final String displayName;

    private final String description;

}