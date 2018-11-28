package kivi.ugran.com.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SharedPreferenceUtils {
    public static void saveString(@NonNull Context context, @NonNull String key,@NonNull String value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Nullable
    public static String loadString(@NonNull Context context, @NonNull String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString(key,null);
        return value;
    }

    public static void saveBoolean(@NonNull Context context, @NonNull String key,@NonNull Boolean value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static Boolean loadBoolean(@NonNull Context context, @NonNull String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean value = preferences.getBoolean(key,false);
        return value;
    }

}
