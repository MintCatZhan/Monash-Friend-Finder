package com.example.yuanzhan.monashff.RESTClients;

import android.util.Log;

import com.example.yuanzhan.monashff.Constants.Constants;
import com.example.yuanzhan.monashff.EntityRepository.Student;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by YuanZhan on 22/04/2017.
 */

public class RestClient {

    public static long getMaxStdId() {
        long maxStdId = getMaxID();
        System.out.println("ididididid" + maxStdId);
        return maxStdId;
    }

    // check if the email is unique
    public static boolean checkIfEmailUnique(String email) {
        if (RestClient.getResultStr("student", "/findByEmailAddr", "/" + email).length() != 0) {
            return false;
        } else {
            return true;
        }
    }

    public static long getMaxID() {
        // the searchMethod should be "/findByStdId", the parameter should be "/26693267"
        // the "/" is required
        // e.g. --> location + /findByStdId + /26693267
        String methodPath = "student/count";
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        long id = 0;
        try {
            url = new URL(Constants.BASE_URI + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(Constants.GET_METHOD);
            conn.setRequestProperty(Constants.HTTP_HEADER_CONTENT_TYPE, "text/plain");
            conn.setRequestProperty(Constants.HTTP_HEADER_ACCEPT, "text/plain");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine()) {
                textResult = inStream.nextLine();
            }
            id = Long.parseLong(textResult);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return id;
    }

    public static JSONArray getResultStr(String entityName, String searchMethod, String parameter) {
        // the searchMethod should be "/findByStdId", the parameter should be "/26693267"
        // the "/" is required
        // e.g. --> location + /findByStdId + /26693267
        String methodPath = entityName + searchMethod + parameter;

        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        JSONArray json = null;
        try {
            url = new URL(Constants.BASE_URI + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(Constants.GET_METHOD);
            conn.setRequestProperty(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.HTTP_HEADER_JSON_FORMATE);
            conn.setRequestProperty(Constants.HTTP_HEADER_ACCEPT, Constants.HTTP_HEADER_JSON_FORMATE);
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
            json = new JSONArray(textResult);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return json;
    }

    public static boolean putStudent(Student student) { // update a student by given student
        final String methodPath = "student" + "/" + student.getStdId();

        URL url = null;
        HttpURLConnection conn = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

            String stringStudentJson = "{" +
                    "\"address\":\"" + student.getAddress() + "\"," +
                    "\"course\":\"" + student.getCourse() + "\"," +
                    "\"currentJob\":\"" + student.getCurrentJob() + "\"," +
                    "\"dob\":\"" + sdf.format(student.getDob()) + "\"," +
                    "\"emailAddr\":\"" + student.getEmailAddr() + "\"," +
                    "\"favouriteMovie\":\"" + student.getFavouriteMovie() + "\"," +
                    "\"favouriteSport\":\"" + student.getFavouriteSport() + "\"," +
                    "\"favouriteUnit\":\"" + student.getFavouriteUnit() + "\"," +
                    "\"fname\":\"" + student.getFname() + "\"," +
                    "\"gender\":" + student.getGender() + "," +
                    "\"lang\":\"" + student.getLang() + "\"," +
                    "\"lname\":\"" + student.getLname() + "\"," +
                    "\"mode\":" + student.getMode() + "," +
                    "\"nationality\":\"" + student.getNationality() + "\"," +
                    "\"pwd\":\"" + student.getPwd() + "\"," +
                    "\"stdId\":" + student.getStdId() + "," +
                    "\"subscriptDatetime\":\"" + sdf.format(student.getSubscriptDatetime()) + "\"," +
                    "\"suburb\":\"" + student.getSuburb() + "\"" +
                    "}";
            System.out.println(stringStudentJson);
            url = new URL(Constants.BASE_URI + methodPath);

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(Constants.PUT_METHOD);
            //
            conn.setDoOutput(true);
            //
            conn.setFixedLengthStreamingMode(stringStudentJson.getBytes().length);
            conn.setRequestProperty(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.HTTP_HEADER_JSON_FORMATE);

            // send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(stringStudentJson);
            out.close();
            Log.i("Error", new Integer(conn.getResponseCode()).toString());
            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return false;
    }


    public static boolean postFriendship(Student student, JSONObject friend) {
        final String methodPath = "friendship";

        URL url = null;
        HttpURLConnection conn = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            Date currDateTime = new Date();


            String fdshipPKJson = "{\"friendshipPK\":{\"friendId\":" + friend.getLong("stdId") + ",\"stdId\":" + student.getStdId() + "},";
            String fdshipDateJson = "\"stratingDate\":\"" + sdf.format(currDateTime) + "\",";

            String stringFriendJson = friend.toString();

            String stringStudentJson = "\"student1\":{" +
                    "\"address\":\"" + student.getAddress() + "\"," +
                    "\"course\":\"" + student.getCourse() + "\"," +
                    "\"currentJob\":\"" + student.getCurrentJob() + "\"," +
                    "\"dob\":\"" + sdf.format(student.getDob()) + "\"," +
                    "\"emailAddr\":\"" + student.getEmailAddr() + "\"," +
                    "\"favouriteMovie\":\"" + student.getFavouriteMovie() + "\"," +
                    "\"favouriteSport\":\"" + student.getFavouriteSport() + "\"," +
                    "\"favouriteUnit\":\"" + student.getFavouriteUnit() + "\"," +
                    "\"fname\":\"" + student.getFname() + "\"," +
                    "\"gender\":" + student.getGender() + "," +
                    "\"lang\":\"" + student.getLang() + "\"," +
                    "\"lname\":\"" + student.getLname() + "\"," +
                    "\"mode\":" + student.getMode() + "," +
                    "\"nationality\":\"" + student.getNationality() + "\"," +
                    "\"pwd\":\"" + student.getPwd() + "\"," +
                    "\"stdId\":" + student.getStdId() + "," +
                    "\"subscriptDatetime\":\"" + sdf.format(student.getSubscriptDatetime()) + "\"," +
                    "\"suburb\":\"" + student.getSuburb() + "\"" +
                    "}}";

            String wholeJson = fdshipPKJson + fdshipDateJson + "\"student\":" + stringFriendJson + "," + stringStudentJson;


            System.out.println(wholeJson);
            url = new URL(Constants.BASE_URI + methodPath);

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(Constants.POST_METHOD);
            //
            conn.setDoOutput(true);
            //
            conn.setFixedLengthStreamingMode(wholeJson.getBytes().length);
            conn.setRequestProperty(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.HTTP_HEADER_JSON_FORMATE);

            // send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(wholeJson);
            out.close();
            Log.i("Error", new Integer(conn.getResponseCode()).toString());
            if (new Integer(conn.getResponseCode()) == 500) {
                return false;
            } else {
                return true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return false;
    }


    public static boolean postStudent(Student student) {
        final String methodPath = "student";

        URL url = null;
        HttpURLConnection conn = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

            String stringStudentJson = "{" +
                    "\"address\":\"" + student.getAddress() + "\"," +
                    "\"course\":\"" + student.getCourse() + "\"," +
                    "\"currentJob\":\"" + student.getCurrentJob() + "\"," +
                    "\"dob\":\"" + sdf.format(student.getDob()) + "\"," +
                    "\"emailAddr\":\"" + student.getEmailAddr() + "\"," +
                    "\"favouriteMovie\":\"" + student.getFavouriteMovie() + "\"," +
                    "\"favouriteSport\":\"" + student.getFavouriteSport() + "\"," +
                    "\"favouriteUnit\":\"" + student.getFavouriteUnit() + "\"," +
                    "\"fname\":\"" + student.getFname() + "\"," +
                    "\"gender\":" + student.getGender() + "," +
                    "\"lang\":\"" + student.getLang() + "\"," +
                    "\"lname\":\"" + student.getLname() + "\"," +
                    "\"mode\":" + student.getMode() + "," +
                    "\"nationality\":\"" + student.getNationality() + "\"," +
                    "\"pwd\":\"" + student.getPwd() + "\"," +
                    "\"stdId\":" + student.getStdId() + "," +
                    "\"subscriptDatetime\":\"" + sdf.format(student.getSubscriptDatetime()) + "\"," +
                    "\"suburb\":\"" + student.getSuburb() + "\"" +
                    "}";
            System.out.println(stringStudentJson);
            url = new URL(Constants.BASE_URI + methodPath);

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(Constants.POST_METHOD);
            //
            conn.setDoOutput(true);
            //
            conn.setFixedLengthStreamingMode(stringStudentJson.getBytes().length);
            conn.setRequestProperty(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.HTTP_HEADER_JSON_FORMATE);

            // send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(stringStudentJson);
            out.close();
            Log.i("Error", new Integer(conn.getResponseCode()).toString());
            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return false;
    }


    public static boolean deleteFriend(long stdID, long friendID) {
        final String methodPath = "friendship/friendshipPK;stdId=" + stdID + ";friendId=" + friendID;
        URL url = null;
        HttpURLConnection conn = null;
        String txtResult = "";
        // Making HTTP request
        try {
            url = new URL(Constants.BASE_URI + methodPath);
            System.out.println(Constants.BASE_URI + methodPath);
//open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the connection method to DELETE
            conn.setRequestMethod(Constants.DELETE_METHOD);
            Log.i("error", new Integer(conn.getResponseCode()).toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return false;
    }


    // get a student's latest locaiton
    public static JSONObject getLatestLocation(long stdId) {
        JSONArray allLocs = getResultStr("location", "/findByStdId", "/" + stdId);
//
//
        if (allLocs == null || allLocs.length() == 0) {
            return null;
        } else if (allLocs.length() == 1) {
            try {
                return allLocs.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                JSONArray sortedJsonArray = new JSONArray();
                List<JSONObject> jsonList = new ArrayList<JSONObject>();
                for (int i = 0; i < allLocs.length(); i++) {
                    jsonList.add(allLocs.getJSONObject(i));
                }

                Collections.sort(jsonList, new Comparator<JSONObject>() {

                    public int compare(JSONObject a, JSONObject b) {
                        String valA = new String();
                        String valB = new String();

                        try {
                            valA = a.getJSONObject("locationPK").getString("dateTime");
                            valB = b.getJSONObject("locationPK").getString("dateTime");
                        } catch (JSONException e) {
                        }

                        return valA.compareTo(valB);
                    }
                });

                for (int i = 0; i < allLocs.length(); i++) {
                    sortedJsonArray.put(jsonList.get(i));
                }

                return sortedJsonArray.getJSONObject(0);


            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
    }


}





















