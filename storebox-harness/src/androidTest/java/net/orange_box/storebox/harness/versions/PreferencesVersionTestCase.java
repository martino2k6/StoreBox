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

package net.orange_box.storebox.harness.versions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import net.orange_box.storebox.StoreBox;
import net.orange_box.storebox.harness.interfaces.versions.FirstVersionInterface;
import net.orange_box.storebox.harness.interfaces.versions.NoVersionInterface;
import net.orange_box.storebox.harness.interfaces.versions.SecondVersionInterface;

public class PreferencesVersionTestCase extends InstrumentationTestCase {
    
    private SharedPreferences prefs;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void tearDown() throws Exception {
        prefs.edit().clear().commit();
        prefs = null;
        
        super.tearDown();
    }

    @SmallTest
    public void testNoChange() {
        StoreBox.create(getContext(), NoVersionInterface.class);
        
        assertFalse(containsVersion());
    }

    @SmallTest
    public void testZeroToOne() {
        final NoVersionInterface zero =
                StoreBox.create(getContext(), NoVersionInterface.class);
        
        zero.putPreference("value");

        StoreBox.create(getContext(), FirstVersionInterface.class);
        
        assertEquals(1, getVersion());
        assertFalse(containsPreference());
    }

    @SmallTest
    public void testOneToTwo() {
        final FirstVersionInterface one =
                StoreBox.create(getContext(), FirstVersionInterface.class);

        assertEquals(1, getVersion());
        one.putPreference("value");

        final SecondVersionInterface two =
                StoreBox.create(getContext(), SecondVersionInterface.class);

        assertEquals(2, getVersion());
        assertEquals(1, two.getPreference());
    }

    @SmallTest
    public void testZeroToTwo() {
        StoreBox.create(getContext(), NoVersionInterface.class);
        final SecondVersionInterface two =
                StoreBox.create(getContext(), SecondVersionInterface.class);

        assertEquals(2, getVersion());
        assertEquals(1, two.getPreference());
    }
    
    private Context getContext() {
        return getInstrumentation().getTargetContext();
    }
    
    private boolean containsVersion() {
        return prefs.contains("net.orange_box.storebox.version");
    }
    
    private boolean containsPreference() {
        return prefs.contains("key");
    }
    
    private int getVersion() {
        return prefs.getInt("net.orange_box.storebox.version", 0);
    }
}
