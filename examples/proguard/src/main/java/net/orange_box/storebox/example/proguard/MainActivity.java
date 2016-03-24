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

package net.orange_box.storebox.example.proguard;

import android.app.Activity;
import android.os.Bundle;

import net.orange_box.storebox.StoreBox;
import net.orange_box.storebox.listeners.OnPreferenceValueChangedListener;

import java.util.concurrent.atomic.AtomicInteger;

public final class MainActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final Preferences prefs = StoreBox.create(this, Preferences.class);
        prefs.clear();
        
        final AtomicInteger listenerValue = new AtomicInteger(-1);
        final OnPreferenceValueChangedListener<Integer> listener =
                new OnPreferenceValueChangedListener<Integer>() {
                    @Override
                    public void onChanged(Integer newValue) {
                        listenerValue.set(newValue);
                    }
                };
        
        prefs.regIntListener(listener);
        
        if (prefs.getInt() != 1) {
            throw new RuntimeException("Failed at getInt() default value");
        }
        
        prefs.setInt(2);
        
        if (prefs.getInt() != 2) {
            throw new RuntimeException("Failed at getInt() after setInt()");
        }
        
        if (listenerValue.get() != 2) {
            throw new RuntimeException("Listener was not invoked");
        }
        
        prefs.unregIntListener(listener);
        prefs.setInt(3);
        
        if (listenerValue.get() != 2) {
            throw new RuntimeException("Listener shouldn't have been invoked");
        }
        
        if (getPreferences(MODE_PRIVATE).getAll().isEmpty()) {
            throw new RuntimeException("Not saved to activity preferences");
        }
    }
}
