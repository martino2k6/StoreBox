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

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;

import net.orange_box.storebox.adapters.StoreType;
import net.orange_box.storebox.enums.SaveMode;

import java.util.Locale;
import java.util.Set;

public final class PreferenceUtils {
    
    public static Object getValue(
            SharedPreferences prefs,
            String key,
            StoreType type,
            Object defValue) {
        
        switch (type) {
            case BOOLEAN:
                return prefs.getBoolean(key, (Boolean) defValue);
            
            case FLOAT:
                return prefs.getFloat(key, (Float) defValue);
            
            case INTEGER:
                return prefs.getInt(key, (Integer) defValue);
            
            case LONG:
                return prefs.getLong(key, (Long) defValue);
            
            case STRING:
                return prefs.getString(key, (String) defValue);
            
            case STRING_SET:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    return getStringSetApi11(
                            prefs, key, (Set<String>) defValue);
                }

            default:
                throw new UnsupportedOperationException(String.format(
                        Locale.ENGLISH,
                        "Retrieving type %1$s from the preferences is not supported",
                        type.name()));
        }
    }
    
    public static void putValue(
            SharedPreferences.Editor editor,
            String key,
            StoreType type,
            Object value) {
        
        switch (type) {
            case BOOLEAN:
                editor.putBoolean(key, (Boolean) value);
                break;
            
            case FLOAT:
                editor.putFloat(key, (Float) value);
                break;
            
            case INTEGER:
                editor.putInt(key, (Integer) value);
                break;
            
            case LONG:
                editor.putLong(key, (Long) value);
                break;
            
            case STRING:
                editor.putString(key, (String) value);
                break;
            
            case STRING_SET:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    putStringSetApi11(editor, key, (Set<String>) value);
                    break;
                }
            
            default:
                throw new UnsupportedOperationException(String.format(
                        Locale.ENGLISH,
                        "Saving type %1$s into the preferences is not supported",
                        type.name()));
        }
    }
    
    public static void saveChanges(
            SharedPreferences.Editor editor,
            SaveMode mode) {
        
        switch (mode) {
            case APPLY:
                editor.apply();
                break;

            case COMMIT:
                editor.commit();
                break;

            case NOME:
            default:
                // NOP
        }
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static Set<String> getStringSetApi11(
            SharedPreferences prefs,
            String key,
            Set<String> defValue) {
        
        return prefs.getStringSet(key, defValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void putStringSetApi11(
            SharedPreferences.Editor editor,
            String key,
            Set<String> value) {
        
        editor.putStringSet(key, value);
    }
    
    private PreferenceUtils() {}
}
