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

import net.orange_box.storebox.adapters.StoreBoxTypeAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which should be used at interface-level to define the
 * {@link StoreBoxTypeAdapter} to be used for a set of keys defined
 * as strings in {@link #stringKeys()} and/or as XML string resource
 * identifiers in {@link #resourceKeys()}.
 * <p>
 * This annotation only needs to be used when storing/retrieving
 * custom types which are not supported by Android natively or as
 * built-in type adapters provided by the library.
 * <p>
 * Please note that only one instance of the annotation will be read
 * from the interface. If you wish to use more than a single custom
 * type then you should use the {@link TypeAdapters} annotation
 * instead.
 * 
 * @see TypeAdapters
 * @see StoreBoxTypeAdapter
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeAdapter {

    Class<? extends StoreBoxTypeAdapter> adapter();
    
    String[] stringKeys() default {};
    
    int[] resourceKeys() default {};
}
