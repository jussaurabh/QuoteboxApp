package com.example.quotebox.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.quotebox.R;
import com.example.quotebox.models.Users;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public void setLoginStatus(boolean status) {
        editor.putBoolean(LOGIN_SHARED_PREF, status);
        editor.commit();
    }

    public boolean getLoginStatus() {
        boolean status;
        status = sharedPreferences.getBoolean(LOGIN_STATUS_PREF, false);
        return status;
    }

    public void setLoggedInUserCreds(String data) {
        Log.d("SHARED_PREF_LOG", "username form setLoggedInUSerCreds: " + data);
        editor.putString(LOGIN_USER_DATA_PREF, data);
        editor.commit();
    }

//    public HashMap<String, String> getLoggedInUserCreds() {
//        HashMap<String, String> userCreds = new HashMap<>();
//
//        Log.d("SHARED_PREF_LOG", "USERNAME : " + sharedPreferences.getString(LOGIN_USERNAME_PREF, ""));
//        userCreds.put(Users.USERNAME, sharedPreferences.getString(LOGIN_USERNAME_PREF, ""));
//        return userCreds;
//    }

    public HashMap<String, HashMap<String, String>> getLoggedInUserCreds() {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, HashMap<String, String>>>(){}.getType();

        return gson.fromJson(
                sharedPreferences.getString(LOGIN_USER_DATA_PREF, ""),
                type
        );

    }

}
