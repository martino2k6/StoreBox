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

package net.orange_box.storebox.harness.changes;

import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.SmallTest;

import net.orange_box.storebox.harness.base.PreferencesTestCase;
import net.orange_box.storebox.harness.interfaces.changes.ChangeListenersInterface;
import net.orange_box.storebox.harness.types.CustomClass;
import net.orange_box.storebox.listeners.OnPreferenceValueChangedListener;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ChangesListenersTestCase extends
        PreferencesTestCase<ChangeListenersInterface> {

    @Override
    protected Class<ChangeListenersInterface> getInterface() {
        return ChangeListenersInterface.class;
    }
    
    @UiThreadTest
    @SmallTest
    public void testIntChanged() {
        final AtomicInteger value = new AtomicInteger(-1);
        final OnPreferenceValueChangedListener<Integer> listener =
                new OnPreferenceValueChangedListener<Integer>() {
                    @Override
                    public void onChanged(Integer newValue) {
                        value.set(newValue);
                    }
                };
        
        uut.registerIntChangeListener(listener);
        uut.setInt(1);
        
        assertEquals(1, value.get());
    }
    
    @UiThreadTest
    @SmallTest
    public void testIntChangedMultiple() {
        final AtomicInteger count = new AtomicInteger(2);
        final OnPreferenceValueChangedListener<Integer> one =
                new OnPreferenceValueChangedListener<Integer>() {
                    @Override
                    public void onChanged(Integer newValue) {
                        assertEquals(1, newValue.intValue());

                        count.decrementAndGet();
                    }
                };
        final OnPreferenceValueChangedListener<Integer> two =
                new OnPreferenceValueChangedListener<Integer>() {
                    @Override
                    public void onChanged(Integer newValue) {
                        assertEquals(1, newValue.intValue());

                        count.decrementAndGet();
                    }
                };

        uut.registerIntChangeListener(one);
        uut.registerIntChangeListener(two);
        uut.setInt(1);

        assertEquals(0, count.get());
    }
    
    @UiThreadTest
    @SmallTest
    public void testIntChangedVarArgs() {
        final AtomicInteger count = new AtomicInteger(2);
        final OnPreferenceValueChangedListener<Integer> one =
                new OnPreferenceValueChangedListener<Integer>() {
                    @Override
                    public void onChanged(Integer newValue) {
                        assertEquals(1, newValue.intValue());

                        count.decrementAndGet();
                    }
                };
        final OnPreferenceValueChangedListener<Integer> two =
                new OnPreferenceValueChangedListener<Integer>() {
                    @Override
                    public void onChanged(Integer newValue) {
                        assertEquals(1, newValue.intValue());

                        count.decrementAndGet();
                    }
                };

        uut.registerIntChangeListenerVarArgs(one, two);
        uut.setInt(1);

        assertEquals(0, count.get());
    }
    
    @UiThreadTest
    @SmallTest
    public void testIntUnregistered() {
        final AtomicBoolean called = new AtomicBoolean();
        final OnPreferenceValueChangedListener<Integer> listener =
                new OnPreferenceValueChangedListener<Integer>() {
                    @Override
                    public void onChanged(Integer newValue) {
                        called.set(true);
                    }
                };

        uut.registerIntChangeListener(listener);
        uut.unregisterIntChangeListener(listener);
        uut.setInt(1);

        assertFalse(called.get());
    }
    
    @UiThreadTest
    @SmallTest
    public void testIntUnregisteredVarArgs() {
        final AtomicInteger count = new AtomicInteger(2);
        final OnPreferenceValueChangedListener<Integer> one =
                new OnPreferenceValueChangedListener<Integer>() {
                    @Override
                    public void onChanged(Integer newValue) {
                        count.decrementAndGet();
                    }
                };
        final OnPreferenceValueChangedListener<Integer> two =
                new OnPreferenceValueChangedListener<Integer>() {
                    @Override
                    public void onChanged(Integer newValue) {
                        count.decrementAndGet();
                    }
                };

        uut.registerIntChangeListenerVarArgs(one, two);
        uut.unregisterIntChangeListenerVarArgs(one, two);
        uut.setInt(1);

        assertEquals(2, count.get());
    }
    
    @UiThreadTest
    @SmallTest
    public void testCustomClassChanged() {
        final AtomicReference<CustomClass> value = new AtomicReference<>();
        final OnPreferenceValueChangedListener<CustomClass> listener =
                new OnPreferenceValueChangedListener<CustomClass>() {
                    @Override
                    public void onChanged(CustomClass newValue) {
                        value.set(newValue);
                    }
                };
        
        uut.registerCustomClassChangeListener(listener);
        uut.setCustomClass(new CustomClass("a", "b"));
        
        assertEquals(new CustomClass("a", "b"), value.get());
    }
    
    @UiThreadTest
    @SmallTest
    public void testCustomClassChangedNull() {
        final AtomicReference<CustomClass> value = new AtomicReference<>();
        final OnPreferenceValueChangedListener<CustomClass> listener =
                new OnPreferenceValueChangedListener<CustomClass>() {
                    @Override
                    public void onChanged(CustomClass newValue) {
                        value.set(newValue);
                    }
                };
        
        uut.setCustomClass(new CustomClass("a", "b"));
        uut.registerCustomClassChangeListener(listener);
        uut.setCustomClass(null);
        
        assertNull(value.get());
    }
    
    @UiThreadTest
    @SmallTest
    public void testCustomClassChangedMultiple() {
        final AtomicInteger count = new AtomicInteger(2);
        final OnPreferenceValueChangedListener<CustomClass> one =
                new OnPreferenceValueChangedListener<CustomClass>() {
                    @Override
                    public void onChanged(CustomClass newValue) {
                        assertEquals(new CustomClass("a", "b"), newValue);

                        count.decrementAndGet();
                    }
                };
        final OnPreferenceValueChangedListener<CustomClass> two =
                new OnPreferenceValueChangedListener<CustomClass>() {
                    @Override
                    public void onChanged(CustomClass newValue) {
                        assertEquals(new CustomClass("a", "b"), newValue);

                        count.decrementAndGet();
                    }
                };

        uut.registerCustomClassChangeListener(one);
        uut.registerCustomClassChangeListener(two);
        uut.setCustomClass(new CustomClass("a", "b"));

        assertEquals(0, count.get());
    }
    
    @UiThreadTest
    @SmallTest
    public void testCustomClassUnregistered() {
        final AtomicBoolean called = new AtomicBoolean();
        final OnPreferenceValueChangedListener<CustomClass> listener =
                new OnPreferenceValueChangedListener<CustomClass>() {
                    @Override
                    public void onChanged(CustomClass newValue) {
                        called.set(true);
                    }
                };

        uut.registerCustomClassChangeListener(listener);
        uut.unregisterCustomClassChangeListener(listener);
        uut.setCustomClass(new CustomClass("a", "b"));

        assertFalse(called.get());
    }
    
    @UiThreadTest
    @SmallTest
    public void testListenerGarbageCollected() throws Exception {
        final AtomicBoolean called = new AtomicBoolean();

        uut.registerIntChangeListener(new OnPreferenceValueChangedListener<Integer>() {
            @Override
            public void onChanged(Integer newValue) {
                called.set(true);
            }
        });
        // nasty, but it does force collection of soft references...
        // TODO is there a better way to do this?
        try {
            Object[] ignored = new Object[(int) Runtime.getRuntime().maxMemory()];
        } catch (OutOfMemoryError e) {
            // NOP
        }
        uut.setInt(1);

        assertFalse(called.get());
    }
}
