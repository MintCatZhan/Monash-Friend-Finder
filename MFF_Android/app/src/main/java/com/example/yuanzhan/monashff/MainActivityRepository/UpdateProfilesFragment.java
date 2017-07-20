package com.example.yuanzhan.monashff.MainActivityRepository;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.yuanzhan.monashff.R;
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

/**
 * Created by YuanZhan on 22/04/2017.
 */

public class UpdateProfilesFragment extends Fragment implements View.OnClickListener {
    protected DBManager dbManager;

    View vUpdateProfile;

    EditText fnameUptET;
    EditText lnameUptET;
    static TextView dobUptTV;
    RadioGroup genderUptRG;
    TextView courseUptTV;
    TextView modeUptTV;
    EditText addrUptET;
    TextView suburbUptTV;
    TextView nationalityUptTV;
    TextView langUptTV;
    TextView favSportUptTV;
    EditText favMovieUptET;
    TextView favUnitUptTV;
    EditText currJobUptET;
    TextView emailUptTV;
    EditText pwdUptET;
    EditText repwdUptET;

    RadioButton maleRB;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vUpdateProfile = inflater.inflate(R.layout.fragment_update_profiles, container, false);
        dbManager = new DBManager(vUpdateProfile.getContext());

        // get the current student object by Intent
        final Student currStd = (Student) getActivity().getIntent().getParcelableExtra("loggedStd");


        // get all editText and let users to modify
        fnameUptET = (EditText) vUpdateProfile.findViewById(R.id.fnameForUpdtET);
        lnameUptET = (EditText) vUpdateProfile.findViewById(R.id.lnameForUpdtET);
        dobUptTV = (TextView) vUpdateProfile.findViewById(R.id.dobForUpdtTV);
        genderUptRG = (RadioGroup) vUpdateProfile.findViewById(R.id.genderForUpdtRG);
        courseUptTV = (TextView) vUpdateProfile.findViewById(R.id.courseForUpdtTV);
        modeUptTV = (TextView) vUpdateProfile.findViewById(R.id.modeForUpdtTV);
        addrUptET = (EditText) vUpdateProfile.findViewById(R.id.addrForUpdtET);
        suburbUptTV = (TextView) vUpdateProfile.findViewById(R.id.suburbForUpdtTV);
        nationalityUptTV = (TextView) vUpdateProfile.findViewById(R.id.nationForUpdtTV);
        langUptTV = (TextView) vUpdateProfile.findViewById(R.id.langForUpdtTV);
        favSportUptTV = (TextView) vUpdateProfile.findViewById(R.id.favSportForUpdtTV);
        favMovieUptET = (EditText) vUpdateProfile.findViewById(R.id.favMovieForUpdtET);
        favUnitUptTV = (TextView) vUpdateProfile.findViewById(R.id.favUnitForUpdtTV);
        currJobUptET = (EditText) vUpdateProfile.findViewById(R.id.jobForUpdtET);
        emailUptTV = (TextView) vUpdateProfile.findViewById(R.id.emailForUpdtTV);
        pwdUptET = (EditText) vUpdateProfile.findViewById(R.id.pwdForUpdtET);
        repwdUptET = (EditText) vUpdateProfile.findViewById(R.id.rePwdForUpdtET);

        maleRB = (RadioButton) vUpdateProfile.findViewById(R.id.maleBtn);
        final String id = maleRB.getId() + "";


        // course spinner adapter
        // set selectedlistener to course spinner
        setSpinnerSelectedListener((Spinner) vUpdateProfile.findViewById(R.id.courseForUpdtSpinner),
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Constants.COURSES_UP),
                courseUptTV);

        // mode spinner adapter
        // set selectedlistener to mode spinner
        setSpinnerSelectedListener((Spinner) vUpdateProfile.findViewById(R.id.modeForUpdtSpinner),
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Constants.MODE_UP),
                modeUptTV);

        // suburb spinner adapter
        // set selectedlistener to suburb spinner
        setSpinnerSelectedListener((Spinner) vUpdateProfile.findViewById(R.id.suburbForUpdtSpinner),
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Constants.SUBURB_UP),
                suburbUptTV);


        // nationality spinner adapter, the data resource is the SQLite database
        // set selectedlistener to nationality spinner
        setSpinnerSelectedListener((Spinner) vUpdateProfile.findViewById(R.id.nationForUpdtSpinner),
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, this.getDataArray("country")),
                nationalityUptTV);

        // language spinner adapter, the data resource is the SQLite database
        // set selectedlistener to language spinner
        setSpinnerSelectedListener((Spinner) vUpdateProfile.findViewById(R.id.langForUpdtSpinner),
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, this.getDataArray("language")),
                langUptTV);

        // favourite sport spinner adapter
        // set selectedlistener to favourite sport spinner
        setSpinnerSelectedListener((Spinner) vUpdateProfile.findViewById(R.id.favSportForUpdtSpinner),
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Constants.FAV_SPORT_UP),
                favSportUptTV);


        // favourite sport spinner adapter
        // set selectedlistener to favourite sport spinner
        setSpinnerSelectedListener((Spinner) vUpdateProfile.findViewById(R.id.favUnitForUpdtSpinner),
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Constants.FAV_UNIT_UP),
                favUnitUptTV);


        fnameUptET.setText(currStd.getFname());
        lnameUptET.setText(currStd.getLname());
        // dob
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dobUptTV.setText(sdf.format(currStd.getDob()));
        // genderUptRG
        if (currStd.getGender()) {
            genderUptRG.check(R.id.maleBtn);
        } else {
            genderUptRG.check(R.id.femaleBtn);
        }
        courseUptTV.setText(currStd.getCourse());
        // mode
        if (currStd.getMode()) {
            modeUptTV.setText(Constants.MODE_UP[2]);
        } else {
            modeUptTV.setText(Constants.MODE_UP[1]);
        }
        addrUptET.setText(currStd.getAddress());
        suburbUptTV.setText(currStd.getSuburb());
        nationalityUptTV.setText(currStd.getNationality());
        langUptTV.setText(currStd.getLang());
        favSportUptTV.setText(currStd.getFavouriteSport());
        favMovieUptET.setText(currStd.getFavouriteMovie());
        favUnitUptTV.setText(currStd.getFavouriteUnit());
        currJobUptET.setText(currStd.getCurrentJob());
        emailUptTV.setText(currStd.getEmailAddr()); // unchangable
        pwdUptET.setText("");
        repwdUptET.setText("");

        Button btn = (Button) vUpdateProfile.findViewById(R.id.dobForUpdtBtn);
        btn.setOnClickListener(this);

        // set onclicklister to regist btn (Submit)
        Button submitRegistBtn = (Button) vUpdateProfile.findViewById(R.id.submitUpdtBtn);
        submitRegistBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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
                                                        if (params[15].length() > 0) {
                                                            if (params[15].equals(params[16])) {
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
                                                                System.out.println("gender data" + params[3]);
                                                                boolean genderFormatted = params[3].toLowerCase().equals(id);

                                                                // convert mode format
                                                                boolean modeFormatted = params[5].toLowerCase().equals("full time");

                                                                Student student = new Student(currStd.getStdId(), params[0], params[1], dobFormatted, genderFormatted,
                                                                        params[4], modeFormatted, params[6], params[7], params[8], params[9],
                                                                        params[10], params[11], params[12], params[13], params[14], Constants.generateSecurePassword(params[15]),
                                                                        currStd.getSubscriptDatetime());

                                                                System.out.println("Updating student -->" + student.getFname() + " " + student.getLname());

                                                                if (RestClient.putStudent(student)) {
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
                                                            }
                                                            showError("Re-pwd not equals to pwd");
                                                            return null;
                                                        }
                                                        showError("Re-pwd is Empty");
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
                            sendToast("Updates successfully");
                        }
                    }


                }.execute(fnameUptET.getText().toString(), lnameUptET.getText().toString(),
                        dobUptTV.getText().toString(), genderUptRG.getCheckedRadioButtonId() + "",
                        courseUptTV.getText().toString(), modeUptTV.getText().toString(),
                        addrUptET.getText().toString(), suburbUptTV.getText().toString(),
                        nationalityUptTV.getText().toString(), langUptTV.getText().toString(),
                        favSportUptTV.getText().toString(), favMovieUptET.getText().toString(),
                        favUnitUptTV.getText().toString(), currJobUptET.getText().toString(),
                        emailUptTV.getText().toString(), repwdUptET.getText().toString(),
                        pwdUptET.getText().toString());
            }

        });


        return vUpdateProfile;
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
                if (dropdown.getSelectedItemId() > 0) {
                    tv.setText(dropdown.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        DialogFragment newFragment = new UpdateProfilesFragment.DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, 1993, 1, 1);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            dobUptTV.setText(year + "-" + month + "-" + day);
        }
    }


    // for error message
    public void sendToast(String msg) {
        Toast.makeText(this.getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    // for error message
    public void showError(final String err) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(vUpdateProfile.getContext(), err, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
