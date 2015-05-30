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

package net.orange_box.storebox.adapters.extra;

import android.support.annotation.Nullable;

import net.orange_box.storebox.adapters.base.BaseStringTypeAdapter;

public class EnumTypeAdapter extends BaseStringTypeAdapter<Enum> {
    
    private final Class<? extends Enum> type;
    
    public EnumTypeAdapter(Class<? extends Enum> type) {
        this.type = type;
    }

    @Nullable
    @Override
    public String adaptForPreferences(@Nullable Enum value) {
        if (value == null) {
            return null;
        }
        
        return value.name();
    }

    @Nullable
    @Override
    public Enum adaptFromPreferences(@Nullable String value) {
        if (value == null) {
            return null;
        }
        
        return Enum.valueOf(type, value);
    }
}
