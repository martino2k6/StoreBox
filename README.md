# StoreBox #
**Android library for streamlining SharedPreferences.**

When getting a value from any preferences, whether private Activity or default shared preferences, you would normally have to get a reference to a ```SharedPreferences``` instance, for example using
```Java
SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(someContext);
```
Only after that we can start getting the values out, such as
```Java
String myValue = prefs.getString("someKey", "someDefaultValue");
```
This can get reasonably tedious if the preferences need to be accessed from multiple places, and the API always requires us to pass a key and a default value. This can get error prone quickly, and increases with the amount of keys and default values that need to be used. Putting commonly accessed preferences behind a wrapper is a reasonable solution, but still requires some boilerplate code.

What if however we could define an interface like
```Java
public interface MyPreferences {
    
    @KeyByString("someKey")
    String getSomeValue();
}
```
for everyone to use in order to save and get values?

With StoreBox that becomes reality. Given the above interface definition you can easily create an instance of the interface using
```Java
MyPeferences prefs = StoreBox.create(context, MyPeferences.class);
```
and you will be able to retrieve the value just by calling the defined method
```Java
String myValue = prefs.getSomeValue();
```

How about saving values? How about doing X or Y? Read on to find out more details about how StoreBox can be used and how you can add it to your Android project.

##Adding to a project##
StoreBox supports API10 and newer (Android 2.3.3+).
###Gradle###
TODO
###Maven###
TODO

##Defining an interface and bringing it to life##
Simply create a new interface class in your IDE or a text editor, give it an access modifier which suits its use, and name it as appropriate.
```Java
public interface MyPreferences {
    
}
```
Now you're ready to use ```StoreBox.create()``` to create an instance.
```Java
MyPreferences instance = StoreBox.create(context, MyPreferences.class);
```

##Adding get and set methods##
If you would like to add a **getter** just add a method to the interface which returns a value and make sure to annotate it using `@KeyByString` or `@KeyByResource`. Please note that for now only values which the `SharedPreferences` accept can be used (except `Set<String>` for the moment).
```Java
@KeyByString("my_string")
String getMyString();

@KeyByResource(R.string.key_my_boolean)
boolean getMyBoolean();
```
If you try to define a method that returns something like a `Date` object, which cannot be saved by Android, an exception will be thrown.
```Java
@KeyByString("some_key")
Date willNotWork(); // invalid
```

Adding a **setter** is just as easy. The same annotations will need to be used as for getter methods, but now our method will return a `Void` type and will have to provide a parameter for supplying the value that we we would like to save.
```Java
@KeyByString("my_string")
void setMyString(String value)

@KeyByResource(R.string.key_my_boolean)
void setMyBoolean(boolean value)
```
Just like with get methods only the common types are supported, and as such the example below will not work.
```Java
@KeyByString("some_key")
void willNotWork(Intent value); // invalid
```

##Specifying defaults for get methods##
This can be achieved in two ways, through an argument or by using an annotation.

For the first option we can just do the following.
```Java
@KeyByString("some_key")
String getValue(String defValue);
```
But we can also use an annotation referencing a default found in the XML resources.
```Java
@KeyByString("some_key")
@DefaultValue(R.string.some_default)
String getValue();
```

For some types, such as `long`, which cannot be added to the resources an integer resource may be used instead.
```Java
@KeyByString("long_key")
@DefaultValue(R.integer.integer_default)
long getValue();
```

##Opening different types of preferences##
In all of the examples so far we have omitted any details about what preferences are opened and how.

Without any annotation the default shared preferences will be used, but the `@DefaultSharedPreferences` annotation can be added to the interface definition for explicitness. Likewise, `@ActivityPreferences` or `@FilePreferences` can be used to respectively open preferences private to an activity or to open preferences using a file name.

We can also specify the mode with which the preferences should be opened, although this option is not supported by all the types.
```Java
@ActivityPreferences(mode = PreferencesMode.MULTI_PROCESS)
public interface WelcomeActivityPreferences {
    
    // method definitions here...
}
```

##Advanced##
###Chaining calls###
With Android's `SharedPreferences.Editor` class it is possible to keep chaining put methods as each returns back the `SharedPreferences.Editor` instance. StoreBox allows the same functionality. All that needs to be done is to change the set method definitions to either return `SharedPreferences.Editor` or the interface type itself.
```Java
public interface ChainingExample {
    
    @KeyByString("key1")
    ChainingExample putValueOne(String value);
    
    @KeyByString("key2")
    SharedPreferences.Editor putValueTwo(String value);
}
```
And we'll be able to chain calls
```Java
preferences.putValueOne("hello").putValueTwo("world").putStringSet(...).apply();
```

###Forwarding calls###
If for some reason you would like to access methods from the `SharedPreferences` or `SharedPreferences.Editor`, you can do that by extending your interface from either of the above (or even both).
```Java
public interface ForwardingExample extends SharedPreferences, SharedPreferences.Editor {
    
    // method definitions here
}
```
And you will just be able to call
```Java
ForwardingExample prefs = // creation...

String someString = prefs.getString("key", "default");
prefs.putString("key", "value").apply();
```

###More control over how values get saved###
Changes to preferences can normally be saved on Android either through `apply()` or `commit()`. Which method gets used can be customised in StoreBox through the use of the `@SaveOption` annotation.

Unlike any of the previous annotations `@SaveOption` can be used to annotate both the interface as well as individual set methods, however an annotation at method-level will take precedence over an interface annotation.
```Java
@SaveOption(SaveMode.APPLY)
public interface SaveModeExample {
    // key annotations omitted
    
    void saveOne(String value); // will save using apply()
    
    @SaveOption(SaveMode.COMMIT)
    void saveTwo(String value); // will save using commit()
}
```

###More control over default values###
Supplying default values, either by using an argument or an annotation, has been covered earlier but there's further customisation possible.

The `@DefaultValueOption` works in a similar way to the `@SaveOption` annotation in how it can be used, but it specifies what default value should be returned in the absence of a more explicit value.
```Java
@DefaulltValueOption(DefaultValueMode.EMPTY)
public interface DefaultValueModeExample {
    // key annotations omitted
    
    String getStringOne(); // will return ""
    
    @DefaultValueOption(DefaultValueMode.NULL)
    String getStringTwo(); // will return null
    
    int getIntOne(); // will return 0
    
    @DefaultValueOption(DefaultValueMode.NULL)
    int getIntTwo(); // will return 0 as primitive types can't hold a reference
}
```

###Obtaining a more customised instance at run-time###
As previously described you can build an instance of your interface using `StoreBox.build()`, however if you'd like to override at run-time any annotations you can use `StoreBox.Builder` and apply different options.
```Java
MyPreferences prefs =
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
