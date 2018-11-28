package kivi.ugran.com.launcher;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import kivi.ugran.com.core.Constants;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.callbacks.ActivityCallback;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.kds.model.requestnotification.Request;
import kivi.ugran.com.kds.usecases.RequestNotificationUseCase;

//....places
//https://sunny89blog.wordpress.com/2016/02/07/placeautocompletefragment-with-custom-ui/   <custom UI>
//https://code.i-harness.com/en/q/2169e47
//https://stackoverflow.com/questions/35036743/can-placeautocompletefragment-overlay-mode-be-called-from-inside-the-fragment
//https://stackoverflow.com/questions/41345987/how-to-use-placeautocompletefragment-widget-in-a-fragment <place fragment dynamically >
//https://developers.google.com/places/android-sdk/autocomplete#add_an_autocomplete_widget
//https://www.truiton.com/2016/03/android-nearby-places-api-autocomplete-widget/

//....maps
//https://stackoverflow.com/questions/19353255/how-to-put-google-maps-v2-on-a-fragment-using-viewpager
//https://developers.google.com/maps/documentation/android-sdk/map-with-marker
//https://stackoverflow.com/questions/36297749/how-can-i-extend-supportmapfragment-in-android-studio
//https://cloud.google.com/maps-platform/?apis=maps,routes,places  < key >
//https://developers.google.com/maps/documentation/android-sdk/controls
//https://stackoverflow.com/questions/17412882/positioning-google-maps-v2-zoom-in-controls-in-android
//https://developers.google.com/maps/documentation/android-sdk/map

//...styling of maps
//https://developers.google.com/maps/documentation/android-sdk/styling
//...marks click handler
////https://stackoverflow.com/questions/14226453/google-maps-api-v2-how-to-make-markers-clickable
public class KiviHomeFragment extends Fragment {

    private SupportPlaceAutocompleteFragment autocompleteFragment = null;
    MapView mapView;
    private GoogleMap googleMap;
    ArrayList<kivi.ugran.com.kds.model.search.Place> kiviLocations = new ArrayList<>();
    BottomSheetBehavior<View> behavior;
    FrameLayout bottomSheetContainer;
    HomeFragmentBottomSheet bottomSheet;
    View bottomSheetView;

    HashMap<String, String> markerIdToIconTextMap = new HashMap<>();
    HashMap<String, kivi.ugran.com.kds.model.search.Place> markerIdToPlaceDataMap = new HashMap<>();

    String requestType;
    String requestSpeed;
    String snapTypelocation;
    String extraInstructions = "";
    String requestLocation;
    kivi.ugran.com.kds.model.search.Place requestPlaceData;

    Place lastPlaceSearched = null;
    Marker lastSearchedMarker = null;

    public static KiviHomeFragment newInstance(Bundle args) {
        KiviHomeFragment fragment = new KiviHomeFragment();
        fragment.setArguments(args);
        LogUtils.d("kivi", "KiviHomeFragment newInstance args set ");
        return fragment;
    }

    public void updateGoogleMapsWithKivis(boolean moveCameraToFirstLocation) {
        if (googleMap != null) {
            googleMap.clear();
        } else {
            return;
        }

        if (lastPlaceSearched != null) {
            addSelectedPlaceMarker(lastPlaceSearched);
        }

        //always set a location to show
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        //Last known location !!.. Do we need this as a default backup
        double lat = preferences.getFloat(Constants.KiviPreferences.KEY_LOCATION_LAT, 0.0f);
        double lng = preferences.getFloat(Constants.KiviPreferences.KEY_LOCATION_LNG, 0.0f);
        LatLng firsKivitLocation = new LatLng(lat, lng);

        if (kiviLocations.size() > 0) {
            Integer index = 0;
            for (kivi.ugran.com.kds.model.search.Place kiviLocation : kiviLocations) {
                lat = kiviLocation.getCoordinates().getLat();
                lng = kiviLocation.getCoordinates().getLng();
                LatLng location = new LatLng(lat, lng);
                index++;

                BitmapDescriptor descriptor = getIconForMarker(index.toString(), 100, 100);
                if (null != descriptor) {
                    MarkerOptions markerOptions = new MarkerOptions().flat(true)
                            .position(location)
                            .title("kivi")
                            .snippet("Snaps Provider")
                            .icon(descriptor);

                    String markerId = googleMap.addMarker(markerOptions).getId();
                    LogUtils.d("kivi", "KiviHomeFragment updateGoogleMapsWithKivis addMarker " + markerId);
                    markerIdToIconTextMap.put(markerId, index.toString());
                    markerIdToPlaceDataMap.put(markerId, kiviLocation);
                }
            }

            //move the map to first location...
            if (index > 0 && moveCameraToFirstLocation) {
                // For dropping a marker at a point on the Map
                // For zooming automatically to the location
                CameraPosition cameraPosition = new CameraPosition.Builder().target(firsKivitLocation).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        } else {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(firsKivitLocation).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        googleMap.setOnMarkerClickListener(marker -> {

            addBottomSheetToContainer();

            //check the clicker marker had a device id associated with it
            kivi.ugran.com.kds.model.search.Place placeData = markerIdToPlaceDataMap.get(marker.getId());
            requestPlaceData = placeData;
            if (placeData != null && behavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                LogUtils.d("kivi", "placeData device_id is " + placeData.getDeviceId());
                ImageView imageView = bottomSheetView.findViewById(R.id.requestedKiviIcon);
                String titleIcon = markerIdToIconTextMap.get(marker.getId());
                if (titleIcon != null) {
                    Bitmap bm = getBitMapForMarker(titleIcon, 100, 100);
                    if (bm != null) {
                        imageView.setImageBitmap(bm);
                    }
                }

                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocation(placeData.getCoordinates().getLat(),
                            placeData.getCoordinates().getLng(), 1);
                    Log.v("kivi", "addresses+)_+++" + addresses);
                    //String CityName = addresses.get(0).getAddressLine(0);
                    String county = addresses.get(0).getSubAdminArea();
                    String CityName = addresses.get(0).getThoroughfare();
                    Log.v("log_tag", "CityName" + CityName);
                    if (CityName != null && !CityName.isEmpty()) {
                        TextView snaptypeLocation = bottomSheetView.findViewById(R.id.snap_type_location);
                        snaptypeLocation.setText(CityName);
                    } else if (county != null && !county.isEmpty()) {
                        TextView snaptypeLocation = bottomSheetView.findViewById(R.id.snap_type_location);
                        snaptypeLocation.setText(county);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //placeData.getDeviceId();
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            return false;
        });
    }

    private void updatePlaceDataFromArguments(Bundle args) {
        if (args != null && args.containsKey("places")) {
            ArrayList<Parcelable> places = args.getParcelableArrayList("places");
            if (places != null && places.size() > 0 && kiviLocations.size() > 0) {
                //discard old entries ...
                kiviLocations.clear();
            }
            if (places != null) {
                for (Parcelable p : places) {
                    kivi.ugran.com.kds.model.search.Place place = (kivi.ugran.com.kds.model.search.Place) p;
                    kiviLocations.add(place);
                }
            }
        } else {
            LogUtils.d("kivi", "KiviHomeFragment updateKiviLocation bundle arg is null");
        }
    }

    public void updateKiviLocation(@Nonnull ArrayList<kivi.ugran.com.kds.model.search.Place> places,
            boolean moveCameraToFirstLocation) {
        if (places.size() > 0 && kiviLocations.size() > 0) {
            //discard old entries ...
            kiviLocations.clear();
        }
        for (Parcelable p : places) {
            kivi.ugran.com.kds.model.search.Place place = (kivi.ugran.com.kds.model.search.Place) p;
            kiviLocations.add(place);
        }
        updateGoogleMapsWithKivis(moveCameraToFirstLocation);
        LogUtils.d("kivi", "KiviHomeFragment updateKiviLocation(places) - size is " + kiviLocations.size());
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.d("kivi", "KiviHomeFragment onAttach");
    }

    @Override public void onStart() {
        super.onStart();
        LogUtils.d("kivi", "KiviHomeFragment onStart");
    }

    @Override public void onResume() {
        super.onResume();
        mapView.onResume();
        LogUtils.d("kivi", "KiviHomeFragment onResume");
    }

    @Override public void onPause() {
        super.onPause();
        mapView.onPause();
        LogUtils.d("kivi", "KiviHomeFragment onPause");
    }

    @Override public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override public void onDestroyView() {
        if (autocompleteFragment != null) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.remove(autocompleteFragment).commitNowAllowingStateLoss();
        }
        super.onDestroyView();
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //updateGoogleMapsWithKivis();
        Bundle args = getArguments();
        if (args != null && args.containsKey("places")) {
            ArrayList<Parcelable> places = args.getParcelableArrayList("places");
            if (places != null && places.size() > 0 && kiviLocations.size() > 0) {
                //discard old entries ...
                kiviLocations.clear();
            }
            if (places != null) {
                for (Parcelable p : places) {
                    kivi.ugran.com.kds.model.search.Place place = (kivi.ugran.com.kds.model.search.Place) p;
                    kiviLocations.add(place);
                }
            }
        } else {
            LogUtils.d("kivi", "KiviHomeFragment onCreateView arg is null");
        }

        if (googleMap == null) {
            LogUtils.d("kivi", "KiviHomeFragment onViewCreated kiviLocations.size is "
                    + kiviLocations.size()
                    + " googleMap == null");
        } else {

            LogUtils.d("kivi", "KiviHomeFragment onViewCreated kiviLocations.size is " + kiviLocations.size());
        }
    }

    @SuppressLint("ClickableViewAccessibility") @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        updatePlaceDataFromArguments(args);
        LogUtils.d("kivi", "KiviHomeFragment onCreateView kiviLocations.size is " + kiviLocations.size());

        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_home_mapview, container, false);
            bottomSheetContainer = rootView.findViewById(R.id.bottomsheet_container);
            bottomSheet = rootView.findViewById(R.id.bottom_sheet);
            //sheet.setTopMarginOffset(50);
            behavior = BottomSheetBehavior.from(bottomSheet);
            androidx.appcompat.widget.Toolbar toolbar = rootView.findViewById(R.id.mapview_toolbar);
            Drawable overflowIcon =
                    ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_overflow_black_1x_24dp, null);
            toolbar.setOverflowIcon(overflowIcon);
            toolbar.inflateMenu(R.menu.menu_overflow_toobar);
            toolbar.setOnMenuItemClickListener(this::menuItemClicked);
            toolbar.setNavigationOnClickListener(view -> {
                //open Drawer
                if (getActivity() instanceof ActivityCallback) {
                    ActivityCallback activityCallback = (ActivityCallback) getActivity();
                    activityCallback.openDrawerMenu();
                }
            });

            mapView = rootView.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);

            mapView.setOnTouchListener((view, motionEvent) -> {
                //open Drawer
                if (getActivity() instanceof ActivityCallback) {
                    ActivityCallback activityCallback = (ActivityCallback) getActivity();
                    activityCallback.closeDrawerMenu();
                }
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                view.performClick();
                return true;
            });

            mapView.onResume(); // needed to get the map to display immediately
            try {
                MapsInitializer.initialize(Objects.requireNonNull(getActivity()).getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            LogUtils.e("kivi", ex.getMessage());
        }

        mapView.getMapAsync(mMap -> {
            googleMap = mMap;
            updatePlaceDataFromArguments(getArguments());
            LogUtils.d("kivi", "KiviHomeFragment onCreateView  google map available kiviLocations.size is "
                    + kiviLocations.size());
            // For showing a move to my location button
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                LogUtils.d("kivi", "insode google map callback return ..");
                return;
            }
            /*
             googleMap.getUiSettings().setZoomGesturesEnabled(true);
             googleMap.getUiSettings().setMapToolbarEnabled(true);
             */
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.setMyLocationEnabled(true);

            GoogleMapOptions options = new GoogleMapOptions();
            options.zoomControlsEnabled(true);
            options.liteMode(true);
            /*
              If you are using a MapView, use the MapView(Context, GoogleMapOptions) constructor and pass in your custom configured options.
             */
            /*
                This code crashes ...
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getActivity(), R.string.style_json));
            */

            //Customise the styling of the base map using a JSON object defined in a raw resource file.
            String jsonStyles = getResources().getString(R.string.style_json);
            boolean success = googleMap.setMapStyle(new MapStyleOptions(jsonStyles));

            LogUtils.d("kivi", "inside google map callback setMapStyle is " + success);
            //updateKiviLocation(kiviLocations);
            updateGoogleMapsWithKivis(true);
            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                private float currentZoom = -1;

                @Override public void onCameraChange(CameraPosition pos) {
                    if (pos.zoom != currentZoom) {
                        currentZoom = pos.zoom;
                        // do you action here where current zoom level requires an execution of search API again
                        //Toast.makeText(getContext(), "Zoomed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        autocompleteFragment = new SupportPlaceAutocompleteFragment();

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.place_fragment_container, autocompleteFragment);
        ft.commit();

        LatLngBounds bounds = new LatLngBounds(new LatLng(-33.880490, 151.184363), new LatLng(-33.858754, 151.229596));
        AutocompleteFilter typeFilter =
                new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
        autocompleteFragment.setBoundsBias(bounds);
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override public void onPlaceSelected(Place place) {
                Log.i("kivi", "KiviHomeFragment onPlaceSelected: " + place.getName());
                if (lastSearchedMarker != null) {
                    lastSearchedMarker.remove();
                }
                lastPlaceSearched = place;
                addSelectedPlaceMarker(place);

                CameraUpdate center = CameraUpdateFactory.newLatLng(place.getLatLng());
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);

                if (getActivity() instanceof ActivityCallback) {
                    ActivityCallback callback = (ActivityCallback) getActivity();
                    callback.notifyActionSubmitted(ActivityCallback.ACTION_EXECUTE_SEARCH, place.toString());
                }
                //Here, I move the camera first, then animate the camera, though both could be animateCamera()
                //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
                //execute search api for the new Lat/Long

                //AppCompatImageButton closeButton = autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button);
                //closeButton.setOnTouchListener(new View.OnTouchListener()
                //{
                //    public boolean onTouch(View v, MotionEvent event)
                //    {
                //        Toast toast = Toast.makeText(
                //                getContext(),
                //                "View touched",
                //                Toast.LENGTH_LONG
                //        );
                //        toast.show();
                //
                //        return true;
                //    }
                //});
                //// Obtain MotionEvent object
                //long downTime = SystemClock.uptimeMillis();
                //long eventTime = SystemClock.uptimeMillis() + 100;
                //float x = 0.0f;
                //float y = 0.0f;
                //// List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
                //int metaState = 0;
                //MotionEvent motionEvent = MotionEvent.obtain(
                //        downTime,
                //        eventTime,
                //        MotionEvent.ACTION_BUTTON_RELEASE,
                //        x,
                //        y,
                //        metaState
                //);
                //
                //// Dispatch touch event to view
                //closeButton.dispatchTouchEvent(motionEvent);

                //closeButton.callOnClick();
                //closeButton.setOnClickListener(new View.OnClickListener() {
                //    @Override public void onClick(View view) {
                //        //Toast.makeText(getContext(),"X",Toast.LENGTH_SHORT).show();
                //
                //    }
                //});
            }

            @Override public void onError(Status status) {
                Log.i("kivi", "KiviHomeFragment onPlaceSelected An error occurred: " + status);
            }
        });

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.i("kivi", "BottomSheetBehavior.STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.i("kivi", "BottomSheetBehavior.STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.i("kivi", "BottomSheetBehavior.STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.i("kivi", "BottomSheetBehavior.STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.i("kivi", "BottomSheetBehavior.STATE_HIDDEN");
                        break;
                }
            }

            @Override public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //Log.i("kivi", "slideOffset: " + slideOffset);
            }
        });
        //addBottomSheetToContainer();
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        return rootView;
    }

    private void updateUserSearchedLocation(LatLng latLng) {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor;
            editor = preferences.edit()
                    .putFloat(Constants.KiviPreferences.KEY_USER_SEARCHED_LOCATION_LAT, (float) latLng.latitude);
            editor.putFloat(Constants.KiviPreferences.KEY_USER_SEARCHED_LOCATION_LNG, (float) latLng.longitude);
            editor.apply();
            LogUtils.d("Kivi", "KiviHomeFragment updateUserSearchedLocation ");
        }
    }

    private void addSelectedPlaceMarker(Place place) {
        Log.i("kivi", "KiviHomeFragment onPlaceSelected: " + place.getName());
        updateUserSearchedLocation(place.getLatLng());
        BitmapDescriptor descriptor = getIconForMarker(" ", 100, 100);
        if (null != descriptor) {
            lastSearchedMarker = googleMap.addMarker(new MarkerOptions().flat(true)
                    .position(place.getLatLng())
                    .title(place.getName().toString())
                    .snippet("Searched Location")
                    .icon(descriptor));
        } else {
            //default
            lastSearchedMarker = googleMap.addMarker(new MarkerOptions().flat(true)
                    .position(place.getLatLng())
                    .title(place.getName().toString())
                    .snippet("Searched Location"));
        }
    }

    /*
     This needs to be replaced when selected images of the icon's are available.
     For now this is a quick indication of selection to user.
     */
    private void setupSelectionIndicator(ImageView imageView) {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            private Rect rect;

            @Override public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    imageView.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.kivi_back_ground, null));
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    imageView.setColorFilter(Color.argb(1, 0, 0, 0));
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                        imageView.setColorFilter(Color.argb(0, 0, 0, 0));
                    }
                }
                return false;
            }
        });
    }

    private void setupPassASnapData() {
        ImageView snapTypeVideo = bottomSheetView.findViewById(R.id.video_icon_1);
        snapTypeVideo.setOnClickListener(view -> requestType = "Video");
        setupSelectionIndicator(snapTypeVideo);

        ImageView snapTypePhoto = bottomSheetView.findViewById(R.id.camera_icon_1);
        snapTypePhoto.setOnClickListener(view -> requestType = "Photo");
        setupSelectionIndicator(snapTypePhoto);

        ImageView speed = bottomSheetView.findViewById(R.id.speed);
        speed.setOnClickListener(view -> requestSpeed = "Speed");
        setupSelectionIndicator(speed);

        ImageView lightSpeed = bottomSheetView.findViewById(R.id.light_speed);
        lightSpeed.setOnClickListener(view -> requestSpeed = "LightSpeed");
        setupSelectionIndicator(lightSpeed);

        EditText extraInstruction = bottomSheetView.findViewById(R.id.extra_instruction);

        extraInstruction.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                extraInstructions = extraInstruction.getText().toString();
                return true;
            }
            return false;
        });

        Button goButton = bottomSheetView.findViewById(R.id.go_button);
        goButton.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "Request Submitted", Toast.LENGTH_SHORT).show();
            executeSnapRequest();
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });
    }

    private void executeSnapRequest() {
        TextView snapTypeLocation = bottomSheetView.findViewById(R.id.snap_type_location);
        snapTypelocation = snapTypeLocation.getText().toString();
        //TextView snaptypeLocation = bottomSheetView.findViewById(R.id.snap_type_location);
        requestLocation = snapTypeLocation.getText().toString();

        Request request =
                new Request().getRequestData(requestPlaceData, requestLocation, requestType, requestSpeed, "Pending",
                        extraInstructions, false);
        if (request != null) {
            //request.setRequestorDeviceId("E4CAB8C2-8AB3-4913-AC99-39C2CDEA4A93");
            String goJson = new Gson().toJson(request, Request.class);
            LogUtils.d("kivi", "goJson is " + goJson);
        }
        RequestNotificationUseCase.requestNotification(Objects.requireNonNull(request), new GenericCallback<Boolean>() {
            @Override public void success(Boolean response) {
                LogUtils.d("kivi", "executeSnapRequest.success is " + response);
            }

            @Override public void error(String err) {
                LogUtils.d("kivi", "executeSnapRequest.success is " + err);
            }
        });

        //
        //Gson gson = new GsonBuilder().registerTypeAdapter(Coordinates.class, new CoordinatorSerializer()).create();
        //String json = gson.toJson(request.getCoordinates());
        //LogUtils.d("kivi", "new goJson is " + json);
    }

    private void addBottomSheetToContainer() {
        if (bottomSheetContainer != null) {
            bottomSheetContainer.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getContext())
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                bottomSheetView = inflater.inflate(R.layout.home_bottom_sheet, bottomSheetContainer, false);
            }
            bottomSheetContainer.addView(bottomSheetView);
            setupPassASnapData();
        }
    }

    @Nullable private BitmapDescriptor getIconForMarker(String iconText, int width, int height) {
        Bitmap thumbImage = writeTextOnDrawable(iconText, width, height);
        BitmapDescriptor descriptor = null;
        if (null != thumbImage) {
            descriptor = BitmapDescriptorFactory.fromBitmap(thumbImage);
        }
        return descriptor;
    }

    @Nullable private Bitmap getBitMapForMarker(String iconText, int width, int height) {
        return writeTextOnDrawable(iconText, width, height);
    }

    @Nullable private Bitmap getThumbnailBitMapForMarker(int width, int height) {
        //https://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(Objects.requireNonNull(getActivity()).getResources(),
                R.drawable.kivi_logo_ico);
        return ThumbnailUtils.extractThumbnail(bitmap, width, height);
    }

    private Bitmap writeTextOnDrawable(String text, int width, int height) {
        //https://stackoverflow.com/questions/13763545/android-maps-api-v2-with-custom-markers
        Bitmap bm = getThumbnailBitMapForMarker(width, height);
        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(Objects.requireNonNull(getContext()), 14));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas;
        if (bm != null) {
            canvas = new Canvas(bm);
            //If the text is bigger than the canvas , reduce the font size
            if (textRect.width() >= (canvas.getWidth()
                    - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            {
                paint.setTextSize(
                        convertToPixels(getContext(), 7));        //Scaling needs to be used for different dpi's
            }

            //Calculate the positions
            int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

            //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
            int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

            canvas.drawText(text, xPos, yPos, paint);
        }
        return bm;
    }

    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;
        return (int) ((nDP * conversionScale) + 0.5f);
    }

    private boolean menuItemClicked(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            //Toast.makeText(getActivity(), "Settings", Toast.LENGTH_SHORT).show();
            if (getActivity() instanceof KiviHomeActivity) {
                KiviHomeActivity activity = (KiviHomeActivity) getActivity();
                activity.openSettings();
            }
        }
        return false;
    }
}
