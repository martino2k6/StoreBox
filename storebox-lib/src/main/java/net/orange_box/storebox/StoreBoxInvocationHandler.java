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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.TypedValue;

import net.orange_box.storebox.annotations.method.DefaultValue;
import net.orange_box.storebox.annotations.method.KeyByResource;
import net.orange_box.storebox.annotations.method.KeyByString;
import net.orange_box.storebox.annotations.method.RemoveMethod;
import net.orange_box.storebox.annotations.option.DefaultValueOption;
import net.orange_box.storebox.annotations.option.SaveOption;
import net.orange_box.storebox.enums.DefaultValueMode;
import net.orange_box.storebox.enums.PreferencesMode;
import net.orange_box.storebox.enums.PreferencesType;
import net.orange_box.storebox.enums.SaveMode;
import net.orange_box.storebox.utils.MethodUtils;
import net.orange_box.storebox.utils.PreferenceUtils;
import net.orange_box.storebox.utils.TypeUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Locale;

/**
 * This is where the magic happens...
 */
@SuppressLint("CommitPrefEdits")
class StoreBoxInvocationHandler implements InvocationHandler {

    private static final Method OBJECT_EQUALS =
            MethodUtils.getObjectMethod("equals", Object.class);
    private static final Method OBJECT_HASHCODE =
            MethodUtils.getObjectMethod("hashCode");
    private static final Method OBJECT_TOSTRING =
            MethodUtils.getObjectMethod("toString");
    
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private final Resources res;
    
    private final SaveMode saveMode;
    private final DefaultValueMode defaultValueMode;
    
    private int hashCode;
    
    public StoreBoxInvocationHandler(
            Context context,
            PreferencesType preferencesType,
            String openNameValue,
            PreferencesMode preferencesMode,
            SaveMode saveMode,
            DefaultValueMode defaultValueMode) {
        
        switch (preferencesType) {
            case ACTIVITY:
                prefs = ((Activity) context).getPreferences(
                        preferencesMode.value());
                break;
            
            case FILE:
                prefs = context.getSharedPreferences(
                        openNameValue, preferencesMode.value());
                break;
            
            case DEFAULT_SHARED:
            default:
                prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }
        
        editor = prefs.edit();
        res = context.getResources();
        
        this.saveMode = saveMode;
        this.defaultValueMode = defaultValueMode;
    }
    
    @Override
    public Object invoke(
            Object proxy, Method method, Object... args) throws Throwable {
        
        /*
         * Find the key for the preference from the method's annotation, or
         * whether it's a remove method, else we try to forward the method to
         * the SharedPreferences or Editor implementations.
         */
        final String key;
        final boolean isRemove;
        if (method.isAnnotationPresent(KeyByString.class)) {
            key = method.getAnnotation(KeyByString.class).value();
            
            isRemove = method.isAnnotationPresent(RemoveMethod.class);
        } else if (method.isAnnotationPresent(KeyByResource.class)) {
            key = res.getString(
                    method.getAnnotation(KeyByResource.class).value());
            
            isRemove = method.isAnnotationPresent(RemoveMethod.class);
        } else if (method.isAnnotationPresent(RemoveMethod.class)) {
            isRemove = true;
            
            key = MethodUtils.getKeyForRemove(res, args);
        } else {
            // handle Object's equals/hashCode/toString
            if (method.equals(OBJECT_EQUALS)) {
                return internalEquals(proxy, args[0]);
            } else if (method.equals(OBJECT_HASHCODE)) {
                return internalHashCode();
            } else if (method.equals(OBJECT_TOSTRING)) {
                return toString();
            }
            
            // can we forward the method to the SharedPreferences?
            try {
                final Method prefsMethod = prefs.getClass().getDeclaredMethod(
                        method.getName(),
                        method.getParameterTypes());
                
                return prefsMethod.invoke(prefs, args);
            } catch (NoSuchMethodException e) {
                // NOP
            }
            
            // can we forward the method to the Editor?
            try {
                final Method editorMethod = editor.getClass().getDeclaredMethod(
                        method.getName(),
                        method.getParameterTypes());
                
                return editorMethod.invoke(editor, args);
            } catch (NoSuchMethodException e) {
                // NOP
            }
            
            // fail fast, rather than ignoring the method invocation
            throw new UnsupportedOperationException(String.format(
                    Locale.ENGLISH,
                    "Failed to invoke %1$s method, " +
                            "perhaps the %2$s or %3$s annotation is missing?",
                    method.getName(),
                    KeyByString.class.getSimpleName(),
                    KeyByResource.class.getSimpleName()));
        }
        
        /*
         * Find out based on the method return type whether it's a get or set
         * operation. We could provide a further annotation for get/set methods,
         * but we can infer this reasonably easily.
         */
        final Class<?> returnType = method.getReturnType();
        if (isRemove) {
            editor.remove(key);
        } else if (
                returnType == Void.TYPE
                || returnType == method.getDeclaringClass()
                || returnType == SharedPreferences.Editor.class) {
            
            /*
             * Set.
             * 
             * Argument types are boxed for us, so we only need to check one
             * variant.
             */
            final Object value = MethodUtils.getValueArg(args);
            final Class<?> type = MethodUtils.getValueParameterType(method);
            
            if (type == Boolean.class) {
                
                editor.putBoolean(key, (Boolean) value);
                
            } else if (type == Float.class) {
                
                editor.putFloat(key, (Float) value);
                
            } else if (type == Integer.class) {
                
                editor.putInt(key, (Integer) value);
                
            } else if (type == Long.class) {
                
                editor.putLong(key, (Long) value);
                
            } else if (type == String.class) {
                
                editor.putString(key, (String) value);
                
            } else {
                throw new UnsupportedOperationException(String.format(
                        Locale.ENGLISH,
                        "Saving type %1$s is not supported",
                        type.getName()));
            }
        } else {
            /*
             * Get.
             * 
             * We wrap any primitive types to their boxed equivalents, as this
             * makes type safety a bit nicer.
             */
            final Class<?> type = TypeUtils.wrapToBoxedType(method.getReturnType());
            final boolean typePrimitive = method.getReturnType().isPrimitive();
            
            if (type == Boolean.class) {

                return prefs.getBoolean(key, getDefaultValueArg(
                        method, Boolean.class, typePrimitive, args));
                
            } else if (type == Float.class) {
                
                return prefs.getFloat(key, getDefaultValueArg(
                        method, Float.class, typePrimitive, args));
                
            } else if (type == Integer.class) {
                
                return prefs.getInt(key, getDefaultValueArg(
                        method, Integer.class, typePrimitive, args));
                
            } else if (type == Long.class) {
                
                return prefs.getLong(key, getDefaultValueArg(
                        method, Long.class, typePrimitive, args));
                
            } else if (type == String.class) {
                
                return prefs.getString(key, getDefaultValueArg(
                        method, String.class, typePrimitive, args));
                
            } else {
                throw new UnsupportedOperationException(String.format(
                        Locale.ENGLISH,
                        "Retrieving type %1$s is not supported",
                        type.getName()));
            }
        }

        // method-level strategy > class-level strategy
        final SaveMode mode;
        if (method.isAnnotationPresent(SaveOption.class)) {
            mode = method.getAnnotation(SaveOption.class).value();
        } else {
            mode = saveMode;
        }
        PreferenceUtils.saveChanges(editor, mode);

        if (returnType == method.getDeclaringClass()) {
            return proxy;
        } else if (returnType == SharedPreferences.Editor.class) {
            return editor;
        } else {
            return null;
        }
    }
    
    private boolean internalEquals(Object us, Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != us.getClass()) {
            return false;
        }

        final InvocationHandler otherHandler =
                Proxy.getInvocationHandler(other);
        return  (otherHandler instanceof StoreBoxInvocationHandler)
                && (us == other);

    }
    
    private int internalHashCode() {
        if (hashCode == 0) {
            hashCode = Arrays.hashCode(new Object[] {
                    prefs, editor, res, saveMode, defaultValueMode});
        }
        
        return hashCode;
    }
    
    private <T> T getDefaultValueArg(
            Method method,
            Class<T> type,
            boolean typePrimitive,
            Object... args) {
        
        Object result = null;
        
        // parameter default > method-level default
        if (args != null && args.length > 0) {
            result = args[0];
        }
        if (result == null && method.isAnnotationPresent(DefaultValue.class)) {
            final TypedValue value = new TypedValue();
            res.getValue(
                    method.getAnnotation(DefaultValue.class).value(),
                    value,
                    true);
            
            if (type == Boolean.class) {
                result = value.data != 0;
            } else if (type == Float.class) {
                result = value.getFloat();
            } else if (type == Integer.class) {
                result = value.data;
            } else if (type == Long.class) {
                result = value.data;
            } else if (type == String.class) {
                if (value.string == null) {
                    result = new Object(); // we'll fail later
                } else {
                    result = value.string;
                }
            } else {
                throw new UnsupportedOperationException(
                        type.getName() + " not supported");
            }
        }
        
        // default was not provided so let's see how we should create it
        if (result == null) {
            final boolean instantiate;
            if (typePrimitive) {
                instantiate = true;
            } else {
                // method-level option > class-level option
                final DefaultValueMode mode;
                if (method.isAnnotationPresent(DefaultValueOption.class)) {
                    mode = method.getAnnotation(DefaultValueOption.class)
                            .value();
                } else {
                    mode = defaultValueMode;
                }
                
                switch (mode) {
                    case EMPTY:
                        instantiate = true;
                        break;
                    
                    case NULL:
                    default:
                        instantiate = false;
                }
            }
            
            if (instantiate) {
                return TypeUtils.createDefaultInstanceFor(type);
            } else {
                return null;
            }
        } else {
            if (result.getClass() != type) {
                throw new UnsupportedOperationException(String.format(
                        Locale.ENGLISH,
                        "Return type %1$s and default value type %2$s not the same",
                        result.getClass().getName(),
                        type.getName()));
            } else {
                return type.cast(result);
            }
        }
    }
}
