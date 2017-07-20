package com.example.yuanzhan.monashff.APIMethods;


import com.example.yuanzhan.monashff.Constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by YuanZhan on 22/04/2017.
 */

public class APIMethods {

    // task1a, using OpenWeatherMap API to get the weather info. of given location in the format of JSON
    // the invocation of this method will be added next to get the current temperature and displayed in the home page
    public static String getTemp(String latitude, String longitude) {
//    public static String getTemp() {
        String urlStr = Constants.OpenWeatherMap_URL + "lat=" + latitude + "&lon=" + longitude + "&units=metric&appid=" + Constants.OpenWeatherMap_Key;
        String textResult = "";

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Scanner reader = new Scanner(conn.getInputStream());

            while (reader.hasNextLine()) {
                textResult += reader.nextLine();
            }

            JSONObject json = new JSONObject(textResult);
            String temp = json.getJSONObject("main").getDouble("temp") + "";
            return temp;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "no1";
        } catch (IOException e) {
            e.printStackTrace();
            return "no2";
        } catch (JSONException e) {
            e.printStackTrace();
            return "no3";
        }
    }


    // get the movie description by google customer search engine
    // this method is going to be invoked in task 4c
    public static String getMovieDesc(String movieName) {
        String urlStr = Constants.MovieCustomSearch_URL_Key_SearchID + movieName;
        String textResult = "";

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Scanner reader = new Scanner(conn.getInputStream());

            while (reader.hasNextLine()) {
                textResult += reader.nextLine();
            }

            JSONObject json = new JSONObject(textResult);
            JSONArray jsonArray = json.getJSONArray("items");

            String snippet = "";
            if(jsonArray != null && jsonArray.length() > 0) {
                snippet = jsonArray.getJSONObject(0).getString("snippet");
            }
            return snippet;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "moveno1 error";
        } catch (IOException e) {
            e.printStackTrace();
            return "moveno2 error";
        } catch (JSONException e) {
            e.printStackTrace();
            return "moveno3 error";
        }
    }

    // get the pic src by google customer search engine
    // this method is going to be invoked in task 4c as well
    public static String getMoviePicSrc(String movieName) {
        String urlStr = Constants.MovieCustomSearch_URL_Key_SearchID + movieName;
        String textResult = "";

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Scanner reader = new Scanner(conn.getInputStream());

            while (reader.hasNextLine()) {
                textResult += reader.nextLine();
            }

            JSONObject json = new JSONObject(textResult);
            // cause to the structure of JSON responding from www.imdb.com
            String imgURL = json.getJSONArray("items").getJSONObject(0).getJSONObject("pagemap")
                    .getJSONArray("movie").getJSONObject(0).getString("image");

            System.out.println(imgURL);

            return imgURL;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "moveno1 error";
        } catch (IOException e) {
            e.printStackTrace();
            return "moveno2 error";
        } catch (JSONException e) {
            e.printStackTrace();
            return "moveno3 error";
        }
    }



    public static String getAddress(double lat, double lon){
        String urlStr = Constants.FIND_ADDRESS_BY_LATLNG + lat + "," + lon;
        String textResult = "";

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Scanner reader = new Scanner(conn.getInputStream());

            while (reader.hasNextLine()) {
                textResult += reader.nextLine();
            }

            JSONObject json = new JSONObject(textResult);
            // cause to the structure of JSON responding from www.imdb.com
            String addressName = json.getJSONArray("items").getJSONObject(0).getJSONObject("pagemap")
                    .getJSONArray("movie").getJSONObject(0).getString("image");

            System.out.println(addressName);

            return addressName;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "addressName1 error";
        } catch (IOException e) {
            e.printStackTrace();
            return "addressName2 error";
        } catch (JSONException e) {
            e.printStackTrace();
            return "addressName3 error";
        }
    }





}
