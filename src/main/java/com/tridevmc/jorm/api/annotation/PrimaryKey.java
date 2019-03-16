package com.tridevmc.jorm.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a column as the primary key of a table.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {

    /**
     * The name of the constraint. Defaults to "PK_[TABLE NAME]" if empty.
     *
     * @return the name of the constraint.
     */
    String name() default "";
}
