package com.hrms.enums;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor //automatically generates a constructor containing parameters only for "required"(final) fields.
public enum ModuleEnum {
    EMPLOYEE("Employee", "Employee Management"),
    LEAVE("Leave","Leave Management"),
    PAYROLL("Payroll","Payroll Management"),
    ATTENDANCE("Attendance", "Attendance Managemnet"),
    INVENTORY("Inventory","Inventory Management"),
    ASSETS("Assets","Assets Management"),
    REPORTS("Report", "Reports Management"),
    PERFORMANCE("Performance","Performance Management");

    private final String displayName;
    private final String description;

    /*  Replaced with @RequiredArgsContructor
    ModuleEnum(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
*/

}
