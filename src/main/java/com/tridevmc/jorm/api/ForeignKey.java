package com.tridevmc.jorm.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a column as a foreign key that references another table.
 *
 * @author CalmBit
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForeignKey {

    /**
     * The name of the column in the table. Defaults to the field name if left empty.
     *
     * @return the name of the column in the table.
     */
    String name() default "";

    /**
     * The class representing the table that this foreign key references.
     *
     * @return the table that this foreign key references.
     */
    Class tableClass();

    /**
     * The name of the column that the foreign key references in the associated table.
     *
     * @return the name of the column that the foreign key references.
     */
    String referenceField();
}
