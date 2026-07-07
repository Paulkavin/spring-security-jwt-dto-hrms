package com.hrms.enums;
import java.util.*;
public class ModulePermissionRegistry {
    private static final Map<ModuleEnum, List<String>> EXTRA_OPERATIONS = new HashMap<>();

    static{
        //List.of() creates unmodifiable lists.
        EXTRA_OPERATIONS.put(ModuleEnum.LEAVE, List.of("APPROVE","REJECT"));
        EXTRA_OPERATIONS.put(ModuleEnum.PAYROLL,
                List.of("PROCESS", "LOCK"));

        EXTRA_OPERATIONS.put(ModuleEnum.INVENTORY,
                List.of("ISSUE", "RETURN", "TRANSFER"));

        EXTRA_OPERATIONS.put(ModuleEnum.EMPLOYEE,
                List.of("RESET_PASSWORD", "PROMOTE"));
    }

    public static List<String> getExtraOperations(ModuleEnum module){
        return EXTRA_OPERATIONS.getOrDefault(module, Collections.emptyList());
    }
}
