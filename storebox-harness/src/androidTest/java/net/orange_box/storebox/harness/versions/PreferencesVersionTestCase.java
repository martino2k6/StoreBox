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
import net.orange_box.storebox.harness.interfaces.versions.VersionHandler;

// TODO test version independence of different shared preferences
public class PreferencesVersionTestCase extends InstrumentationTestCase {
    
    private SharedPreferences prefs;
    private SharedPreferences prefsVersion;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefsVersion = getContext().getSharedPreferences(
                "net.orange_box.storebox.versions", Context.MODE_PRIVATE);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void tearDown() throws Exception {
        prefs.edit().clear().commit();
        prefsVersion.edit().clear().commit();
        
        prefs = null;
        prefsVersion = null;
        
        super.tearDown();
    }

    @SmallTest
    public void testNoChange_Empty() {
        StoreBox.create(getContext(), NoVersionInterface.class);
        
        assertFalse(containsVersion());
        assertFalse(invokedFirst());
        assertFalse(invokedSecond());
    }

    @SuppressLint("CommitPrefEdits")
    @SmallTest
    public void testNoChange_WithItems() {
        prefs.edit().putString("key", "value").commit();
        
        StoreBox.create(getContext(), NoVersionInterface.class);

        assertFalse(containsVersion());
        assertFalse(invokedFirst());
        assertFalse(invokedSecond());
    }

    @SmallTest
    public void testZeroToOne_Empty() {
        StoreBox.create(getContext(), NoVersionInterface.class);
        StoreBox.create(getContext(), FirstVersionInterface.class);
        
        assertEquals(1, getVersion());
        assertFalse(invokedFirst());
        assertFalse(invokedSecond());
    }

    @SuppressLint("CommitPrefEdits")
    @SmallTest
    public void testZeroToOne_WithItems() {
        StoreBox.create(getContext(), NoVersionInterface.class);
        
        prefs.edit().putString("key", "value").commit();
        
        StoreBox.create(getContext(), FirstVersionInterface.class);

        assertEquals(1, getVersion());
        assertTrue(invokedFirst());
        assertFalse(invokedSecond());
    }

    @SmallTest
    public void testOneToTwo_Empty() {
        StoreBox.create(getContext(), FirstVersionInterface.class);

        assertEquals(1, getVersion());
        assertFalse(invokedFirst());
        assertFalse(invokedSecond());

        StoreBox.create(getContext(), SecondVersionInterface.class);

        assertEquals(2, getVersion());
        assertFalse(invokedFirst());
        assertFalse(invokedSecond());
    }

    @SuppressLint("CommitPrefEdits")
    @SmallTest
    public void testOneToTwo_WithItems() {
        StoreBox.create(getContext(), FirstVersionInterface.class);

        assertEquals(1, getVersion());
        assertFalse(invokedFirst());
        assertFalse(invokedSecond());

        prefs.edit().putString("key", "value").commit();

        StoreBox.create(getContext(), SecondVersionInterface.class);

        assertEquals(2, getVersion());
        assertFalse(invokedFirst());
        assertTrue(invokedSecond());
    }

    @SmallTest
    public void testZeroToTwo_Empty() {
        StoreBox.create(getContext(), NoVersionInterface.class);
        StoreBox.create(getContext(), SecondVersionInterface.class);

        assertEquals(2, getVersion());
        assertFalse(invokedFirst());
        assertFalse(invokedSecond());
    }

    @SuppressLint("CommitPrefEdits")
    @SmallTest
    public void testZeroToTwo_WithItems() {
        StoreBox.create(getContext(), NoVersionInterface.class);

        prefs.edit().putString("key", "value").commit();
        
        StoreBox.create(getContext(), SecondVersionInterface.class);

        assertEquals(2, getVersion());
        assertTrue(invokedFirst());
        assertTrue(invokedSecond());
    }
    
    private Context getContext() {
        return getInstrumentation().getTargetContext();
    }
    
    private boolean containsVersion() {
        return prefsVersion
                .contains(getContext().getPackageName() + "_preferences");
    }
    
    private int getVersion() {
        return prefsVersion
                .getInt(getContext().getPackageName() + "_preferences", 0);
    }
    
    private boolean invokedFirst() {
        return prefs.contains(VersionHandler.KEY_FIRST);
    }
    
    private boolean invokedSecond() {
        return prefs.contains(VersionHandler.KEY_SECOND);
    }
}
