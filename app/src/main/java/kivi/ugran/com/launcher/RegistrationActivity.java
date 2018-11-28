package kivi.ugran.com.launcher;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.Constants;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.MaterialDialogUtils;
import kivi.ugran.com.core.SharedPreferenceUtils;
import kivi.ugran.com.core.callbacks.ActivityCallback;
import kivi.ugran.com.kds.model.registration.Registration;

//import butterknife.ButterKnife;

public class RegistrationActivity extends AppCompatActivity implements ActivityCallback {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RegistrationFragment.newInstance())
                    .commitNow();
        }
        int showSkipperTutorialWarning = getIntent().getIntExtra(TutorialActivity.ARG_SHOW_TUTORIAL_SKIPPED_MESSAGE, 0);
        if (showSkipperTutorialWarning == 1) {
            int colorRed = ResourcesCompat.getColor(getResources(), R.color.md_red_900, null);
            int colorWhite = ResourcesCompat.getColor(getResources(), R.color.md_cyan_50, null);

            String snackText = getString(R.string.skipping_tutorial_note);
            MaterialDialogUtils.getSnackBar(findViewById(R.id.container), snackText, colorRed, colorWhite).show();
        }
    }

    private void launchKiviHomeScreen() {
        Intent intent = new Intent(getApplicationContext(), KiviHomeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override public void notifyActionSubmitted(int actionID, String data) {
        switch (actionID) {
            case ACTION_HANDLE_OTP: // OTP Received
                if (data != null) {
                    SharedPreferenceUtils.saveString(this, Constants.RegistrationSettings.KEY_REGISTRATION_OTP, data);
                    LogUtils.d("Kivi", "Rcvd otp.." + SharedPreferenceUtils.loadString(this,
                            Constants.RegistrationSettings.KEY_REGISTRATION_OTP));
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, OtpFragment.newInstance("empty"))
                            .commitNow();
                }
                break;
            case ACTION_LAUNCH_HOME_ACTIVITY:
                launchKiviHomeScreen();
                break;
        }
    }

    @Override public void openDrawerMenu() {
        //Nothing
    }

    @Override public void closeDrawerMenu() {
        //Nothing yet
    }

    @Override public void openSettings() {
        //Nothing yet
    }

    @Nullable public static Registration loadRegistrationData() {
        String name;
        String email;
        String dob;
        String instanceID;
        Boolean termsChecked;
        SharedPreferenceUtils.saveBoolean(KiviApplication.getInstance(),Constants.RegistrationSettings.KEY_KIVI_ACCEPTED_TERMS,true);
        termsChecked = SharedPreferenceUtils.loadBoolean(KiviApplication.getInstance(),
                Constants.RegistrationSettings.KEY_KIVI_ACCEPTED_TERMS);
        instanceID = SharedPreferenceUtils.loadString(KiviApplication.getInstance(),
                Constants.RegistrationSettings.KEY_INSTANCE_ID);
        if (instanceID != null && instanceID.isEmpty()) {
            instanceID = KiviApplication.getInstanceID();
        }
        dob = SharedPreferenceUtils.loadString(KiviApplication.getInstance(),
                Constants.RegistrationSettings.KEY_KIVI_DOB);
        email = SharedPreferenceUtils.loadString(KiviApplication.getInstance(),
                Constants.RegistrationSettings.KEY_KIVI_EMAIL);
        name = SharedPreferenceUtils.loadString(KiviApplication.getInstance(),
                Constants.RegistrationSettings.KEY_KIVI_NAME);
        Registration registration = null;

        if (termsChecked) {
            registration = new Registration();
            registration.initData();
            registration.setDob(dob);
            registration.setEmail(email);
            registration.setName(name);

            //This is dummy , later use updateKivi to put proper values for lat and lon
            registration.getCoordinates().setLat(8.56044);
            registration.getCoordinates().setLng(-76.8807500000001);

            //device details like id, token
            registration.setDeviceName(Build.DEVICE);
            registration.setId(instanceID);
            registration.setDeviceId(KiviApplication.getAndroidDeviceID());
            registration.setDeviceToken(KiviApplication.getDeviceToken());
            registration.setStatus("Offline");

            String encoded = SharedPreferenceUtils.loadString(KiviApplication.getInstance(),
                    Constants.KiviPreferences.KEY_PROFILE_PICTURE);
            if (encoded != null && !encoded.isEmpty()) {
                registration.setProfilePic(encoded);
            }
        }
        return registration;
    }
}
