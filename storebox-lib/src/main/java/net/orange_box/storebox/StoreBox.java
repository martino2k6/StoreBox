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

import net.orange_box.storebox.annotations.option.SaveOption;
import net.orange_box.storebox.annotations.type.ActivityPreferences;
import net.orange_box.storebox.annotations.type.DefaultSharedPreferences;
import net.orange_box.storebox.annotations.type.FilePreferences;
import net.orange_box.storebox.annotations.type.PreferencesVersion;
import net.orange_box.storebox.enums.PreferencesMode;
import net.orange_box.storebox.enums.PreferencesType;
import net.orange_box.storebox.enums.SaveMode;

import java.lang.reflect.Proxy;
import java.util.Locale;

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
        
        private int preferencesVersion = 0;
        private PreferencesVersionHandler preferencesVersionHandler;

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
                            preferencesVersion,
                            preferencesVersionHandler));
        }
        
        private Builder preferencesVersion(
                int version, PreferencesVersionHandler handler) {
            
            preferencesVersion = version;
            preferencesVersionHandler = handler;
            return this;
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
            
            // preferences version
            if (cls.isAnnotationPresent(PreferencesVersion.class)) {
                final PreferencesVersion annotation =
                        cls.getAnnotation(PreferencesVersion.class);
                
                try {
                    preferencesVersion(
                            annotation.version(),
                            annotation.handler().newInstance());
                } catch (InstantiationException e) {
                    throw new RuntimeException(String.format(
                            Locale.ENGLISH,
                            "Failed to instantiate %1$s, perhaps the " +
                                    "no-arguments constructor is missing?",
                            annotation.handler().getSimpleName()),
                            e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(String.format(
                            Locale.ENGLISH,
                            "Failed to instantiate %1$s, perhaps the " +
                                    "no-arguments constructor is not public?",
                            annotation.handler().getSimpleName()),
                            e);
                }
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
