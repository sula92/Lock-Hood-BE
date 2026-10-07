package com.t6.lockhood.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordHasherTest {

    private final PasswordHasher passwordHasher = new PasswordHasher();

    @Test
    void encodeProducesBcryptHashNotPlainText() {
        String hash = passwordHasher.encode("Secret1");

        assertNotEquals("Secret1", hash);
        assertTrue(passwordHasher.isHashed(hash));
    }

    @Test
    void matchesIsCaseSensitiveForHashedPassword() {
        String hash = passwordHasher.encode("Secret1");

        assertTrue(passwordHasher.matches("Secret1", hash));
        assertFalse(passwordHasher.matches("SECRET1", hash));
        assertFalse(passwordHasher.matches("secret1", hash));
    }

    @Test
    void matchesIsCaseSensitiveForLegacyPlainTextPassword() {
        assertFalse(passwordHasher.isHashed("Secret1"));
        assertTrue(passwordHasher.matches("Secret1", "Secret1"));
        assertFalse(passwordHasher.matches("SECRET1", "Secret1"));
    }

    @Test
    void matchesRejectsNullValues() {
        assertFalse(passwordHasher.matches(null, passwordHasher.encode("Secret1")));
        assertFalse(passwordHasher.matches("Secret1", null));
    }
}
