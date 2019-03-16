package com.tridevmc.jorm.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a column as the primary key of a table.
 *
 * @author CalmBit
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {

    /**
     * The name of the column in the table. Defaults to the field name if left empty.
     *
     * @return the name of the column in the table.
     */
    String name() default "";
}
