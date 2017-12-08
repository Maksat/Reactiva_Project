package com.androidadvance.androidsurvey.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.androidadvance.androidsurvey.ISurvey;
import com.androidadvance.androidsurvey.R;
import com.androidadvance.androidsurvey.models.SurveyProperties;

import java.io.File;


public class FragmentStart extends FragmentSurveyQuestion {

    private FragmentActivity mContext;
    private TextView textView_start;

    private SurveyProperties surveyProperties;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_start, container, false);

        textView_start = (TextView) rootView.findViewById(R.id.textView_start);
        setImageView((ImageView) rootView.findViewById(R.id.imageView));
        setVideoView((VideoView) rootView.findViewById(R.id.videoView));

        Button button_continue = (Button) rootView.findViewById(R.id.button_continue);
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ISurvey) mContext).go_to_next(null);
            }
        });

        surveyProperties = (SurveyProperties) getArguments().getSerializable("survery_properties");
        assert surveyProperties != null;

        setImage();
        setVideo();

        return rootView;
    }

    @Override
    public void setImage() {
        String imageName = surveyProperties.getIntroImage();
        if(imageName != null && !imageName.trim().equals("")) {
            this.setImage(imageName);
        }
    }

    @Override
    public void setVideo() {
        String videoName = surveyProperties.getIntroVideo();
        setVideo(videoName);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        textView_start.setText(Html.fromHtml(surveyProperties.getIntroMessage()));
    }

}