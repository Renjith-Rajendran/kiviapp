package kivi.ugran.com.kds.usecases;

import androidx.annotation.NonNull;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.kds.api.ApiKiviServices;
import kivi.ugran.com.kds.model.KiviRating;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateKiviUseCase {
    public static void rateKivi(@NonNull KiviRating kiviRatingData, @NonNull GenericCallback<Boolean> callback) {
        ApiKiviServices services = KiviApplication.getApiKiviServices();
        Call<ResponseBody> rateKiviCall = services.rateKivi(kiviRatingData);
        rateKiviCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                LogUtils.d("kivi",
                        "rateKivi response success is " + response.isSuccessful() + " code    is " + response.code());
                callback.success(response.isSuccessful());
            }

            @Override public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.error(t.getMessage());
            }
        });
    }
}
