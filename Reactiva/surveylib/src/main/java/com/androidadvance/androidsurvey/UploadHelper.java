package com.androidadvance.androidsurvey;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by maksat on 11/25/17.
 */

public class UploadHelper extends AsyncTask<String, String, String> {
    private static final String TAG ="UPLOADFILE";
    private PostUpload callback;
    private String uploadLocation;
    private String uploadImageUrl;
    private String bitmapFileName;
    private Bitmap imageBitmap;
    private String jsonString;
//    private TextView textView;

    public UploadHelper(String uploadLocation, String jsonString,  PostUpload callback){
        this.callback = callback;
        this.uploadLocation = uploadLocation;
        this.jsonString = jsonString;
    }

    public UploadHelper(Bitmap imageBitmap, String uploadImageUrl, String bitmapFileName,  PostUpload callback){
        this.callback = callback;
        this.imageBitmap = imageBitmap;
        this.uploadImageUrl = uploadImageUrl;
        this.bitmapFileName = bitmapFileName;
    }

    private void uploadImage()
    {
        Log.i(TAG, "Uploading image");
        try{

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            //upload image
            URL uploadUrl = new URL(this.uploadImageUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) uploadUrl.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            urlConnection.setRequestProperty("bill", bitmapFileName);


            urlConnection.setDoOutput(true);
            urlConnection.connect();
            OutputStream output = urlConnection.getOutputStream();
            DataOutputStream dos = new DataOutputStream(output);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                    + bitmapFileName + "\"" + lineEnd);

            dos.writeBytes(lineEnd);

            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, output);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            dos.flush();
            dos.close();

            StringBuilder sb = new StringBuilder();
            int HttpResult = urlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                br.close();
                System.out.println("" + sb.toString());
            } else {
                System.out.println(urlConnection.getResponseMessage());
            }

            this.imageBitmap.recycle();
            this.imageBitmap = null;
        } catch (IOException e) {
            //TODO: add message for user
            Log.e(TAG, e.getMessage());
        }
    }

    private void uploadJson()
    {
        URL uploadUrl = null;
        try {
            uploadUrl = new URL(this.uploadLocation);
        } catch (MalformedURLException e) {
            //TODO: add message for user
            Log.e(TAG, e.getMessage());
        }

        try {
            // 1. create HttpClient
            HttpURLConnection urlConnection = (HttpURLConnection) uploadUrl.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(this.jsonString.getBytes("UTF-8"));
            publishProgress("Upload completed");
            os.flush();
            os.close();

            StringBuilder sb = new StringBuilder();
            int HttpResult = urlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                br.close();
                System.out.println("" + sb.toString());
            } else {
                System.out.println(urlConnection.getResponseMessage());
            }
            urlConnection.disconnect();
        } catch (IOException e) {
            //TODO: add message for user
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     *
     * @param strings First member of the array is the address, where to upload the JSON request, second member is the JSON text
     * @return
     */
    @Override
    protected String doInBackground(String... strings) {

        if(this.imageBitmap != null)
        {
           uploadImage();

        }else {
           uploadJson();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

//        textView.setText("Uploaded "+values);

    }

//    public void setTextView(TextView textView) {
//        this.textView = textView;
//    }

    public static interface PostUpload{
        void uploadDone();
    }
}
