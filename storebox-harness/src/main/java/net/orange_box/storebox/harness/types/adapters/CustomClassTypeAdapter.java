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

import net.orange_box.storebox.adapters.base.BaseStringTypeAdapter;
import net.orange_box.storebox.harness.types.CustomClass;

public class CustomClassTypeAdapter extends BaseStringTypeAdapter<CustomClass> {

    @Nullable
    @Override
    public String adaptForPreferences(@Nullable CustomClass value) {
        if (value == null) {
            return null;
        }

        return value.getOne() + "|" + value.getTwo();
    }

    @Nullable
    @Override
    public CustomClass adaptFromPreferences(@Nullable String value) {
        if (value == null) {
            return null;
        }

        if (value.length() == 1) {
            return new CustomClass();
        } else {
            final String split[] = value.split("\\|");

            return new CustomClass(split[0], split[1]);
        }
    }
}
