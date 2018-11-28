package kivi.ugran.com.launcher;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.Constants;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.MaterialDialogUtils;
import kivi.ugran.com.core.PlatformUtils;
import kivi.ugran.com.core.SharedPreferenceUtils;
import kivi.ugran.com.launcher.viewpagerindicator.DotsIndicator;
import kivi.ugran.com.core.UiUtils.ZoomOutPageTransformer;

public class TutorialActivity extends AppCompatActivity
        implements ViewPager.OnPageChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String ARG_SHOW_TUTORIAL_SKIPPED_MESSAGE = "show_tutorial_warning";
    private final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private final int PERMISSION_ACCESS_CAMERA = 2;
    private int viewPagerCount;
    private boolean launchedFromDrawerMenu = false;
    private GoogleApiClient googleApiClient;
    FloatingActionButton fab;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PlatformUtils.TAKE_PHOTO: {
                if (resultCode == RESULT_OK) {
                    PlatformUtils.takePicture(this);
                }
                break;
            }
            case PlatformUtils.SELECT_PHOTO: {
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        byte[] bytesFromBitmap = PlatformUtils.getBytesFromUri(this, selectedImage);
                        KiviApplication kiviApplication = (KiviApplication) KiviApplication.getInstance();
                        kiviApplication.testFireBaseStorage(bytesFromBitmap);
                    }
                }
                break;
            }
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Has Location access", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Need your location for the app to work properly", Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_ACCESS_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Has Camera access", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Need access to camera for the app to work properly.", Toast.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        if(getIntent().hasExtra(Constants.KiviTutorial.KEY_LAUNCHED_BY_MENU)){
            launchedFromDrawerMenu = getIntent().getBooleanExtra(Constants.KiviTutorial.KEY_LAUNCHED_BY_MENU,false);
        }

        if (launchedFromDrawerMenu) {
            viewPagerCount = Constants.KiviTutorial.VIEW_PAGER_COUNT_TUTORIAL_MODE;
            initView();
        } else {
            //First Install and Run
            viewPagerCount = Constants.KiviTutorial.VIEW_PAGER_COUNT_TUTORIAL_AND_REGISTRATION_MODE;
            checkUserAuthenticationStatus();
        }
    }

    @Override protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void initView() {

        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
        //SpringDotsIndicator dotsIndicator1 = findViewById(R.id.spring_dots_indicator);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        //mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        dotsIndicator.setViewPager(mViewPager);
        //dotsIndicator1.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                if (position == 1 || position == 2) {
                    //Ask only when user moves to next fragment
                    requestAppPermissions(position - 1);
                }
                if (position == 3) {
                    Bundle args = new Bundle();
                    args.putInt(ARG_SHOW_TUTORIAL_SKIPPED_MESSAGE, 0);
                    launchRegistrationScreen(args);
                }
                updateFabText(position);
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if(launchedFromDrawerMenu){
                finish();
            } else {
                if (mViewPager.getCurrentItem() != 3) {

                    Bundle args = new Bundle();
                    args.putInt(ARG_SHOW_TUTORIAL_SKIPPED_MESSAGE, 1);
                    launchRegistrationScreen(args);
                    //int colorRed = ResourcesCompat.getColor(getResources(), R.color.md_red_900, null);
                    //int colorWhite = ResourcesCompat.getColor(getResources(), R.color.md_cyan_50, null);

                    //ToDo firebase testing
                    //boolean testFireBaseStorage = false;
                    //if (testFireBaseStorage) {
                    //    PlatformUtils.openGallery(this);
                    //} else {
                    //    Bundle args = new Bundle();
                    //    args.putInt(ARG_SHOW_TUTORIAL_SKIPPED_MESSAGE, 1);
                    //    launchRegistrationScreen(args);
                    //}
                } else if (mViewPager.getCurrentItem() == 3) {
                    int colorBlack = ResourcesCompat.getColor(getResources(), R.color.kivi_back_ground, null);
                    int colorWhite = ResourcesCompat.getColor(getResources(), R.color.md_cyan_50, null);
                    MaterialDialogUtils.getSnackBar(view, "Registration Completed", colorBlack, colorWhite).show();
                }
            }

        });
    }

    private void updateFabText(int position) {
        TextView fabtextView = findViewById(R.id.fabtext);
        if(launchedFromDrawerMenu){
            if (position == 2) {
                fabtextView.setText(R.string.done);
            } else {
                fabtextView.setText(R.string.skip);
            }
        }
        else {
            if (position == 3) {
                fabtextView.setText(R.string.done);
            } else {
                fabtextView.setText(R.string.skip);
            }
        }

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override public void onConnected(@Nullable Bundle bundle) {
        LogUtils.d("kivi", "gms connected, now get the last known location and updated to settings");
        updateLastKnowLocation();
    }

    @Override public void onConnectionSuspended(int i) {
        LogUtils.d("kivi", "gms disconnected");
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogUtils.e("kivi", "gms connect attempt failed");
    }

    private void updateLastKnowLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (KiviApplication.getFusedLocationClient() != null) {
                KiviApplication.getFusedLocationClient().getLastLocation().addOnSuccessListener(lastLocation -> {
                    // Got last known location. In some rare situations this can be null.
                    if (lastLocation != null) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
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
    }

    private void launchRegistrationScreen(Bundle bundle) {
        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        if (bundle != null) {
            intent.putExtra(ARG_SHOW_TUTORIAL_SKIPPED_MESSAGE, bundle.getInt(ARG_SHOW_TUTORIAL_SKIPPED_MESSAGE));
        }
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void launchKiviHomeScreen() {
        Intent intent = new Intent(getApplicationContext(), KiviHomeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    //private void checkUserAuthenticationStatus() {
    //    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    //
    //    final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
    //    progressDialog.setIndeterminate(true);
    //    progressDialog.setMessage("Authenticating...");
    //    progressDialog.show();
    //    //dismiss porgress after 3 seconds
    //    new android.os.Handler().postDelayed(() -> {
    //        progressDialog.dismiss();
    //        //Request Location Permision..
    //        Boolean userAuthenticated = SharedPreferenceUtils.loadBoolean(getApplicationContext(),
    //                Constants.RegistrationSettings.KEY_REGISTRATION_COMPLETED);
    //        //if (preferences.contains(Constants.RegistrationSettings.KEY_REGISTRATION_OTP)) {
    //        if (preferences.contains(Constants.RegistrationSettings.KEY_REGISTRATION_COMPLETED) && userAuthenticated) {
    //            //ToDo
    //            String userAuth = this.getResources().getString(R.string.user_authenticated);
    //            Toast.makeText(this, userAuth, Toast.LENGTH_SHORT).show();
    //            LogUtils.d("Kivi", "User is registered Launching HomeKiviActivity");
    //            launchKiviHomeScreen();
    //            //initView();
    //        } else {
    //            LogUtils.d("Kivi", "User not registered ...");
    //            initView();
    //        }
    //    }, 1000);
    //}

    private void checkUserAuthenticationStatus() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        //progressDialog.setIndeterminate(true);
        //progressDialog.setMessage("Authenticating...");
        //progressDialog.show();
        //dismiss porgress after 3 seconds
        new android.os.Handler().postDelayed(() -> {
            //progressDialog.dismiss();
            //Request Location Permision..
            Boolean userAuthenticated = SharedPreferenceUtils.loadBoolean(getApplicationContext(),
                    Constants.RegistrationSettings.KEY_REGISTRATION_COMPLETED);
            //if (preferences.contains(Constants.RegistrationSettings.KEY_REGISTRATION_OTP)) {
            if (preferences.contains(Constants.RegistrationSettings.KEY_REGISTRATION_COMPLETED) && userAuthenticated) {
                //ToDo
                String userAuth = this.getResources().getString(R.string.user_authenticated);
                Toast.makeText(this, userAuth, Toast.LENGTH_SHORT).show();
                LogUtils.d("Kivi", "User is registered Launching HomeKiviActivity");
                launchKiviHomeScreen();
                //initView();
            } else {
                LogUtils.d("Kivi", "User not registered ...");
                initView();
            }
        }, 500);
    }

    private void requestAppPermissions(int currentFragment) {
        if (currentFragment == 0) {
            //Check for Location
            LogUtils.d("kivi", "Checking permission for Location...");
            if (ContextCompat.checkSelfPermission(TutorialActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TutorialActivity.this,
                        new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, PERMISSION_ACCESS_COARSE_LOCATION);
            }
        }

        if (currentFragment == 1) {
            //Check for Camera
            LogUtils.d("kivi", "Checking permission for Camera...");
            if (ContextCompat.checkSelfPermission(TutorialActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TutorialActivity.this, new String[] { Manifest.permission.CAMERA },
                        PERMISSION_ACCESS_CAMERA);
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a TutorialFragment (defined as a static inner class below).
            Fragment fragment = TutorialFragment.newInstance(position);
            switch (position) {
                default:
                    //fragment = TutorialFragment.newInstance(position);
                    break;
            }
            return fragment;
        }

        //@Override public int getCount() {
        //    return 4;
        //}

        @Override public int getCount() {
            return viewPagerCount;
        }
    }

    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override public void onPageSelected(int position) {

    }

    @Override public void onPageScrollStateChanged(int state) {

    }
}
