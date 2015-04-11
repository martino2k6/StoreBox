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
import net.orange_box.storebox.harness.interfaces.SaveModeApplyInterface;
import net.orange_box.storebox.harness.interfaces.SaveModeCommitInterface;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SaveModeTestCase extends ActivityUnitTestCase<TestActivity> {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    
    public SaveModeTestCase() {
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
    }

    @Override
    protected void tearDown() throws Exception {
        prefs = null;
        editor = null;
        
        super.tearDown();
    }

    @SmallTest
    public void testApply() {
        final SaveModeApplyInterface uut = StoreBox.create(
                getActivity(),
                SaveModeApplyInterface.class);
        
        uut.setValue("value");
        
        verify(editor).apply();
        verify(editor, never()).commit();
    }

    @SmallTest
    public void testApplyUsingMethodPrecedence() {
        final SaveModeApplyInterface uut = StoreBox.create(
                getActivity(),
                SaveModeApplyInterface.class);
        
        uut.setValueWithMethodAnnotation("value");
        
        verify(editor).commit();
        verify(editor, never()).apply();
    }
    
    @SmallTest
    public void testApplyWithoutSaving() {
        final SaveModeApplyInterface uut = StoreBox.create(
                getActivity(),
                SaveModeApplyInterface.class);
        
        uut.setValueWithoutApplying("value");

        verify(editor, never()).apply();
        verify(editor, never()).commit();
    }
    
    @SmallTest
    public void testCommit() {
        final SaveModeCommitInterface uut = StoreBox.create(
                getActivity(),
                SaveModeCommitInterface.class);

        uut.setValue("value");

        verify(editor).commit();
        verify(editor, never()).apply();
    }
    
    @SmallTest
    public void testCommitUsingMethodPrecedence() {
        final SaveModeCommitInterface uut = StoreBox.create(
                getActivity(),
                SaveModeCommitInterface.class);

        uut.setValueWithMethodAnnotation("value");

        verify(editor).apply();
        verify(editor, never()).commit();
    }

    @SmallTest
    public void testCommitWithoutSaving() {
        final SaveModeCommitInterface uut = StoreBox.create(
                getActivity(),
                SaveModeCommitInterface.class);

        uut.setValueWithoutApplying("value");

        verify(editor, never()).commit();
        verify(editor, never()).apply();
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
