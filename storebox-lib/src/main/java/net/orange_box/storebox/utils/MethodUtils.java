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

import android.content.res.Resources;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public final class MethodUtils {
    
    @SafeVarargs
    public static <T extends Class<? extends Annotation>> boolean areAnyAnnotationsPresent(
            Method method, T... types) {

        for (final T type : types) {
            if (method.isAnnotationPresent(type)) {
                return true;
            }
        }
        return false;
    }
    
    public static String getKeyForRemove(Resources res, Object... args) {
        if (args != null && args.length > 0) {
            final Object value = args[0];
            final Class<?> type = TypeUtils.wrapToBoxedType(value.getClass());
            
            if (type == String.class) {
                return (String) value;
            } else if (type == Integer.class) {
                return res.getString((int) value);
            } else {
                throw new UnsupportedOperationException(
                        "Only String or int supported for remove method");
            }
        } else {
            throw new UnsupportedOperationException(
                    "String or int key argument not found for remove method");
        }
    }

    public static Object getValueArg(Object... args) {
        if (args != null && args.length > 0) {
            return args[0];
        } else {
            throw new UnsupportedOperationException(
                    "Value argument not found");
        }
    }

    public static Class<?> getValueParameterType(Method method) {
        final Class<?>[] types = method.getParameterTypes();
        if (types != null && types.length > 0) {
            return TypeUtils.wrapToBoxedType(types[0]);
        } else {
            throw new UnsupportedOperationException(
                    "Value parameter type not found");
        }
    }

    public static Method getObjectMethod(String name, Class... types) {
        try {
            return Object.class.getMethod(name, types);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    private MethodUtils() {}
}
