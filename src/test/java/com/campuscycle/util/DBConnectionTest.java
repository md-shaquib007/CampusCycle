package com.campuscycle.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DBConnectionTest {
    @Test
    void convertsNeonUriWithCredentialsToJdbcUrl() {
        String input = "postgresql://demo_user:p%40ss%2Bword@ep-example.neon.tech/neondb"
                + "?sslmode=require&channel_binding=require";

        assertEquals(
                "jdbc:postgresql://ep-example.neon.tech/neondb"
                        + "?sslmode=require&channel_binding=require"
                        + "&user=demo_user&password=p%40ss%2Bword",
                DBConnection.normalizeJdbcUrl(input));
    }
}
