package net.orange_box.storebox.example.proguard;

import net.orange_box.storebox.annotations.method.ClearMethod;
import net.orange_box.storebox.annotations.method.DefaultValue;
import net.orange_box.storebox.annotations.method.KeyByResource;
import net.orange_box.storebox.annotations.method.KeyByString;
import net.orange_box.storebox.annotations.method.RegisterChangeListenerMethod;
import net.orange_box.storebox.annotations.method.UnregisterChangeListenerMethod;
import net.orange_box.storebox.annotations.type.ActivityPreferences;
import net.orange_box.storebox.listeners.OnPreferenceValueChangedListener;

@ActivityPreferences
interface Preferences {
    
    @KeyByString("int")
    @DefaultValue(R.integer.default_int)
    int getInt();
    
    @KeyByResource(R.string.key_int)
    void setInt(int value);
    
    @RegisterChangeListenerMethod
    @KeyByString("int")
    void regIntListener(OnPreferenceValueChangedListener<Integer> listener);
    
    @UnregisterChangeListenerMethod
    @KeyByResource(R.string.key_int)
    void unregIntListener(OnPreferenceValueChangedListener<Integer> listener);
    
    @ClearMethod
    void clear();
}
