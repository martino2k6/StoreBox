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

package net.orange_box.storebox.harness.interfaces.types;

import android.net.Uri;

import net.orange_box.storebox.annotations.method.KeyByString;
import net.orange_box.storebox.annotations.method.TypeAdapter;
import net.orange_box.storebox.harness.types.CustomClass;
import net.orange_box.storebox.harness.types.CustomEnum;
import net.orange_box.storebox.harness.types.adapters.CustomClassListTypeAdapter;
import net.orange_box.storebox.harness.types.adapters.CustomClassTypeAdapter;

import java.util.Date;
import java.util.List;

public interface CustomTypesInterface {
    
    @KeyByString("key_custom_enum")
    CustomEnum getCustomEnum();

    @KeyByString("key_custom_enum")
    CustomEnum getCustomEnum(CustomEnum defValue);
    
    @KeyByString("key_custom_enum")
    void setCustomEnum(CustomEnum value);


    @KeyByString("key_date")
    Date getDate();
    
    @KeyByString("key_date")
    Date getDate(Date defValue);

    @KeyByString("key_date")
    void setDate(Date value);


    @KeyByString("key_double")
    double getDouble();

    @KeyByString("key_double")
    double getDouble(double defValue);

    @KeyByString("key_double")
    void setDouble(double value);


    @KeyByString("key_uri")
    Uri getUri();

    @KeyByString("key_uri")
    Uri getUri(Uri defValue);

    @KeyByString("key_uri")
    void setUri(Uri value);
    
    
    @TypeAdapter(CustomClassTypeAdapter.class)
    @KeyByString("key_custom_class")
    CustomClass getCustomClass();

    @TypeAdapter(CustomClassTypeAdapter.class)
    @KeyByString("key_custom_class")
    CustomClass getCustomClass(CustomClass defValue);

    @TypeAdapter(CustomClassTypeAdapter.class)
    @KeyByString("key_custom_class")
    void setCustomClass(CustomClass value);

    
    @TypeAdapter(CustomClassListTypeAdapter.class)
    @KeyByString("key_custom_class_list")
    List<CustomClass> getCustomClassList();

    @TypeAdapter(CustomClassListTypeAdapter.class)
    @KeyByString("key_custom_class_list")
    List<CustomClass> getCustomClassList(List<CustomClass> defValue);

    @TypeAdapter(CustomClassListTypeAdapter.class)
    @KeyByString("key_custom_class_list")
    void setCustomClassList(List<CustomClass> value);
}
