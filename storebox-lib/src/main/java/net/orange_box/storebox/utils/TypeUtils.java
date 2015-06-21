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

import android.net.Uri;

import net.orange_box.storebox.adapters.StoreBoxTypeAdapter;
import net.orange_box.storebox.adapters.extra.DateTypeAdapter;
import net.orange_box.storebox.adapters.extra.DoubleTypeAdapter;
import net.orange_box.storebox.adapters.extra.EnumTypeAdapter;
import net.orange_box.storebox.adapters.extra.UriTypeAdapter;
import net.orange_box.storebox.adapters.standard.BooleanTypeAdapter;
import net.orange_box.storebox.adapters.standard.FloatTypeAdapter;
import net.orange_box.storebox.adapters.standard.IntegerTypeAdapter;
import net.orange_box.storebox.adapters.standard.LongTypeAdapter;
import net.orange_box.storebox.adapters.standard.StringSetTypeAdapter;
import net.orange_box.storebox.adapters.standard.StringTypeAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class TypeUtils {
    
    private static final Object LOCK = new Object();
    
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_BOXED_MAP;
    static {
        final Map<Class<?>, Class<?>> map = new HashMap<>(5);
        // standard
        map.put(boolean.class, Boolean.class);
        map.put(float.class, Float.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        // extra
        map.put(double.class, Double.class);
        
        PRIMITIVE_TO_BOXED_MAP = map;
    }
    
    private static final Map<Class<?>, StoreBoxTypeAdapter> ADAPTERS_MAP;
    static {
        final Map<Class<?>, StoreBoxTypeAdapter> map = new HashMap<>(9);
        
        // standard
        map.put(Boolean.class, new BooleanTypeAdapter());
        map.put(Float.class, new FloatTypeAdapter());
        map.put(Integer.class, new IntegerTypeAdapter());
        map.put(Long.class, new LongTypeAdapter());
        map.put(String.class, new StringTypeAdapter());
        map.put(Set.class, new StringSetTypeAdapter());
        
        // extra
        map.put(Date.class, new DateTypeAdapter());
        map.put(Double.class, new DoubleTypeAdapter());
        map.put(Uri.class, new UriTypeAdapter());
        
        ADAPTERS_MAP = map;
    }
    
    public static Class<?> wrapToBoxedType(Class<?> type) {
        if (type.isPrimitive() && PRIMITIVE_TO_BOXED_MAP.containsKey(type)) {
            return PRIMITIVE_TO_BOXED_MAP.get(type);
        } else {
            return type;
        }
    }

    public static StoreBoxTypeAdapter getTypeAdapter(
            String key,
            Class<?> type,
            Map<String, StoreBoxTypeAdapter> typeAdapters) {

        StoreBoxTypeAdapter adapter;
        
        synchronized (LOCK) {
            adapter = typeAdapters.get(key);
            if (adapter != null) {
                return adapter;
            }

            adapter = ADAPTERS_MAP.get(type);
            if (adapter == null && type.isEnum()) {
                // enums have a special type adapter
                adapter = new EnumTypeAdapter((Class<Enum>) type);

                ADAPTERS_MAP.put(type, adapter);
                typeAdapters.put(key, adapter);
            }
        }

        if (adapter != null) {
            return adapter;
        }

        throw new RuntimeException(String.format(
                Locale.ENGLISH,
                "Failed to find type adapter for %1$s",
                type.getName()));
    }
    
    private TypeUtils() {}
}
