package com.hrms.service;

import org.springframework.stereotype.Service;

import com.hrms.authorization.DefaultRolePermissionRegistry;
import com.hrms.entity.Module;
import com.hrms.entity.Permission;
import com.hrms.entity.Role;
import com.hrms.enums.ModuleEnum;
import com.hrms.enums.CrudOperationEnum;
import com.hrms.enums.ModulePermissionRegistry;
import com.hrms.enums.RoleEnum;
import com.hrms.repository.ModuleRepository;
import com.hrms.repository.PermissionRepository;
import com.hrms.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j // for logging kind of alternative to print statement
@Service
@RequiredArgsConstructor
public class PermissionInitializationServiceImpl implements PermissionInitializationService {

    private final ModuleRepository moduleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;


    @Override
    public void initialize() {
        log.info("Starting authorization initialization...");
        initializeModules();
        initializePermissions();
        initializeRoles();
        initializeRolePermissions();

        log.info("Authorization initialization completed.");
    }

    private void initializeModules() {

        for (ModuleEnum moduleEnum : ModuleEnum.values()) {
            // The values() method is built automatically into every Java Enum by the Java
            // compiler itself.
            // It returns a standard Java array containing all the choices listed inside
            // that specific Enum, in the exact order you wrote them.
            moduleRepository.findByName(moduleEnum.name())
                    .orElseGet(() -> {

                        Module module = new Module();

                        module.setName(moduleEnum.name());

                        module.setDisplayName(toDisplayName(moduleEnum.getDisplayName()));

                        module.setDescription(moduleEnum.getDescription());

                        module.setActive(true);

                        log.info("Creating module: {}", module.getName());

                        return moduleRepository.save(module);
                    });

        }

    }

    private String toDisplayName(String value) {

        String lower = value.toLowerCase();

        return Character.toUpperCase(lower.charAt(0))
                + lower.substring(1);

    }

    private void initializePermissions() {
        for (ModuleEnum moduleEnum : ModuleEnum.values()) {

            Module module = moduleRepository.findByName(moduleEnum.name())
                    .orElseThrow(() -> new IllegalStateException("Module not initialized: " + moduleEnum.name()));

            initializeCrudPermissions(moduleEnum, module);

            initializeExtraPermissions(moduleEnum, module);
        }
    }

    private void initializeCrudPermissions(ModuleEnum moduleEnum, Module module) {

        for (CrudOperationEnum operation : CrudOperationEnum.values()) {

            createPermissionIfNotExists(
                    module,
                    moduleEnum.name() + "_" + operation.name(),
                    operation.name() + " " + moduleEnum.getDisplayName());
        }

    }

    private void initializeExtraPermissions(ModuleEnum moduleEnum, Module module) {

        for (String operation : ModulePermissionRegistry.getExtraOperations(moduleEnum)) {

            createPermissionIfNotExists(
                    module,
                    moduleEnum.name() + "_" + operation,
                    operation + " " + moduleEnum.getDisplayName());
        }

    }

    private void initializeRolePermissions() {

        for (RoleEnum roleEnum : RoleEnum.values()) {

        Role role = roleRepository.findByName(roleEnum.name())
                .orElseThrow(() ->
                        new IllegalStateException("Role not found: " + roleEnum.name()));

        if (roleEnum == RoleEnum.ADMIN) {

            role.getPermissions().addAll(permissionRepository.findAll());

        } else {

            for (String authority : DefaultRolePermissionRegistry.getPermissions(roleEnum)) {

                Permission permission = permissionRepository.findByAuthority(authority)
                        .orElseThrow(() ->
                                new IllegalStateException("Permission not found: " + authority));

                role.getPermissions().add(permission);

            }

        }

        roleRepository.save(role);

    }


    }

    private void createPermissionIfNotExists(
            Module module,
            String authority,
            String description) {

        permissionRepository.findByAuthority(authority)
                .orElseGet(() -> {

                    Permission permission = new Permission();

                    permission.setAuthority(authority);
                    permission.setDescription(description);
                    permission.setModule(module);

                    log.info("Creating permission: {}", authority);

                    return permissionRepository.save(permission);

                });

    }

    private void initializeRoles() {

        for (RoleEnum roleEnum : RoleEnum.values()) {

            roleRepository.findByName(roleEnum.name())
                    .orElseGet(() -> {

                        Role role = new Role();

                        role.setName(roleEnum.name());

                        role.setDisplayName(roleEnum.getDisplayName());

                        role.setDescription(roleEnum.getDescription());

                        role.setActive(true);

                        log.info("Creating role {}", role.getName());

                        return roleRepository.save(role);

                    });

        }

    }

}
