package com.t6.lockhood.controller;

import com.t6.lockhood.dto.UserInfoDTO;
import com.t6.lockhood.dto.UserLoginDTO;
import com.t6.lockhood.model.User;
import com.t6.lockhood.repository.UserRepository;
import com.t6.lockhood.utility.PasswordHasher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserRepository userRepository;

    @Spy
    PasswordHasher passwordHasher = new PasswordHasher();

    @InjectMocks
    UserController userController;

    private User user(long id, String userName, String password, String privilege) {
        return User.builder().id(id).name(userName).userName(userName).password(password)
                .privilege(privilege).email(userName + "@x.com").build();
    }

    @Test
    void saveUserStoresHashNotPlainText() {
        when(userRepository.save(any(User.class))).then(returnsFirstArg());

        User saved = userController.saveUser(user(0, "alice", "Secret1", "user"));

        assertNotEquals("Secret1", saved.getPassword());
        assertTrue(passwordHasher.isHashed(saved.getPassword()));
        assertTrue(passwordHasher.matches("Secret1", saved.getPassword()));
    }

    @Test
    void loginRejectsWrongCasePassword() {
        User alice = user(1, "alice", passwordHasher.encode("Secret1"), "user");
        when(userRepository.findAll()).thenReturn(Collections.singletonList(alice));

        assertEquals("error.html", userController.login(new UserLoginDTO("alice", "SECRET1")).getPage());
        assertEquals("error.html", userController.login(new UserLoginDTO("alice", "secret1")).getPage());
    }

    @Test
    void loginWithExactPasswordRoutesByPrivilege() {
        User alice = user(1, "alice", passwordHasher.encode("Secret1"), "user");
        User root = user(2, "root", passwordHasher.encode("Admin1"), "admin");
        when(userRepository.findAll()).thenReturn(Arrays.asList(alice, root));

        UserInfoDTO userLogin = userController.login(new UserLoginDTO("alice", "Secret1"));
        UserInfoDTO adminLogin = userController.login(new UserLoginDTO("root", "Admin1"));

        assertEquals("user.html", userLogin.getPage());
        assertEquals("admin.html", adminLogin.getPage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginWithLegacyPlainTextPasswordUpgradesToHash() {
        User alice = user(1, "alice", "Secret1", "user");
        when(userRepository.findAll()).thenReturn(Collections.singletonList(alice));

        assertEquals("error.html", userController.login(new UserLoginDTO("alice", "SECRET1")).getPage());
        verify(userRepository, never()).save(any(User.class));

        assertEquals("user.html", userController.login(new UserLoginDTO("alice", "Secret1")).getPage());
        verify(userRepository).save(alice);
        assertTrue(passwordHasher.isHashed(alice.getPassword()));
        assertTrue(passwordHasher.matches("Secret1", alice.getPassword()));
    }

    @Test
    void editUserWithNewPasswordStoresHash() {
        User stored = user(1, "alice", passwordHasher.encode("Secret1"), "user");
        when(userRepository.findById(1L)).thenReturn(Optional.of(stored));
        when(userRepository.save(any(User.class))).then(returnsFirstArg());

        User saved = userController.editUser(1, user(1, "alice", "NewPass2", "user"));

        assertNotEquals("NewPass2", saved.getPassword());
        assertTrue(passwordHasher.matches("NewPass2", saved.getPassword()));
    }

    @Test
    void editUserResendingStoredHashKeepsLoginWorking() {
        String storedHash = passwordHasher.encode("Secret1");
        User stored = user(1, "alice", storedHash, "user");
        when(userRepository.findById(1L)).thenReturn(Optional.of(stored));
        when(userRepository.save(any(User.class))).then(returnsFirstArg());

        User saved = userController.editUser(1, user(1, "alice", storedHash, "user"));

        assertEquals(storedHash, saved.getPassword());
        when(userRepository.findAll()).thenReturn(Collections.singletonList(saved));
        assertEquals("user.html", userController.login(new UserLoginDTO("alice", "Secret1")).getPage());
    }

    @Test
    void editUserWithBlankPasswordKeepsStoredHash() {
        String storedHash = passwordHasher.encode("Secret1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user(1, "alice", storedHash, "user")));
        when(userRepository.save(any(User.class))).then(returnsFirstArg());

        User saved = userController.editUser(1, user(1, "alice", "", "user"));

        assertEquals(storedHash, saved.getPassword());
    }

    @Test
    void editUserOfLegacyPlainTextRowStoresHash() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user(1, "alice", "Secret1", "user")));
        when(userRepository.save(any(User.class))).then(returnsFirstArg());

        User saved = userController.editUser(1, user(1, "alice", "Secret1", "user"));

        assertTrue(passwordHasher.isHashed(saved.getPassword()));
        assertTrue(passwordHasher.matches("Secret1", saved.getPassword()));
    }
}
