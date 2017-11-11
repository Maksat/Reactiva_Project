package com.nanxi.reactiva;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by maksat on 10/24/17.
 */

public class ServerHelper {

    public static void postData(final int emotionId, final double confidence, final int imageId)
    {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    HashMap values = new HashMap<String, String>();
                    values.put("emotionId", new Integer(emotionId).toString());
                    values.put("confidence", new Double(confidence).toString());
                    values.put("image", new Integer(imageId).toString());

                    String urlParameters  = getDataString(values);

                    byte[] postData = urlParameters.getBytes( Charset.forName("UTF-8") );
                    int postDataLength = postData.length;
                    String request = "http://yurttaslama.com";
                    URL url = new URL( request );
                    HttpURLConnection client= (HttpURLConnection) url.openConnection();
                    client.setDoOutput(true);
                    client.setInstanceFollowRedirects(false);
                    client.setRequestMethod("POST");
                    client.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    client.setRequestProperty("charset", "utf-8");
                    client.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
                    client.setUseCaches(false);

                    DataOutputStream wr = new DataOutputStream(client.getOutputStream());
                    wr.write( postData );

                    InputStream inputStream = new BufferedInputStream(client.getInputStream());
                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    Log.i("response", response.toString());
                    client.disconnect();
                }catch(Exception e)
                {
                    e.printStackTrace();

                    //@TODO: display message that connection was unsuccessful
                }
            }
        });

        thread.start();
    }

    private static String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
