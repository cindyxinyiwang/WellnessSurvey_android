package com.example.cindywang.wellnesssurvey;

import android.app.Activity;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DFragment extends DialogFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_d, container, false);
        getDialog().setTitle("Invalid username or password");
        return rootView;
    }
}
