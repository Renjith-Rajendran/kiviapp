package kivi.ugran.com.kds.usecases;

import androidx.annotation.NonNull;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.kds.api.ApiKiviServices;
import kivi.ugran.com.kds.model.configuration.Configuration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetConfigurationUseCase {
    public static void getConfiguration(GenericCallback<Configuration> callback) {
        ApiKiviServices services = KiviApplication.getApiKiviServices();
        Call<Configuration> configurationCall = services.loadKiViConfiguration();
        configurationCall.enqueue(new Callback<Configuration>() {
            @Override public void onResponse(@NonNull Call<Configuration> call, @NonNull Response<Configuration> response) {
                LogUtils.d("kivi", "response from getConfiguration Rcvd... ");
                if (null != callback) {
                    callback.success(response.body());
                }
            }

            @Override public void onFailure(@NonNull Call<Configuration> call, @NonNull Throwable t) {
                LogUtils.d("kivi", "Error response from getConfiguration Rcvd... ");
                if (null != callback) {
                    callback.error(t.getMessage());
                }
            }
        });
    }
}
