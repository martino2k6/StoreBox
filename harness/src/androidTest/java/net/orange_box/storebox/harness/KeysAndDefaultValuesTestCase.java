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
import android.app.Instrumentation;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import net.orange_box.storebox.StoreBox;
import net.orange_box.storebox.harness.interfaces.KeysAndDefaultValuesInterface;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KeysAndDefaultValuesTestCase extends InstrumentationTestCase {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    
    private KeysAndDefaultValuesInterface uut;
    
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        prefs = mock(SharedPreferences.class);
        editor = mock(SharedPreferences.Editor.class);
        
        when(prefs.edit()).thenReturn(editor);
        
        uut = StoreBox.create(
                new InjectedContext(getInstrumentation(), prefs),
                KeysAndDefaultValuesInterface.class);
    }

    @Override
    protected void tearDown() throws Exception {
        uut = null;
        
        prefs = null;
        editor = null;
        
        super.tearDown();
    }
    
    @SmallTest
    public void testKeyByString() {
        uut.getValue();
        verify(prefs).getString(eq("string"), any(String.class));
        
        uut.setValue("value");
        verify(editor).putString(eq("string"), any(String.class));
    }
    
    @SmallTest
    public void testKeyByResource() {
        uut.getValueAlt();
        verify(prefs).getString(eq("string"), any(String.class));
        
        uut.setValueAlt("value");
        verify(editor).putString(eq("string"), any(String.class));
    }

    @SmallTest
    public void testGetWithDefault() {
        uut.getValueWithDefault();
        uut.getValueWithDefault("default");
        
        verify(prefs, times(2)).getString(eq("string"), eq("default"));
    }
    
    @SmallTest
    public void testGetWithDefaultPrecedence() {
        uut.getValueWithDefaultPrecedence("test");
        verify(prefs).getString(eq("string"), eq("test"));
    }
    
    @SmallTest
    public void testGetWithBadDefault() {
        try {
            uut.getValueWithBadDefault();
            
            fail("Exception not thrown");
        } catch (UnsupportedOperationException e) {
            // pass
        }
        
        try {
            uut.getValueWithBadDefault(true);

            fail("Exception not thrown");
        } catch (UnsupportedOperationException e) {
            // pass
        }
    }
    
    private static class InjectedContext extends ContextWrapper {
        
        private final SharedPreferences prefs;
        
        public InjectedContext(Instrumentation instr, SharedPreferences prefs) {
            super(instr.getTargetContext());
            
            this.prefs = prefs;
        }

        @Override
        public SharedPreferences getSharedPreferences(String name, int mode) {
            return prefs;
        }
    }
}
