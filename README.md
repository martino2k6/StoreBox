# StoreBox #
**Android library for streamlining SharedPreferences.**  
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-StoreBox-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1737)

StoreBox is an annotation-based library for interacting with Android's SharedPreferences, with the aim take out the the *how* and *where* parts of retrieving/storing values and instead focus on the more important *what* part.

Normally when retrieving or storing values we need to know two pieces of information during each call: the key and the type.
```Java
String username = preferences.getString("key_username", null);
preferences.edit().putString("key_date_of_birth", "30/09/2004").apply(); // should this be a String or a long?
```

With StoreBox the operations above can be changed into pre-defined methods with improved semantics.
```Java
// definition
public interface MyPreferences {
    
    @KeyByString("key_username")
    String getUsername();
    
    @KeyByString("key_date_of_birth")
    void setDateOfBirth(String value);
}

// usage
MyPreferences preferences = StoreBox.create(context, MyPreferences.class);
String username = preferences.getUsername();
preferences.setDateOfBirth("30/09/2004");
```
The caller now doesn't need to worry about the key, neither about what type the values are stored under. The only important part that needs to be taken into consideration is *what* is done with the values, whether that is storing them for later, showing them to the user in the UI, or just changing application behaviour.

Read on to find out more details about how StoreBox can be used and how it can be added to an Android project.

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
If you would like to add a **getter** just add a method to the interface which returns a value and make sure to annotate it using `@KeyByString` or `@KeyByResource`.
```Java
@KeyByString("key_nickname")
String getNickname();

@KeyByResource(R.string.key_notifications)
boolean shouldShowNotifications();
```

Adding a **setter** is just as easy. The same annotations will need to be used as for getter methods, but now our method will return nothing and will have to provide a parameter for supplying the value that should be saved.
```Java
@KeyByString("key_nickname")
void setNickname(String value)

@KeyByResource(R.string.key_notifications)
void setNotifications(boolean value)
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

##Storing and retrieving custom types##
Saving custom types, which are not understood by Android's SharedPreferences, can be supported through the use of type adapters. A type adapter implementation can be provided by extending from one of the following classes:
* `BaseBooleanTypeAdapter` for storing as a `Boolean`
* `BaseFloatTypeAdapter` for storing as a `Float` and so on...
* `BaseIntegerTypeAdapter`
* `BaseLongTypeAdapter`
* `BaseStringTypeAdapter`
* `BaseStringSetTypeAdapter` (only supported on API11 and newer)

Telling StoreBox which type adapter should be used can be done by adding the `@TypeAdapter` annotation to the get and set methods.
```Java
@KeyByString("key_region")
@TypeAdapter(RegionTypeAdapter.class)
Region getRegion();

@KeyByString("key_region")
@TypeAdapter(RegionTypeAdapter.class)
void setRegion(Region value);
```

Which type adapter needs to be extended depends on the use case. Take a look at the [`DateTypeAdapter`](https://github.com/martino2k6/StoreBox/tree/master/storebox-lib/src/main/java/net/orange_box/storebox/adapters/extra/DateTypeAdapter.class), [`UriTypeAdapter`]([`DateTypeAdapter`](https://github.com/martino2k6/StoreBox/tree/master/storebox-lib/src/main/java/net/orange_box/storebox/adapters/extra/UriTypeAdapter.class), and [`CustomClassListTypeAdapter`]([`DateTypeAdapter`](https://github.com/martino2k6/StoreBox/tree/master/storebox-harness/src/main/java/net/orange_box/storebox/harness/types/adapters/CustomClassListTypeAdapter.class) for some examples. It is worth noting that in the last example Gson is being used for serialising the type, as opposed to writing a custom implementation. Gson is not used internally by StoreBox, as such if you wish to use Gson for a type adapter you will need to add it to your project as a dependency.

The following types will work out of the box, so type adapters don't need to be provided for them:
* `Date`
* `Enum`
* `Uri`

*Disclaimer: APIs around type adapters may change in the future, as I will keep looking for a less verbose way of achieving the same goal without requiring the use of Gson.*

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
[v1.1.0 JAR](https://oss.sonatype.org/service/local/repositories/releases/content/net/orange-box/storebox/storebox-lib/1.1.0/storebox-lib-1.1.0.jar)  
[v1.1.0 JavaDoc JAR](https://oss.sonatype.org/service/local/repositories/releases/content/net/orange-box/storebox/storebox-lib/1.1.0/storebox-lib-1.1.0-javadoc.jar)
###Gradle###
```
compile 'net.orange-box.storebox:storebox-lib:1.1.0'
```
###Maven###
```
<dependency>
  <groupId>net.orange-box.storebox</groupId>
  <artifactId>storebox-lib</artifactId>
  <version>1.1.0</version>
</dependency>
```

##Advanced##
###Remove methods###
In order to remove a value stored in the preferences under a key a method to perform the removal can be annotated with the `@RemoveMethod` annotation. The key can be supplied in two ways;

The key can be provided thorough an argument in the method, using either a `String` or an `int` in the case of the key being specified in an XML resource.
```Java
public interface RemoveMethodExample {
    
    @RemoveMethod
    void remove(String key);
    
    @RemoveMethod
    void remove(int keyRes);
}

// usage
preferences.remove("key_username");
preferences.remove(R.string.key_password);
```

Or a value-specific remove method can be defined with the help of the `@KeyByString` or `@KeyByResource` annotations.
```Java
public interface RemoveMethodExample {
    
    @KeyByString("key_username")
    @RemoveMethod
    void removeUsername();
    
    @KeyByResource(R.string.key_password)
    @RemoveMethod
    void removePassword()
}

// usage
preferences.removeUsername();
preferences.removePassword();
```

###Chaining calls###
With Android's `SharedPreferences.Editor` class it is possible to keep chaining put methods as each returns back the `SharedPreferences.Editor` instance. StoreBox allows the same functionality. All that needs to be done is to change the set/remove method definitions to either return interface type itself or `SharedPreferences.Editor`.
```Java
public interface ChainingExample {
    
    @KeyByString("key_username")
    ChainingExample setUsername(String value);
    
    @KeyByString("key_password")
    ChainingExample setPassword(String value);
    
    @KeyByString("key_country")
    ChainingExample removeCountry();
}
```
And calls can be chained as
```Java
preferences.setUsername("Joe").setPassword("jOe").removeCountry();
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

Unlike any of the previous annotations `@SaveOption` can be used to annotate both the interface as well as individual set/remove methods, however an annotation at method-level will take precedence over an interface annotation.
```Java
@SaveOption(SaveMode.APPLY)
public interface SaveModeExample {
    // key annotations omitted
    
    void setUsername(String value); // will save using apply()
    
    @SaveOption(SaveMode.COMMIT)
    void setPassword(String value); // will save using commit()
    
    @SaveOption(SaveMode.COMMIT)
    void removeUsername(); // will persist using commit()
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
