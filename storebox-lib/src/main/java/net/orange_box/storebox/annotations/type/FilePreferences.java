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

import net.orange_box.storebox.enums.PreferencesMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which should be used at interface-level to define that the
 * preferences should be opened from a file name.
 * <p>
 * When this annotation is used a file name needs to be specified using
 * {@link #value()}.
 *
 * @see net.orange_box.storebox.enums.PreferencesType#FILE
 * @see android.content.Context#getSharedPreferences(String, int)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FilePreferences {
    
    String value();
    
    PreferencesMode mode() default PreferencesMode.MODE_PRIVATE;
}
