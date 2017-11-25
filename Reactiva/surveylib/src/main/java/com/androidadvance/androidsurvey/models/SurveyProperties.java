package com.androidadvance.androidsurvey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SurveyProperties implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("intro_message")
    @Expose
    private String introMessage;
    @SerializedName("intro_image")
    @Expose
    private String introImage;
    @SerializedName("intro_video")
    @Expose
    private String introVideo;
    @SerializedName("end_message")
    @Expose
    private String endMessage;
    @SerializedName("skip_intro")
    @Expose
    private Boolean skipIntro;

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The introMessage
     */
    public String getIntroMessage() {
        return introMessage;
    }

    /**
     * @param introMessage The intro_message
     */
    public void setIntroMessage(String introMessage) {
        this.introMessage = introMessage;
    }

    /**
     * @return The endMessage
     */
    public String getEndMessage() {
        return endMessage;
    }

    /**
     * @param endMessage The end_message
     */
    public void setEndMessage(String endMessage) {
        this.endMessage = endMessage;
    }

    /**
     * @return The skipIntro
     */
    public Boolean getSkipIntro() {
        return skipIntro;
    }

    /**
     * @param skipIntro The skip_intro
     */
    public void setSkipIntro(Boolean skipIntro) {
        this.skipIntro = skipIntro;
    }


    /**
     * @return The name of the image file to display on the intro page
     */
    public String getIntroImage() {
        return introImage;
    }

    /**
     * @param introImage The name of the image file to display on the intro page
     */
    public void setIntroImage(String introImage) {
        this.introImage = introImage;
    }

    /**
     * @return The name of the video file to display on the intro page
     */
    public String getIntroVideo() {
        return introVideo;
    }

    /**
     * @param introVideo The name of the video file to display on the intro page
     */
    public void setIntroVideo(String introVideo) {
        this.introVideo = introVideo;
    }
}