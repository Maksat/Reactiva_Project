package com.androidadvance.androidsurvey.fragment;

import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.androidadvance.androidsurvey.Answers;
import com.androidadvance.androidsurvey.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentCheckboxes extends FragmentSurveyQuestion {

    private LinearLayout linearLayout_checkboxes;
    private final ArrayList<CheckBox> allCb = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_checkboxes, container, false);

        this.setButtonContinue((Button) rootView.findViewById(R.id.button_continue));
        this.setTextViewTitle((TextView) rootView.findViewById(R.id.textview_q_title));
        this.setVideoView((VideoView) rootView.findViewById(R.id.videoView));
        this.setImageView((ImageView)rootView.findViewById(R.id.imageView));

        linearLayout_checkboxes = (LinearLayout) rootView.findViewById(R.id.linearLayout_checkboxes);

        return rootView;
    }

    private void collect_data() {
        getQuestion().getAnswer().clearAnswerStrings();

        //----- collection & validation for is_required
        boolean at_leaset_one_checked = false;
        for (CheckBox cb : allCb) {
            if (cb.isChecked()) {
                at_leaset_one_checked = true;
                getQuestion().getAnswer().addAnswerString(cb.getText().toString());
            }
        }

        if (this.getQuestion().getRequired()) {
            this.setContinueButtonVisible(at_leaset_one_checked);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<String> qq_data = getQuestion().getChoices();
        if (getQuestion().getRandomChoices()) {
            Collections.shuffle(qq_data);
        }

        for (String choice : qq_data) {
            CheckBox cb = new CheckBox(getActivity());
            cb.setText(Html.fromHtml(choice));
            cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            cb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout_checkboxes.addView(cb);
            allCb.add(cb);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    collect_data();
                }
            });
        }
    }
}