package kivi.ugran.com.launcher;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GalleryFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    public GalleryFragment() {
    }

    public static GalleryFragment newInstance(Bundle args ) {
        GalleryFragment fragment = new GalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        return rootView;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
