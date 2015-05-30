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

import android.support.annotation.Nullable;

import net.orange_box.storebox.adapters.StoreBoxTypeAdapter;
import net.orange_box.storebox.adapters.StoreType;

/**
 * A {@link StoreBoxTypeAdapter} which should be extended in order to provide
 * an adapter implementation for storing {@link T} as a {@link String}.
 *
 * @param <T> type which needs to be adapted
 */
public abstract class BaseStringTypeAdapter<T> implements
        StoreBoxTypeAdapter<T, String> {
    
    @Override
    public final StoreType getStoreType() {
        return StoreType.STRING;
    }

    @Nullable
    @Override
    public String getDefaultValue() {
        return null;
    }

    @Nullable
    @Override
    public abstract String adaptForPreferences(@Nullable T value);

    @Nullable
    @Override
    public abstract T adaptFromPreferences(@Nullable String value);
}
