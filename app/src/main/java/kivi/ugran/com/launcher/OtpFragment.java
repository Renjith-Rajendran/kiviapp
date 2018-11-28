package kivi.ugran.com.launcher;

import com.afollestad.materialdialogs.MaterialDialog;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.Constants;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.MaterialDialogUtils;
import kivi.ugran.com.core.SharedPreferenceUtils;
import kivi.ugran.com.core.callbacks.ActivityCallback;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.kds.model.GeneratePassword;
import kivi.ugran.com.kds.usecases.ResendOtpUseCase;
import kivi.ugran.com.kds.usecases.VerifyOTPAndGeneratePasswordUseCase;

public class OtpFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "otp";
    TextView resendTextButton;
    TextView submitTextButton;
    EditText otp;

    public OtpFragment() {
    }

    /**
     * Returns a new instance of this fragment
     */
    public static OtpFragment newInstance(String otp) {
        OtpFragment fragment = new OtpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_NUMBER, otp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_otp, container, false);
        initView(rootView);
        return rootView;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(@NonNull View view) {
        resendTextButton = view.findViewById(R.id.text_resend);
        submitTextButton = view.findViewById(R.id.text_submit);
        otp = view.findViewById(R.id.input_security);

        resendTextButton.setOnClickListener(view1 -> executeResendOTP());
        submitTextButton.setOnClickListener(view12 -> executeSubmitOTP());
    }

    private void executeResendOTP() {
        ResendOtpUseCase.resendOtp(KiviApplication.getAndroidDeviceID(), new GenericCallback<String>() {
            @Override public void success(String response) {
                LogUtils.d("kivi", "ResendOtpUseCase- success");
            }

            @Override public void error(String err) {
                if (isAdded()) {
                    String resendOtp = getString(R.string.resend_otp_error);
                    LogUtils.d("kivi", "ResendOtpUseCase- failed.. Show an error dialog");
                    MaterialDialog.Builder alert =
                            new MaterialDialog.Builder(Objects.requireNonNull(getActivity())).title(resendOtp)
                                    .positiveText("OK")
                                    .onPositive((dialog, which) -> {
                                    });

                    if (MaterialDialogUtils.isSafeToOperate(getActivity())) {
                        alert.show();
                    }
                }
            }
        });
    }

    private void executeSubmitOTP() {
        String otpFromUser = otp.getText().toString();
        String cachedOTP = SharedPreferenceUtils.loadString(Objects.requireNonNull(getContext()),
                Constants.RegistrationSettings.KEY_REGISTRATION_OTP);
        if (cachedOTP != null && cachedOTP.equalsIgnoreCase(otpFromUser)) {
            LogUtils.d("kivi", "OTP is success execute passwordgenertaion");
            GeneratePassword generatePassword = new GeneratePassword(KiviApplication.getAndroidDeviceID(), 1);
            VerifyOTPAndGeneratePasswordUseCase.verifyOTPAndGeneratePassword(generatePassword,
                    new GenericCallback<GeneratePassword>() {
                        @Override public void success(GeneratePassword response) {
                            //Launch Home Activity
                            LogUtils.d("kivi", "verifyOTPAndGeneratePassword- OTP verified "
                                    + response.getRegistrationCompleted());
                            SharedPreferenceUtils.saveBoolean(Objects.requireNonNull(getContext()),
                                    Constants.RegistrationSettings.KEY_REGISTRATION_COMPLETED, true);

                            String userAuthenticated = getString(R.string.user_authenticated);
                            Toast.makeText(Objects.requireNonNull(getContext()), userAuthenticated, Toast.LENGTH_SHORT)
                                    .show();
                            LogUtils.d("Kivi", "User is registered Launching HomeKiviActivity");

                            if (getActivity() instanceof ActivityCallback) {
                                ActivityCallback listener = (ActivityCallback) getActivity();
                                listener.notifyActionSubmitted(ActivityCallback.ACTION_LAUNCH_HOME_ACTIVITY,
                                        userAuthenticated);
                            }
                        }

                        @Override public void error(String err) {
                            LogUtils.d("kivi", "verifyOTPAndGeneratePassword- failed");
                            SharedPreferenceUtils.saveBoolean(Objects.requireNonNull(getContext()),
                                    Constants.RegistrationSettings.KEY_REGISTRATION_COMPLETED, false);
                        }
                    });
        } else {

            if (isAdded()) {
                SharedPreferenceUtils.saveBoolean(Objects.requireNonNull(getContext()),
                        Constants.RegistrationSettings.KEY_REGISTRATION_COMPLETED, false);

                String matchingError = getString(R.string.matching_otp_error);
                MaterialDialog.Builder alert =
                        new MaterialDialog.Builder(Objects.requireNonNull(getActivity())).title(matchingError)
                                .positiveText("OK")
                                .onPositive((dialog, which) -> {
                                });
                if (MaterialDialogUtils.isSafeToOperate(getActivity())) {
                    alert.show();
                }
            }
        }
    }
}

