package kivi.ugran.com.launcher.navigationdrawer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.core.Constants;
import kivi.ugran.com.core.LogUtils;
import kivi.ugran.com.core.SharedPreferenceUtils;
import kivi.ugran.com.core.callbacks.GenericCallback;
import kivi.ugran.com.kds.model.UpdateKiviData;
import kivi.ugran.com.kds.model.registration.Coordinates;
import kivi.ugran.com.kds.usecases.UpdateKiviUseCase;
import kivi.ugran.com.launcher.R;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link DrawerMenuFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link DrawerMenuFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class DrawerMenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    public static final int HELP_COMMAND = 1;
    public static final int GO_ONLINE_COMMAND = 2;
    public static final int PRIVACY_POLICY_COMMNAD = 3;
    public static final int TERMS_OF_USE_COMMNAD = 4;

    private ListView listView;
    private DrawerMenuAdapter menuListAdapter;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    //private OnFragmentInteractionListener mListener;
    private boolean kiviOnline = true;

    public DrawerMenuFragment() {
        onCheckedChangeListener = (compoundButton, isChecked) -> {
            kiviOnline = isChecked;
            SharedPreferenceUtils.saveBoolean(Objects.requireNonNull(getActivity()),
                    Constants.KiviPreferences.KEY_KIVI_ONLINE, kiviOnline);
            updateOnlineStatus();
        };
    }

    private void updateOnlineStatus() {
        //get location and prepare UpdateKiviData
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        double lat = preferences.getFloat(Constants.KiviPreferences.KEY_LOCATION_LAT, 0.0f);
        double lng = preferences.getFloat(Constants.KiviPreferences.KEY_LOCATION_LNG, 0.0f);

        String kiviAvailableStatus = "Available";
        if (!SharedPreferenceUtils.loadBoolean(Objects.requireNonNull(getActivity()),
                Constants.KiviPreferences.KEY_KIVI_ONLINE)) {
            kiviAvailableStatus = "Offline";
        }

        UpdateKiviData updateKiviData =
                new UpdateKiviData(new Coordinates(lat, lng), KiviApplication.getAndroidDeviceID(), KiviApplication.getDeviceToken(),
                        kiviAvailableStatus);

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

    ///**
    // * Use this factory method to create a new instance of
    // * this fragment using the provided parameters.
    // *
    // * @param param1 Parameter 1.
    // * @param param2 Parameter 2.
    // * @return A new instance of fragment DrawerMenuFragment.
    // */

    //public static DrawerMenuFragment newInstance(String param1, String param2) {
    //    DrawerMenuFragment fragment = new DrawerMenuFragment();
    //    Bundle args = new Bundle();
    //    args.putString(ARG_PARAM1, param1);
    //    args.putString(ARG_PARAM2, param2);
    //    fragment.setArguments(args);
    //    return fragment;
    //}

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        //if (context instanceof OnFragmentInteractionListener) {
        //    //mListener = (OnFragmentInteractionListener) context;
        //} else {
        //    throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        //}
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kiviOnline = SharedPreferenceUtils.loadBoolean(Objects.requireNonNull(getActivity()),
                    Constants.KiviPreferences.KEY_KIVI_ONLINE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_drawer_navigation_menu, container, false);
        view.setFitsSystemWindows(true);
        return view;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.dialog_listview);
        initializeDrawerMenu();
    }

    private void initializeDrawerMenu() {
        menuListAdapter = new DrawerMenuAdapter(getActivity(), 0);
        menuListAdapter.setCompoundButtonOnCheckedChangeListener(onCheckedChangeListener);
        listView.setAdapter(menuListAdapter);
        listView.setOnItemClickListener(mOnSelectMenuItemClickListener);

        List<DrawerMenuOptionItem> items = new ArrayList<>();

        DrawerMenuOptionItem divider =
                new DrawerMenuOptionItem.Builder().rowType(DrawerMenuAdapter.RowType.DIVIDER).enabled(true).build();

        DrawerMenuOptionItem privacyPolicyMenuItem =
                new DrawerMenuOptionItem.Builder().rowType(DrawerMenuAdapter.RowType.ITEM)
                        .title(getString(R.string.menu_privacy_policy))
                        .textStyle(DrawerMenuOptionItem.TEXT_STYLE_LIGHT)
                        .textSize(14)
                        .enabled(true)
                        .viewContext(0)
                        .command(getCommandForId(PRIVACY_POLICY_COMMNAD))
                        .build();

        DrawerMenuOptionItem termsMenuItem = new DrawerMenuOptionItem.Builder().rowType(DrawerMenuAdapter.RowType.ITEM)
                .title(getString(R.string.menu_kivi_terms))
                .textStyle(DrawerMenuOptionItem.TEXT_STYLE_LIGHT)
                .textSize(14)
                .enabled(true)
                .viewContext(0)
                .command(getCommandForId(TERMS_OF_USE_COMMNAD))
                .build();

        DrawerMenuOptionItem helpMenuItem = new DrawerMenuOptionItem.Builder().rowType(DrawerMenuAdapter.RowType.ITEM)
                .title(getString(R.string.menu_help))
                .textStyle(DrawerMenuOptionItem.TEXT_STYLE_LIGHT)
                .textSize(14)
                .enabled(true)
                .viewContext(0)
                .command(getCommandForId(HELP_COMMAND))
                .build();

        String toggleText = getString(R.string.menu_go_online);
        DrawerMenuOptionItem goOnlineMenuItem =
                new DrawerMenuOptionItem.Builder().rowType(DrawerMenuAdapter.RowType.TOGGLEABLE)
                        .title(toggleText)
                        .textStyle(DrawerMenuOptionItem.TEXT_STYLE_LIGHT)
                        .textSize(14)
                        .enabled(true)
                        .viewContext(0)
                        .command(getCommandForId(GO_ONLINE_COMMAND))
                        .build();

        items.add(divider);
        items.add(goOnlineMenuItem);
        items.add(divider);
        items.add(helpMenuItem);
        items.add(divider);
        items.add(termsMenuItem);
        items.add(divider);
        items.add(privacyPolicyMenuItem);
        items.add(divider);

        menuListAdapter.addAll(items);
        menuListAdapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener mOnSelectMenuItemClickListener = new AdapterView.OnItemClickListener() {
        @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DrawerMenuOptionItem option = menuListAdapter.getItem(position);
            if (option != null && isAdded()) {
                option.command.execute(Objects.requireNonNull(getActivity()));
            }
        }
    };

    private MenuCommand getCommandForId(int itemId) {
        MenuCommand command = null;
        switch (itemId) {
            case HELP_COMMAND:
            case PRIVACY_POLICY_COMMNAD:
            case GO_ONLINE_COMMAND:
            case TERMS_OF_USE_COMMNAD:
                command = new MenuCommandImpl(itemId);
            default:
                break;
        }
        return command;
    }

    @Override public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int menuid);
    }
}
