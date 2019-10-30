package com.example.quotebox.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.quotebox.R;
import com.example.quotebox.models.Users;

import java.util.HashMap;
import java.util.Map;

public class SharedPreferencesConfig {

    private static final String LOGIN_SHARED_PREF = "loginSharedPref";
    public static final String LOGIN_STATUS_PREF = "loginStatusPref";
    public static final String LOGIN_USERNAME_PREF = "loginUsernamePref";


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

    public void setLoggedInUserCreds(String username) {
        Log.d("SHARED_PREF_LOG", "username form setLoggedInUSerCreds: " + username);
        editor.putString(LOGIN_USERNAME_PREF, username);
        editor.commit();
    }

    public HashMap<String, String> getLoggedInUserCreds() {
        HashMap<String, String> userCreds = new HashMap<>();

        Log.d("SHARED_PREF_LOG", "USERNAME : " + sharedPreferences.getString(LOGIN_USERNAME_PREF, ""));
        userCreds.put(Users.USERNAME, sharedPreferences.getString(LOGIN_USERNAME_PREF, ""));
        return userCreds;
    }

}
