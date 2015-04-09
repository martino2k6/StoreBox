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
import android.content.Intent;
import android.content.SharedPreferences;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import net.orange_box.storebox.StoreBox;
import net.orange_box.storebox.harness.activities.TestActivity;
import net.orange_box.storebox.harness.interfaces.ForwardingMethodsInterface;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ForwardingMethodsTestCase extends ActivityUnitTestCase<TestActivity> {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private ForwardingMethodsInterface uut;
    
    public ForwardingMethodsTestCase() {
        super(TestActivity.class);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        prefs = mock(SharedPreferences.class);
        editor = mock(SharedPreferences.Editor.class);

        when(prefs.edit()).thenReturn(editor);

        setActivityContext(new InjectedContext(getInstrumentation(), prefs));
        startActivity(
                new Intent(
                        getInstrumentation().getTargetContext(),
                        TestActivity.class),
                null,
                null);
        
        uut = StoreBox.create(getActivity(), ForwardingMethodsInterface.class);
    }

    @Override
    protected void tearDown() throws Exception {
        prefs = null;
        editor = null;
        uut = null;

        super.tearDown();
    }
    
    @SmallTest
    public void testSharedPreferencesMethodsForwarded() {
        uut.getString("key", "default");
        
        verify(prefs).getString(eq("key"), eq("default"));
    }
    
    @SmallTest
    public void testEditorMethodsForwarded() {
        uut.putString("key", "value");
        uut.apply();

        verify(editor).putString(eq("key"), eq("value"));
        verify(editor).apply();
    }

    private static class InjectedContext extends ContextWrapper {

        private final SharedPreferences prefs;

        public InjectedContext(
                Instrumentation instr, SharedPreferences prefs) {

            super(instr.getTargetContext());

            this.prefs = prefs;
        }

        @Override
        public SharedPreferences getSharedPreferences(String name, int mode) {
            return prefs;
        }
    }
}
