package kivi.ugran.com.launcher;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TutorialFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public TutorialFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TutorialFragment newInstance(int sectionNumber) {
        TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        if ((getArguments() != null ? getArguments().getInt(ARG_SECTION_NUMBER) : 0) == 0) {
            rootView = inflater.inflate(R.layout.fragment_tutorial_page_1, container, false);
        } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
            rootView = inflater.inflate(R.layout.fragment_tutorial_page_2, container, false);
        } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
            rootView = inflater.inflate(R.layout.fragment_tutorial_page_3, container, false);
        }
        return rootView;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}