package com.androidadvance.androidsurvey.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidadvance.androidsurvey.R;

public class FragmentTextSimple extends FragmentSurveyText {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_text_simple, container, false);
        this.setRootView(rootView);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}