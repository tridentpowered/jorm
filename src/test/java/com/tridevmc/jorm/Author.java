package com.tridevmc.jorm;

import com.tridevmc.jorm.api.annotation.Column;
import com.tridevmc.jorm.api.annotation.PrimaryKey;
import com.tridevmc.jorm.api.annotation.Table;

@Table
public class Author {

    @Column
    @PrimaryKey
    public int id;

    @Column(length = 24)
    public String firstName;

    @Column(length = 24)
    public String lastName;
}
