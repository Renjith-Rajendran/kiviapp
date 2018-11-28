package kivi.ugran.com.firebase.usecases;

import androidx.annotation.NonNull;
import android.util.Log;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.firebase.api.ApiFirebaseServices;
import kivi.ugran.com.firebase.model.Notification;
import kivi.ugran.com.firebase.model.RequestNotificaton;
import okhttp3.ResponseBody;
import retrofit2.Callback;

public class RequestNotificationUsecase {
    public static void testPushNotification(String body, String title) {
        Notification sendNotificationModel = new Notification(body, title, "", "");

        RequestNotificaton requestNotificaton = new RequestNotificaton("", sendNotificationModel);
        //token is id , whom you want to send notification
        requestNotificaton.setToken(KiviApplication.getDeviceToken());

        ApiFirebaseServices apiService = KiviApplication.getApiFirebaseServices();
        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendChatNotification(requestNotificaton);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override public void onResponse(@NonNull retrofit2.Call<ResponseBody> call,
                    @NonNull retrofit2.Response<ResponseBody> response) {
                Log.d("kkkk", "done");
            }

            @Override public void onFailure(@NonNull retrofit2.Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

    public static void pushNotification(String body, String title, String icon, String sound, String deviceToken) {
        Notification sendNotificationModel = new Notification(body, title, icon, sound);

        //token is id , whom you want to send notification
        RequestNotificaton requestNotificaton = new RequestNotificaton(deviceToken, sendNotificationModel);

        ApiFirebaseServices apiService = KiviApplication.getApiFirebaseServices();
        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendChatNotification(requestNotificaton);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override public void onResponse(@NonNull retrofit2.Call<ResponseBody> call,
                    @NonNull retrofit2.Response<ResponseBody> response) {
                Log.d("kivi", "pushNotification success");
            }

            @Override public void onFailure(@NonNull retrofit2.Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("kivi", "pushNotification failed");
            }
        });
    }
}
