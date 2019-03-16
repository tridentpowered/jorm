package com.tridevmc.jorm;

import org.junit.Test;

public class DatabaseUtilityTest {

    @Test
    public void createTableIfNotExists() {
        DatabaseUtility.createTableIfNotExists(Author.class);
        DatabaseUtility.createTableIfNotExists(Book.class);
    }
}