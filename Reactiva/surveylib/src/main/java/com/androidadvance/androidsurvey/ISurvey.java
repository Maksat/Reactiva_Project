package com.androidadvance.androidsurvey;

import com.androidadvance.androidsurvey.fragment.FragmentEnd;
import com.androidadvance.androidsurvey.fragment.FragmentSurveyQuestion;

/**
 * Created by maksat on 11/18/17.
 */

public interface ISurvey {

    /*
    Invoked by a Question Fragment, to go to next question
     */
    void go_to_next(FragmentSurveyQuestion question);

    /*
    Invoked by the last page of the survey, when survey is completed
     */
    void event_survey_completed(Answers instance, FragmentEnd fragmentEnd);
}
