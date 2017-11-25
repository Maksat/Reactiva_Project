package com.androidadvance.androidsurvey.fragment;

import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.androidadvance.androidsurvey.Answers;
import com.androidadvance.androidsurvey.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentRadioboxes extends FragmentSurveyQuestion {

    private RadioGroup radioGroup;
    private final ArrayList<RadioButton> allRb = new ArrayList<>();
    private boolean at_leaset_one_checked = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_radioboxes, container, false);

        this.setButtonContinue((Button) rootView.findViewById(R.id.button_continue));
        this.setTextViewTitle((TextView) rootView.findViewById(R.id.textview_q_title));
        this.setVideoView((VideoView) rootView.findViewById(R.id.videoView));
        this.setImageView((ImageView)rootView.findViewById(R.id.imageView));
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);

        return rootView;
    }

    private void collect_data() {

        //----- collection & validation for is_required
        getQuestion().getAnswer().clearAnswerStrings();
        at_leaset_one_checked = false;
        for (RadioButton rb : allRb) {
            if (rb.isChecked()) {
                at_leaset_one_checked = true;
                getQuestion().getAnswer().addAnswerString(rb.getText().toString());
            }
        }

        toggleContinueButtonVisibility();
    }

    private void toggleContinueButtonVisibility()
    {
        if (this.getQuestion().getRequired()) {
            if (at_leaset_one_checked) {
                this.setContinueButtonVisible(true);
            } else {
                this.setContinueButtonVisible(false);
            }
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
            RadioButton rb = new RadioButton(getActivity());
            rb.setText(Html.fromHtml(choice));
            rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            rb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            radioGroup.addView(rb);
            allRb.add(rb);

            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    collect_data();
                }
            });
        }

        toggleContinueButtonVisibility();
    }
}