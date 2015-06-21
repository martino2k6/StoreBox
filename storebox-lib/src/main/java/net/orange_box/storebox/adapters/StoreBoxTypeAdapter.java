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

package net.orange_box.storebox.adapters;

/**
 * Interface defining a type adapter for adapting type {@link F} to {@link T},
 * which can be natively stored in the preferences.
 * <p>
 * This interface should not be implemented directly, but instead one of the
 * following classes should be extended to provide an adapter implementation
 * for storing {@link F}:
 * <ul>
 * <li>{@link net.orange_box.storebox.adapters.base.BaseBooleanTypeAdapter}
 * as a {@link Boolean}</li>
 * <li>{@link net.orange_box.storebox.adapters.base.BaseFloatTypeAdapter}
 * as a {@link Float}</li>
 * <li>{@link net.orange_box.storebox.adapters.base.BaseIntegerTypeAdapter}
 * as an {@link Integer}</li>
 * <li>{@link net.orange_box.storebox.adapters.base.BaseLongTypeAdapter}
 * as a {@link Long}</li>
 * <li>{@link net.orange_box.storebox.adapters.base.BaseStringTypeAdapter}
 * as a {@link String}</li>
 * <li>{@link net.orange_box.storebox.adapters.base.BaseStringSetTypeAdapter}
 * as a {@link java.util.Set} of {@link String}s</li>
 * </ul>
 * 
 * @param <F> type which needs to be adapted
 * @param <T> type which goes into the preferences
 * 
 * @see net.orange_box.storebox.annotations.type.TypeAdapter
 * @see net.orange_box.storebox.annotations.type.TypeAdapters
 */
public interface StoreBoxTypeAdapter<F, T> {
    
    StoreType getStoreType();
    
    T getDefaultValue();

    T adaptForPreferences(F value);

    F adaptFromPreferences(T value);
}
