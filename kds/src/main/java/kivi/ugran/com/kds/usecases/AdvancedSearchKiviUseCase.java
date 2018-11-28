package kivi.ugran.com.kds.usecases;

import androidx.annotation.NonNull;

import java.util.List;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.kds.api.ApiKiviServices;
import kivi.ugran.com.kds.model.search.AdvancedSearch;
import kivi.ugran.com.kds.model.search.Place;
import kivi.ugran.com.kds.model.search.Places;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvancedSearchKiviUseCase {
    public static void advancedSearchKivi(@NonNull AdvancedSearch advancedSearch,
            @NonNull GenericCallback<List<Place>> callback) {

        ApiKiviServices services = KiviApplication.getApiKiviServices();
        Call<Places> searchCAll = services.advancedSearch(advancedSearch);
        searchCAll.enqueue(new Callback<Places>() {
            @Override public void onResponse(@NonNull Call<Places> call, @NonNull Response<Places> response) {
                LogUtils.d("kivi", "advancedSearch response success is "
                        + response.isSuccessful()
                        + " code    is "
                        + response.code());
                if (response.body() != null) {
                    callback.success(response.body().getPlaces());
                }
            }

            @Override public void onFailure(@NonNull Call<Places> call, @NonNull Throwable t) {
                callback.error(t.getMessage());
            }
        });
    }
}
