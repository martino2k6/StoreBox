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
import android.support.annotation.Nullable;

import net.jodah.typetools.TypeResolver;
import net.orange_box.storebox.adapters.StoreBoxTypeAdapter;
import net.orange_box.storebox.annotations.method.ChangeListenerRegisterMethod;
import net.orange_box.storebox.annotations.method.ChangeListenerUnregisterMethod;
import net.orange_box.storebox.annotations.method.TypeAdapter;
import net.orange_box.storebox.listeners.OnValueChangedListener;
import net.orange_box.storebox.utils.PreferenceUtils;
import net.orange_box.storebox.utils.TypeUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO should use a ReferenceQueue for cleaning up
 */
public class ChangeListenerMethodHandler implements
        MethodHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {
    
    private final SharedPreferences prefs;
    private final Map<String, Set<ListenerInfo>> listeners;
    
    public ChangeListenerMethodHandler(SharedPreferences prefs) {
        this.prefs = prefs;

        listeners = new ConcurrentHashMap<>();
        
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public Object handleInvocation(
            String key,
            Object proxy,
            Method method,
            Object... args)
            throws Throwable {
        
        if (args.length == 0) {
            throw new IllegalArgumentException(
                    "Cannot have empty argument for register/unregister" +
                            " listener method");
        } else if (!(args[0] instanceof OnValueChangedListener)) {
            throw new IllegalArgumentException(String.format(
                    Locale.ENGLISH,
                    "Argument for register/unregister listener method must be" +
                            " of %s type",
                    OnValueChangedListener.class.getSimpleName()));
        }
        
        final ListenerInfo listenerInfo = new ListenerInfo(
                (OnValueChangedListener) args[0],
                TypeUtils.getTypeAdapter(
                        TypeResolver.resolveRawArguments(
                                OnValueChangedListener.class,
                                args[0].getClass())[0],
                        method.getAnnotation(TypeAdapter.class)));
        
        if (method.isAnnotationPresent(
                ChangeListenerRegisterMethod.class)) {
            
            if (listeners.containsKey(key)) {
                listeners.get(key).add(listenerInfo);
            } else {
                final Set<ListenerInfo> set = new HashSet<>();
                set.add(listenerInfo);
                
                listeners.put(key, set);
            }
        } else if (method.isAnnotationPresent(
                ChangeListenerUnregisterMethod.class)) {
            
            if (listeners.containsKey(key)) {
                listeners.get(key).remove(listenerInfo);
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
            final Iterator<ListenerInfo> it = listeners.iterator();
            while (it.hasNext()) {
                final ListenerInfo listenerInfo = it.next();
                final StoreBoxTypeAdapter adapter = listenerInfo.getAdapter();

                final Object newValue = PreferenceUtils.getValue(
                        prefs,
                        key,
                        adapter.getStoreType(),
                        adapter.getDefaultValue());

                final OnValueChangedListener listener =
                        listenerInfo.getListener();

                if (listener != null) {
                    listener.onChanged(adapter.adaptFromPreferences(newValue));
                } else {
                    it.remove();
                }
            }
        }
    }
    
    private class ListenerInfo {
        
        private final WeakReference<OnValueChangedListener> listener;
        private final StoreBoxTypeAdapter adapter;
        
        private final int hashCode;
        
        public ListenerInfo(
                OnValueChangedListener listener,
                StoreBoxTypeAdapter adapter) {
            
            this.listener = new WeakReference<>(listener);
            this.adapter = adapter;
            
            hashCode = listener.hashCode();
        }
        
        @Nullable
        public OnValueChangedListener getListener() {
            return listener.get();
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
            
            return (getListener() == null)
                    ? (other.getListener() == null)
                    : getListener().equals(other.getListener());
        }
        
        @Override
        public int hashCode() {
            return hashCode;
        }
    }
}
