package kivi.ugran.com.firebase.api;

import androidx.annotation.NonNull;

import java.io.IOException;

import kivi.ugran.com.core.Constants;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptorFirebase implements Interceptor {
    @Override public Response intercept(@NonNull Chain chain) throws IOException {
        //For Later When DataMuse API is required
        Request originalRequest = chain.request();
        HttpUrl originalHttpUrl = originalRequest.url();
        HttpUrl url = originalHttpUrl.newBuilder().addQueryParameter("api_key", Constants.API_KIVI_KEY).build();
        Response response = chain.proceed(chain.request());
        return response;
    }
}

