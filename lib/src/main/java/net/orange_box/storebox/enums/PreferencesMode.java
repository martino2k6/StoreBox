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

package net.orange_box.storebox.enums;

import android.content.Context;

public enum PreferencesMode {

    /**
     * Default.
     * 
     * @see android.content.Context#MODE_PRIVATE
     */
    MODE_PRIVATE(Context.MODE_PRIVATE),
    
    /**
     * @see android.content.Context#MODE_MULTI_PROCESS
     */
    MODE_MULTI_PROCESS(Context.MODE_MULTI_PROCESS),
    
    /**
     * @see android.content.Context#MODE_WORLD_READABLE
     */
    @Deprecated
    MODE_WORLD_READABLE(Context.MODE_WORLD_READABLE),
    
    /**
     * @see android.content.Context#MODE_WORLD_WRITEABLE
     */
    @Deprecated
    MODE_WORLD_WRITEABLE(Context.MODE_WORLD_WRITEABLE);
    
    private final int value;
    
    private PreferencesMode(int value) {
        this.value = value;
    }
    
    public int value() {
        return value;
    }
}
