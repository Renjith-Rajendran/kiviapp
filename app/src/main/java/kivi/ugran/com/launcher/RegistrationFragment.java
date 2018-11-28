package kivi.ugran.com.launcher;

import com.afollestad.materialdialogs.MaterialDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
//import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.Constants;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.MaterialDialogUtils;
import kivi.ugran.com.core.SharedPreferenceUtils;
import kivi.ugran.com.core.callbacks.ActivityCallback;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.kds.model.UpdateKiviData;
import kivi.ugran.com.kds.model.registration.Coordinates;
import kivi.ugran.com.kds.model.registration.Otp;
import kivi.ugran.com.kds.model.registration.Registration;
import kivi.ugran.com.kds.usecases.GetOTPUseCase;
import kivi.ugran.com.kds.usecases.UpdateKiviUseCase;
import kivi.ugran.com.kds.usecases.UpdateKiviWorker;

//import butterknife.BindView;
//import butterknife.ButterKnife;

public class RegistrationFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    EditText nameText;
    //
    //@BindView(R.id.input_name) EditText nameText;
    //@BindView(R.id.input_email) EditText emailText;
    //@BindView(R.id.input_dob) EditText dobText;
    //@BindView(R.id.input_terms) CheckBox termsCheckBox;
    //@BindView(R.id.input_security) EditText securityText;
    //@BindView(R.id.textsend_register) TextView registerTextButton;
    //@BindView(R.id.textresend_register) TextView resendTextButton;
    EditText emailText;
    EditText dobText;
    CheckBox termsCheckBox;
    TextView registerTextButton;

    //final int animateInDurationInMillis = 2000;
    //final int animateOutDurationInMillis = 1500;
    //private Drawable hintDrawable;
    //private AnimatorSet hintDrawableInAnimatorSet = new AnimatorSet();
    //private AnimatorSet hintDrawableOutAnimatorSet = new AnimatorSet();

    //TextView resendTextButton;
    public RegistrationFragment() {
    }

    public static RegistrationFragment newInstance() {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, 0);//not used
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //ButterKnife.bind(this,rootView);
        return inflater.inflate(R.layout.fragment_registration_page, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private boolean validate() {

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String dob = dobText.getText().toString();
        boolean termsChecked = termsCheckBox.isChecked();

        boolean valid = true;

        //name
        if (name.isEmpty() || name.length() < 3) {
            String nameError = getString(R.string.user_name_invalid);
            nameText.setError(nameError);
            valid = false;
        } else {
            nameText.setError(null);
        }

        //email
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            String emailError = getString(R.string.user_email_invalid);
            emailText.setError(emailError);
            valid = false;
        } else {
            emailText.setError(null);
        }

        //dob
        if (dob.isEmpty() || dob.length() < 6) {
            String dobError = getString(R.string.user_dob_invalid);
            dobText.setError(dobError);
            valid = false;
        } else {
            dobText.setError(null);
        }

        //terms
        if (!termsChecked) {
            String ok = getString(R.string.ok);
            String termsNotAcceptedError = getString(R.string.terms_not_accepted_error);
            MaterialDialog.Builder alert =
                    new MaterialDialog.Builder(Objects.requireNonNull(getActivity())).title(termsNotAcceptedError)
                            .positiveText(ok)
                            .onPositive((dialog, which) -> {
                            });

            if (MaterialDialogUtils.isSafeToOperate(getActivity())) {
                alert.show();
            }
            valid = false;
        }
        return valid;
    }

    private void initView(@NonNull View view) {
        registerTextButton = view.findViewById(R.id.textsend_register);
        nameText = view.findViewById(R.id.input_name);
        emailText = view.findViewById(R.id.input_email);
        dobText = view.findViewById(R.id.input_dob);
        termsCheckBox = view.findViewById(R.id.checkbox_terms);
        registerTextButton.setOnClickListener(v -> {
            if (validate()) {
                if (MaterialDialogUtils.isSafeToOperate(getActivity())) {
                    String name = nameText.getText().toString();
                    String email = emailText.getText().toString();
                    String dob = dobText.getText().toString();
                    boolean termsChecked = termsCheckBox.isChecked();

                    registerTextButton.setEnabled(false);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString(Constants.RegistrationSettings.KEY_INSTANCE_ID, KiviApplication.getInstanceID());
                    editor.putBoolean(Constants.RegistrationSettings.KEY_KIVI_ACCEPTED_TERMS, termsChecked);
                    editor.putString(Constants.RegistrationSettings.KEY_KIVI_DOB, dob);
                    editor.putString(Constants.RegistrationSettings.KEY_KIVI_EMAIL, email);
                    editor.putString(Constants.RegistrationSettings.KEY_KIVI_NAME, name);
                    editor.apply();

                    //all values are saved to shared preference now load
                    Registration registration = RegistrationActivity.loadRegistrationData();
                    if(registration!=null) {
                        executeOTP(registration);
                    }
                }
            }
        });
    }

    public void executeOTP(@NonNull Registration registration) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        String creatingAccount = getString(R.string.creating_account);
        progressDialog.setMessage(creatingAccount);
        progressDialog.show();

        final String[] otp = { "" };
        GetOTPUseCase.getOTP(registration, new GenericCallback<Otp>() {
            Boolean existingKivi = false;

            @Override public void success(Otp response) {
                boolean success = response != null;
                existingKivi = response != null ? response.getExistingKivi() : false;
                if (existingKivi) {
                    otp[0] = SharedPreferenceUtils.loadString(Objects.requireNonNull(getContext()),
                            Constants.RegistrationSettings.KEY_REGISTRATION_OTP);
                } else {
                    otp[0] = (response != null) ? response.getOtp() : "null";
                    SharedPreferenceUtils.saveString(Objects.requireNonNull(getContext()),
                            Constants.RegistrationSettings.KEY_REGISTRATION_OTP, "null");
                }
                String serverError = getString(R.string.user_not_authenticated_server_error);
                progressDialog.setMessage(serverError);
                if (success) {
                    String otpfromsp = SharedPreferenceUtils.loadString(getContext(),
                            Constants.RegistrationSettings.KEY_REGISTRATION_OTP);
                    LogUtils.d("Kivi",
                            "Registration Done..otpfromsp is " + otpfromsp + " existingKivi is " + existingKivi);
                    LogUtils.d("Kivi", "Registration Done..otp[0] is " + otp[0] + " existingKivi is " + existingKivi);
                }
                dismissProgress(success);
            }

            private void dismissProgress(boolean success) {
                //dismiss progress
                if (isAdded()) {
                    if (!success) {
                        new android.os.Handler().postDelayed(() -> {
                            progressDialog.dismiss();
                            registerTextButton.setEnabled(true);
                        }, 1500);
                    } else {
                        progressDialog.dismiss();
                        //update KiviData instant
                        updateKiviData();

                        //schedule a periodic one with 15 minutes interval
                        updateKiviDataPeriodically();
                        if (existingKivi) {
                            //ToDo  launch HomeKiviActivity
                            LogUtils.d("Kivi", "Launching HomeKiviActivity");
                            String existingUser = getString(R.string.existing_user);
                            launchKiviHomeScreen();

                            Toast.makeText(getActivity(), existingUser, Toast.LENGTH_SHORT).show();
                        } else {
                            if (getActivity() instanceof ActivityCallback) {
                                ActivityCallback listener = (ActivityCallback) getActivity();
                                listener.notifyActionSubmitted(ActivityCallback.ACTION_HANDLE_OTP, otp[0]);
                            }
                        }
                    }
                }
            }

            @Override public void error(String err) {
                dismissProgress(false);
                if (existingKivi) {
                    progressDialog.setMessage(getString(R.string.user_already_authenticated));
                } else {
                    progressDialog.setMessage(getString(R.string.user_not_authenticated_network_error));
                }
                LogUtils.d("Kivi", "Registration Failed err is " + err);
            }
        });
    }

    private void launchKiviHomeScreen() {
        Intent intent = new Intent(getActivity(), KiviHomeActivity.class);
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void updateKiviData() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        double lat = preferences.getFloat(Constants.KiviPreferences.KEY_LOCATION_LAT, 0.0f);
        double lng = preferences.getFloat(Constants.KiviPreferences.KEY_LOCATION_LNG, 0.0f);

        //save kiviAvailableStatus to shared preference
        String kiviAvailableStatus = "Available";
        SharedPreferenceUtils.saveBoolean(Objects.requireNonNull(getActivity()),
                Constants.KiviPreferences.KEY_KIVI_ONLINE, true);
        UpdateKiviData updateKiviData =
                new UpdateKiviData(new Coordinates(lat, lng), KiviApplication.getAndroidDeviceID(), KiviApplication.getDeviceToken(),
                        kiviAvailableStatus);

        //execute api
        UpdateKiviUseCase.updateKivi(updateKiviData, new GenericCallback<Boolean>() {
            @Override public void success(Boolean response) {
                LogUtils.d("Kivi", "UpdateKiviWorker success");
            }

            @Override public void error(String err) {
                LogUtils.d("Kivi", "UpdateKiviWorker error");
            }
        });
    }

    private void updateKiviDataPeriodically() {
        LogUtils.d("Kivi", "Exec UpdateKiviWorker periodic is " + true);
        // Create a Constraints object that defines when the task should run
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                // Many other constraints are available, see the
                // Constraints.Builder reference
                .build();

        PeriodicWorkRequest.Builder periodicKiviWorkerBuilder =
                new PeriodicWorkRequest.Builder(UpdateKiviWorker.class, 16, TimeUnit.MINUTES);
        periodicKiviWorkerBuilder.setConstraints(constraints);

        // Create the actual work object:
        PeriodicWorkRequest periodicKiviWork = periodicKiviWorkerBuilder.build();
        // Then enqueue the recurring task:
        WorkManager.getInstance()
                .enqueue(periodicKiviWork)
                .addListener(() -> LogUtils.d("Kivi", "Exec UpdateKiviWorker periodic.addListener.runnable"),
                        runnable -> LogUtils.d("Kivi", "Exec UpdateKiviWorker periodic.addListener.executor"));

        //https://stackoverflow.com/questions/50415756/android-workmanager-api-for-running-daily-task-in-background
        //WorkManager.getInstance().getStatusById(mathWork.getId())
        //        .observe(lifecycleOwner, status -> {
        //            if (status != null && status.getState().isFinished()) {
        //                int myResult = status.getOutputData().getInt(KEY_RESULT,
        //                        myDefaultValue));
        //                // ... do something with the result ...
        //            }
        //        });
    }

    //private void updateKiviDataOneTime() {
    //    LogUtils.d("Kivi", "Exec UpdateKiviWorker periodic is " + false);
    //    // Create a Constraints object that defines when the task should run
    //    Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
    //            // Many other constraints are available, see the
    //            // Constraints.Builder reference
    //            .build();
    //
    //    // ...then create a OneTimeWorkRequest that uses those constraints
    //    OneTimeWorkRequest updateKiviWork =
    //            new OneTimeWorkRequest.Builder(UpdateKiviWorker.class).setConstraints(constraints).build();
    //    //enqueue
    //    LogUtils.d("Kivi", "Exec WorkManager.getInstance().enqueue(compressionWork)");
    //    WorkManager.getInstance().enqueue(updateKiviWork);
    //}

    //private void animateHintDrawble(View view) {
    //    TextView viewText = view.findViewById(R.id.texthint_code);
    //    Drawable[] drawables = viewText.getCompoundDrawables();
    //    hintDrawable = drawables[0];
    //
    //    @SuppressLint("ObjectAnimatorBinding") ObjectAnimator alphaIn =
    //            ObjectAnimator.ofFloat(hintDrawable, "alpha", 0f, 1f);
    //    alphaIn.setRepeatCount(ValueAnimator.INFINITE);
    //    alphaIn.setRepeatCount(ValueAnimator.RESTART);
    //
    //    @SuppressLint("ObjectAnimatorBinding") ObjectAnimator alphaOut =
    //            ObjectAnimator.ofFloat(hintDrawable, "alpha", 1f, 0f);
    //    alphaOut.setRepeatCount(ValueAnimator.INFINITE);
    //    alphaOut.setRepeatCount(ValueAnimator.RESTART);
    //
    //    /*
    //
    //     */
    //
    //    hintDrawableInAnimatorSet.setInterpolator(new AccelerateInterpolator());
    //    hintDrawableInAnimatorSet.setDuration(animateInDurationInMillis);
    //    hintDrawableInAnimatorSet.play(alphaOut).with(alphaIn);
    //}
}