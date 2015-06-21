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

package net.orange_box.storebox.harness.base;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.InstrumentationTestCase;

import net.orange_box.storebox.StoreBox;

public abstract class PreferencesTestCase<T> extends InstrumentationTestCase {

    protected T uut;
    private SharedPreferences prefs;

    protected abstract Class<T> getInterface();
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        uut = StoreBox.create(
                getInstrumentation().getTargetContext(),
                getInterface());

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
}
