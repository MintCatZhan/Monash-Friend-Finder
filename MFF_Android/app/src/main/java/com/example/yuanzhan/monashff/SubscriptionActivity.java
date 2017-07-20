package com.example.yuanzhan.monashff;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuanzhan.monashff.Constants.Constants;
import com.example.yuanzhan.monashff.Constants.DBManager;
import com.example.yuanzhan.monashff.EntityRepository.Student;
import com.example.yuanzhan.monashff.MainActivityRepository.NavigationDrawerActivity;
import com.example.yuanzhan.monashff.RESTClients.RestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SubscriptionActivity extends AppCompatActivity {

    // attribute for registering a new student
    protected DBManager dbManager;


    long stdId;
    String email = "NotValidEmail";
    static TextView dateTV;
    TextView courseTV;
    TextView modeTV;
    TextView countryTV;
    TextView langTV;
    TextView favSportTV;
    TextView favUnitTV;
    EditText fnameET;
    EditText lnameET;
    EditText addrET;
    //    EditText suburbET;
    TextView suburbTV;

    EditText favMovieET;
    EditText jobET;
    EditText emailET;
    EditText originPwdET;
    EditText pwdET;
    RadioGroup genderRG;
    RadioButton maleRB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        dbManager = new DBManager(this);

        fnameET = (EditText) findViewById(R.id.fnameForSubET);
        lnameET = (EditText) findViewById(R.id.lnameForSubET);
        addrET = (EditText) findViewById(R.id.addrForSubET);
        favMovieET = (EditText) findViewById(R.id.favMovieForSubET);
        jobET = (EditText) findViewById(R.id.jobForSubET);
        emailET = (EditText) findViewById(R.id.emailForSubET);
        originPwdET = (EditText) findViewById(R.id.pwdForSubET);
        pwdET = (EditText) findViewById(R.id.rePwdForSubET);

        dateTV = (TextView) findViewById(R.id.dobForSubTV);
        courseTV = (TextView) findViewById(R.id.courseForSubTV);
        modeTV = (TextView) findViewById(R.id.modeForSubTV);
        countryTV = (TextView) findViewById(R.id.nationForSubTV);
        suburbTV = (TextView) findViewById(R.id.suburbForSubTV);
        langTV = (TextView) findViewById(R.id.langForSubTV);
        favSportTV = (TextView) findViewById(R.id.favSportForSubTV);
        favUnitTV = (TextView) findViewById(R.id.favUnitForSubTV);

        genderRG = (RadioGroup) findViewById(R.id.genderForSubRG);
        maleRB = (RadioButton) findViewById(R.id.maleBtnSub);
        genderRG.check(R.id.maleBtnSub);
        final String id = maleRB.getId() + "";


        // course spinner adapter
        // set selectedlistener to course spinner
        setSpinnerSelectedListener((Spinner) findViewById(R.id.courseForSubSpinner),
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constants.COURSES),
                courseTV);

        // mode spinner adapter
        // set selectedlistener to mode spinner
        setSpinnerSelectedListener((Spinner) findViewById(R.id.modeForSubSpinner),
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constants.MODE),
                modeTV);

        // suburb spinner adapter
        // set selectedlistener to suburb spinner
        setSpinnerSelectedListener((Spinner) findViewById(R.id.suburbForSubSpinner),
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constants.SUBURB),
                suburbTV);

        // first insert record to db
        this.insertCountry();

        // nationality spinner adapter, the data resource is the SQLite database
        // set selectedlistener to nationality spinner
        setSpinnerSelectedListener((Spinner) findViewById(R.id.nationForSubSpinner),
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.getDataArray("country")),
                countryTV);

        // language spinner adapter, the data resource is the SQLite database
        // set selectedlistener to language spinner
        setSpinnerSelectedListener((Spinner) findViewById(R.id.langForSubSpinner),
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.getDataArray("language")),
                langTV);

        // favourite sport spinner adapter
        // set selectedlistener to favourite sport spinner
        setSpinnerSelectedListener((Spinner) findViewById(R.id.favSportForSubSpinner),
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constants.FAV_SPORT),
                favSportTV);


        // favourite sport spinner adapter
        // set selectedlistener to favourite sport spinner
        setSpinnerSelectedListener((Spinner) findViewById(R.id.favUnitForSubSpinner),
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constants.FAV_UNIT),
                favUnitTV);


        // get the config by SharedPreference file
        SharedPreferences pref = getSharedPreferences(Constants.CONFIG_PREF, MODE_PRIVATE);
        String color = pref.getString("font-color", "#020202");
        // change the color back for next time testing
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("font-color", "#f3f5f5");
        editor.commit();

        TextView titleTV = (TextView) findViewById(R.id.titleOfRegstPageTV);
        titleTV.setTextColor(Color.parseColor(color));


        // set onclicklistener to cancel btn
        Button cancelRegistBtn = (Button) findViewById(R.id.cancelRegistBtn);
        cancelRegistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // set onclicklister to regist btn (Submit)
        Button submitRegistBtn = (Button) findViewById(R.id.submitRegistBtn);
        submitRegistBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // to get the id
                new AsyncTask<Void, Void, Long>() {

                    @Override
                    protected Long doInBackground(Void... params) {
                        return RestClient.getMaxStdId();
                    }

                    @Override
                    protected void onPostExecute(Long response) {
                        stdId = response;
                    }
                }.execute();

                new AsyncTask<String, Void, Student>() {

                    @Override
                    protected Student doInBackground(String... params) {

                        // 0 1 6 11 13 14 15
                        if (params[0].length() > 0) {
                            if (params[1].length() > 0) {
                                if (!params[2].equals("MM-DD-YYYY")) {
                                    if (params[6].length() > 0) {
                                        if (params[11].length() > 0) {
                                            if (params[13].length() > 0) {
                                                if (params[14].length() > 0) {
                                                    if (Constants.checkIfisEmailAddr(params[14])) {
                                                        if (params[15].length() > 0) {
                                                            if (params[15].equals(params[16])) {

                                                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                                                                // get current datetime
                                                                Date currDate = new Date();
                                                                dateFormat.format(currDate);

                                                                //convert dob format
                                                                String dateStr = params[2];
                                                                DateFormat dobFormat = new SimpleDateFormat("yyyy-MM-dd");
                                                                Date dobFormatted = null;
                                                                try {
                                                                    dobFormatted = dobFormat.parse(dateStr);
                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }

                                                                // convert gender format
                                                                System.out.println(params[3] + "<-- gender" + "id......>>>>" + id);
                                                                boolean genderFormatted = params[3].toLowerCase().equals(id);

                                                                // convert mode format
                                                                boolean modeFormatted = params[5].toLowerCase().equals("full time");

                                                                if (RestClient.checkIfEmailUnique(params[14])) {
                                                                    System.out.print(params[0]);
                                                                    Student student = new Student(stdId + 1, params[0], params[1], dobFormatted, genderFormatted,
                                                                            params[4], modeFormatted, params[6], params[7], params[8], params[9],
                                                                            params[10], params[11], params[12], params[13], params[14], Constants.generateSecurePassword(params[15]),
                                                                            currDate);

                                                                    System.out.println("Adding student -->" + student.getFname() + " " + student.getLname());
                                                                    if (RestClient.postStudent(student)) {
                                                                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
                                                                        try {
                                                                            System.out.println("currEmail-->" + params[14]);
                                                                            Student curStd = gson.fromJson(
                                                                                    RestClient.getResultStr("student", "/findByEmailAddr", "/" + params[14]).getJSONObject(0).toString(),
                                                                                    Student.class);
                                                                            return curStd;

                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                            return null;
                                                                        }
                                                                    } else {
                                                                        return null;
                                                                    }
                                                                } else {
                                                                    showError("Email is Used");
                                                                    return null;
                                                                }
                                                            }
                                                            showError("Re-pwd not equals to pwd");
                                                            return null;
                                                        }
                                                        showError("Re-pwd is Empty");
                                                        return null;
                                                    }
                                                    showError("Email Syntax not Valid");
                                                    return null;
                                                }
                                                showError("Email is Empty");
                                                return null;
                                            }
                                            showError("Job is Empty");
                                            return null;
                                        }
                                        showError("Movie is Empty");
                                        return null;
                                    }
                                    showError("Address is Empty");
                                    return null;
                                }
                                showError("DOB not chosen");
                                return null;
                            }
                            showError("Last Name is Empty");
                            return null;
                        }
                        showError("First Name is Empty");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Student curStd) {
                        if (curStd != null) {
                            sendToast("Logged in");
                            Intent intent = new Intent(SubscriptionActivity.this, NavigationDrawerActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putParcelable("loggedStd", curStd);
                            intent.putExtras(mBundle);
                            startActivity(intent);
                        }
                    }


                }.execute(fnameET.getText().toString(), lnameET.getText().toString(),
                        dateTV.getText().toString(), genderRG.getCheckedRadioButtonId() + "",
                        courseTV.getText().toString(), modeTV.getText().toString(),
                        addrET.getText().toString(), suburbTV.getText().toString(),
                        countryTV.getText().toString(), langTV.getText().toString(),
                        favSportTV.getText().toString(), favMovieET.getText().toString(),
                        favUnitTV.getText().toString(), jobET.getText().toString(),
                        emailET.getText().toString(), pwdET.getText().toString(),
                        originPwdET.getText().toString());
            }

        });

    }


    public void insertCountry() {
        try {
            dbManager.open();
            // delete all record from db
            dbManager.deleteAllCountry();
            dbManager.insertCountry("CN", "China", "Chinese");
            dbManager.insertCountry("EN", "England", "English");
            dbManager.insertCountry("AU", "Australia", "English");
            dbManager.insertCountry("IN", "India", "English");
            dbManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] getDataArray(String dataType) {

        try {
            dbManager.open();
            Cursor c = dbManager.getAllCountry();
            List<String> list = new ArrayList<>();
            int colNum = 0;
            if (dataType.equals("country")) {
                colNum = 1;
            } else if (dataType.equals("language")) {
                colNum = 2;
            }

            if (c.moveToFirst()) {
                do {
                    list.add(c.getString(colNum));
                } while (c.moveToNext());
            }
            //convert the list to array
            String[] array = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = list.get(i);
            }
            dbManager.close();
            return array;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    // this method is used to set onSelectedLister to spinner
    public void setSpinnerSelectedListener(Spinner dropdownPara, ArrayAdapter<String> adapterPara, TextView tvPara) {
        final Spinner dropdown = dropdownPara;
        final TextView tv = tvPara;
        dropdown.setAdapter(adapterPara);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv.setText(dropdown.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    // for date picker
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            dateTV.setText(year + "-" + (month + 1) + "-" + day);
        }
    }


    // for error message
    public void sendToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    // for error message
    public void showError(final String err) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), err, Toast.LENGTH_SHORT).show();
            }
        });
    }
}