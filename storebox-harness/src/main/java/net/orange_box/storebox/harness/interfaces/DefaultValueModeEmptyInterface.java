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

package net.orange_box.storebox.harness.interfaces;

import net.orange_box.storebox.annotations.method.DefaultValue;
import net.orange_box.storebox.annotations.method.KeyByResource;
import net.orange_box.storebox.annotations.option.DefaultValueOption;
import net.orange_box.storebox.enums.DefaultValueMode;
import net.orange_box.storebox.harness.R;

@DefaultValueOption(DefaultValueMode.EMPTY)
public interface DefaultValueModeEmptyInterface {
    
    @KeyByResource(R.string.key_int)
    int getInt();

    @KeyByResource(R.string.key_int)
    @DefaultValueOption(DefaultValueMode.NULL)
    int getIntPrecedence();

    @KeyByResource(R.string.key_int)
    @DefaultValueOption(DefaultValueMode.NULL)
    @DefaultValue(R.integer.default_int)
    int getIntMethodPrecedence();

    @KeyByResource(R.string.key_int)
    @DefaultValueOption(DefaultValueMode.NULL)
    int getIntArgumentPrecedence(int defValue);


    @KeyByResource(R.string.key_string)
    String getString();

    @KeyByResource(R.string.key_string)
    @DefaultValueOption(DefaultValueMode.NULL)
    String getStringPrecedence();

    @KeyByResource(R.string.key_string)
    @DefaultValueOption(DefaultValueMode.NULL)
    @DefaultValue(R.string.default_string)
    String getStringMethodPrecedence();

    @KeyByResource(R.string.key_string)
    @DefaultValueOption(DefaultValueMode.NULL)
    String getStringArgumentPrecedence(String defValue);
}
