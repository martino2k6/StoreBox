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

import net.orange_box.storebox.adapters.base.BaseLongTypeAdapter;

import java.util.Date;

/**
 * A {@link net.orange_box.storebox.adapters.StoreBoxTypeAdapter} for
 * {@link Date} objects which uses {@link Long#MIN_VALUE} to represent
 * an absent/null preference value internally.
 */
public class DateTypeAdapter extends BaseLongTypeAdapter<Date> {

    @Override
    public Long getDefaultValue() {
        return Long.MIN_VALUE;
    }

    @Override
    public Long adaptForPreferences(@Nullable Date value) {
        if (value == null) {
            return Long.MIN_VALUE;
        }
        
        return value.getTime();
    }

    @Nullable
    @Override
    public Date adaptFromPreferences(Long value) {
        if (value == Long.MIN_VALUE) {
            return null;
        }
        
        return new Date(value);
    }
}
