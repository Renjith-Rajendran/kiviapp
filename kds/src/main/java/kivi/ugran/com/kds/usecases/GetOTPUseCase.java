package kivi.ugran.com.kds.usecases;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.kds.api.ApiKiviServices;
import kivi.ugran.com.kds.model.registration.Otp;
import kivi.ugran.com.kds.model.registration.Registration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetOTPUseCase {
    public static void getOTP(@NonNull Registration registration, @NonNull GenericCallback<Otp> callback) {
        ArrayList<Registration> regList = new ArrayList<>();
        regList.add(registration);

        ApiKiviServices services = KiviApplication.getApiKiviServices();
        Call<Otp> otpCall = services.getOTP(regList);
        otpCall.enqueue(new Callback<Otp>() {
            @Override public void onResponse(@NonNull Call<Otp> call, @NonNull Response<Otp> response) {
                LogUtils.d("kivi", "OTP 1 response success is " + response.isSuccessful() + " code    is " + response.code());
                callback.success(response.body());
            }

            @Override public void onFailure(@NonNull Call<Otp> call, @NonNull Throwable t) {
                callback.error(t.getMessage());
            }
        });
    }
}
