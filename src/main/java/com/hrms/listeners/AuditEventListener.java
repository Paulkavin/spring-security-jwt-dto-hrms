package com.hrms.listeners;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.hrms.events.EmployeeCreatedEvent;

@Component
public class AuditEventListener{

    @EventListener
    public void handleEmployeeCreated(EmployeeCreatedEvent event){
        System.out.println("===== AUDIT LOG =====");
        System.out.println("Employee Created");
        System.out.println("ID          : " + event.employeeId());
        System.out.println("Name        : " + event.firstName() + " " + event.lastName());
        System.out.println("Department  : " + event.department());
        System.out.println("Joining Date: " + event.joiningDate());
        System.out.println("=====================");
    }
}