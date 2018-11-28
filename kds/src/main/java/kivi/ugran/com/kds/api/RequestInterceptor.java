package kivi.ugran.com.kds.api;

import androidx.annotation.NonNull;

import java.io.IOException;

import kivi.ugran.com.core.Constants;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    @Override public Response intercept(@NonNull Chain chain) throws IOException {
        //Request request = chain.request();
        //if (BuildConfig.DEBUG) {
        //    Log.d(getClass().getName(), request.method() + " " + request.url());
        //    Log.d(getClass().getName(), "" + request.header("Cookie"));
        //
        //    RequestBody rb = request.body();
        //    Buffer buffer = new Buffer();
        //    if (rb != null) {
        //        rb.writeTo(buffer);
        //        Log.d("kivi", "Payload- " + buffer.readUtf8());
        //    }

        //}

        //For Later When DataMuse API is required
        Request originalRequest = chain.request();

        HttpUrl originalHttpUrl = originalRequest.url();

        HttpUrl url = originalHttpUrl.newBuilder().addQueryParameter("api_key", Constants.API_KIVI_KEY).build();
        Response response = chain.proceed(chain.request());
        return response;
    }
}
