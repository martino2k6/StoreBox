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

import android.app.Activity;
import android.content.Context;

import net.orange_box.storebox.enums.PreferencesType;

final class PreferencesVersionDetails {
    
    private final String key;
    private final int version;
    private final PreferencesVersionHandler handler;

    PreferencesVersionDetails(
            Context context,
            PreferencesType type,
            String name,
            int version,
            PreferencesVersionHandler handler) {
        
        switch (type) {
            case DEFAULT_SHARED:
                key = context.getPackageName() + "_preferences";
                break;
            
            case ACTIVITY:
                key = ((Activity) context).getLocalClassName();
                break;
            
            case FILE:
                key = name;
                break;
            
            default:
                throw new RuntimeException("Case not handled: " + type);
        }
        
        this.version = version;
        this.handler = handler;
    }
    
    String getKey() {
        return key;
    }
    
    int getVersion() {
        return version;
    }
    
    PreferencesVersionHandler getHandler() {
        return handler;
    }
}
