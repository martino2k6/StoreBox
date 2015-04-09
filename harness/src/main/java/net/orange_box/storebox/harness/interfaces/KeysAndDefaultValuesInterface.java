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
import net.orange_box.storebox.annotations.method.KeyByString;
import net.orange_box.storebox.harness.R;

public interface KeysAndDefaultValuesInterface {

    @KeyByString("string")
    String getValue();
    
    @KeyByString("string")
    void setValue(String value);

    
    @KeyByResource(R.string.key_string)
    String getValueAlt();
    
    @KeyByResource(R.string.key_string)
    void setValueAlt(String value);

    
    @KeyByResource(R.string.key_string)
    @DefaultValue(R.string.default_string)
    String getValueWithDefault();
    
    @KeyByResource(R.string.key_string)
    String getValueWithDefault(String defValue);

    
    @KeyByResource(R.string.key_string)
    @DefaultValue(R.string.default_string)
    String getValueWithDefaultPrecedence(String defValue);
    
    
    @KeyByResource(R.string.key_string)
    @DefaultValue(R.bool.default_boolean)
    String getValueWithBadDefault();
    
    @KeyByResource(R.string.key_string)
    String getValueWithBadDefault(boolean defValue);
}
