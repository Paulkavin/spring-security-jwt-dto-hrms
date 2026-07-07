package com.hrms.initializer;

import com.hrms.service.PermissionInitializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class AuthorizationInitializer implements CommandLineRunner {

    private final PermissionInitializationService permissionInitializationService;
    //What should happen after Spring starts?
    @Override
    public void run(String... args) {

        permissionInitializationService.initialize();

    }
}