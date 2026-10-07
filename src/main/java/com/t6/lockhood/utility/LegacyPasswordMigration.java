package com.t6.lockhood.utility;

import com.t6.lockhood.model.User;
import com.t6.lockhood.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LegacyPasswordMigration implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public LegacyPasswordMigration(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<User> legacyUsers = userRepository.findAll().stream()
                .filter(user -> user.getPassword() != null && !passwordHasher.isHashed(user.getPassword()))
                .collect(Collectors.toList());
        if (legacyUsers.isEmpty()) {
            return;
        }
        legacyUsers.forEach(user -> user.setPassword(passwordHasher.encode(user.getPassword())));
        userRepository.saveAll(legacyUsers);
    }
}
