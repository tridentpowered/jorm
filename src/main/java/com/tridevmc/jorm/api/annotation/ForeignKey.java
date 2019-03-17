package com.tridevmc.jorm.api.annotation;

/*
 * Copyright 2019 Trident Development (Tridev)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
     * The name of the constraint. Defaults to "FK_[TABLE_NAME]_TO_[REFERENCED TABLE NAME]" if empty.
     *
     * @return the name of the constraint.
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
