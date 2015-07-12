/*
 * Copyright 2015 Martin Bella
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.orange_box.storebox.annotations.type;

import net.orange_box.storebox.PreferencesVersionHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used at interface-level to define the version of the
 * preferences and the {@link PreferencesVersionHandler} for handling the
 * version changes.
 * <p>
 * If not previously specified, the version used by default is {@code 0},
 * as such first time the version needs to be changed due to schema changes
 * then a {@link #version()} of {@code 1} should be used.
 * <p>
 * This annotation will normally not be required, but it may be needed for
 * handling changes to the schema of stored values, for example if a value
 * stored as a string is changed to a different type, or if an enum constant
 * is renamed or removed, amongst other cases.
 * 
 * @see PreferencesVersionHandler
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreferencesVersion {
    
    int version();
    
    Class<? extends PreferencesVersionHandler> handler();
}
