package kivi.ugran.com.launcher.preferences;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.launcher.R;

public class KiviPreferenceActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        KiviApplication.getInstance().getApplicationContext();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new KiviPreferenceFragment())
                .commit();

        Toolbar upButton = findViewById(R.id.preference_toolbar);
        upButton.setOnClickListener(view -> finish());
    }
}