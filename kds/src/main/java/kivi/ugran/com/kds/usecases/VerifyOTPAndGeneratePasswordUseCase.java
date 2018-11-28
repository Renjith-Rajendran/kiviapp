package kivi.ugran.com.kds.usecases;

import androidx.annotation.NonNull;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.kds.api.ApiKiviServices;
import kivi.ugran.com.kds.model.GeneratePassword;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
{
    "registrationCompleted": true,
    "request": "success"
}
 */
public class VerifyOTPAndGeneratePasswordUseCase {

    public static void verifyOTPAndGeneratePassword(@NonNull GeneratePassword generatePassword,
            @NonNull GenericCallback<GeneratePassword> callback) {
        LogUtils.d("kivi", "Enter verifyOTPAndGeneratePassword");
        ApiKiviServices services = KiviApplication.getApiKiviServices();
        Call<GeneratePassword> verifyOtpCAll = services.verifyOTPAndGeneratePassword(generatePassword);
        verifyOtpCAll.enqueue(new Callback<GeneratePassword>() {
            @Override public void onResponse(@NonNull Call<GeneratePassword> call, @NonNull Response<GeneratePassword> response) {
                if (response.isSuccessful()) {
                    callback.success(response.body());
                } else {
                    LogUtils.d("kivi", "verifyOtpCAll.onResponse failed");
                }
            }

            @Override public void onFailure(@NonNull Call<GeneratePassword> call, @NonNull Throwable t) {
                callback.error("verifyOtpCAll.onFailure failed");
            }
        });
    }
}
