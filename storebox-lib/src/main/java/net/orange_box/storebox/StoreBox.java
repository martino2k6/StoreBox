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

package net.orange_box.storebox;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import net.orange_box.storebox.adapters.StoreBoxTypeAdapter;
import net.orange_box.storebox.annotations.option.SaveOption;
import net.orange_box.storebox.annotations.type.ActivityPreferences;
import net.orange_box.storebox.annotations.type.DefaultSharedPreferences;
import net.orange_box.storebox.annotations.type.FilePreferences;
import net.orange_box.storebox.annotations.type.TypeAdapter;
import net.orange_box.storebox.annotations.type.TypeAdapters;
import net.orange_box.storebox.enums.PreferencesMode;
import net.orange_box.storebox.enums.PreferencesType;
import net.orange_box.storebox.enums.SaveMode;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Creates a no-thrills instance of the supplied interface, by reading any
 * options provided through interface-level annotations.
 * <p>
 * If you'd like to provide options dynamically at run-time, take a look at
 * {@link Builder}.
 */
public final class StoreBox {

    /**
     * @param context - the context under which the
     * {@link android.content.SharedPreferences} should be opened
     * @param cls - the interface class which should be instantiated
     * @return new instance of class {@code cls} using {@code context}
     */
    public static <T> T create(Context context, Class<T> cls) {
        return new Builder<>(context, cls).build();
    }

    private StoreBox() {}

    /**
     * Can be used to provide a customised instance of the supplied interface,
     * by setting custom options through builder methods.
     * 
     * @param <T>
     */
    public static class Builder<T> {

        private final Context context;
        private final Class<T> cls;

        private PreferencesType preferencesType = PreferencesType.DEFAULT_SHARED;
        private String preferencesName = "";
        private PreferencesMode preferencesMode = PreferencesMode.MODE_PRIVATE;
        private SaveMode saveMode = SaveMode.APPLY;
        private Map<String, StoreBoxTypeAdapter> typeAdapters = new HashMap<>();
        
        public Builder(Context context, Class<T> cls) {
            this.context = context;
            this.cls = cls;
            
            readAnnotations();
        }

        public Builder preferencesType(PreferencesType value) {
            preferencesType = value;
            return this;
        }

        public Builder preferencesType(
                PreferencesType value, String name) {

            preferencesType = value;
            preferencesName = name;
            return this;
        }

        public Builder preferencesMode(PreferencesMode value) {
            preferencesMode = value;
            return this;
        }

        public Builder saveMode(SaveMode value) {
            saveMode = value;
            return this;
        }
        
        public Builder typeAdapter(
                Class<? extends StoreBoxTypeAdapter> adapter,
                String[] keys) {
            
            final StoreBoxTypeAdapter adapterInstance;
            try {
                adapterInstance = adapter.newInstance();
            } catch (InstantiationException e) {
                throw new IllegalArgumentException(String.format(
                        Locale.ENGLISH,
                        "Failed to instantiate %s, perhaps the no-arguments " +
                                "constructor is missing?",
                        adapter.getSimpleName()),
                        e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(String.format(
                        Locale.ENGLISH,
                        "Failed to instantiate %s, perhaps the no-arguments " +
                                "constructor is not public?",
                        adapter.getSimpleName()),
                        e);
            }
            
            for (final String key : keys) {
                typeAdapters.put(key, adapterInstance);
            }
            return this;
        }

        public Builder typeAdapter(
                Class<? extends StoreBoxTypeAdapter> adapter,
                int[] resourceKeys) {
            
            final String[] keys = new String[resourceKeys.length];
            for (int i = 0; i < keys.length; i++) {
                keys[i] = context.getString(resourceKeys[i]);
            }
            
            return typeAdapter(adapter, keys);
        }

        /**
         * @return new instance of class {@code cls} using {@code context}
         */
        @SuppressWarnings("unchecked")
        public T build() {
            validate();

            return (T) Proxy.newProxyInstance(
                    cls.getClassLoader(),
                    new Class[]{cls},
                    new StoreBoxInvocationHandler(
                            context,
                            preferencesType,
                            preferencesName,
                            preferencesMode,
                            saveMode,
                            typeAdapters));
        }
        
        private void readAnnotations() {
            // type/mode option
            if (cls.isAnnotationPresent(DefaultSharedPreferences.class)) {
                preferencesType(PreferencesType.DEFAULT_SHARED);
            } else if (cls.isAnnotationPresent(ActivityPreferences.class)) {
                final ActivityPreferences annotation =
                        cls.getAnnotation(ActivityPreferences.class);

                preferencesType(PreferencesType.ACTIVITY);
                preferencesMode(annotation.mode());
            } else if (cls.isAnnotationPresent(FilePreferences.class)) {
                final FilePreferences annotation =
                        cls.getAnnotation(FilePreferences.class);

                preferencesType(PreferencesType.FILE, annotation.value());
                preferencesMode(annotation.mode());
            }
            // save option 
            if (cls.isAnnotationPresent(SaveOption.class)) {
                saveMode(cls.getAnnotation(SaveOption.class).value());
            }
            
            // type adapter(s)
            if (cls.isAnnotationPresent(TypeAdapters.class)) {
                for (   final TypeAdapter value
                        : cls.getAnnotation(TypeAdapters.class).value()) {
                    
                    readTypeAdapterAnnotation(value);
                }
            } else if (cls.isAnnotationPresent(TypeAdapter.class)) {
                readTypeAdapterAnnotation(cls.getAnnotation(TypeAdapter.class));
            }
        }
        
        private void readTypeAdapterAnnotation(TypeAdapter annotation) {
            if (    annotation.stringKeys().length == 0
                    && annotation.resourceKeys().length == 0) {
                
                throw new IllegalArgumentException(
                        "A string or resource key must be specified for a" +
                                " type adapter");
            }
            
            if (annotation.stringKeys().length > 0) {
                typeAdapter(annotation.adapter(), annotation.stringKeys());
            }
            if (annotation.resourceKeys().length > 0) {
                typeAdapter(annotation.adapter(), annotation.resourceKeys());
            }
        }
        
        private void validate() {
            if (context == null) {
                throw new IllegalArgumentException(
                        "Context cannot be null");
            }
            if (cls == null) {
                throw new IllegalArgumentException(
                        "Class cannot be null");
            } else if (!cls.isInterface()) {
                throw new IllegalArgumentException(
                        "Class needs to be an interface");
            }
            
            if (preferencesType == PreferencesType.ACTIVITY) {
                if (!(context instanceof Activity)) {
                    throw new IllegalArgumentException(String.format(
                            Locale.ENGLISH,
                            "Cannot use %1$s without an Activity context",
                            PreferencesType.ACTIVITY.name()));
                }
            } else if (preferencesType == PreferencesType.FILE) {
                if (TextUtils.isEmpty(preferencesName)) {
                    throw new IllegalArgumentException(String.format(
                            Locale.ENGLISH,
                            "Cannot use %1$s with an empty file name",
                            PreferencesType.FILE.name()));
                }
            }
        }
    }
}
