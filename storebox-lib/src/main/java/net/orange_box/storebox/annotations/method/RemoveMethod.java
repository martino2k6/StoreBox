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

package net.orange_box.storebox.annotations.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which should be used for a remove method. The key for the value
 * which should be removed can be provided in two ways:
 * <ul>
 * <li>
 * Using just this annotation and adding either a {@link String} or {@code int}
 * type parameter to the method. The latter case can be used if the key name
 * will be provided from an XML resource.
 * </li>
 * <li>
 * Adding a {@link KeyByString} or {@link KeyByResource} annotation to the
 * method.
 * </li>
 * </ul>
 * 
 * @see android.content.SharedPreferences.Editor#remove(String)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RemoveMethod {}
