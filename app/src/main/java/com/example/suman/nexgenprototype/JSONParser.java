package com.example.suman.nexgenprototype;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Suman on 22-06-2017.
 */

public class JSONParser {
    static InputStreamReader in = null;
    static JSONObject jObj = null;
    static String json = "";

    private final String TAG = this.getClass().getSimpleName();

    // constructor
    public JSONParser() {

    }

    public JSONObject getJSONFromUrl(String url) {

        HttpURLConnection conn = null;
        // Making HTTP request
        try {
            // defaultHttpClient
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            try {
                in = new InputStreamReader(conn.getInputStream());
                BufferedReader reader = new BufferedReader(in);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "n");
                }
                json = sb.toString();
                Log.e(TAG, json);
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            in.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }


        // return JSON String
        return jObj;

    }
}
