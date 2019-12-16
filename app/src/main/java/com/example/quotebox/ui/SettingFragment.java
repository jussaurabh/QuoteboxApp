package com.example.quotebox.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreference;

import com.example.quotebox.R;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingFragment extends PreferenceFragmentCompat {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    GlobalClass globalClass;

    Activity activity;
    SwitchPreference switchPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        activity = this.getActivity();
        globalClass = (GlobalClass) activity.getApplicationContext();

        switchPreference = findPreference("pref_account_privacy");

        if (globalClass.getAllUsersData().get(FirebaseAuth.getInstance().getCurrentUser().getUid()).isPrivateProfile()) {
            switchPreference.setChecked(true);
        } else {
            switchPreference.setChecked(false);
        }

        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (switchPreference.isChecked()) {
                    Toast.makeText(activity, "privat UNchecked", Toast.LENGTH_LONG).show();
                    switchPreference.setChecked(false);

                    firestore.collection(CollectionNames.USERS).document(firebaseUser.getUid())
                            .update(Users.IS_PRIVATE_PROFILE, false);
                }
                else {
                    Toast.makeText(activity, "privat checked", Toast.LENGTH_LONG).show();
                    switchPreference.setChecked(true);

                    firestore.collection(CollectionNames.USERS).document(firebaseUser.getUid())
                            .update(Users.IS_PRIVATE_PROFILE, true);
                }

                return false;
            }
        });

    }

}
