package com.ecomerce.store.configuration.security;

import com.ecomerce.store.entity.Role;
import com.ecomerce.store.entity.User;
import com.ecomerce.store.entity.UserRole;
import com.ecomerce.store.entity.key.UserRoleId;
import com.ecomerce.store.repository.RoleRepository;
import com.ecomerce.store.repository.UserRepository;
import com.ecomerce.store.repository.UserRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository
    ) {
        return args -> {
            Role adminRole = roleRepository.findByRoleNameIgnoreCase("ADMIN")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .roleName("ADMIN")
                                    .description("Administrator role")
                                    .build()
                    ));

            roleRepository.findByRoleNameIgnoreCase("USER")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .roleName("USER")
                                    .description("Default user role")
                                    .build()
                    ));

            userRepository.findByUserName("admin").orElseGet(() -> {
                User user = User.builder()
                        .userName("admin")
                        .email("admin@admin")
                        .password(passwordEncoder.encode("admin"))
                        .enabled(true)
                        .build();
                userRepository.save(user);

                UserRoleId id = new UserRoleId(user.getUserId(), adminRole.getRoleId());

                UserRole userRole = UserRole
                        .builder()
                        .userRoleId(id)
                        .user(user)
                        .role(adminRole)
                        .build();
                userRoleRepository.save(userRole);

                return user;
            });
        };
    }
}
