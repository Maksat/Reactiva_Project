package com.androidadvance.androidsurvey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.androidadvance.androidsurvey.adapters.AdapterFragmentQ;
import com.androidadvance.androidsurvey.fragment.FragmentCheckboxes;
import com.androidadvance.androidsurvey.fragment.FragmentEnd;
import com.androidadvance.androidsurvey.fragment.FragmentMultiline;
import com.androidadvance.androidsurvey.fragment.FragmentNumber;
import com.androidadvance.androidsurvey.fragment.FragmentRadioboxes;
import com.androidadvance.androidsurvey.fragment.FragmentStart;
import com.androidadvance.androidsurvey.fragment.FragmentTextSimple;
import com.androidadvance.androidsurvey.models.Question;
import com.androidadvance.androidsurvey.models.SurveyPojo;
import com.androidadvance.androidsurvey.models.SurveyProperties;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class SurveyActivity extends FragmentActivity implements ISurvey {

    private SurveyPojo mSurveyPojo;
    private ViewPager mPager;
    private String style_string = null;
    private String jsonSurvey;

    protected void initializePager()
    {
        jsonSurvey = loadSurveyJson("survey.json");
        mSurveyPojo = new Gson().fromJson(jsonSurvey, SurveyPojo.class);

        Log.i("json Object = ", String.valueOf(mSurveyPojo.getQuestions()));

        final ArrayList<Fragment> listOfFragments = createListOfFragments(style_string, mSurveyPojo.getSurveyProperties());

        mPager = (ViewPager) findViewById(R.id.pager);
        AdapterFragmentQ mPagerAdapter = new AdapterFragmentQ(getSupportFragmentManager(), listOfFragments);
        mPager.setAdapter(mPagerAdapter);
    }

    //json stored in the assets folder. but you can get it from wherever you like.
    private String loadSurveyJson(String filename) {
        try {
            java.io.InputStream is = getAssets().open(filename);
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

    private ArrayList<Fragment> createListOfFragments(String styleString, SurveyProperties surveyProperties)
    {
        ArrayList<Fragment> arraylist_fragments = new ArrayList<>();

        //- START -
        if (!surveyProperties.getSkipIntro()) {
            FragmentStart frag_start = new FragmentStart();
            Bundle sBundle = new Bundle();
            sBundle.putSerializable("survery_properties", surveyProperties);
            sBundle.putString("style", styleString);
            frag_start.setArguments(sBundle);
            arraylist_fragments.add(frag_start);
        }

        //- FILL -
        for (Question mQuestion : mSurveyPojo.getQuestions()) {
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
                default:
                    continue;
            }

            frag.setArguments(xBundle);
            arraylist_fragments.add(frag);
        }

        //- END -
        FragmentEnd frag_end = new FragmentEnd();
        Bundle eBundle = new Bundle();
        eBundle.putSerializable("survery_properties", surveyProperties);
        eBundle.putString("style", styleString);
        frag_end.setArguments(eBundle);
        arraylist_fragments.add(frag_end);

        return arraylist_fragments;
    }

    public void go_to_next() {
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

    public void event_survey_completed(Answers instance) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("answers", instance.get_json_object());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
