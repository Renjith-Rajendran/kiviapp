package kivi.ugran.com.kds.usecases;

import org.json.JSONException;
import org.json.JSONObject;
import androidx.annotation.NonNull;
import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.kds.api.ApiKiviServices;
import kivi.ugran.com.kds.model.registration.Otp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*
{
    "otp": "266709",
    "kivi_name": "dufugi",
    "email": "jcjc@dg.com",
    "dob": "12/12/1234",
    "device_id": "testy2",
    "request": "success"
}

is response
 */
public class ResendOtpUseCase {
    public static void resendOtp(@NonNull String deviceId, @NonNull GenericCallback<String> callback) {
        LogUtils.d("kivi", "Enter resendOtp");

        /*
            //only for reference
            ApiKiviServices services = KiviApplication.getApiKiviServices();
            RequestBody body =
                    RequestBody.create(MediaType.parse("text/plain"), deviceId);

            Call<ResponseBody> call = services.resendOtp(body);
            Response<ResponseBody> response = null;
            try {
                response = call.execute();
                String value = response.body().string();
                LogUtils.d("kivi", "Enter resendOtp response is " + value);
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e("kivi", "Enter resendOtp response is " + e.getMessage());
            }
        */
        ApiKiviServices services = KiviApplication.getApiKiviServices();
        JSONObject paramObject = new JSONObject();
        try
        {
            paramObject.put("device_id", deviceId);
        } catch (JSONException ignored) {

        }
        Call<Otp> verifyOtpCAll = services.resendOtp(paramObject.toString());
        verifyOtpCAll.enqueue(new Callback<Otp>() {
            @Override public void onResponse(@NonNull Call<Otp> call, @NonNull Response<Otp> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        callback.success(response.body().toString());
                    }
                } else {
                    LogUtils.d("kivi", "resendOtp.onResponse failed");
                    callback.error("resendOtp.onResponse failed");
                }
            }

            @Override public void onFailure(@NonNull Call<Otp> call, @NonNull Throwable t) {
                callback.error("resendOtp.onFailure failed");
            }
        });

    }
}
