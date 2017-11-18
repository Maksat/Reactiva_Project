package com.androidadvance.androidsurvey;

/**
 * Created by maksat on 11/18/17.
 */

public interface ISurvey {

    /*
    Invoked by a Question Fragment, to go to next question
     */
    void go_to_next();

    /*
    Invoked by the last page of the survey, when survey is completed
     */
    void event_survey_completed(Answers instance);
}
