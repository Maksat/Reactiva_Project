package com.androidadvance.androidsurvey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by maksat on 11/25/17.
 */

public class Emotion implements Serializable {
    @SerializedName("i")
    @Expose
    private int emotionId;
    @SerializedName("s")
    @Expose
    private float score;
    @SerializedName("t")
    @Expose
    private Date time;
    @SerializedName("v")
    @Expose
    private float valence;
    @SerializedName("a")
    @Expose
    private float arousal;
    @SerializedName("d")
    @Expose
    private float disgust;
    @SerializedName("f")
    @Expose
    private float fear;
    @SerializedName("j")
    @Expose
    private float joy;
    @SerializedName("sad")
    @Expose
    private float sadness;
    @SerializedName("surp")
    @Expose
    private float surprise;
    @SerializedName("cont")
    @Expose
    private float contempt;

    public int getEmotionId() {
        return emotionId;
    }

    public void setEmotionId(int emotionId) {
        this.emotionId = emotionId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public float getValence() {
        return valence;
    }

    public void setValence(float valence) {
        this.valence = valence;
    }

    public float getArousal() {
        return arousal;
    }

    public void setArousal(float arousal) {
        this.arousal = arousal;
    }

    public float getDisgust() {
        return disgust;
    }

    public void setDisgust(float disgust) {
        this.disgust = disgust;
    }

    public float getFear() {
        return fear;
    }

    public void setFear(float fear) {
        this.fear = fear;
    }

    public float getJoy() {
        return joy;
    }

    public void setJoy(float joy) {
        this.joy = joy;
    }

    public float getSadness() {
        return sadness;
    }

    public void setSadness(float sadness) {
        this.sadness = sadness;
    }

    public float getSurprise() {
        return surprise;
    }

    public void setSurprise(float surprise) {
        this.surprise = surprise;
    }

    public float getContempt() {
        return contempt;
    }

    public void setContempt(float contempt) {
        this.contempt = contempt;
    }

    /*
    emotion.setArousal(score);
                        }else if(metric == MetricsManager.Emotions.DISGUST)
                        {
                            emotion.setDisgust(score);
                        }else if(metric == MetricsManager.Emotions.FEAR)
                        {
                            emotion.setFear(score);
                        }else if(metric == MetricsManager.Emotions.JOY)
                        {
                            emotion.setJoy(score);
                        }else if(metric == MetricsManager.Emotions.SADNESS)
                        {
                            emotion.setSadness(score);
                        }else if(metric == MetricsManager.Emotions.SURPRISE)
                        {
                            emotion.setSurprise(score);
                        }else if(metric == MetricsManager.Emotions.CONTEMPT)
                        {
                            emotion.setContempt(score);
     */
}
