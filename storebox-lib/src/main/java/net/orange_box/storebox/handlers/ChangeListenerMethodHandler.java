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
import net.orange_box.storebox.annotations.method.RegisterChangeListenerMethod;
import net.orange_box.storebox.annotations.method.UnregisterChangeListenerMethod;
import net.orange_box.storebox.annotations.method.TypeAdapter;
import net.orange_box.storebox.listeners.OnPreferenceValueChangedListener;
import net.orange_box.storebox.utils.PreferenceUtils;
import net.orange_box.storebox.utils.TypeUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Arrays;
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
                    "At least one argument must be supplied for" +
                            "register/unregister listener method");
        }
        
        final Set<ListenerInfo> listenerInfos = new HashSet<>();
        for (final Object arg : args) {
            if (arg instanceof OnPreferenceValueChangedListener) {
                listenerInfos.add(ListenerInfo.create(
                        (OnPreferenceValueChangedListener) arg,
                        method.getAnnotation(TypeAdapter.class)));
            } else if (arg instanceof OnPreferenceValueChangedListener[]) {
                listenerInfos.addAll(Arrays.asList(ListenerInfo.create(
                        (OnPreferenceValueChangedListener[]) arg,
                        method.getAnnotation(TypeAdapter.class))));
            } else {
                throw new IllegalArgumentException(String.format(
                        Locale.ENGLISH,
                        "Argument for register/unregister" +
                                " listener method must be of %s type",
                        OnPreferenceValueChangedListener.class.getSimpleName()));
            }
        }
        
        if (method.isAnnotationPresent(
                RegisterChangeListenerMethod.class)) {
            
            if (listeners.containsKey(key)) {
                listeners.get(key).addAll(listenerInfos);
            } else {
                listeners.put(key, listenerInfos);
            }
        } else if (method.isAnnotationPresent(
                UnregisterChangeListenerMethod.class)) {
            
            if (listeners.containsKey(key)) {
                listeners.get(key).removeAll(listenerInfos);
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

                final OnPreferenceValueChangedListener listener =
                        listenerInfo.getListener();

                if (listener != null) {
                    listener.onChanged(adapter.adaptFromPreferences(newValue));
                } else {
                    it.remove();
                }
            }
        }
    }
    
    private static class ListenerInfo {
        
        private final WeakReference<OnPreferenceValueChangedListener> listener;
        private final StoreBoxTypeAdapter adapter;
        
        private final int hashCode;
        
        public ListenerInfo(
                OnPreferenceValueChangedListener listener,
                StoreBoxTypeAdapter adapter) {
            
            this.listener = new WeakReference<>(listener);
            this.adapter = adapter;
            
            hashCode = listener.hashCode();
        }
        
        @Nullable
        public OnPreferenceValueChangedListener getListener() {
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

        private static ListenerInfo create(
                OnPreferenceValueChangedListener listener,
                TypeAdapter adapterAnnotation) {

            return new ListenerInfo(
                    listener,
                    TypeUtils.getTypeAdapter(
                            TypeResolver.resolveRawArguments(
                                    OnPreferenceValueChangedListener.class,
                                    listener.getClass())[0],
                            adapterAnnotation));
        }

        private static ListenerInfo[] create(
                OnPreferenceValueChangedListener[] listeners,
                TypeAdapter adapterAnnotation) {

            final ListenerInfo[] result = new ListenerInfo[listeners.length];
            
            for (int i = 0; i < listeners.length; i++) {
                result[i] = create(listeners[i], adapterAnnotation);
            }
            
            return result;
        }
    }
}
