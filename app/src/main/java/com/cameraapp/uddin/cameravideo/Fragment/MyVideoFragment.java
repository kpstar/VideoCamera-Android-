package com.cameraapp.uddin.cameravideo.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cameraapp.uddin.cameravideo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyVideoFragment extends Fragment {


    public MyVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_video, container, false);
        return view;
    }

}
