package kivi.ugran.com.launcher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.Constants;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.SharedPreferenceUtils;
import kivi.ugran.com.core.UiUtils.SplashView;

public class InitializationActivity extends AppCompatActivity {
    protected boolean startHomeRemoteReady;
    private SplashView splashView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);

        splashView = findViewById(R.id.init_activity_splash_screen);
        KiviApplication.getInstance().getApplicationContext();
    }

    @Override protected void onPause() {
        super.onPause();
        splashView.removeCallbacks();
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override protected void onResume() {
        super.onResume();
        if (splashView != null) {
            splashView.setAnimationStateChangeCallback((oldState, newState) -> {
                if (newState == SplashView.ANIMATION_COMPLETE_IN) {
                    startHomeRemoteReady = true;
                }
            });

            splashView.animateLogoIn(this::animateOutKiviIntroductionScene);
        }
    }

    protected void animateOutKiviIntroductionScene() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override public void run() {
                if (!isFinishing()) {
                    if (splashView != null) {
                        if (splashView.animationState() != SplashView.ANIMATION_RUNNING) {
                            //kiviSplashView.animateLogoOut(() -> launchTutorialFlow());
                            splashView.animateLogoOut(() -> checkUserAuthenticationStatus());
                        } else {
                            handler.postDelayed(this, 100);
                        }
                    }
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private void launchTutorialFlow() {
        Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void launchKiviHomeScreen() {
        Intent intent = new Intent(getApplicationContext(), KiviHomeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void checkUserAuthenticationStatus() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        //dismiss porgress after 3 seconds
        new android.os.Handler().postDelayed(() -> {
            progressDialog.dismiss();
            //Request Location Permision..
            Boolean userAuthenticated = SharedPreferenceUtils.loadBoolean(getApplicationContext(),
                    Constants.RegistrationSettings.KEY_REGISTRATION_COMPLETED);
            //if (preferences.contains(Constants.RegistrationSettings.KEY_REGISTRATION_OTP)) {
            if (preferences.contains(Constants.RegistrationSettings.KEY_REGISTRATION_COMPLETED) && userAuthenticated) {
                String userAuth = this.getResources().getString(R.string.user_authenticated);
                Toast.makeText(this, userAuth, Toast.LENGTH_SHORT).show();
                LogUtils.d("Kivi", "User is registered Launching HomeKiviActivity");
                launchKiviHomeScreen();
            } else {
                LogUtils.d("Kivi", "User not registered ...");
                launchTutorialFlow();
            }
        }, 1000);
    }
}
