package com.androidadvance.androidsurvey.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.androidadvance.androidsurvey.ISurvey;
import com.androidadvance.androidsurvey.R;


public class FragmentLoading extends Fragment {

    private FragmentActivity mContext;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_loading, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        Button button_continue = (Button) rootView.findViewById(R.id.button_cancel);

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ISurvey) mContext).go_to_next(null);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
    }
}