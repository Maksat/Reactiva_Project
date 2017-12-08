package com.androidadvance.androidsurvey;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.androidadvance.androidsurvey.adapters.AdapterFragmentQ;
import com.androidadvance.androidsurvey.fragment.FragmentCheckboxes;
import com.androidadvance.androidsurvey.fragment.FragmentEmpty;
import com.androidadvance.androidsurvey.fragment.FragmentEnd;
import com.androidadvance.androidsurvey.fragment.FragmentLoading;
import com.androidadvance.androidsurvey.fragment.FragmentMultiline;
import com.androidadvance.androidsurvey.fragment.FragmentNumber;
import com.androidadvance.androidsurvey.fragment.FragmentRadioboxes;
import com.androidadvance.androidsurvey.fragment.FragmentStart;
import com.androidadvance.androidsurvey.fragment.FragmentSurveyQuestion;
import com.androidadvance.androidsurvey.fragment.FragmentTextSimple;
import com.androidadvance.androidsurvey.models.Question;
import com.androidadvance.androidsurvey.models.SurveyPojo;
import com.androidadvance.androidsurvey.models.SurveyProperties;
import com.androidadvance.androidsurvey.DownloadHelper;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public abstract class SurveyActivity extends FragmentActivity implements ISurvey, ViewPager.OnPageChangeListener {

    private SurveyPojo mSurveyPojo;
    private ViewPager mPager;
    private String style_string = null;
    private String jsonSurvey;
    private ArrayList<Fragment> listOfFragments;
    private FragmentSurveyQuestion displayedFragment;

    protected void initializePager()
    {
        mSurveyPojo = new Gson().fromJson(jsonSurvey, SurveyPojo.class);

        Log.i("json Object = ", String.valueOf(mSurveyPojo.getQuestions()));

        addFragmentsFromPojo(style_string, mSurveyPojo.getSurveyProperties());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.getAdapter().notifyDataSetChanged();
        mPager.setCurrentItem(0);
        displayedFragment = (FragmentSurveyQuestion) listOfFragments.get(0);

        mPager.addOnPageChangeListener(this);
    }

    protected void downloadSurvey()
    {
        listOfFragments = new ArrayList<>();
        FragmentLoading frag_loading = new FragmentLoading();
        Bundle sBundle = new Bundle();
        frag_loading.setArguments(sBundle);
        listOfFragments.add(frag_loading);

        mPager = (ViewPager) findViewById(R.id.pager);
        AdapterFragmentQ mPagerAdapter = new AdapterFragmentQ(getSupportFragmentManager(), listOfFragments);
        mPager.setAdapter(mPagerAdapter);

        downloadAndUnzipContent();
    }

    //json stored in the assets folder. but you can get it from wherever you like.
    private String loadSurveyJson(String filename) {
        try {
            java.io.InputStream is = new FileInputStream(new File(filename));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void downloadAndUnzipContent(){
        String url = "http://showmanbayram.com/survey.zip";
        DownloadHelper download = new DownloadHelper(this.getFilesDir()+"/survey.zip", this, new DownloadHelper.PostDownload(){
            @Override
            public void downloadDone(File file) {
                // check unzip file now
                Decompress unzip = new Decompress(file);
                unzip.unzip();

                jsonSurvey = loadSurveyJson(file.getParent()+"/survey.json");
                ((AdapterFragmentQ)mPager.getAdapter()).removeItem(0);
                initializePager();
            }
        });
        download.execute(url);
    }

    public void uploadAnswers(final FragmentEnd fragmentEnd)
    {
        String url = "http://showmanbayram.com/upload_answers.php";
        String jsonString = Answers.getInstance().get_json_object();

        System.out.println(jsonString);
        //Log.i("Survey Activity", jsonString);

        UploadHelper uploadHelper = new UploadHelper(url, jsonString, new UploadHelper.PostUpload() {
            @Override
            public void uploadDone() {
                Log.i("Survey Activity", "upload completed");
                fragmentEnd.hideProgressBar();
                //exitSurvey();
            }
        });
//        uploadHelper.setTextView(fragmentEnd.getTextView());
        uploadHelper.execute();
        Log.i("Survey Activity", "Started uploading answers");
    }

    private ArrayList<Fragment> addFragmentsFromPojo(String styleString, SurveyProperties surveyProperties)
    {
        //- START -
        if (!surveyProperties.getSkipIntro()) {
            FragmentStart frag_start = new FragmentStart();
            Bundle sBundle = new Bundle();
            sBundle.putSerializable("survery_properties", surveyProperties);
            sBundle.putString("style", styleString);
            frag_start.setArguments(sBundle);
            listOfFragments.add(frag_start);
        }

        //- FILL -
        for (Question mQuestion : mSurveyPojo.getQuestions()) {
            if(mQuestion == null)
            {
                continue;
            }

            Bundle xBundle = new Bundle();
            xBundle.putSerializable("data", mQuestion);
            xBundle.putString("style", styleString);
            Fragment frag;

            switch(mQuestion.getQuestionType())
            {
                case "String":
                    frag = new FragmentTextSimple();
                    break;
                case "Checkboxes":
                    frag = new FragmentCheckboxes();
                    break;
                case "Radioboxes":
                    frag = new FragmentRadioboxes();
                    break;
                case "Number":
                    frag = new FragmentNumber();
                    break;
                case "StringMultiline":
                    frag = new FragmentMultiline();
                    break;
                case "Empty":
                    frag = new FragmentEmpty();
                    break;
                default:
                    continue;
            }

            frag.setArguments(xBundle);
            listOfFragments.add(frag);
        }

        //- END -
        FragmentEnd frag_end = new FragmentEnd();
        Bundle eBundle = new Bundle();
        eBundle.putSerializable("survery_properties", surveyProperties);
        eBundle.putString("style", styleString);
        frag_end.setArguments(eBundle);
        listOfFragments.add(frag_end);

        return listOfFragments;
    }

    public void go_to_next(FragmentSurveyQuestion question) {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void exitSurvey()
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("answers", Answers.getInstance().get_json_object());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        if(displayedFragment != null)
        {
            displayedFragment.onFragmentHidden();
        }
        FragmentSurveyQuestion fragmentSurveyQuestion = (FragmentSurveyQuestion) listOfFragments.get(position);
        fragmentSurveyQuestion.onFragmentDisplayed();
        displayedFragment = fragmentSurveyQuestion;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setImage(Bitmap image)
    {
        displayedFragment.setImage(image);
    }
}
