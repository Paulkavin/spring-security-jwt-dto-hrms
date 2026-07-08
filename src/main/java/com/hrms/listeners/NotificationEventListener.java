package com.hrms.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.hrms.events.EmployeeCreatedEvent;

@Component
public class NotificationEventListener{
    
    @EventListener
    public void handleEmployeeCreated(EmployeeCreatedEvent event){
        System.out.println( "Welcome email will be sent to: " + event.email());
    }
}

/**
 * Auto-Detection: When your application starts, Spring scans your project files. If it sees a class with @Component, 
 * it registers it as a reusable block of code in its "Container".
 * Dependency Injection: Because Spring manages this component, it makes it incredibly easy to use this class inside 
 * other parts of your code without instantiating it over and over.
 * Singleton by default: By default, Spring will only create one instance of this class and share it everywhere it is 
 * needed, which saves memory.
 */