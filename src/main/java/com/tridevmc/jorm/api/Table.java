package com.tridevmc.jorm.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a class as a reference to a table within a database.
 *
 * @author CalmBit
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    /**
     * The name of the table in the database, defaults to class name if left empty.
     *
     * @return the name of the table in the database.
     */
    String name() default "";
}
