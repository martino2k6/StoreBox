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

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import net.orange_box.storebox.StoreBox;
import net.orange_box.storebox.harness.interfaces.DefaultValueModeEmptyInterface;
import net.orange_box.storebox.harness.interfaces.DefaultValueModeNullInterface;

public class DefaultValueOptionTestCase extends InstrumentationTestCase {

    @SmallTest
    public void testEmptyPrimitiveType() {
        final DefaultValueModeEmptyInterface uut = StoreBox.create(
                getInstrumentation().getTargetContext(),
                DefaultValueModeEmptyInterface.class);
        
        assertEquals(0, uut.getInt());
        assertEquals(0, uut.getIntPrecedence());
        assertEquals(i(R.integer.default_int), uut.getIntMethodPrecedence());
        assertEquals(1, uut.getIntArgumentPrecedence(1));
    }
    
    @SmallTest
    public void testEmptyObjectType() {
        final DefaultValueModeEmptyInterface uut = StoreBox.create(
                getInstrumentation().getTargetContext(),
                DefaultValueModeEmptyInterface.class);
        
        assertEquals("", uut.getString());
        assertEquals(null, uut.getStringPrecedence());
        assertEquals(s(R.string.default_string), uut.getStringMethodPrecedence());
        assertEquals("default", uut.getStringArgumentPrecedence("default"));
    }

    @SmallTest
    public void testNullPrimitiveType() {
        final DefaultValueModeNullInterface uut = StoreBox.create(
                getInstrumentation().getTargetContext(),
                DefaultValueModeNullInterface.class);

        assertEquals(0, uut.getInt());
        assertEquals(0, uut.getIntPrecedence());
        assertEquals(i(R.integer.default_int), uut.getIntMethodPrecedence());
        assertEquals(1, uut.getIntArgumentPrecedence(1));
    }

    @SmallTest
    public void testNullObjectType() {
        final DefaultValueModeNullInterface uut = StoreBox.create(
                getInstrumentation().getTargetContext(),
                DefaultValueModeNullInterface.class);

        assertEquals(null, uut.getString());
        assertEquals("", uut.getStringPrecedence());
        assertEquals(s(R.string.default_string), uut.getStringMethodPrecedence());
        assertEquals("default", uut.getStringArgumentPrecedence("default"));
    }
    
    private int i(int resid) {
        return getInstrumentation().getTargetContext().getResources()
                .getInteger(resid);
    }
    
    private String s(int resid) {
        return getInstrumentation().getTargetContext().getString(resid);
    }
}
