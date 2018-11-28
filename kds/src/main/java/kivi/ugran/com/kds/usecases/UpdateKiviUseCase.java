package kivi.ugran.com.kds.usecases;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.kds.api.ApiKiviServices;
import kivi.ugran.com.kds.model.UpdateKiviData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateKiviUseCase {
    public static void updateKivi(@NonNull UpdateKiviData updateKiviData, @NonNull GenericCallback<Boolean> callback) {
        ArrayList<UpdateKiviData> kiviData = new ArrayList<>();
        kiviData.add(updateKiviData);

        ApiKiviServices services = KiviApplication.getApiKiviServices();
        Call<ResponseBody> otpCall = services.updateKiviData(kiviData);
        otpCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                LogUtils.d("kivi",
                        "updateKivi response success is " + response.isSuccessful() + " code    is " + response.code());
                callback.success(response.isSuccessful());
            }

            @Override public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.error(t.getMessage());
            }
        });
    }
}
