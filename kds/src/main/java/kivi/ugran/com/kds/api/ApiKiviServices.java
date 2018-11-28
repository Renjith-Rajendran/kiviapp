package kivi.ugran.com.kds.api;

import java.util.List;

import kivi.ugran.com.kds.model.GeneratePassword;
import kivi.ugran.com.kds.model.KiviRating;
import kivi.ugran.com.kds.model.UpdateKiviData;
import kivi.ugran.com.kds.model.configuration.Configuration;
import kivi.ugran.com.kds.model.registration.Otp;
import kivi.ugran.com.kds.model.registration.Registration;
import kivi.ugran.com.kds.model.search.AdvancedSearch;
import kivi.ugran.com.kds.model.search.Places;
import kivi.ugran.com.kds.model.search.SearchKivi;
import kivi.ugran.com.kds.model.requestnotification.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
//https://futurestud.io/tutorials/retrofit-2-how-to-send-plain-text-request-body
//https://futurestud.io/tutorials/retrofit-2-add-multiple-query-parameter-with-querymap
//https://exceptionshub.com/how-to-post-raw-whole-json-in-the-body-of-a-retrofit-request.html
public interface ApiKiviServices {
    @GET("configurationdata") Call<Configuration> loadKiViConfiguration();

    @POST("userregistration") Call<Otp> getOTP(@Body List<Registration> registration);

    @POST("resendotp") Call<Otp> resendOtp(@Body String deviceId);

    @POST("passwordgeneration") Call<GeneratePassword> verifyOTPAndGeneratePassword(
            @Body GeneratePassword generatePassword);

    @POST("updatekividata") Call<ResponseBody> updateKiviData(
            @Body List<UpdateKiviData> updateKiviData);

    @POST("searchkivi") Call<Places> searchKivi(
            @Body SearchKivi searchKivi);


    @POST("advancedsearch") Call<Places> advancedSearch(
            @Body AdvancedSearch advancedSearch);

    @POST("ratingforkivi") Call<ResponseBody> rateKivi(
            @Body KiviRating kiviRating);

    @POST("requestnotification") Call<ResponseBody> requestNotification(
            @Body Request requestNotificationData);
}

/*
    The @Body annotation defines a single request body.

    Single<ResponseBody> postComment(@Url String url, @Body Registration issue);
    @Url annotation. With the help of this annotation, we can provide the URL for this request.

    @GET("/repos/{owner}/{repo}/issues")
    The @Path annotation binds the parameter value to the corresponding variable (curly brackets) in the request URL

    @QueryMap
 */