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

package net.orange_box.storebox.harness.interfaces.changes;

import net.orange_box.storebox.annotations.method.KeyByString;
import net.orange_box.storebox.annotations.method.TypeAdapter;
import net.orange_box.storebox.annotations.method.ChangeListenerRegisterMethod;
import net.orange_box.storebox.annotations.method.ChangeListenerUnregisterMethod;
import net.orange_box.storebox.harness.types.CustomClass;
import net.orange_box.storebox.harness.types.adapters.CustomClassTypeAdapter;
import net.orange_box.storebox.listeners.OnValueChangedListener;

public interface ChangeListenersInterface {
    
    @KeyByString("key_int")
    void setInt(int value);
    
    @KeyByString("key_int")
    @ChangeListenerRegisterMethod
    void registerIntChangeListener(
            OnValueChangedListener<Integer> newValue);

    @KeyByString("key_int")
    @ChangeListenerUnregisterMethod
    void unregisterIntChangeListener(
            OnValueChangedListener<Integer> newValue);


    @KeyByString("key_custom_class")
    @TypeAdapter(CustomClassTypeAdapter.class)
    void setCustomClass(CustomClass value);

    @KeyByString("key_custom_class")
    @ChangeListenerRegisterMethod
    @TypeAdapter(CustomClassTypeAdapter.class)
    void registerCustomClassChangeListener(
            OnValueChangedListener<CustomClass> newValue);

    @KeyByString("key_custom_class")
    @ChangeListenerUnregisterMethod
    @TypeAdapter(CustomClassTypeAdapter.class)
    void unregisterCustomClassChangeListener(
            OnValueChangedListener<CustomClass> newValue);
}
