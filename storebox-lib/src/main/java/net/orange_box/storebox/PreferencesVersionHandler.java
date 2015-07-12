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

package net.orange_box.storebox;

import android.content.SharedPreferences;

/**
 * Helper class to manage preference versioning.
 * 
 * @see net.orange_box.storebox.annotations.type.PreferencesVersion
 */
public abstract class PreferencesVersionHandler {
    
    /**
     * Called when the preferences need to be upgraded. The implementation
     * should use this method to perform any changes to the schema of the
     * stored values.
     * <p>
     * {@link SharedPreferences.Editor#apply()} or
     * {@link SharedPreferences.Editor#commit()} do not need to be called
     * after any changes have been performed.
     * 
     * @param prefs The {@link SharedPreferences} instance for reading values.
     * @param editor The {@link SharedPreferences.Editor} instance for writing
     *               values.
     * @param oldVersion The old preferences version.
     * @param newVersion The new preferences version.
     */
    public abstract void onUpgrade(
            SharedPreferences prefs,
            SharedPreferences.Editor editor,
            int oldVersion,
            int newVersion);
    
    /**
     * Called when the preferences need to be downgraded. It is not mandatory
     * to provide an implementation, however by default downgrades will be
     * rejected with an {@link UnsupportedOperationException} being thrown.
     * <p>
     * {@link SharedPreferences.Editor#apply()} or
     * {@link SharedPreferences.Editor#commit()} do not need to be called
     * after any changes have been performed.
     *
     * @param prefs The {@link SharedPreferences} instance for reading values.
     * @param editor The {@link SharedPreferences.Editor} instance for writing
     *               values.
     * @param oldVersion The old preferences version.
     * @param newVersion The new preferences version.
     */
    public void onDowngrade(
            SharedPreferences prefs,
            SharedPreferences.Editor editor,
            int oldVersion,
            int newVersion) {
        
        throw new UnsupportedOperationException(
                "Version downgrade not supported");
    }
}
