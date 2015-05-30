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

import net.orange_box.storebox.adapters.base.BaseLongTypeAdapter;

import java.util.Date;

public class DateTypeAdapter extends BaseLongTypeAdapter<Date> {
    
    @Override
    public Long adaptForPreferences(Date value) {
        return value.getTime();
    }

    @Override
    public Date adaptFromPreferences(Long value) {
        return new Date(value);
    }
}
