package com.example.quotebox.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreference;

import com.example.quotebox.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;

    SharedPreferences sharedPreferences;
    SwitchPreference switchPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        switchPreference = findPreference(getActivity().getString(R.string.pref_account_privacy));


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getActivity().getString(R.string.pref_account_privacy))) {
            boolean isPrivate = sharedPreferences.getBoolean(key, false);
            Log.d("SETTING_FRAG", isPrivate ? "true" : "false");
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(switchPreference)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();

        }

        return false;
    }
}
