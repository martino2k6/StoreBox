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

package net.orange_box.storebox.harness.types.adapters;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.orange_box.storebox.adapters.base.BaseStringTypeAdapter;
import net.orange_box.storebox.harness.types.CustomClass;

import java.util.List;

public class CustomClassListTypeAdapter extends
        BaseStringTypeAdapter<List<CustomClass>> {
    
    private static final Gson GSON = new Gson();
    
    @Nullable
    @Override
    public String adaptForPreferences(@Nullable List<CustomClass> value) {
        return GSON.toJson(value);
    }

    @Nullable
    @Override
    public List<CustomClass> adaptFromPreferences(@Nullable String value) {
        return GSON.fromJson(value, new TypeToken<List<CustomClass>>(){}.getType());
    }
}
