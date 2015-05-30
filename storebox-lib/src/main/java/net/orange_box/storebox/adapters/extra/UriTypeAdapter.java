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

import android.net.Uri;
import android.support.annotation.Nullable;

import net.orange_box.storebox.adapters.base.BaseStringTypeAdapter;

public class UriTypeAdapter extends BaseStringTypeAdapter<Uri> {

    @Nullable
    @Override
    public String adaptForPreferences(@Nullable Uri value) {
        if (value == null) {
            return null;
        }
        
        return value.toString();
    }

    @Nullable
    @Override
    public Uri adaptFromPreferences(@Nullable String value) {
        if (value == null) {
            return null;
        }
        
        return Uri.parse(value);
    }
}
