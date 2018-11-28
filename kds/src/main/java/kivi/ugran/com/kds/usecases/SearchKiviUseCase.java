package kivi.ugran.com.kds.usecases;

import androidx.annotation.NonNull;

import java.util.List;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.kds.api.ApiKiviServices;
import kivi.ugran.com.kds.model.search.Place;
import kivi.ugran.com.kds.model.search.Places;
import kivi.ugran.com.kds.model.search.SearchKivi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchKiviUseCase {
    public static void searchKivi(@NonNull SearchKivi searchKiviData, @NonNull GenericCallback<List<Place>> callback) {
        ApiKiviServices services = KiviApplication.getApiKiviServices();
        Call<Places> searchCAll = services.searchKivi(searchKiviData);
        searchCAll.enqueue(new Callback<Places>() {
            @Override public void onResponse(@NonNull Call<Places> call, @NonNull Response<Places> response) {
                LogUtils.d("kivi",
                        "searchKivi response success is " + response.isSuccessful() + " code    is " + response.code());
                Places places = response.body();
                if (places != null) {
                    callback.success(places.getPlaces());
                }
            }

            @Override public void onFailure(@NonNull Call<Places> call, @NonNull Throwable t) {
                LogUtils.d("kivi", "searchKivi onFailure error is " + t.getMessage());
                callback.error(t.getMessage());
            }
        });
    }
}
