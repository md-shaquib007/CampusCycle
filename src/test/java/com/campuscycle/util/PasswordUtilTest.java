package com.campuscycle.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {
    @Test
    void hashesAndVerifiesPasswordsWithoutStoringPlainText() {
        String password = "portfolio-password-123";
        String hash = PasswordUtil.hash(password);

        assertNotEquals(password, hash);
        assertTrue(PasswordUtil.verify(password, hash));
        assertFalse(PasswordUtil.verify("wrong-password", hash));
    }

    @Test
    void rejectsInvalidHashes() {
        assertFalse(PasswordUtil.verify("password", "not-a-bcrypt-hash"));
    }
}
