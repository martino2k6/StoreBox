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

import android.test.suitebuilder.annotation.SmallTest;

import net.orange_box.storebox.harness.base.PreferencesTestCase;
import net.orange_box.storebox.harness.interfaces.changes.ChangeListenersInterface;
import net.orange_box.storebox.harness.types.CustomClass;
import net.orange_box.storebox.listeners.OnValueChangedListener;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ChangesListenersTestCase extends
        PreferencesTestCase<ChangeListenersInterface> {

    @Override
    protected Class<ChangeListenersInterface> getInterface() {
        return ChangeListenersInterface.class;
    }
    
    @SmallTest
    public void testIntChanged() {
        final AtomicBoolean called = new AtomicBoolean();
        final OnValueChangedListener<Integer> listener =
                new OnValueChangedListener<Integer>() {
                    @Override
                    public void onChanged(Integer newValue) {
                        assertEquals(1, newValue.intValue());

                        called.set(true);
                    }
                };
        
        uut.registerIntChangeListener(listener);
        uut.setInt(1);
        
        assertTrue(called.get());
    }
    
    @SmallTest
    public void testIntChangedMultiple() {
        final AtomicInteger count = new AtomicInteger(2);
        final OnValueChangedListener<Integer> one =
                new OnValueChangedListener<Integer>() {
                    @Override
                    public void onChanged(Integer newValue) {
                        assertEquals(1, newValue.intValue());

                        count.decrementAndGet();
                    }
                };
        final OnValueChangedListener<Integer> two =
                new OnValueChangedListener<Integer>() {
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
    
    @SmallTest
    public void testIntUnregistered() {
        final AtomicBoolean called = new AtomicBoolean();
        final OnValueChangedListener<Integer> listener =
                new OnValueChangedListener<Integer>() {
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

    @SmallTest
    public void testCustomClassChanged() {
        final AtomicBoolean called = new AtomicBoolean();
        final OnValueChangedListener<CustomClass> listener =
                new OnValueChangedListener<CustomClass>() {
                    @Override
                    public void onChanged(CustomClass newValue) {
                        assertEquals(new CustomClass("a", "b"), newValue);

                        called.set(true);
                    }
                };

        uut.registerCustomClassChangeListener(listener);
        uut.setCustomClass(new CustomClass("a", "b"));

        assertTrue(called.get());
    }

    @SmallTest
    public void testCustomClassChangedNull() {
        final AtomicBoolean called = new AtomicBoolean();
        final OnValueChangedListener<CustomClass> listener =
                new OnValueChangedListener<CustomClass>() {
                    @Override
                    public void onChanged(CustomClass newValue) {
                        assertNull(newValue);

                        called.set(true);
                    }
                };

        uut.setCustomClass(new CustomClass("a", "b"));
        uut.registerCustomClassChangeListener(listener);
        uut.setCustomClass(null);

        assertTrue(called.get());
    }

    @SmallTest
    public void testCustomClassChangedMultiple() {
        final AtomicInteger count = new AtomicInteger(2);
        final OnValueChangedListener<CustomClass> one =
                new OnValueChangedListener<CustomClass>() {
                    @Override
                    public void onChanged(CustomClass newValue) {
                        assertEquals(new CustomClass("a", "b"), newValue);

                        count.decrementAndGet();
                    }
                };
        final OnValueChangedListener<CustomClass> two =
                new OnValueChangedListener<CustomClass>() {
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

    @SmallTest
    public void testCustomClassUnregistered() {
        final AtomicBoolean called = new AtomicBoolean();
        final OnValueChangedListener<CustomClass> listener =
                new OnValueChangedListener<CustomClass>() {
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
}
