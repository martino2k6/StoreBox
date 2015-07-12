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

package net.orange_box.storebox.harness.interfaces.versions;

import android.content.SharedPreferences;

import net.orange_box.storebox.PreferencesVersionHandler;

public class VersionHandler extends PreferencesVersionHandler {
    
    @Override
    public void onUpgrade(
            SharedPreferences prefs,
            SharedPreferences.Editor editor,
            int oldVersion,
            int newVersion) {
        
        int version = oldVersion;
        
        while (version <= newVersion) {
            switch (version) {
                case 1:
                    editor.clear();
                    break;
                
                case 2:
                    editor.putInt("key", 1);
                    break;
            }
            
            version++;
        }
    }
}
