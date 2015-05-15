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

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import net.orange_box.storebox.StoreBox;
import net.orange_box.storebox.harness.interfaces.RemoveMethodInterface;

public class RemoveMethodTestCase extends InstrumentationTestCase {
    
    private static final String KEY = "int";
    private static final int VALUE = 1;
    
    private RemoveMethodInterface uut;
    private SharedPreferences prefs;
    
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        uut = StoreBox.create(
                getInstrumentation().getTargetContext(), RemoveMethodInterface.class);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(
                getInstrumentation().getTargetContext());
        
        prefs.edit().putInt(KEY, VALUE).commit();
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
    public void testRemoveUsingStringKey() {
        uut.removeUsingStringKey(KEY);
        assertFalse(prefs.contains(KEY));
    }
    
    @SmallTest
    public void testRemoveUsingIntKey() {
        uut.removeUsingIntKey(R.string.key_int);
        assertFalse(prefs.contains(KEY));
    }
    
    @SmallTest
    public void testRemoveWithStringAnnotation() {
        uut.removeWithStringAnnotation();
        assertFalse(prefs.contains(KEY));
    }
    
    @SmallTest
    public void testRemoveWithResourceAnnotation() {
        uut.removeWithResourceAnnotation();
        assertFalse(prefs.contains(KEY));
    }
}
