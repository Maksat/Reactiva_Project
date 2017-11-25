package com.androidadvance.androidsurvey.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.androidadvance.androidsurvey.R;

public class FragmentMultiline extends FragmentSurveyText{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_text_multiline, container, false);
        this.setRootView(rootView);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() > 3) {
            this.setContinueButtonVisible(true);
        } else {
            this.setContinueButtonVisible(false);
        }

        super.afterTextChanged(editable);
    }
}