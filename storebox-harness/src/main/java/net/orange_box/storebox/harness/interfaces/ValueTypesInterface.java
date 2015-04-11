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

import net.orange_box.storebox.annotations.method.KeyByResource;
import net.orange_box.storebox.harness.R;

public interface ValueTypesInterface {
    
    @KeyByResource(R.string.key_boolean)
    boolean getBoolean();
    
    @KeyByResource(R.string.key_boolean)
    void setBoolean(boolean value);


    @KeyByResource(R.string.key_float)
    float getFloat();

    @KeyByResource(R.string.key_float)
    void setFloat(float value);


    @KeyByResource(R.string.key_int)
    int getInt();

    @KeyByResource(R.string.key_int)
    void setInt(int value);


    @KeyByResource(R.string.key_long)
    long getLong();

    @KeyByResource(R.string.key_long)
    void setLong(long value);


    @KeyByResource(R.string.key_string)
    String getString();

    @KeyByResource(R.string.key_string)
    void setString(String value);
}
