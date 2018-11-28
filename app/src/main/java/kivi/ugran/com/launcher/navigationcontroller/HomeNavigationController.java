package kivi.ugran.com.launcher.navigationcontroller;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.FragmentTransaction;
import kivi.ugran.com.core.Constants;
import kivi.ugran.com.launcher.GalleryFragment;
import kivi.ugran.com.launcher.KiviHomeFragment;
import kivi.ugran.com.launcher.R;
import kivi.ugran.com.launcher.RequestFragment;

public class HomeNavigationController {
    public Fragment currentFragment;
    public int currentTab;
    FragmentActivity context;
    public Map<Integer, ArrayList<String>> navigationStackOfTags = new HashMap<>();
    Map<String, Fragment> stackOfFragments = new HashMap<>();

    public HomeNavigationController(@NonNull FragmentActivity context) {
        this.context = context;
        navigationStackOfTags.put(BottomTab.HOME.ordinal(), new ArrayList<>());
        navigationStackOfTags.put(BottomTab.REQUESTS.ordinal(), new ArrayList<>());
        navigationStackOfTags.put(BottomTab.GALLERY.ordinal(), new ArrayList<>());
    }

    public void switchFragments(FragmentTransactionRequest fragmentTransactionRequest, Bundle args) {
        navigateTo(fragmentTransactionRequest,args);
    }

    public void onBackPressed() {

    }

    public void navigateTo(@NonNull FragmentTransactionRequest fragmentTransactionRequest, @NonNull Bundle args) {
        Log.d("mapsample_tracking",
                "Enter navigateTo for tag " + fragmentTransactionRequest.tag + " tabId " + fragmentTransactionRequest.tabId);
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        Fragment fragmentProvided = getFragment(fragmentTransactionRequest, args);
        //Decide navigateTo the received fragment and hide all others

        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (fragmentProvided != null) {
            //add Or hide and Show sequence
            if (navigationStackOfTags.get(fragmentTransactionRequest.tabId).contains(fragmentTransactionRequest.tag)) {

                //hide 'currentFragment' and show 'fragmentProvided'
                if (currentFragment != null) {
                    ft.hide(currentFragment);
                }
                if (fragmentProvided != null) {
                    ft.show(fragmentProvided);
                }
                ft.commit();
                Log.d("mapsample_tracking", "hide currentFragment - " + currentFragment.getClass().getCanonicalName());
                Log.d("mapsample_tracking",
                        "show fragmentProvided - " + fragmentProvided.getClass().getCanonicalName());
            } else {
                //add fragment
                String tag = fragmentTransactionRequest.tag;
                fragmentManager.beginTransaction().add(R.id.contentFrame, fragmentProvided, tag).commit();
                Log.d("mapsample_tracking", "add fragment - " + fragmentProvided.getClass().getCanonicalName());
                navigationStackOfTags.get(fragmentTransactionRequest.tabId).add(tag);
            }
            //update
            currentFragment = fragmentProvided;
            Log.d("mapsample_tracking", "currentFragment is - " + currentFragment.getClass().getCanonicalName());
            currentTab = fragmentTransactionRequest.tabId;
        } else {
            //throw exception
            Log.d("mapsample_tracking", "Exit (Failed) navigateTo for tag "
                    + fragmentTransactionRequest.tag
                    + " tabId "
                    + fragmentTransactionRequest.tabId);
        }
    }

    @Nullable private Fragment getFragment(FragmentTransactionRequest fragmentTransactionRequest, Bundle args) {
        Fragment fragment;
        FragmentManager fragmentManager = context.getSupportFragmentManager();

        //find the fragment tag which will be the root fragment for the tab index
        String requiredTag = fragmentTransactionRequest.tag;
        //find the stack of tags
        ArrayList<String> fragmentTags = navigationStackOfTags.get(fragmentTransactionRequest.tabId);
        if (fragmentTags != null && fragmentTags.contains(requiredTag)) {
            //already created , just navigateTo the fragment
            FragmentTransaction ft = fragmentManager.beginTransaction();
            fragment = fragmentManager.findFragmentByTag(requiredTag);
            fragment.setArguments(args);
        } else {
            //create the fragment
            fragment = createFragment(fragmentTransactionRequest, args);
        }
        return fragment;
    }

    @Nullable private Fragment createFragment(FragmentTransactionRequest fragmentTransactionRequest, Bundle args) {
        Fragment fragment = null;
        switch (fragmentTransactionRequest.tag) {
            case Constants.ChildFragmentTags.HOME_ROOT_FRAGMENT_TAG:
                fragment = KiviHomeFragment.newInstance(args);
                break;
            case Constants.ChildFragmentTags.REQUEST_ROOT_FRAGMENT_TAG:
                fragment = RequestFragment.newInstance(args);
                break;
            case Constants.ChildFragmentTags.GALLERY_ROOT_FRAGMENT_TAG:
                fragment = GalleryFragment.newInstance(args);
                break;
        }
        return fragment;
    }

    @Nullable private String getFragmentTag(int navigationID) {
        String tag = null;
        switch (navigationID) {
            case R.id.navigation_home:
                tag = Constants.ChildFragmentTags.HOME_ROOT_FRAGMENT_TAG;
                break;
            case R.id.navigation_requests:
                tag = Constants.ChildFragmentTags.REQUEST_ROOT_FRAGMENT_TAG;
                break;
            case R.id.navigation_gallery:
                tag = Constants.ChildFragmentTags.GALLERY_ROOT_FRAGMENT_TAG;
                break;
        }
        return tag;
    }



    //
    //@Nullable private Fragment getFragment(int navigationID) {
    //    Fragment fragment;
    //    FragmentManager fragmentManager = context.getSupportFragmentManager();
    //
    //    //find the fragment tag which will be the root fragment for the tab index
    //    String requiredTag = getFragmentTag(navigationID);
    //    //find the stack of tags
    //    ArrayList<String> fragmentTags = navigationStackOfTags.get(navigationID);
    //    if (fragmentTags != null && fragmentTags.contains(requiredTag)) {
    //        //already created , just navigateTo the fragment
    //        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
    //        fragment = fragmentManager.findFragmentByTag(requiredTag);
    //    } else {
    //        //create the fragment
    //        fragment = createFragment(navigationID);
    //    }
    //    return fragment;
    //}
    //
    //
    //public void navigateTo(int navigationID) {
    //    FragmentManager fragmentManager = context.getSupportFragmentManager();
    //    Fragment fragmentProvided = getFragment(navigationID);
    //    //Decide navigateTo the received fragment and hide all others
    //
    //    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
    //
    //    if (fragmentProvided != null) {
    //        //hide current fragment
    //        if (currentFragment != null) {
    //            //hide currentFragment and navigateTo fragmentProvided
    //            ft.hide(currentFragment);
    //            ft.show(fragmentProvided);
    //            ft.commit();
    //            Log.d("mapsample_tracking", "hide currentFragment - " + currentFragment.getClass().getCanonicalName());
    //            Log.d("mapsample_tracking",
    //                    "show fragmentProvided - " + fragmentProvided.getClass().getCanonicalName());
    //        } else {
    //            String tag = getFragmentTag(navigationID);
    //            Log.d("mapsample_tracking", "add fragment - " + fragmentProvided.getClass().getCanonicalName());
    //            Log.d("mapsample_tracking", "currentFragment is - " + currentFragment.getClass().getCanonicalName());
    //
    //            fragmentManager.beginTransaction().add(R.id.contentFrame, fragmentProvided, tag).commit();
    //
    //            navigationStackOfTags.get(navigationID).add(tag);
    //        }
    //        currentFragment = fragmentProvided;
    //    } else {
    //        //throw exception
    //    }
    //}
    //
    //public void switchFragments(int navigationID) {
    //    navigateTo(navigationID);
    //}
    //
    //@Nullable private Fragment createFragment(int navigationID) {
    //    Fragment fragment = null;
    //    switch (navigationID) {
    //        case R.id.navigation_home:
    //            ArrayList<kivi.ugran.com.kds.model.search.Place> placeList = new ArrayList<>();
    //            Bundle args = new Bundle();
    //            args.putParcelableArrayList("places", placeList);
    //            fragment = KiviHomeFragment.newInstance(args);
    //            break;
    //        case R.id.navigation_requests:
    //            fragment = TutorialFragment.newInstance(1);
    //            break;
    //        case R.id.navigation_gallery:
    //            fragment = TutorialFragment.newInstance(1);
    //            break;
    //    }
    //    return fragment;
    //}
}

