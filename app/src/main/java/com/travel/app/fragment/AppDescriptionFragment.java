package com.travel.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travel.app.R;

/**
 * Created by user on 21.10.2016.
 */

public class AppDescriptionFragment extends Fragment {

    private static final String mGitHub = "https://github.com/AndreiKry";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_about, container, false);
        return view;
    }
}
