package kivi.ugran.com.kds.usecases;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.Constants;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.SharedPreferenceUtils;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.kds.model.UpdateKiviData;
import kivi.ugran.com.kds.model.registration.Coordinates;

//https://codelabs.developers.google.com/codelabs/android-workmanager/#11
//https://stackoverflow.com/questions/51117630/periodic-work-requests-using-workmanager-not-working
//https://developer.android.com/topic/libraries/architecture/workmanager/advanced#params
public class UpdateKiviWorker extends Worker {

    public UpdateKiviWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull @Override public Worker.Result doWork() {

        //Do the work here--in this case, compress the stored images.
        //In this example no parameters are passed; the task is
        //assumed to be "compress the whole library."

        LogUtils.d("Kivi", "Enter UpdateKiviWorker");
        //Indicate success or failure with your return value:
        Worker.Result result = updateKiviData(getApplicationContext());
        LogUtils.d("Kivi", "Exit UpdateKiviWorker");
        return result;

        //(Returning RETRY tells WorkManager to try this task again
        //later; FAILURE says not to try again.)
    }

    private Worker.Result updateKiviData(@NonNull Context context) {
        final Result[] result = { Result.SUCCESS };
        final Boolean[] resultsReceived = { false };
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                LogUtils.w("Kivi", "UpdateKiviWorker Location Permission not Available");
                return result[0];
            } else {
                if (KiviApplication.getFusedLocationClient() != null) {
                    KiviApplication.getFusedLocationClient().getLastLocation().addOnSuccessListener(lastLocation -> {
                        // Got last known location. In some rare situations this can be null.
                        if (lastLocation != null) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED) {
                                double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();

                                SharedPreferences.Editor editor;
                                editor = preferences.edit()
                                        .putFloat(Constants.KiviPreferences.KEY_LOCATION_LAT, (float) lat);
                                editor.putFloat(Constants.KiviPreferences.KEY_LOCATION_LNG, (float) lon);
                                editor.apply();
                                LogUtils.d("Kivi", "UpdateKiviWorker Last Location Available..1");
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            //Any Exception Short Circuit worker
            return result[0];
        }

        //block Location for 1 sec
        int countLoc = 0;
        int limitLoc = 10;
        while (!resultsReceived[0] && countLoc < limitLoc) {
            try {
                Thread.sleep(100);
                countLoc++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        LogUtils.d("Kivi", "UpdateKiviWorker Last Location Available..2");

        //reset
        result[0] = Result.SUCCESS;
        resultsReceived[0] = false;

        //get location and prepare UpdateKiviData
        double lat = preferences.getFloat(Constants.KiviPreferences.KEY_LOCATION_LAT, 0.0f);
        double lng = preferences.getFloat(Constants.KiviPreferences.KEY_LOCATION_LNG, 0.0f);
        String kiviAvailableStatus = "Available";
        if(!SharedPreferenceUtils.loadBoolean(context, Constants.KiviPreferences.KEY_KIVI_ONLINE)){
            kiviAvailableStatus = "Offline";
        }
        UpdateKiviData updateKiviData =
                new UpdateKiviData(new Coordinates(lat, lng), KiviApplication.getAndroidDeviceID(), KiviApplication.getDeviceToken(),kiviAvailableStatus);

        //execute api
        UpdateKiviUseCase.updateKivi(updateKiviData, new GenericCallback<Boolean>() {
            @Override public void success(Boolean response) {
                LogUtils.d("Kivi", "UpdateKiviWorker success");
                resultsReceived[0] = true;
            }

            @Override public void error(String err) {
                LogUtils.d("Kivi", "UpdateKiviWorker error");
                result[0] = Result.FAILURE;
                resultsReceived[0] = true;
            }
        });

        //block for API up to 10 sec maximum
        int count = 0;
        int limit = 20;
        while (!resultsReceived[0] && count < limit) {
            try {
                Thread.sleep(500);
                count++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return result[0];
    }
}
