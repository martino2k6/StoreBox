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

package net.orange_box.storebox.adapters.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.Nullable;

import net.orange_box.storebox.adapters.StoreBoxTypeAdapter;
import net.orange_box.storebox.adapters.StoreType;

import java.util.Set;

/**
 * A {@link StoreBoxTypeAdapter} which should be extended in order to provide
 * an adapter implementation for storing {@link T} as a {@link Set} of
 * {@link String}s.
 * 
 * @param <T> type which needs to be adapted
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class BaseStringSetTypeAdapter<T> implements
        StoreBoxTypeAdapter<T, Set<String>> {
    
    @Override
    public final StoreType getStoreType() {
        return StoreType.STRING_SET;
    }

    @Nullable
    @Override
    public Set<String> getDefaultValue() {
        return null;
    }

    @Nullable
    @Override
    public abstract Set<String> adaptForPreferences(@Nullable T value);

    @Nullable
    @Override
    public abstract T adaptFromPreferences(@Nullable Set<String> value);
}
