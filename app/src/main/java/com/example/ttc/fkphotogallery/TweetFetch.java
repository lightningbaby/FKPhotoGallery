package com.example.ttc.fkphotogallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ttc on 2017/3/9.
 */

public class TweetFetch {

    private static final String TAG = "FlickrFetchr";
    private static final String ACCESS_TOKEN = "74dbedca-3933-4597-902a-3c39ddd6910f";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url=new URL(urlSpec);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            InputStream in=connection.getInputStream();

            if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();

        }finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<Tweet> fetchItems(String s ){

        List<Tweet> items = new ArrayList<>();

        try {
            String url= Uri.parse("https://www.oschina.net/action/openapi/tweet_pub")
                    .buildUpon()
                    .appendQueryParameter("access_token",ACCESS_TOKEN)
                    .appendQueryParameter("msg", s)
                    .appendQueryParameter("img", "false")
                    .build().toString();
            String jsonString=getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);

            //parse the json result
            JSONObject jsonBody=new JSONObject(jsonString);
            parseItems(items, jsonBody);//调用解析函数
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch items", e);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse JSON", e);
        }
        return items;
    }

    private void parseItems(List<Tweet> tweets, JSONObject jsonBody) throws JSONException {

        //先提出photos，在结果上提出photo数组
//
        Tweet tweet = new Tweet();
        tweet.setAuthor(jsonBody.getString("error_description"));
        tweet.setTweetBody(jsonBody.getString("error"));
        tweets.add(tweet);

//        JSONArray tweetsJsonArray = jsonBody.getJSONArray("projectlist");

//        //取出photo数组中的object，实例一个galleryitem 存起来，填好ID，caption，如果有url,也村上
//        for (int i = 0; i < tweetsJsonArray.length(); i++) {
//            JSONObject tweetJsonObject= tweetsJsonArray.getJSONObject(i);
//            Tweet tweet = new Tweet();
////            tweet.setPortraitUrl(tweetJsonObject.getString("url"));
//            tweet.setAuthor(tweetJsonObject.getString("name"));
//            tweet.setTweetBody(tweetJsonObject.getString("description"));
//            tweets.add(tweet);
//        }
    }
}
