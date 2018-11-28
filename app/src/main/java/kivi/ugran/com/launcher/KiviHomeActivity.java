package kivi.ugran.com.launcher;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.afollestad.materialdialogs.MaterialDialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.Constants;
import kivi.ugran.com.core.DialogWebView;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.MaterialDialogUtils;
import kivi.ugran.com.core.PlatformUtils;
import kivi.ugran.com.core.SharedPreferenceUtils;
import kivi.ugran.com.core.UiUtils.CircleTransform;
import kivi.ugran.com.core.callbacks.ActivityCallback;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.kds.model.KiviRating;
import kivi.ugran.com.kds.model.UpdateKiviData;
import kivi.ugran.com.kds.model.registration.Coordinates;
import kivi.ugran.com.kds.model.registration.Otp;
import kivi.ugran.com.kds.model.registration.Registration;
import kivi.ugran.com.kds.model.search.Place;
import kivi.ugran.com.kds.model.search.SearchKivi;
import kivi.ugran.com.kds.usecases.GetOTPUseCase;
import kivi.ugran.com.kds.usecases.RateKiviUseCase;
import kivi.ugran.com.kds.usecases.SearchKiviUseCase;
import kivi.ugran.com.kds.usecases.UpdateKiviUseCase;
import kivi.ugran.com.launcher.navigationdrawer.DrawerMenuFragment;
import kivi.ugran.com.launcher.navigationcontroller.FragmentTransactionRequest;
import kivi.ugran.com.launcher.navigationcontroller.HomeNavigationController;
import kivi.ugran.com.launcher.preferences.KiviPreferenceActivity;

public class KiviHomeActivity extends AppCompatActivity
        implements DrawerMenuFragment.OnFragmentInteractionListener, ActivityCallback {

    private final int PERMISSION_WRITE_EXTERNAL_STORAGE = 3;
    private DrawerLayout drawerLayout;
    private Uri mPhotoUri;
    ImageView profilecamera;
    ImageView profilePicture;
    TextView guestName;
    //KiviHomeFragment mapFragment;
    ArrayList<Place> places = new ArrayList<>();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
               launchHomeMapView();
                return true;
            case R.id.navigation_gallery:
                launchGalleryView();
                return true;
            case R.id.navigation_requests:
                launchRequestView();
                return true;
        }
        return false;
    };

    @Override protected void onResume() {
        super.onResume();
        //executeAdvancedSearch();
        //executeSearch();
        //rateKivi("testy2", "Hello123", KiviApplication.getAndroidDeviceID(), "rateKivi", "Comment for Session", 5);
    }

    private void rateKivi(String device_id, String kiviRequestId, String requestorDeviceId, String kiviNotificationType,
            String kiviComment, int kiviRating) {

        KiviRating rating =
                new KiviRating(device_id, kiviRequestId, requestorDeviceId, requestorDeviceId, kiviComment, kiviRating);

        RateKiviUseCase.rateKivi(rating, new GenericCallback<Boolean>() {
            @Override public void success(Boolean response) {
                LogUtils.e("Kivi", "rateKivi success response :" + response);
            }

            @Override public void error(String err) {
                LogUtils.e("Kivi", "rateKivi error " + err.toString());
            }
        });
    }

    private void executeSearch(String latVal, String lngVal, boolean moveCameraToFirstLocation) {
        SearchKivi searchKiviData = new SearchKivi(KiviApplication.getAndroidDeviceID(), latVal, lngVal,
                Constants.KiviPreferences.SEARCH_RESULTS_DEFAULT_LIMIT,
                Constants.KiviPreferences.SEARCH_DISTANCE_DEFAULT_LIMIT);
        SearchKiviUseCase.searchKivi(searchKiviData, new GenericCallback<List<Place>>() {
            @Override public void success(List<Place> response) {
                LogUtils.d("Kivi", "mapsample_tracking - SearchKivi success count :" + response.size());
                places.clear();
                places.addAll(response);
                if(navigationController.currentFragment instanceof KiviHomeFragment) {
                    LogUtils.d("Kivi", "mapsample_tracking - executeSearch <updateKiviLocation> SearchKivi success count :" + places.size());
                    KiviHomeFragment kiviHomeFragment = (KiviHomeFragment)navigationController.currentFragment;
                    kiviHomeFragment.updateKiviLocation(places,moveCameraToFirstLocation);
                } else {
                    LogUtils.d("Kivi", "mapsample_tracking - executeSearch <launchHomeMapView> SearchKivi success count :" + places.size());
                    launchHomeMapView();
                }
            }

            @Override public void error(String err) {
                LogUtils.e("Kivi", "mapsample_tracking - executeSearch SearchKivi error " + err.toString());
            }
        });
    }

    @Override protected void onStop() {
        super.onStop();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kivi_home);
        drawerLayout = findViewById(R.id.drawerLayout);
        final Activity kiviActivity = this;
        profilePicture = drawerLayout.findViewById(R.id.profilepic);
        profilecamera = drawerLayout.findViewById(R.id.profilecamera);
        guestName = drawerLayout.findViewById(R.id.profilename);
        profilecamera.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(kiviActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(kiviActivity,
                        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, PERMISSION_WRITE_EXTERNAL_STORAGE);
            } else {
                mPhotoUri = PlatformUtils.takePictureUri(kiviActivity);
            }
        });

        //load profile picture if one is available
        Bitmap bm = PlatformUtils.loadProfilePictureFromPreference(this);
        if (bm != null) {
            CircleTransform circleTransform = new CircleTransform();
            profilePicture.setImageBitmap(circleTransform.transform(bm));
        }

        //update the profile name if one is available
        String profileName = SharedPreferenceUtils.loadString(this, Constants.RegistrationSettings.KEY_KIVI_NAME);
        if (profileName != null && !profileName.isEmpty()) {
            guestName.setText(profileName);
        }

        //setup bottom navigation menu
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        checkPlayServices();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        double lat = preferences.getFloat(Constants.KiviPreferences.KEY_LOCATION_LAT, 0.0f);
        double lng = preferences.getFloat(Constants.KiviPreferences.KEY_LOCATION_LNG, 0.0f);
        String latVal = String.valueOf(lat);
        String lngVal = String.valueOf(lng);
        executeSearch(latVal,lngVal, true);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String profilePicDone = getString(R.string.profile_picture_done);
        switch (requestCode) {
            case PlatformUtils.TAKE_PHOTO: {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, profilePicDone, Toast.LENGTH_SHORT).show();
                    String[] projection = {
                            MediaStore.MediaColumns._ID, MediaStore.Images.ImageColumns.ORIENTATION,
                            MediaStore.Images.Media.DATA
                    };
                    Cursor c = getContentResolver().query(mPhotoUri, projection, null, null, null);
                    if (c != null) {
                        c.moveToFirst();
                    }

                    String photoFileName = null;
                    if (c != null) {
                        photoFileName = c.getString(c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    }
                    Bitmap bitmap = BitmapFactory.decodeFile(photoFileName);
                    Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 400, 300);
                    if (null != thumbImage) {
                        /* save profile picture ,transform before showing */
                        PlatformUtils.saveProfilePictureToPreference(this, thumbImage);
                        CircleTransform circleTransform = new CircleTransform();
                        profilePicture.setImageBitmap(circleTransform.transform(thumbImage));
                    }
                    //Update Registration with Profile Picture
                    /*
                       This will be added later when required
                     */
                    //Registration registration = RegistrationActivity.loadRegistrationData();
                    //if (registration != null) {
                    //    updateProfilePicture(registration);
                    //}
                }
                break;
            }
            case PlatformUtils.SELECT_PHOTO: {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, profilePicDone, Toast.LENGTH_SHORT).show();
                    //Uri selectedImage = data.getData();
                    //if (selectedImage != null) {
                    //    byte[] bytesFromBitmap = PlatformUtils.getBytesFromUri(this, selectedImage);
                    //    KiviApplication kiviApplication = (KiviApplication) KiviApplication.getInstance();
                    //    kiviApplication.testFireBaseStorage(bytesFromBitmap);
                    //}
                }
                break;
            }
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != Activity.RESULT_OK) {
                    String ok = getString(R.string.ok);
                    String message = getString(R.string.requires_google_play_service);
                    String termsNotAcceptedError = getString(R.string.terms_not_accepted_error);
                    MaterialDialog.Builder alert =
                            new MaterialDialog.Builder(Objects.requireNonNull(this)).title(message)
                                    .positiveText(ok)
                                    .onPositive((dialog, which) -> {
                                    });

                    if (MaterialDialogUtils.isSafeToOperate(this)) {
                        alert.show();
                    }
                }
                //Play services are available
                LogUtils.d("Kivi", "mapsample_tracking - Play services are available");

                launchHomeMapView();
                break;
        }
        //if (mapFragment != null) mapFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        String hasStorage = getString(R.string.has_storage_toast);
        String needStorage = getString(R.string.need_storage_permission);
        switch (requestCode) {
            case PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, hasStorage, Toast.LENGTH_SHORT).show();
                    //now take a profile Pic
                    mPhotoUri = PlatformUtils.takePictureUri(this);
                } else {
                    Toast.makeText(this, needStorage, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * This is for access drawer form child fragments
     * @param menuid
     */
    @Override public void onFragmentInteraction(int menuid) {
        switch (menuid) {
            case DrawerMenuFragment.HELP_COMMAND:
                launchTutorialFlow(this);
                break;
            case DrawerMenuFragment.GO_ONLINE_COMMAND:
                String kiviAvailableStatus = "Available";
                if (!SharedPreferenceUtils.loadBoolean(this, Constants.KiviPreferences.KEY_KIVI_ONLINE)) {
                    kiviAvailableStatus = "Offline";
                }
                Toast.makeText(this, kiviAvailableStatus, Toast.LENGTH_SHORT).show();
                break;
            case DrawerMenuFragment.TERMS_OF_USE_COMMNAD:
                launchTermsOfUseDialog();
                break;
            case DrawerMenuFragment.PRIVACY_POLICY_COMMNAD:
                launchPrivacyPolciyDialog();
                break;
        }
    }

    public void openDrawer() {
        if (null != drawerLayout) {
            drawerLayout.openDrawer(Gravity.START);
        }
    }

    public void closeDrawer() {
        if (null != drawerLayout) {
            drawerLayout.closeDrawer(Gravity.START);
        }
    }

    private void launchTermsOfUseDialog() {
        DialogWebView dialogWebView = new DialogWebView(this);
        String tosHTML = PlatformUtils.loadAssetFileAsString(this, "terms_of_use.html");
        dialogWebView.loadData(tosHTML);
        MaterialDialog.Builder termsOfUseDialogBuilder =
                new MaterialDialog.Builder(this).customView(dialogWebView, false)
                        .cancelable(false)
                        .title(getString(R.string.menu_kivi_terms))
                        .negativeText(getString(R.string.cancel))
                        .onNegative((dialog, which) -> {
                            // do something
                            SharedPreferenceUtils.saveBoolean(this, Constants.KiviPreferences.KEY_KIVI_ONLINE, false);
                        })
                        .negativeColor(R.color.black)
                        .positiveText(getString(R.string.ok))
                        .positiveColor(R.color.black)
                        .onPositive((dialog, which) -> SharedPreferenceUtils.saveBoolean(this,
                                Constants.KiviPreferences.KEY_KIVI_ONLINE, true))
                        .dismissListener(dialog -> {
                            dialogWebView.removeAllViews();
                            updateOnlineStatus();
                        });

        if (MaterialDialogUtils.isSafeToOperate(this)) {
            termsOfUseDialogBuilder.show();
        }
    }

    private void launchPrivacyPolciyDialog() {
        DialogWebView dialogWebView = new DialogWebView(this);
        String tosHTML = PlatformUtils.loadAssetFileAsString(this, "privacy_policy.html");
        dialogWebView.loadData(tosHTML);
        MaterialDialog.Builder termsOfUseDialogBuilder =
                new MaterialDialog.Builder(this).customView(dialogWebView, false)
                        .cancelable(false)
                        .title(getString(R.string.menu_privacy_policy))
                        .negativeText(getString(R.string.cancel))
                        .onNegative((dialog, which) -> {
                            // update Kivi
                            SharedPreferenceUtils.saveBoolean(this, Constants.KiviPreferences.KEY_KIVI_ONLINE, false);
                        })
                        .negativeColor(R.color.black)
                        .positiveText(getString(R.string.ok))
                        .positiveColor(R.color.black)
                        .onPositive((dialog, which) -> {
                            // update Kivi
                            SharedPreferenceUtils.saveBoolean(this, Constants.KiviPreferences.KEY_KIVI_ONLINE, true);
                        })
                        .dismissListener(dialog -> {
                            dialogWebView.removeAllViews();
                            updateOnlineStatus();
                        });

        if (MaterialDialogUtils.isSafeToOperate(this)) {
            termsOfUseDialogBuilder.show();
        }
    }

    HomeNavigationController navigationController = new HomeNavigationController(this);
    @Nullable
    private FragmentTransactionRequest createFragmentTransactionRequest(int navigationId){
        switch (navigationId) {
            case R.id.navigation_home:
                return FragmentTransactionRequest.HOME_ROOT_FRAGMENT;
            case R.id.navigation_requests:
                return FragmentTransactionRequest.REQUEST_ROOT_FRAGMENT;
            case R.id.navigation_gallery:
                return FragmentTransactionRequest.GALLERY_ROOT_FRAGMENT;
        }
        return null;
    }

    private void launchHomeMapView() {
        Bundle args = new Bundle();
        args.putParcelableArrayList("places", places);
        navigationController.navigateTo(createFragmentTransactionRequest(R.id.navigation_home),args);
    }

    private void launchRequestView(){
        Bundle args = new Bundle();
        navigationController.navigateTo(createFragmentTransactionRequest(R.id.navigation_requests),args);
    }

    private void launchGalleryView(){
        Bundle args = new Bundle();
        navigationController.navigateTo(createFragmentTransactionRequest(R.id.navigation_gallery),args);
    }

    private void launchSettings(){
        Intent intent = new Intent(this, KiviPreferenceActivity.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void updateOnlineStatus() {
        //get location and prepare UpdateKiviData
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        double lat = preferences.getFloat(Constants.KiviPreferences.KEY_LOCATION_LAT, 0.0f);
        double lng = preferences.getFloat(Constants.KiviPreferences.KEY_LOCATION_LNG, 0.0f);

        String kiviAvailableStatus = "Available";
        if (!SharedPreferenceUtils.loadBoolean(this, Constants.KiviPreferences.KEY_KIVI_ONLINE)) {
            kiviAvailableStatus = "Offline";
        }

        UpdateKiviData updateKiviData =
                new UpdateKiviData(new Coordinates(lat, lng), KiviApplication.getAndroidDeviceID(),
                        KiviApplication.getDeviceToken(), kiviAvailableStatus);

        //execute api
        UpdateKiviUseCase.updateKivi(updateKiviData, new GenericCallback<Boolean>() {
            @Override public void success(Boolean response) {
                LogUtils.d("Kivi", "UpdateKiviWorker success");
            }

            @Override public void error(String err) {
                LogUtils.d("Kivi", "UpdateKiviWorker error");
            }
        });
    }

    private void launchTutorialFlow(@NonNull Context context) {
        Intent intent = new Intent(context, TutorialActivity.class);
        intent.putExtra(Constants.KiviTutorial.KEY_LAUNCHED_BY_MENU, true);
        context.startActivity(intent);
        if (context instanceof Activity) {
            KiviHomeActivity activity = (KiviHomeActivity) context;
            activity.closeDrawer();
            activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    @MainThread private void updateProfilePicture(@NonNull Registration registration) {
        GetOTPUseCase.getOTP(registration, new GenericCallback<Otp>() {
            Boolean existingKivi = false;

            @Override public void success(Otp response) {
                existingKivi = response != null ? response.getExistingKivi() : false;
                if (existingKivi) {
                    LogUtils.d("Kivi", "Proifle Picture Done" + " existingKivi is " + existingKivi);
                }
            }

            private void dismissProgress(boolean success) {
                //dismiss progress
                if (success) {
                    //This is a must call with every registration api. <workaround for lat,long issue from server>
                    updateOnlineStatus();
                    if (existingKivi) {
                        String existingUser = getString(R.string.existing_user);
                        Toast.makeText(getApplicationContext(), existingUser, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override public void error(String err) {
                LogUtils.d("Kivi", "Registration Failed err is " + err);
            }
        });
    }

    //https://stackoverflow.com/questions/31016722/googleplayservicesutil-vs-googleapiavailability
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;

    private void checkPlayServices() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(this);
        if (code == ConnectionResult.SUCCESS) {
            onActivityResult(REQUEST_GOOGLE_PLAY_SERVICES, Activity.RESULT_OK, null);
        } else if (api.isUserResolvableError(code) && api.showErrorDialogFragment(this, code,
                REQUEST_GOOGLE_PLAY_SERVICES)) {
            // wait for onActivityResult call (see below)
        } else {
            Toast.makeText(this, api.getErrorString(code), Toast.LENGTH_LONG).show();
        }
    }

    @Override public void notifyActionSubmitted(int actionID, String data) {
        switch(actionID){
            case ACTION_EXECUTE_SEARCH:
                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                double lat = preferences.getFloat(Constants.KiviPreferences.KEY_USER_SEARCHED_LOCATION_LAT, 0.0f);
                double lng = preferences.getFloat(Constants.KiviPreferences.KEY_USER_SEARCHED_LOCATION_LNG, 0.0f);
                String latVal = String.valueOf(lat);
                String lngVal = String.valueOf(lng);
                /*
                  Do not move camera when search is executed when user moving camera to his location
                 */
                executeSearch(latVal,lngVal, false);
                break;
        }
    }

    @Override public void openDrawerMenu() {
        openDrawer();
    }

    @Override public void closeDrawerMenu() {
        closeDrawer();
    }

    @Override public void openSettings() {
        launchSettings();
    }
}