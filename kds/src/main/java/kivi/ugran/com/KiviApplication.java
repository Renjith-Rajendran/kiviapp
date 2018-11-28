package kivi.ugran.com;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import kivi.ugran.com.core.BaseApplication;
import kivi.ugran.com.core.Constants;
import kivi.ugran.com.core.JSONUtils;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.SharedPreferenceUtils;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.firebase.api.ApiFirebaseServices;
import kivi.ugran.com.firebase.api.RequestInterceptorFirebase;
import kivi.ugran.com.kds.api.ApiKiviServices;
import kivi.ugran.com.kds.api.RequestInterceptor;
import kivi.ugran.com.kds.model.configuration.Configuration;
import kivi.ugran.com.kds.model.registration.Coordinates;
import kivi.ugran.com.kds.serializer.CoordinatorSerializer;
import kivi.ugran.com.kds.usecases.GetConfigurationUseCase;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class KiviApplication extends BaseApplication {
    protected static ApiKiviServices apiKiviServices;
    protected static ApiFirebaseServices apiFirebaseServices;
    protected static FirebaseFirestore firebaseFirestore;
    protected static FirebaseStorage firebaseStorage;
    protected static Configuration configuration;
    protected static String deviceToken;
    protected static FusedLocationProviderClient mFusedLocationClient;

    public static FusedLocationProviderClient getFusedLocationClient() {
        return mFusedLocationClient;
    }

    protected static String androidID;

    public static String getAndroidDeviceID() {
        boolean debugOnly = false;
        if (debugOnly) {
            return "testy6_nexus51";
        } else {
            return androidID;
        }
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static FirebaseStorage getFirebaseStorage() {
        return firebaseStorage;
    }

    public static FirebaseFirestore getFireBaseStore() {
        return firebaseFirestore;
    }

    public static String getInstanceID() {
        String instanceID;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getInstance().getBaseContext());
        if (preferences.contains(Constants.RegistrationSettings.KEY_REGISTRATION_OTP) == false) {
            instanceID = UUID.randomUUID().toString();
            SharedPreferences.Editor editor =
                    preferences.edit().putString(Constants.RegistrationSettings.KEY_INSTANCE_ID, instanceID);
            editor.apply();
        } else {
            instanceID = preferences.getString(Constants.RegistrationSettings.KEY_REGISTRATION_OTP, "null");
        }
        LogUtils.d("Kivi", "Registration Done..instanceID is " + instanceID);
        return instanceID;
    }

    public static String getDeviceToken() {
        String token = "";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getInstance().getBaseContext());
        if (preferences.contains(Constants.RegistrationSettings.KEY_KIVI_DEVICE_TOKEN)) {
            token = preferences.getString(Constants.RegistrationSettings.KEY_KIVI_DEVICE_TOKEN, "null");
        }
        LogUtils.d("Kivi", "Device Token is " + token);
        return token;
    }

    public static void getLastKnownLocation() {

    }

    public static void setDeviceToken(String token) {
        deviceToken = token;
        SharedPreferenceUtils.saveString(getInstance(), Constants.RegistrationSettings.KEY_KIVI_DEVICE_TOKEN, token);
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getInstance().getBaseContext());
        //SharedPreferences.Editor editor =
        //        preferences.edit().putString(Constants.RegistrationSettings.KEY_KIVI_DEVICE_TOKEN, token);
        //editor.apply();
        LogUtils.d("Kivi", "Device Token set as " + token);
    }

    public static synchronized ApiKiviServices getApiKiviServices() {
        return apiKiviServices;
    }

    public static synchronized ApiFirebaseServices getApiFirebaseServices() {
        return apiFirebaseServices;
    }

    private void createApiServices() {
        apiKiviServices = provideRetrofit(provideOkHttpClient());
        apiFirebaseServices = provideRetrofitFireBase(provideOkHttpClientForFirebase());
    }

    private void createFusedLocationProvider() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void createDeviceToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String token = instanceIdResult.getToken();
            LogUtils.d("kivi", "FCM_TOKEN " + "is " + token);
            KiviApplication.setDeviceToken(token);
            //RequestNotificationUsecase.testPushNotification("Check","Miss Me!!");
        });
    }

    private void createKiviConfiguration() {
        GetConfigurationUseCase.getConfiguration(new GenericCallback<Configuration>() {
            @Override public void success(Configuration response) {
                if (response != null) {

                    configuration = response;
                    //Save to Shared Preference
                    String json = JSONUtils.convertAsJsonString(response);
                    if (json != null) {
                        SharedPreferenceUtils.saveString(getInstance(),
                                Constants.RegistrationSettings.KEY_CONFIGURATION_ID, json);
                    }

                    LogUtils.d("kivi", "Configuration to json is " + json);
                    LogUtils.d("kivi", "GetConfigurationUseCase.getConfiguration.success");
                } else {
                    LogUtils.e("kivi",
                            "GetConfigurationUseCase.getConfiguration.success has null response !!!. Continuing...");
                    //Load previous value from shared preference
                    String json = SharedPreferenceUtils.loadString(getInstance(),
                            Constants.RegistrationSettings.KEY_CONFIGURATION_ID);
                    if (json != null) {
                        //load from shared preference and deserialize
                        configuration = new Gson().fromJson(json, Configuration.class);
                    }
                }
            }

            @Override public void error(String err) {
                LogUtils.e("kivi",
                        "Tutorial Activity :- GetConfigurationUseCase.getConfiguration error is " + err + " !!!");
            }
        });
    }

    @Override protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override public void onCreate() {
        super.onCreate();
        //apiKiviServices = provideRetrofit(provideOkHttpClient());
        //apiFirebaseServices = provideRetrofitFireBase(provideOkHttpClientForFirebase());
        createApiServices();
        createDeviceToken();
        createAndroidID();
        createFireBaseStore();
        createFirebaseStorage();
        createKiviConfiguration();
        createFusedLocationProvider();
    }

    //public ApiKiviServices provideRetrofit(OkHttpClient okHttpClient) {
    //    Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_KIVI_ENDPOINT)
    //            .addConverterFactory(ScalarsConverterFactory.create())
    //            .addConverterFactory(GsonConverterFactory.create())
    //            .client(okHttpClient)
    //            .build();
    //    return retrofit.create(ApiKiviServices.class);
    //}

    public ApiKiviServices provideRetrofit(OkHttpClient okHttpClient) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Coordinates.class, new CoordinatorSerializer()).create();

        GsonConverterFactory factory = GsonConverterFactory.create(gson);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_KIVI_ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(factory)
                .client(okHttpClient)
                .build();
        return retrofit.create(ApiKiviServices.class);
    }

    public ApiFirebaseServices provideRetrofitFireBase(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_FIREBASE_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(ApiFirebaseServices.class);
    }

    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(Constants.TIMEOUT_IN_SEC, TimeUnit.SECONDS);
        okHttpClient.readTimeout(Constants.TIMEOUT_IN_SEC, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(new RequestInterceptor());//Do it if you have to insert API keys
        okHttpClient.addInterceptor(logging);  // <-- this is the important line!
        return okHttpClient.build();
    }

    OkHttpClient provideOkHttpClientForFirebase() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(Constants.TIMEOUT_IN_SEC, TimeUnit.SECONDS);
        okHttpClient.readTimeout(Constants.TIMEOUT_IN_SEC, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(new RequestInterceptorFirebase());//Do it if you have to insert API keys
        okHttpClient.addInterceptor(logging);  // <-- this is the important line!
        return okHttpClient.build();
    }

    private void createFirebaseStorage() {
        FirebaseStorage db = FirebaseStorage.getInstance();
        if (db.getApp() == null) {
            LogUtils.w("Kivi", "FirebaseStorage is Not Available..");
        } else {
            firebaseStorage = db;
        }
    }

    private void createFireBaseStore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (db.getApp() == null) {
            LogUtils.w("Kivi", "FirebaseFirestore is Not Available..");
            firebaseFirestore = null;
        } else {
            firebaseFirestore = db;
            //testFirebaseStore();
        }
    }

    public void testFireBaseStorage(byte[] data) {
        LogUtils.d("Kivi", "testFireBaseStorage Enter");
        String path = getDeviceToken() + "/" + UUID.randomUUID() + ".png";
        StorageReference fireMeme = getFirebaseStorage().getReference(path);
        StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("token", getDeviceToken()).build();
        UploadTask uploadTask = fireMeme.putBytes(data, metadata);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            LogUtils.w("Kivi", "testFireBaseStorage upload image completed");
            Uri uri = taskSnapshot.getUploadSessionUri();
        });
    }

    private void testFirebaseStore() {
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        getFireBaseStore().collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> LogUtils.d("kivi",
                        "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception e) {
                        LogUtils.w("kivi", "Error adding document " + e.getMessage());
                    }
                });
    }

    private void createAndroidID() {
        androidID = Settings.Secure.getString(getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
