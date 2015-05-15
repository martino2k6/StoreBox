# StoreBox #
**Android library for streamlining SharedPreferences.**

When getting a value from any preferences, whether private Activity or default shared preferences, you would normally have to get a reference to a ```SharedPreferences``` instance, for example using
```Java
SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
```
And after that the values can be retrieved/saved.
```Java
String username = preferences.getString("username", "");
preferences.edit().putString("username", "androiduser").apply();
```
This can get tedious if the values need to be saved and retrieved from multiple places as the caller is always required to supply the key of the preference, know under what type it is saved, and know the appropriate default to be used for the invocation. With a bigger set of keys used throughout an application this approach can become increasingly error-prone. Putting commonly accessed preferences behind a wrapper is a solution which is commonly used, however there is an alternative.

What if an interface could be defined like
```Java
public interface MyPreferences {
    
    @KeyByString("username")
    String getUsername();
    
    @KeyByString("username")
    void setUsername(String value);
}
```
For everyone to use in order to save and get values? The caller doesn't need to worry about the keys, neither needs to think about what value type is saved under the key, and the process of retrieving/saving is hidden behind a method name with improved semantics.

With StoreBox that becomes possible. Given the above interface definition you can easily create an instance using
```Java
MyPreferences preferences = StoreBox.create(context, MyPreferences.class);
```
and you will be able to retrieve and save the values just by calling the defined methods
```Java
String username = preferences.getUsername();
preferences.setUsername("androiduser");
```

Read on to find out more details about how StoreBox can be used and how you can add it to your Android project.

##Defining an interface and bringing it to life##
Simply create a new interface class in your IDE or a text editor, give it an access modifier which suits its use, and name it as appropriate.
```Java
public interface MyPreferences {
    
}
```
Now you're ready to use ```StoreBox.create()``` to obtain an instance.
```Java
MyPreferences instance = StoreBox.create(context, MyPreferences.class);
```

##Adding get and set methods##
If you would like to add a **getter** just add a method to the interface which returns a value and make sure to annotate it using `@KeyByString` or `@KeyByResource`. Please note that for now only values which the `SharedPreferences` accept can be used (except `Set<String>` for the moment).
```Java
@KeyByString("key_nickname")
String getNickname();

@KeyByResource(R.string.key_notifications)
boolean shouldShowNotifications();
```
If you try to define a method that returns something like a `Date` object, which cannot be saved by Android, an exception will be thrown.
```Java
@KeyByString("key_date_of_birth")
Date getDateOfBirth(); // invalid
```

Adding a **setter** is just as easy. The same annotations will need to be used as for getter methods, but now our method will return nothing and will have to provide a parameter for supplying the value that should be saved.
```Java
@KeyByString("key_nickname")
void setNickname(String value)

@KeyByResource(R.string.key_notifications)
void setNotifications(boolean value)
```
Just like with get methods only the common types are supported, and as such the example below will not work.
```Java
@KeyByString("key_date_of_birth")
void setDateOfBirth(Date value); // invalid
```

##Specifying defaults for get methods##
This can be achieved in two ways, through an argument or by using an annotation.

For the first option the following will work.
```Java
@KeyByString("key_phone_number")
String getPhoneNumber(String defValue);
```
And using an annotation referencing a default found in the XML resources.
```Java
@KeyByString("key_phone_number")
@DefaultValue(R.string.default_phone_number)
String getPhoneNumber();
```

For some types, such as `long`, which cannot be added to the resources an integer resource may be used instead.
```Java
@KeyByString("key_refresh_interval")
@DefaultValue(R.integer.default_refresh_interval)
long getRefreshInterval();
```

##Opening different types of preferences##
In all of the examples so far details about what preferences are opened and how have been omitted.

Without any annotation the default shared preferences will be used, but the `@DefaultSharedPreferences` annotation can be added to the interface definition for explicitness. Likewise, `@ActivityPreferences` or `@FilePreferences` can be used to respectively open preferences private to an activity or to open preferences using a file name.

The mode with which the preferences should be opened can also be specified, although this option is not supported by all the types.
```Java
@ActivityPreferences(mode = PreferencesMode.MULTI_PROCESS)
public interface WelcomeActivityPreferences {
    
    // method definitions here...
}
```

##Adding to a project##
StoreBox can be used in Android projects using minimum SDK version 10 and newer (Android 2.3+).
###JAR###
[v1.0.1 JAR](https://oss.sonatype.org/service/local/repositories/releases/content/net/orange-box/storebox/storebox-lib/1.0.1/storebox-lib-1.0.1.jar)  
[v1.0.1 JavaDoc JAR](https://oss.sonatype.org/service/local/repositories/releases/content/net/orange-box/storebox/storebox-lib/1.0.1/storebox-lib-1.0.1-javadoc.jar)
###Gradle###
```
compile 'net.orange-box.storebox:storebox-lib:1.0.1'
```
###Maven###
```
<dependency>
  <groupId>net.orange-box.storebox</groupId>
  <artifactId>storebox-lib</artifactId>
  <version>1.0.1</version>
</dependency>
```

##Advanced##
###Chaining calls###
With Android's `SharedPreferences.Editor` class it is possible to keep chaining put methods as each returns back the `SharedPreferences.Editor` instance. StoreBox allows the same functionality. All that needs to be done is to change the set method definitions to either return interface type itself or `SharedPreferences.Editor`.
```Java
public interface ChainingExample {
    
    @KeyByString("key_username")
    ChainingExample setUsername(String value);
    
    @KeyByString("key_password")
    ChainingExample setPassword(String value);
}
```
And calls can be chained as
```Java
preferences.setUsername("Joe").setPassword("jOe");
```

###Forwarding calls###
If you would like to access methods from the `SharedPreferences` or `SharedPreferences.Editor`, you can do that by extending your interface from either of the above (or even both).
```Java
public interface ForwardingExample extends SharedPreferences, SharedPreferences.Editor {
    
    // method definitions here
}
```
And the methods from either of the extended interfaces will be callable.
```Java
String username = preferences.getString("key_username", "");
preferences.putString("key_username", "Joe").apply();
```

###Save modes###
Changes to preferences can normally be saved on Android either through `apply()` or `commit()`. Which method gets used can be customised in StoreBox through the use of the `@SaveOption` annotation.

Unlike any of the previous annotations `@SaveOption` can be used to annotate both the interface as well as individual set methods, however an annotation at method-level will take precedence over an interface annotation.
```Java
@SaveOption(SaveMode.APPLY)
public interface SaveModeExample {
    // key annotations omitted
    
    void setUsername(String value); // will save using apply()
    
    @SaveOption(SaveMode.COMMIT)
    void setPassword(String value); // will save using commit()
}
```

###Default value modes###
Supplying default values, either by using an argument or an annotation, has been covered earlier but there's further customisation possible.

The `@DefaultValueOption` works in a similar way to the `@SaveOption` annotation in how it can be used, but it specifies what default value should be returned in the absence of a more explicit value.
```Java
@DefaulltValueOption(DefaultValueMode.EMPTY)
public interface DefaultValueModeExample {
    // key annotations omitted
    
    String getUsername(); // will return ""
    
    @DefaultValueOption(DefaultValueMode.NULL)
    String getPassword(); // will return null
    
    int getAppOpenedCount(); // will return 0
    
    @DefaultValueOption(DefaultValueMode.NULL)
    int getSettingsOpenedCount(); // will return 0 as primitive types can't hold a reference
}
```

###Obtaining a more customised instance at run-time###
As previously described you can build an instance of your interface using `StoreBox.build()`, however if you'd like to override at run-time any annotations you can use `StoreBox.Builder` and apply different options.
```Java
MyPreferences preferences =
        new StoreBox.Builder(context, MyPreferences.class)
        .preferencesMode(PreferencesMode.MULTI_PROCESS)
        .build()
```

###Defaults###
Given the minimum amount of details provided to the interface and method definitions through the use of StoreBox's annotations, the following defaults will get used:
* Preferences type: Default shared preferences
* Preferences mode: Private
* Save mode: Apply
* Default value mode: Empty

##License##
```
Copyright 2015 Martin Bella

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
