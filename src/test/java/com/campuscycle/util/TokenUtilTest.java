package com.campuscycle.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenUtilTest {
    @Test
    void generatesUniqueHexResetTokens() {
        String first = TokenUtil.generateToken();
        String second = TokenUtil.generateToken();

        assertEquals(32, first.length());
        assertTrue(first.matches("[0-9a-f]+"));
        assertNotEquals(first, second);
    }
}
