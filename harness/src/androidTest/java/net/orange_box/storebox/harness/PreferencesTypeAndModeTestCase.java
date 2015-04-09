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

package net.orange_box.storebox.harness;

import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import net.orange_box.storebox.StoreBox;
import net.orange_box.storebox.harness.activities.TestActivity;
import net.orange_box.storebox.harness.interfaces.PreferencesModeMultiProcessInterface;
import net.orange_box.storebox.harness.interfaces.PreferencesModePrivateInterface;
import net.orange_box.storebox.harness.interfaces.PreferencesModeWordWriteableInterface;
import net.orange_box.storebox.harness.interfaces.PreferencesModeWorldReadableInterface;
import net.orange_box.storebox.harness.interfaces.PreferencesTypeActivityInterface;
import net.orange_box.storebox.harness.interfaces.PreferencesTypeDefaultSharedInterface;
import net.orange_box.storebox.harness.interfaces.PreferencesTypeFileInterface;

import java.util.concurrent.atomic.AtomicInteger;

public class PreferencesTypeAndModeTestCase extends ActivityUnitTestCase<TestActivity> {
    
    public PreferencesTypeAndModeTestCase() {
        super(TestActivity.class);
    }
    
    @SmallTest
    public void testTypeDefaultShared() {
        final AtomicInteger count = new AtomicInteger(1);
        setActivityContext(new InjectedContext(
                getInstrumentation(),
                getInstrumentation().getTargetContext().getPackageName() + "_preferences",
                null,
                count));
        startActivity();
        
        StoreBox.create(getActivity(), PreferencesTypeDefaultSharedInterface.class);

        assertEquals(0, count.get());
    }
    
    @SmallTest
    public void testTypeActivity() {
        final AtomicInteger count = new AtomicInteger(1);
        setActivityContext(new InjectedContext(
                getInstrumentation(),
                TestActivity.class.getName().substring(getInstrumentation()
                        .getTargetContext().getPackageName().length() + 1),
                null,
                count));
        startActivity();
        
        StoreBox.create(getActivity(), PreferencesTypeActivityInterface.class);

        assertEquals(0, count.get());
    }
    
    @SmallTest
    public void testTypeFile() {
        final AtomicInteger count = new AtomicInteger(1);
        setActivityContext(new InjectedContext(
                getInstrumentation(), "test", null, count));
        startActivity();
        
        StoreBox.create(getActivity(), PreferencesTypeFileInterface.class);
        
        assertEquals(0, count.get());
    }
    
    @SmallTest
    public void testModePrivate() {
        final AtomicInteger count = new AtomicInteger(1);
        setActivityContext(new InjectedContext(
                getInstrumentation(), null, Context.MODE_PRIVATE, count));
        startActivity();

        StoreBox.create(getActivity(), PreferencesModePrivateInterface.class);

        assertEquals(0, count.get());
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SmallTest
    public void testModeMultiProcess() {
        final AtomicInteger count = new AtomicInteger(1);
        setActivityContext(new InjectedContext(
                getInstrumentation(), null, Context.MODE_MULTI_PROCESS, count));
        startActivity();

        StoreBox.create(getActivity(), PreferencesModeMultiProcessInterface.class);

        assertEquals(0, count.get());
    }
    
    @SuppressWarnings("deprecation")
    @SmallTest
    public void testModeWorldReadable() {
        final AtomicInteger count = new AtomicInteger(1);
        setActivityContext(new InjectedContext(
                getInstrumentation(), null, Context.MODE_WORLD_READABLE, count));
        startActivity();

        StoreBox.create(getActivity(), PreferencesModeWorldReadableInterface.class);

        assertEquals(0, count.get());
    }
    
    @SuppressWarnings("deprecation")
    @SmallTest
    public void testModeWorldWriteable() {
        final AtomicInteger count = new AtomicInteger(1);
        setActivityContext(new InjectedContext(
                getInstrumentation(), null, Context.MODE_WORLD_WRITEABLE, count));
        startActivity();

        StoreBox.create(getActivity(), PreferencesModeWordWriteableInterface.class);

        assertEquals(0, count.get());
    }
    
    private void startActivity() {
        startActivity(
                new Intent(
                        getInstrumentation().getTargetContext(),
                        TestActivity.class),
                null,
                null);
    }
    
    private static class InjectedContext extends ContextWrapper {
        
        private final String expectedName;
        private final Integer expectedMode;
        private final AtomicInteger count;
        
        public InjectedContext(
                Instrumentation instr,
                String expectedName,
                Integer expectedMode,
                AtomicInteger count) {
            
            super(instr.getTargetContext());
            
            this.expectedName = expectedName;
            this.expectedMode = expectedMode;
            this.count = count;
        }

        @Override
        public SharedPreferences getSharedPreferences(String name, int mode) {
            if (expectedName != null) {
                assertEquals(expectedName, name);
            }
            if (expectedMode != null) {
                assertEquals(expectedMode.intValue(), mode);
            }
            count.decrementAndGet();
            
            return super.getSharedPreferences(name, mode);
        }
    }
}
