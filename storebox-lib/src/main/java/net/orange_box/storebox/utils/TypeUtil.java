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

package net.orange_box.storebox.utils;

import java.util.HashMap;
import java.util.Map;

public final class TypeUtil {
    
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_BOXED_MAP;
    static {
        final Map<Class<?>, Class<?>> map = new HashMap<>(4);
        map.put(boolean.class, Boolean.class);
        map.put(float.class, Float.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        
        PRIMITIVE_TO_BOXED_MAP = map;
    }

    private static boolean DEFAULT_BOOLEAN;
    private static float DEFAULT_FLOAT;
    private static int DEFAULT_INT;
    private static long DEFAULT_LONG;
    
    public static Class<?> wrapToBoxedType(Class<?> type) {
        if (type.isPrimitive() && PRIMITIVE_TO_BOXED_MAP.containsKey(type)) {
            return PRIMITIVE_TO_BOXED_MAP.get(type);
        } else {
            return type;
        }
    }
    
    public static <T> T createDefaultInstanceFor(Class<T> type) {
        if (type == Boolean.class) {
            return type.cast(DEFAULT_BOOLEAN);
        } else if (type == Float.class) {
            return type.cast(DEFAULT_FLOAT);
        } else if (type == Integer.class) {
            return type.cast(DEFAULT_INT);
        } else if (type == Long.class) {
            return type.cast(DEFAULT_LONG);
        } else if (type == String.class) {
            return type.cast("");
        } else {
            throw new UnsupportedOperationException(
                    type.getName() + " not supported");
        }
    }
    
    private TypeUtil() {}
}
