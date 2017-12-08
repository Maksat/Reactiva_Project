package com.androidadvance.androidsurvey.models;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by maksat on 11/25/17.
 */

public class Answer implements Serializable {

    @SerializedName("time_start")
    @Expose
    private Date timeStart;
    @SerializedName("time_end")
    @Expose
    private Date timeEnd;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("answer_strings")
    @Expose
    private ArrayList<String> answerStrings;

    private transient Bitmap screenshotImage;

    public Answer()
    {
        answerStrings = new ArrayList<>();
    }

    public void addAnswerString(String answerString)
    {
        answerStrings.add(answerString);
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public ArrayList<String> getAnswerStrings() {
        return answerStrings;
    }

    public void clearAnswerStrings()
    {
        answerStrings.clear();
    }


    public Bitmap getScreenshotImage() {
        return screenshotImage;
    }

    public void setScreenshotImage(Bitmap screenshotImage) {
        this.screenshotImage = screenshotImage;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
