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
import android.net.Uri;
import android.preference.PreferenceManager;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import net.orange_box.storebox.StoreBox;
import net.orange_box.storebox.harness.interfaces.types.CustomTypesInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CustomTypesTestCase extends InstrumentationTestCase {
    
    private CustomTypesInterface uut;
    private SharedPreferences prefs;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        uut = StoreBox.create(
                getInstrumentation().getTargetContext(),
                CustomTypesInterface.class);

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
    public void testCustomEnumDefaults() {
        assertNull(uut.getCustomEnum());
        assertSame(CustomEnum.ONE, uut.getCustomEnum(CustomEnum.ONE));
    }
    
    @SmallTest
    public void testCustomEnum() {
        uut.setCustomEnum(CustomEnum.TWO);
        assertSame(CustomEnum.TWO, uut.getCustomEnum());
        assertSame(CustomEnum.TWO, uut.getCustomEnum(CustomEnum.ONE));
    }

    @SmallTest
    public void testCustomEnumNull() {
        uut.setCustomEnum(CustomEnum.TWO);
        uut.setCustomEnum(null);
        
        assertNull(uut.getCustomEnum());
    }
    
    @SmallTest
    public void testDateDefaults() {
        assertNull(uut.getDate());
        assertEquals(new Date(0), uut.getDate(new Date(0)));
    }
    
    @SmallTest
    public void testDate() {
        final Date date = new Date();
        
        uut.setDate(date);
        assertEquals(date, uut.getDate());
        assertEquals(date, uut.getDate(new Date(0)));
    }
    
    @SmallTest
    public void testDateNull() {
        uut.setDate(new Date());
        uut.setDate(null);
        
        assertNull(uut.getDate());
    }
    
    @SmallTest
    public void testUriDefaults() {
        assertNull(uut.getUri());
        assertEquals(Uri.EMPTY, uut.getUri(Uri.EMPTY));
    }
    
    @SmallTest
    public void testUri() {
        final Uri value = Uri.parse("http://www.google.com");
        
        uut.setUri(value);
        assertEquals(value, uut.getUri());
        assertEquals(value, uut.getUri(Uri.EMPTY));
    }
    
    @SmallTest
    public void testUriNull() {
        uut.setUri(Uri.parse("http://www.google.com"));
        uut.setUri(null);
        
        assertNull(uut.getUri());
    }

    @SmallTest
    public void testCustomClassDefaults() {
        assertNull(uut.getCustomClass());
        assertEquals(new CustomClass(), uut.getCustomClass(new CustomClass()));
    }

    @SmallTest
    public void testCustomClass() {
        final CustomClass value = new CustomClass("one", "two");

        uut.setCustomClass(value);
        assertEquals(value, uut.getCustomClass());
        assertEquals(value, uut.getCustomClass(new CustomClass()));
    }

    @SmallTest
    public void testCustomClassNull() {
        uut.setCustomClass(new CustomClass("one", "two"));
        uut.setCustomClass(null);

        assertNull(uut.getCustomClass());
    }
    
    @SmallTest
    public void testUriListDefaults() {
        assertNull(uut.getCustomClassList());
        assertEquals(
                new ArrayList<CustomClass>(),
                uut.getCustomClassList(new ArrayList<CustomClass>()));
    }
    
    @SmallTest
    public void testUriList() {
        final List<CustomClass> value = Arrays.asList(
                new CustomClass("a", "b"),
                new CustomClass("b", "c"));
        
        uut.setCustomClassList(value);
        assertEquals(value, uut.getCustomClassList());
    }
    
    @SmallTest
    public void testUriListNull() {
        uut.setCustomClassList(Collections.singletonList(new CustomClass()));
        uut.setCustomClassList(null);
        
        assertNull(uut.getCustomClassList());
    }
}
