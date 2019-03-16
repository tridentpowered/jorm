package com.tridevmc.jorm;

import org.junit.Test;

public class JormTest {

    @Test
    public void createTableIfNotExists() {
        Jorm.createTableIfNotExists(Author.class);
        Jorm.createTableIfNotExists(Book.class);
    }
}