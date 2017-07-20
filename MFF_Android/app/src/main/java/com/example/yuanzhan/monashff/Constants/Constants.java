package com.example.yuanzhan.monashff.Constants;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YuanZhan on 22/04/2017.
 */

public class Constants {
    public static final String OpenWeatherMap_URL = "http://api.openweathermap.org/data/2.5/weather?";
    public static final String OpenWeatherMap_Key = "226238a589dc062ef84c21f1241ec21b";
    public static final String MovieCustomSearch_URL_Key_SearchID
            = "https://www.googleapis.com/customsearch/v1?key=AIzaSyAt40IByIyLNws7YAgyrjZEsqcFAuaoq9Y&cx=009360026477565911717:w831iruhjyq&q=";

    public static final String FIND_ADDRESS_BY_LATLNG = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";

    public static final String BASE_URI = "http://118.139.6.215:8080/MonashFFWS/webresources/monashffpkg.";
    public static final String GET_METHOD = "GET";
    public static final String PUT_METHOD = "PUT";
    public static final String POST_METHOD = "POST";
    public static final String DELETE_METHOD = "DELETE";

    public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HTTP_HEADER_JSON_FORMATE = "application/json";
    public static final String HTTP_HEADER_ACCEPT = "Accept";

    public static final String CONFIG_PREF = "configPref";

    // course array
    public static final String[] COURSES = {"MIT", "MDS", "MNS", "MBIS"};
    public static final String[] MODE = {"Part Time", "Full Time"};
    public static final String[] SUBURB = {"Chadstone", "Clayton", "Caulfield", "South Yarra"};
    public static final String[] FAV_SPORT = {"Football", "Basketball", "Badminton", "Tennis"};
    public static final String[] FAV_UNIT = {"FIT5042", "FIT5032", "FIT5046", "FIT5148", "FIT5057", "FIT5170", "FIT5211"};

    public static final String[] COURSES_UP = {"<Choose>", "MIT", "MDS", "MNS", "MBIS"};
    public static final String[] MODE_UP = {"<Choose>", "Part Time", "Full Time"};
    public static final String[] SUBURB_UP = {"<Choose>", "Chadstone", "Clayton", "Caulfield", "South Yarra"};
    public static final String[] FAV_SPORT_UP = {"<Choose>", "Football", "Basketball", "Badminton", "Tennis"};
    public static final String[] FAV_UNIT_UP = {"<Choose>", "FIT5042", "FIT5032", "FIT5046", "FIT5148", "FIT5057", "FIT5170", "FIT5211"};
    // distance to choose
    public static final String[] DISTANCE = {"<Choose>", "1 km", "2 km", "5 km","All"};

    // criteria array
    public static final String[] CRITERIAS = {"suburb", "nationality","language","sport","movie","unit","job"};

    // check if email is email
    public static boolean checkIfisEmailAddr(String addr){
        Pattern testPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher teststring = testPattern.matcher(addr);
        return teststring.matches();
    }



    // get hashed pwd
    public static String generateSecurePassword(String originalPassword) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // Add password bytes to digest
            messageDigest.update(originalPassword.getBytes());
            // Get the hash's bytes
            byte[] bytes = messageDigest.digest();
            // This bytes[] has bytes in decimal format;
            // Convert it to hexadecimal format
            StringBuilder stringBuilder = new StringBuilder();
            for (byte aByte : bytes) {
                stringBuilder.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
