package com.androidadvance.androidsurvey.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.androidadvance.androidsurvey.R;

public class FragmentEmpty extends FragmentSurveyQuestion {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_empty, container, false);
        this.setRootView(rootView);

        this.setButtonContinue((Button) getRootView().findViewById(R.id.button_continue));
        this.setTextViewTitle((TextView) getRootView().findViewById(R.id.textview_q_title));

        this.setVideoView((VideoView) getRootView().findViewById(R.id.videoView));
        this.setImageView((ImageView)getRootView().findViewById(R.id.imageView));

        return rootView;
    }

}