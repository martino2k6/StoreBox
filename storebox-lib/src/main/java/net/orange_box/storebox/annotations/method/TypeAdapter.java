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

import net.orange_box.storebox.adapters.StoreBoxTypeAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which should be used on set and get methods to declare the
 * {@link StoreBoxTypeAdapter} to be used for adapting the type for the
 * preferences.
 * <p>
 * Type adapters for {@link java.util.Date}, {@link Enum}, and
 * {@link android.net.Uri} are already supported and as such there is no need
 * to provide a type adapter for them.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TypeAdapter {

    Class<? extends StoreBoxTypeAdapter> value();
}
