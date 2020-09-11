package com.example.studentplanner;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class JSONParser {
    static InputStream inputStream = null;
    static JSONObject jsonObj = null;
    static String response = "";

    //    Constructor
    public JSONParser(){

    }

    public String makeHttpRequest(String requestUrl, String method, HashMap<String, String> params){

        URL url;

        try{
            url = new URL(requestUrl);


//            Connecting to API and passing some parameters
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(15000);
            con.setConnectTimeout(15000);
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);

//            Getting response stream from the server/API
            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, StandardCharsets.UTF_8));

            writer.write(getPostDataString(params));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = con.getResponseCode();

            //If response code is success

            if (responseCode == HttpsURLConnection.HTTP_OK){
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                //REad while we have something in input stream
                while ((line = br.readLine()) != null){
                    response += line;
                }
            }
            else{
                //If no success code then no reponse
                response = "";
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()){
            if (first){
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                first = false;
            }
            else {

                //Here we are building a query string for the HTTP request
                result.append("&");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

        }

        return result.toString();

    }
}
