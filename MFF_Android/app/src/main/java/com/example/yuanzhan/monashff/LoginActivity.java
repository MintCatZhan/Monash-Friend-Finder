package com.example.yuanzhan.monashff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yuanzhan.monashff.Constants.Constants;
import com.example.yuanzhan.monashff.EntityRepository.Student;
import com.example.yuanzhan.monashff.MainActivityRepository.NavigationDrawerActivity;
import com.example.yuanzhan.monashff.RESTClients.RestClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // get the info. input by user
        final EditText emailET = (EditText) findViewById(R.id.emailForLoginET);
        final EditText pwdET = (EditText) findViewById(R.id.pwdForLoginET);


        // change the font color
        Button colorBtn = (Button) findViewById(R.id.fontColorChgBtn);
        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for testing, simply change the color to blue
                modifyFontColor("#e10808");
            }
        });

        // jump to subscription page
        Button subBtn = (Button) findViewById(R.id.subscribBtn);
        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SubscriptionActivity.class);
                startActivity(intent);
            }
        });


        // for skip login
        Button skipLoginBtn = (Button) findViewById(R.id.skipLoginBtn);
        skipLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Student>() {
                    @Override
                    protected Student doInBackground(Void... params) {
                        // need to get the longitude and latitude of the user
                        String skipEmail = "default@gmail.com";
                        String skipPwd = "pwd";
                        if (loginCheck(skipEmail, Constants.generateSecurePassword(skipPwd))) {
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
                            try {
                                Student student = gson.fromJson(
                                        RestClient.getResultStr("student", "/findByEmailAddr", "/" + skipEmail).getJSONObject(0).toString(),
                                        Student.class);
                                return student;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(Student student) {
                        if (student != null) {
                            Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putParcelable("loggedStd", student);
                            intent.putExtras(mBundle);
                            startActivity(intent);
                        } else {
                        }
                    }
                }.execute();
            }
        });


        // jump to homepage
        Button loginBtn = (Button) findViewById(R.id.loginBtn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailET.getText().toString();
                final String pwd = pwdET.getText().toString();


                new AsyncTask<Void, Void, Student>() {
                    @Override
                    protected Student doInBackground(Void... params) {
                        // need to get the longitude and latitude of the user
                        if (loginCheck(email, Constants.generateSecurePassword(pwd))) {
//                            Gson gson = new GsonBuilder().create();
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
                            try {
                                Student student = gson.fromJson(
                                        RestClient.getResultStr("student", "/findByEmailAddr", "/" + email).getJSONObject(0).toString(),
                                        Student.class);
                                return student;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(Student student) {
                        if (student != null) {
                            Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putParcelable("loggedStd", student);
                            intent.putExtras(mBundle);
                            startActivity(intent);
                        } else {
                            // error message
                            pwdET.setError("Wrong Email or Password");
                        }
                    }
                }.execute();
            }
        });
    }

    public Boolean loginCheck(String email, String pwd) {
        Boolean isLoggedIn = false;

        if (!email.isEmpty() && !pwd.isEmpty()) {
            System.out.println("Not empty email&pwd");
            JSONArray resultJSON = RestClient.getResultStr("student", "/findByEmailAddr", "/" + email);

            // if the email is in db
            if (resultJSON.length() > 0) {
                System.out.println("get the student");
                try {
                    // get the student
                    JSONObject std = resultJSON.getJSONObject(0);
                    // get the pwd of this student
                    String pwdStored = std.getString("pwd");
                    // if the pwd is the one that inserted by user, set flag to "TRUE"
                    if (pwd.equals(pwdStored)) {
                        isLoggedIn = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("empty email&pwd");
        }
        return isLoggedIn;
    }

    // change the font color of "REGISTER PAGE" by modifying sharedpreferences
    public void modifyFontColor(String fontColor) {

        SharedPreferences pref = getSharedPreferences(Constants.CONFIG_PREF, MODE_PRIVATE);
        // create editor, for modifying
        SharedPreferences.Editor editor = pref.edit();
        // store config info.
        editor.putString("font-color", fontColor);

        // commit
        editor.commit();
    }


    // for error message
    public void sendToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
