package com.tridevmc.jorm.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a field as a reference to a column within a table.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    /**
     * The name of the column in the table. Defaults to the field name if left empty.
     *
     * @return the name of the column in the table.
     */
    String name() default "";

    /**
     * The maximum length of the content that can be stored in the column. Defaults to no restraint if 0.
     *
     * @return the maximum length of the content that can be stored in the column.
     */
    int length() default 0;
}
