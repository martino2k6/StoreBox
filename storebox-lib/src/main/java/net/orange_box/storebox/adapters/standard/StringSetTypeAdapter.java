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

package net.orange_box.storebox.adapters.standard;

import net.orange_box.storebox.adapters.base.BaseStringSetTypeAdapter;

import java.util.Set;

public class StringSetTypeAdapter extends
        BaseStringSetTypeAdapter<Set<String>> {

    @Override
    public Set<String> adaptForPreferences(Set<String> value) {
        return value;
    }

    @Override
    public Set<String> adaptFromPreferences(Set<String> value) {
        return value;
    }
}
