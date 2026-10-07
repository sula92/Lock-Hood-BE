package com.t6.lockhood.utility;

import com.t6.lockhood.model.User;
import com.t6.lockhood.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LegacyPasswordMigrationTest {

    @Mock
    UserRepository userRepository;

    private final PasswordHasher passwordHasher = new PasswordHasher();

    @Test
    @SuppressWarnings("unchecked")
    void hashesOnlyPlainTextPasswords() {
        String existingHash = passwordHasher.encode("Other1");
        User legacy = User.builder().id(1).userName("alice").password("Secret1").privilege("user").build();
        User hashed = User.builder().id(2).userName("bob").password(existingHash).privilege("admin").build();
        when(userRepository.findAll()).thenReturn(Arrays.asList(legacy, hashed));

        new LegacyPasswordMigration(userRepository, passwordHasher).run(null);

        ArgumentCaptor<List<User>> saved = ArgumentCaptor.forClass(List.class);
        verify(userRepository).saveAll(saved.capture());
        assertEquals(1, saved.getValue().size());
        assertSame(legacy, saved.getValue().get(0));
        assertTrue(passwordHasher.isHashed(legacy.getPassword()));
        assertTrue(passwordHasher.matches("Secret1", legacy.getPassword()));
        assertEquals(existingHash, hashed.getPassword());
    }

    @Test
    void doesNothingWhenAllPasswordsAreHashed() {
        User hashed = User.builder().id(2).userName("bob").password(passwordHasher.encode("Other1")).privilege("admin").build();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(hashed));

        new LegacyPasswordMigration(userRepository, passwordHasher).run(null);

        verify(userRepository, never()).saveAll(any());
    }
}
