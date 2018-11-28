package kivi.ugran.com.firebase.api;

import kivi.ugran.com.firebase.model.RequestNotificaton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiFirebaseServices {
    @Headers({"Authorization: key=AAAA7XPHWIw:APA91bEN7P6rSaqwKzopkMmF4MlC3JOygSK5AzYU3ogzAmJSyIOMbYMvVU46M2PYvdG0dTI8NK4dMGZGhBER7HKurfQ37dZ5d2uHeOwJluLi-a0macuLTNE61GF1zsqVL-4Y-JTAJ86z",
            "Content-Type:application/json"})
    @POST("fcm/send") Call<ResponseBody> sendChatNotification(@Body RequestNotificaton requestNotificaton);
}
