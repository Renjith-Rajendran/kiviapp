package kivi.ugran.com.launcher.preferences;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import kivi.ugran.com.launcher.R;

public class KiviPreferenceFragment extends PreferenceFragmentCompat {
    @Override public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}