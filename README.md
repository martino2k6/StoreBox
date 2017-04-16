# StoreBox #
**Android library for streamlining SharedPreferences.**  
[![Build Status](https://travis-ci.org/martino2k6/StoreBox.svg)](https://travis-ci.org/martino2k6/StoreBox)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/b86aa22c7a8f48be9b3c3bd254710475)](https://www.codacy.com/app/martino2k6/StoreBox)
[![Download](https://api.bintray.com/packages/martino2k6/maven/storebox/images/download.svg) ](https://bintray.com/martino2k6/maven/storebox/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-StoreBox-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1737)

## Contents ##
* [Overview](#overview)
* [Adding to a project](#adding-to-a-project)
* [Interface and creation](#defining-an-interface-and-bringing-it-to-life)
* [Get and set methods](#adding-get-and-set-methods)
* [Defaults for get methods](#specifying-defaults-for-get-methods)
* [Custom types](#storing-and-retrieving-custom-types)
* [Preference types](#opening-different-types-of-preferences)
* [Advanced](#advanced)
 * [Remove and clear methods](#remove-and-clear-methods)
 * [Change listeners](#change-listeners)
 * [Chaining calls](#chaining-calls)
 * [Forwarding calls](#forwarding-calls)
 * [Save modes](#save-modes)
 * [Versioning](#versioning)
 * [Builder & Defaults](#obtaining-a-more-customised-instance-at-run-time)
 * [ProGuard](#proguard)
* [Contributing](#contributing)
* [License](#license)

## Overview ##
StoreBox is an annotation-based library for interacting with Android's [SharedPreferences](http://developer.android.com/reference/android/content/SharedPreferences.html), with the aim take out the the *how* and *where* parts of retrieving/storing values and instead focus on the more important *what* part.

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

## Adding to a project ##
StoreBox can be used in Android projects using minimum SDK version 10 and newer (Android 2.3+).
### JAR ###
[v1.4.0 JAR](https://oss.sonatype.org/service/local/repositories/releases/content/net/orange-box/storebox/storebox-lib/1.4.0/storebox-lib-1.4.0.jar)  
[v1.4.0 JavaDoc JAR](https://oss.sonatype.org/service/local/repositories/releases/content/net/orange-box/storebox/storebox-lib/1.4.0/storebox-lib-1.4.0-javadoc.jar)
### Gradle ###
```
compile 'net.orange-box.storebox:storebox-lib:1.4.0'
```
### Maven ###
```
<dependency>
  <groupId>net.orange-box.storebox</groupId>
  <artifactId>storebox-lib</artifactId>
  <version>1.4.0</version>
</dependency>
```

## Defining an interface and bringing it to life ##
Simply create a new interface class in your IDE or a text editor, give it an access modifier which suits its use, and name it as appropriate.
```Java
public interface MyPreferences {
    
}
```
Now you're ready to use [`StoreBox.create()`](https://github.com/martino2k6/StoreBox/blob/master/storebox-lib/src/main/java/net/orange_box/storebox/StoreBox.java) to obtain an instance.
```Java
MyPreferences instance = StoreBox.create(context, MyPreferences.class);
```

## Adding get and set methods ##
If you would like to add a **getter** just add a method to the interface which returns a value and make sure to annotate it using [`@KeyByString`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/method/KeyByString.java) or [`@KeyByResource`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/method/KeyByResource.java).
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

## Specifying defaults for get methods ##
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

## Storing and retrieving custom types ##
Saving custom types, which are not understood by Android's SharedPreferences, can be supported through the use of type adapters. A type adapter implementation can be provided by extending from one of the following classes:
* [`BaseBooleanTypeAdapter`](storebox-lib/src/main/java/net/orange_box/storebox/adapters/base/BaseBooleanTypeAdapter.java) for storing as a `Boolean`
* [`BaseFloatTypeAdapter`](storebox-lib/src/main/java/net/orange_box/storebox/adapters/base/BaseFloatTypeAdapter.java) for storing as a `Float` and so on...
* [`BaseIntegerTypeAdapter`](storebox-lib/src/main/java/net/orange_box/storebox/adapters/base/BaseIntegerTypeAdapter.java)
* [`BaseLongTypeAdapter`](storebox-lib/src/main/java/net/orange_box/storebox/adapters/base/BaseLongTypeAdapter.java)
* [`BaseStringTypeAdapter`](storebox-lib/src/main/java/net/orange_box/storebox/adapters/base/BaseStringTypeAdapter.java)
* [`BaseStringSetTypeAdapter`](storebox-lib/src/main/java/net/orange_box/storebox/adapters/base/BaseStringSetTypeAdapter.java) (only supported on API11 and newer)

Telling StoreBox which type adapter should be used can be done by adding the [`@TypeAdapter`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/method/TypeAdapter.java) annotation to the get and set methods.
```Java
@KeyByString("key_region")
@TypeAdapter(RegionTypeAdapter.class)
Region getRegion();

@KeyByString("key_region")
@TypeAdapter(RegionTypeAdapter.class)
void setRegion(Region value);
```

Which type adapter needs to be extended depends on the use case. Take a look at the [`DateTypeAdapter`](storebox-lib/src/main/java/net/orange_box/storebox/adapters/extra/DateTypeAdapter.java), [`UriTypeAdapter`](storebox-lib/src/main/java/net/orange_box/storebox/adapters/extra/UriTypeAdapter.java), and [`CustomClassListTypeAdapter`](storebox-harness/src/main/java/net/orange_box/storebox/harness/types/adapters/CustomClassListTypeAdapter.java) for some examples. It is worth noting that in the last example Gson is being used for serialising the type, as opposed to writing a custom implementation. Gson is not used internally by StoreBox, as such if you wish to use Gson for a type adapter you will need to add it to your project as a dependency.

The following types will work out of the box, so type adapters don't need to be provided for them:
* `Date`
* `Double`
* `Enum`
* `Uri`

*Disclaimer: APIs around type adapters may change in the future, as I will keep looking for a less verbose way of achieving the same goal without requiring the use of Gson.*

## Opening different types of preferences ##
In all of the examples so far details about what preferences are opened and how have been omitted.

Without any annotation the default shared preferences will be used, but the [`@DefaultSharedPreferences`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/type/DefaultSharedPreferences.java) annotation can be added to the interface definition for explicitness. Likewise, [`@ActivityPreferences`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/type/ActivityPreferences.java) or [`@FilePreferences`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/type/FilePreferences.java) can be used to respectively open preferences private to an activity or to open preferences using a file name.

The mode with which the preferences should be opened can also be specified, although this option is not supported by all the types.
```Java
@ActivityPreferences(mode = PreferencesMode.MULTI_PROCESS)
public interface WelcomeActivityPreferences {
    
    // method definitions here...
}
```

## Advanced
### Remove and clear methods
In order to remove a value stored in the preferences under a key a method to perform the removal can be annotated with the [`@RemoveMethod`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/method/RemoveMethod.java) annotation. The key can be supplied in two ways;

The key can be provided thorough an argument in the method, using either a `String` or an `int` in the case of the key being specified in an XML resource.
```java
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

Or a value-specific remove method can be defined with the help of the [`@KeyByString`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/method/KeyByString.java) or [`@KeyByResource`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/method/KeyByResource.java) annotations.
```java
public interface RemoveMethodExample {
    
    @KeyByString("key_username")
    @RemoveMethod
    void removeUsername();
    
    @KeyByResource(R.string.key_password)
    @RemoveMethod
    void removePassword();
}

// usage
preferences.removeUsername();
preferences.removePassword();
```

Clearing all values stored in the preferences can be done by annotating a method with the [`@ClearMethod`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/method/ClearMethod.java) annotation.
```java
public interface ClearMethodExample {
    
    @ClearMethod
    void clear();
}

// usage
preferences.clear();
```

### Change listeners ###
Callbacks can be received when a preference value changes through the use of the [`OnPreferenceValueChangedListener`](storebox-lib/src/main/java/net/orange_box/storebox/listeners/OnPreferenceValueChangedListener.java) interface. The listeners need to be parametrised with the type which is used for the value whose changes we would like to listen for. For example, if we would like to listen to changes to the password (from previous examples) then we could define the listener as
```Java
OnPreferenceValueChangedListener<String> listener = new OnPreferenceValueChangedListener<String>() {
    @Override
    public void onChanged(String newValue) {
        // do something with newValue
    }
}
```
To register this listener a method for registering the listener would need to be defined using the [`@RegisterChangeListenerMethod`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/method/RegisterChangeListenerMethod.java) annotation in the interface which gets passed to [`StoreBox.create()`](storebox-lib/src/main/java/net/orange_box/storebox/StoreBox.java). Likewise, for unregistering a listener the method needs to be annotated with [`@UnregisterChangeListenerMethod`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/method/UnregisterChangeListenerMethod.java) instead. The [`@KeyByString`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/method/KeyByString.java) or [`@KeyByResource`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/method/KeyByResource.java) annotation also needs to be used to specify which value we are interested in.
```Java
public interface ChangeListenerExample {
    
    @KeyByString("key_password")
    @RegisterChangeListenerMethod
    void registerPasswordListener(OnPreferenceValueChangedListener<String> listener);
    
    @KeyByString("key_password")
    @UnregisterChangeListenerMethod
    void unregisterPasswordListener(OnPreferenceValueChangedListener<String> listener);
}
```
If you would like to listen for changes to a custom type then the [`@TypeAdapter`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/method/TypeAdapter.java) annotation will need to be added to the method in order to tell StoreBox how the value should be adapted when retrieving it from the preferences.

More than one listener can be registered and unregistered at a time by changing the method definitions in the interface to use variable arguments.
```Java
// annotations omitted
void registerPasswordListeners(OnPreferenceValueChangedListener<String>... listeners);
```

**Caution:** StoreBox does not store strong references to the listeners. A strong reference must be kept to the listener for as long as the listener will be required, otherwise it will be susceptible to garbage collection.

### Chaining calls ###
With Android's [`SharedPreferences.Editor`](http://developer.android.com/reference/android/content/SharedPreferences.Editor.html) class it is possible to keep chaining put methods as each returns back the `SharedPreferences.Editor` instance. StoreBox allows the same functionality. All that needs to be done is to change the set/remove method definitions to either return interface type itself or `SharedPreferences.Editor`.
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

### Forwarding calls ###
If you would like to access methods from the [`SharedPreferences`](http://developer.android.com/reference/android/content/SharedPreferences.html) or [`SharedPreferences.Editor`](http://developer.android.com/reference/android/content/SharedPreferences.Editor.html), you can do that by extending your interface from either of the above (or even both).
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

### Save modes ###
Changes to preferences can normally be saved on Android either through `apply()` or `commit()`. Which method gets used can be customised in StoreBox through the use of the [`@SaveOption`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/option/SaveOption.java) annotation.

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

### Versioning ###
StoreBox supports versioning of preferences through the use of the [`@PreferencesVersion`](storebox-lib/src/main/java/net/orange_box/storebox/annotations/type/PreferencesVersion.java) interface-level annotation, in a similar fashion to Android's [`SQLiteOpenHelper`](http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html). This functionality may be required in the case when the schema of the preferences needs to be changed, such as when a key or type of a preference changes, an enum constant is added/renamed/removed, or a class which is being stored in the preferences changes internally. The `@PreferencesVersion` annotation needs to be added to the interface which will be used with `StoreBox.create()`.

By default, without the `@PreferencesVersion` annotation, the version used is assumed to be `0`. The first time a change is required the `version` for the annotation should be set to `1`, with the value being incremented for any subsequent changes. To provide the logic for handling version upgrades a `handler` class extending from [`PreferencesVersionHandler`](storebox-lib/src/main/java/net/orange_box/storebox/PreferencesVersionHandler.java) needs to be specified.

```Java
@PreferencesVersion(version = 1, handler = MyPreferencesVersionHandler.class)
public interface MyPreferences {
    
    // method definitions here
}

public class MyPreferencesVersionHandler extends PreferencesVersionHandler {
    
    @Override
    protected void onUpgrade(
            SharedPreferences prefs,
            SharedPreferences.Editor editor,
            int oldVersion,
            int newVersion) {
        
        // logic for handling upgrades
    }
}
```

For an initial upgrade from `0` to `1` the `onUpgrade` method will be called with `oldVersion = 0` and `newVersion = 1`. If the version of the preferences would be updated to `2`, then the handler would be called with `oldVersion = 1` and `newVersion = 2`. If however an application update using version `1` was skipped, then `onUpgrade` would be called with `oldVersion = 0` and `newVersion = 2`, which means that handling the intermediate upgrade between versions `1` and `2` would be required. Calling `apply()` or `commit()` on the `editor` is not required after changes are made, as StoreBox will take care of this when saving the new version value into the preferences. Take a look [here](storebox-harness/src/main/java/net/orange_box/storebox/harness/interfaces/versions/VersionHandler.java) for an example of how upgrades could be handled.

The versions are also independent of each other, and apply only to specific preference files. For example, you could have a shared preferences with *version X*, *activity A* preferences with *version Y*, and *activity B* preferences with *version Z*. Or none at all, if versioning is not needed.

### Obtaining a more customised instance at run-time ###
As previously described you can build an instance of your interface using `StoreBox.create()`, however if you'd like to override at run-time any annotations you can use [`StoreBox.Builder`](storebox-lib/src/main/java/net/orange_box/storebox/StoreBox.java) and apply different options.
```Java
MyPreferences preferences =
        new StoreBox.Builder(context, MyPreferences.class)
        .preferencesMode(PreferencesMode.MULTI_PROCESS)
        .build()
```

### Defaults ###
Given the minimum amount of details provided to the interface and method definitions through the use of StoreBox's annotations, the following defaults will get used:
* Preferences type: Default shared preferences
* Preferences mode: Private
* Save mode: Apply
* Default value mode: Empty

### Proguard
If you are using ProGuard add the following lines to your configuration.
```
-dontwarn net.jodah.typetools.TypeResolver
-keep class net.orange_box.storebox.** { *; }
-keepattributes *Annotation*,Exceptions,InnerClasses,Signature

```

## Contributing ##
Any contributions thorough pull requests as well as raised issues (bugs are rated just as highly as features!) will be welcome and highly appreciated.

If you would like to submit a pull request please make sure to do so against the develop branch, and please follow a similar code style to the one used in the existing code base. Running the tests before and after any changes is highly recommended, just as is adding new test cases.

### Contributors ###
* [cr5315](https://github.com/cr5315)

## License ##
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
