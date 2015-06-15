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

package net.orange_box.storebox.handlers;

import android.content.SharedPreferences;

import net.jodah.typetools.TypeResolver;
import net.orange_box.storebox.adapters.StoreBoxTypeAdapter;
import net.orange_box.storebox.annotations.method.TypeAdapter;
import net.orange_box.storebox.annotations.method.ChangeListenerRegisterMethod;
import net.orange_box.storebox.annotations.method.ChangeListenerUnregisterMethod;
import net.orange_box.storebox.listeners.OnValueChangedListener;
import net.orange_box.storebox.utils.PreferenceUtils;
import net.orange_box.storebox.utils.TypeUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChangeListenerMethodHandler implements
        MethodHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {
    
    private final SharedPreferences prefs;
    private final Map<String, Set<ListenerInfo>> listeners;
    
    public ChangeListenerMethodHandler(SharedPreferences prefs) {
        this.prefs = prefs;
        
        listeners = new HashMap<>();
        
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public Object handleInvocation(
            String key,
            Object proxy,
            Method method,
            Object... args)
            throws Throwable {
        
        final ListenerInfo value = new ListenerInfo(
                (OnValueChangedListener) args[0],
                TypeUtils.getTypeAdapter(
                        TypeResolver.resolveRawArguments(
                                OnValueChangedListener.class,
                                args[0].getClass())[0],
                        method.getAnnotation(TypeAdapter.class)));
        
        if (method.isAnnotationPresent(
                ChangeListenerRegisterMethod.class)) {
            
            if (listeners.containsKey(key)) {
                listeners.get(key).add(value);
            } else {
                final Set<ListenerInfo> set = new HashSet<>();
                set.add(value);
                
                listeners.put(key, set);
            }
        } else if (method.isAnnotationPresent(
                ChangeListenerUnregisterMethod.class)) {
            
            if (listeners.containsKey(key)) {
                listeners.get(key).remove(value);
            }
        }
        
        return null;
    }

    @Override
    public void onSharedPreferenceChanged(
            SharedPreferences sharedPreferences, 
            String key) {
        
        final Set<ListenerInfo> listeners = this.listeners.get(key);
        if (listeners != null) {
            for (final ListenerInfo listener : listeners) {
                final Object newValue = PreferenceUtils.getValue(
                        prefs,
                        key,
                        listener.getAdapter().getStoreType(),
                        listener.getAdapter().getDefaultValue());
                
                listener.getListener().onChanged(
                        listener.getAdapter().adaptFromPreferences(newValue));
            }
        }
    }
    
    private static class ListenerInfo {
        
        private final OnValueChangedListener listener;
        private final StoreBoxTypeAdapter adapter;
        
        private int hashCode;
        
        public ListenerInfo(
                OnValueChangedListener listener,
                StoreBoxTypeAdapter adapter) {
            
            this.listener = listener;
            this.adapter = adapter;
        }
        
        public OnValueChangedListener getListener() {
            return listener;
        }
        
        public StoreBoxTypeAdapter getAdapter() {
            return adapter;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof ListenerInfo)) {
                return false;
            } else if (this == o) {
                return true;
            }
            
            final ListenerInfo other = (ListenerInfo) o;
            return listener.equals(other.listener);
        }
        
        @Override
        public int hashCode() {
            if (hashCode == 0) {
                hashCode = listener.hashCode();
            }
            
            return hashCode;
        }
    }
}
