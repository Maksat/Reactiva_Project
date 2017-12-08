package com.androidadvance.androidsurvey.fragment;

import android.app.Service;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.androidadvance.androidsurvey.Answers;
import com.androidadvance.androidsurvey.R;

public class FragmentSurveyText extends FragmentSurveyQuestion implements TextWatcher {

    protected EditText editText_answer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.setButtonContinue((Button)getRootView().findViewById(R.id.button_continue));
        this.setTextViewTitle((TextView) getRootView().findViewById(R.id.textview_q_title));

        this.setVideoView((VideoView) getRootView().findViewById(R.id.videoView));
        this.setImageView((ImageView)getRootView().findViewById(R.id.imageView));

        editText_answer = (EditText) getRootView().findViewById(R.id.editText_answer);

        return getRootView();
    }

    @Override
    public void onClick(View view)
    {
        getQuestion().getAnswer().clearAnswerStrings();

        getQuestion().getAnswer().addAnswerString(editText_answer.getText().toString().trim());
        super.onClick(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editText_answer.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            this.setContinueButtonVisible(true);
        } else {
            this.setContinueButtonVisible(false);
        }
    }

    @Override
    public void onFragmentDisplayed() {
        super.onFragmentDisplayed();
        editText_answer.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText_answer, 0);
        }
    }
}