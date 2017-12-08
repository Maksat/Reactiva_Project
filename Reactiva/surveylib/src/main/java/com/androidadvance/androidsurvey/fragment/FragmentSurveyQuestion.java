package com.androidadvance.androidsurvey.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.androidadvance.androidsurvey.Answers;
import com.androidadvance.androidsurvey.ISurvey;
import com.androidadvance.androidsurvey.models.Question;

import java.io.File;
import java.util.Date;


public class FragmentSurveyQuestion extends Fragment implements View.OnClickListener{
    private ISurvey survey;
    private Button buttonContinue;
    private TextView textViewTitle;
    private ViewGroup rootView;
    private Question question;
    private ImageView imageView;
    protected VideoView videoView;
    private FragmentSurveyQuestion fragmentSurveyQuestion;

    public void setSurvey(ISurvey survey) {
        this.survey = survey;
    }

    public void setButtonContinue(Button buttonContinue) {
        this.buttonContinue = buttonContinue;
        this.buttonContinue.setOnClickListener(this);
    }

    public TextView getTextViewTitle() {
        return textViewTitle;
    }

    public void setTextViewTitle(TextView textViewTitle) {
        this.textViewTitle = textViewTitle;
    }

    public String getTitle()
    {
        return this.textViewTitle.getText().toString();
    }

    public void setContinueButtonVisible(boolean visible)
    {
        if(visible) {
            this.buttonContinue.setVisibility(View.VISIBLE);
        }else
        {
            this.buttonContinue.setVisibility(View.GONE);
        }
    }

    private void setTitleTextFromQuestion()
    {
        this.getTextViewTitle().setText(Html.fromHtml(question.getQuestionTitle()));
    }

    @Override
    public void onClick(View view) {
        survey.go_to_next(this);
    }

    public ViewGroup getRootView() {
        return rootView;
    }

    public void setRootView(ViewGroup rootView) {
        this.rootView = rootView;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question q_data) {
        this.question = q_data;
        this.setTitleTextFromQuestion();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.setSurvey((ISurvey) getActivity());

        Question q_data = (Question)getArguments().getSerializable("data");

        if(q_data != null) {
            this.setQuestion(q_data);

            if (q_data.getRequired() != null && q_data.getRequired()) {
                this.setContinueButtonVisible(false);
            }
        }
        setVideo();
        setImage();

        if(question != null) {
            Answers.getInstance().put_answer(question.getQuestionTitle(), question.getAnswer());
        }
    }

    public void setImage()
    {
        if(question == null)
        {
            return;
        }
        String imgFileName = question.getQuestionImage();

        Log.i("set Image", question.getQuestionTitle());

        if(imgFileName == null || imgFileName.trim().equals(""))
        {
            if(imageView != null) {
                imageView.setVisibility(View.GONE);
            }
        }else {
            setImage(imgFileName);
        }
    }

    public void setImage(String fileName)
    {
        if(fileName != null && imageView != null) {
            File imgFile = new File(this.getContext().getFilesDir() + "/" + fileName);
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
            }
        }
    }

    public void setImage(Bitmap image)
    {
        imageView.setImageBitmap(image);
    }

    public void setVideo()
    {
        if(question == null)
        {
            return;
        }

        String videoFileName = question.getQuestionVideo();
        setVideo(videoFileName);

    }

    public void setVideo(String fileName)
    {
        if(fileName == null || fileName.trim().equals(""))
        {
            if(videoView != null) {
                videoView.setVisibility(View.GONE);
            }
        }else {

            videoView.setVideoPath(this.getContext().getFilesDir() + "/" + fileName);
            videoView.start();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
        }
    }
    public void onFragmentHidden() {
        if(videoView != null) {
            videoView.stopPlayback();
        }

        if(question != null) {
            question.getAnswer().setTimeEnd(new Date());
        }
    }

    public void onFragmentDisplayed() {
        setVideo();
        if(question != null) {
            question.getAnswer().setTimeStart(new Date());


            if (question.getTimeToNextQuestion() != null && question.getTimeToNextQuestion() > 0) {
                setContinueButtonVisible(false);
                Handler handler = new Handler();
                fragmentSurveyQuestion = this;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        survey.go_to_next(fragmentSurveyQuestion);
                    }
                }, question.getTimeToNextQuestion() * 1000);
            }
        }
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setVideoView(VideoView videoView) {
        this.videoView = videoView;
    }
}
