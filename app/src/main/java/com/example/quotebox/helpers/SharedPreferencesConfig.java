package com.example.quotebox.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.quotebox.R;

public class SharedPreferencesConfig {

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Context context;


    public SharedPreferencesConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_shared_pred), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void setLoginStatus(boolean status) {
        editor.putBoolean(context.getResources().getString(R.string.login_status_prefs), status);
        editor.commit();
    }

    public boolean getLoginStatus() {
        boolean status;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status_prefs), false);
        return status;
    }

}
