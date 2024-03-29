package com.example.quotebox.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.quotebox.models.Users;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;

public class SharedPreferencesConfig {

    private static final String LOGIN_SHARED_PREF = "loginSharedPref";
    public static final String LOGIN_STATUS_PREF = "loginStatusPref";
    public static final String LOGIN_USERNAME_PREF = "loginUsernamePref";
    public static final String LOGIN_USER_DATA_PREF = "loginUserDataPref";


    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Context context;


    public SharedPreferencesConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(LOGIN_SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


//    public void setLoggedInUserCreds(Users user) {
//        String data = new Gson().toJson(user);
//        Log.d("SHARED_PREF_LOG", "username form setLoggedInUSerCreds: " + data);
//        editor.putString(LOGIN_USER_DATA_PREF, data);
//        editor.commit();
//    }
//
//    public Users getLoggedInUserCreds() {
//        Gson gson = new Gson();
//        Type type = new TypeToken<Users>(){}.getType();
//
//        return gson.fromJson(
//                sharedPreferences.getString(LOGIN_USER_DATA_PREF, ""),
//                type
//        );
//
//    }

}
