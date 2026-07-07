package com.hrms.authorization;

import java.util.List;
import java.util.Map;

import com.hrms.enums.RoleEnum;

public final class DefaultRolePermissionRegistry {

    private DefaultRolePermissionRegistry() {
    }

    private static final Map<RoleEnum, List<String>> ROLE_PERMISSIONS = Map.of(

            RoleEnum.EMPLOYEE,
            List.of(
                    "EMPLOYEE_READ",
                    "LEAVE_CREATE",
                    "LEAVE_READ_OWN",
                    "LEAVE_UPDATE",
                    "LEAVE_DELETE"
            ),

            RoleEnum.HR,
            List.of(
                    "EMPLOYEE_CREATE",
                    "EMPLOYEE_READ",
                    "EMPLOYEE_UPDATE",

                    "LEAVE_READ",
                    "LEAVE_APPROVE",
                    "LEAVE_REJECT"
            ),

            RoleEnum.ADMIN,
            List.of("*")
    );

    public static List<String> getPermissions(RoleEnum role) {

        return ROLE_PERMISSIONS.getOrDefault(role, List.of());

    }

}