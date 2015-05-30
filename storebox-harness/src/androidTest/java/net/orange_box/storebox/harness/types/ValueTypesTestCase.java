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

package net.orange_box.storebox.harness.types;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import net.orange_box.storebox.StoreBox;
import net.orange_box.storebox.harness.interfaces.types.ValueTypesInterface;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ValueTypesTestCase extends InstrumentationTestCase {
    
    private ValueTypesInterface uut;
    private SharedPreferences prefs;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        uut = StoreBox.create(
                getInstrumentation().getTargetContext(), ValueTypesInterface.class);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(
                getInstrumentation().getTargetContext());
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void tearDown() throws Exception {
        uut = null;
        
        // we are saving to the actual preferences so let's clear them
        prefs.edit().clear().commit();
        prefs = null;
        
        super.tearDown();
    }
    
    @SmallTest
    public void testBoolean() {
        assertFalse(uut.getBoolean());
        
        uut.setBoolean(true);
        assertTrue(uut.getBoolean());
    }

    @SmallTest
    public void testFloat() {
        assertEquals(0F, uut.getFloat());
        
        uut.setFloat(1.0F);
        assertEquals(1.0F, uut.getFloat());
    }

    @SmallTest
    public void testInt() {
        assertEquals(0, uut.getInt());
        
        uut.setInt(1);
        assertEquals(1, uut.getInt());
    }

    @SmallTest
    public void testLong() {
        assertEquals(0L, uut.getLong());
        
        uut.setLong(1L);
        assertEquals(1L, uut.getLong());
    }

    @SmallTest
    public void testString() {
        assertNull(uut.getString());
        
        uut.setString("string");
        assertEquals("string", uut.getString());
    }

    @SmallTest
    public void testStringNull() {
        uut.setString("string");
        uut.setString(null);
        
        assertEquals(null, uut.getString());
    }
    
    @SmallTest
    public void testStringSet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            assertNull(uut.getStringSet());

            final Set<String> value =
                    new HashSet<>(Collections.singletonList("one"));
            uut.setStringSet(value);
            assertEquals(value, uut.getStringSet());
        } else {
            try {
                uut.setStringSet(Collections.<String>emptySet());
                
                fail("Exception not thrown");
            } catch (UnsupportedOperationException e) {
                // pass
            }
            
            try {
                uut.getStringSet();
                
                fail("Exception not thrown");
            } catch (UnsupportedOperationException e) {
                // pass
            }
        }
    }

    @SmallTest
    public void testStringSetNull() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            uut.setStringSet(new HashSet<>(Collections.singletonList("one")));
            uut.setStringSet(null);

            assertEquals(null, uut.getStringSet());
        } else {
            // testStringSet() covers this scenario
        }
    }
}
