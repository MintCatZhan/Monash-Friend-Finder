package com.example.yuanzhan.monashff.MainActivityRepository;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuanzhan.monashff.Constants.Constants;
import com.example.yuanzhan.monashff.Constants.MultiSelectionSpinner;
import com.example.yuanzhan.monashff.Constants.SearchMovieDialogFragment;
import com.example.yuanzhan.monashff.EntityRepository.Student;
import com.example.yuanzhan.monashff.MainActivityRepository.GoogleMapActivity.GoogleMapActivity;
import com.example.yuanzhan.monashff.R;
import com.example.yuanzhan.monashff.RESTClients.RestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YuanZhan on 22/04/2017.
 */

public class SearchFragment extends Fragment {
    View vSearch;

    LinearLayout futureFridLL;
    Button searchBtn;
    int tvID;
    private Student currStudent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        vSearch = inflater.inflate(R.layout.fragment_search, container, false);
        // initialization
        tvID = 0;
        futureFridLL = (LinearLayout) vSearch.findViewById(R.id.futureFridLL);
        searchBtn = (Button) vSearch.findViewById(R.id.searchBtn);


        final MultiSelectionSpinner spinner = (MultiSelectionSpinner) vSearch.findViewById(R.id.multi_spinner);
        // set items to spinner
        spinner.setItems(Constants.CRITERIAS);


        // get curr student
        final Student currStd = (Student) getActivity().getIntent().getParcelableExtra("loggedStd");
        currStudent = currStd;

        // set onclicklistener to search btn
        searchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                futureFridLL.removeAllViews();
                final List<String> critList = spinner.getSelectedStrings();

                new AsyncTask<String[], Void, JSONArray>() {

                    @Override
                    protected JSONArray doInBackground(String[]... params) {
                        String searchContentStr = "";
                        for (int j = 1; j <= critList.size(); j++) {
                            searchContentStr = searchContentStr + "keyword" + j + "=" + critList.get(j - 1) + "&";
                        }

                        for (int x = critList.size(); x < 7; x++) {
                            searchContentStr = searchContentStr + "keyword" + (x + 1) + "=&";
                        }


                        String searchContent = "/" + currStd.getStdId() + "?" + searchContentStr.substring(0, searchContentStr.length() - 1);
                        System.out.println(searchContent);
                        JSONArray futureFrid = RestClient.getResultStr("student", "/findFutureFriendsEnhancedByUsingQueryParam", searchContent);


                        return futureFrid;
                    }

                    @Override
                    protected void onPostExecute(final JSONArray futureFrid) {
                        if (futureFrid == null) {
                            createFutureFriendsTable(vSearch, futureFridLL, "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", -1, "error");
                            System.out.println("No future Friend");
                        } else {
                            System.out.println("Have future friends");

                            // create the bundle to send the matching students to map screen\
                            final Button shownOnMap = (Button) vSearch.findViewById(R.id.showOnMapBtn);
                            shownOnMap.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(final View v) {
                                    System.out.println("show on map btn clicked-->");
                                    buildFutureFridsBundle(futureFrid);
                                }
                            });


                            int numOFFF = futureFrid.length();

                            for (int i = 0; i < numOFFF; i++) {
                                try {
                                    final long fridId = futureFrid.getJSONObject(i).getLong("stdId");
                                    String fullName = futureFrid.getJSONObject(i).getString("fname") + " " + futureFrid.getJSONObject(i).getString("lname");
                                    String nationality = futureFrid.getJSONObject(i).getString("nationality");
                                    String gender;
                                    if (futureFrid.getJSONObject(i).getBoolean("gender")) {
                                        gender = "male";
                                    } else {
                                        gender = "female";
                                    }
                                    String favSport = futureFrid.getJSONObject(i).getString("favouriteSport");
                                    String favUnit = futureFrid.getJSONObject(i).getString("favouriteUnit");
                                    final String favMovie = futureFrid.getJSONObject(i).getString("favouriteMovie");

                                    createFutureFriendsTable(vSearch, futureFridLL, fullName, nationality, gender, favSport, favUnit, favMovie, i, "AddFrid" + i);

                                    // then handle the onclick method for search a movie here
                                    Button searchMovieBtn = (Button) vSearch.findViewById(i);
                                    searchMovieBtn.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(final View v) {
                                            System.out.println("search movie btn clicked");
                                            // create a bundle to send the movie name to other frag
                                            Bundle movieBd = new Bundle();
                                            movieBd.putString("MovieName", favMovie);
                                            SearchMovieDialogFragment searchMovieDialogFragment = new SearchMovieDialogFragment();
                                            searchMovieDialogFragment.setArguments(movieBd);
                                            searchMovieDialogFragment.show(getFragmentManager(), "searchMovieDialogFrag");
                                        }
                                    });

                                    // then handle the onclick method for add a friend
                                    final Button addAFriend = (Button) vSearch.findViewWithTag("AddFrid" + i);
                                    final int finalI = i;
                                    addAFriend.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(final View v) {
                                            System.out.println("add a friend btn clicked-->" + "AddFrid" + finalI + "<--" + fridId);

                                            // add friend
                                            try {
                                                addFriend(currStd, futureFrid.getJSONObject(finalI));
                                                addAFriend.setText("ADDED");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    createFutureFriendsTable(vSearch, futureFridLL, "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", -1, "error");
                                }
                            }
                        }

                    }
                }.execute();
            }
        });


        return vSearch;
    }


    // create a matching student bundle: list of students to be sent to map screen
    public void buildFutureFridsBundle(final JSONArray futureFridsJSONArray) {


        new AsyncTask<Void, Void, Student[]>() {

            @Override
            protected Student[] doInBackground(Void... params) {
                Student[] futureFrids = new Student[futureFridsJSONArray.length()];
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();

                for (int i = 0; i < futureFridsJSONArray.length(); i++) {
                    try {
                        Student curStd = gson.fromJson(
                                RestClient.getResultStr("student", "/findByEmailAddr", "/" + futureFridsJSONArray.getJSONObject(i).getString("emailAddr")).getJSONObject(0).toString(),
                                Student.class);
                        futureFrids[i] = curStd;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return futureFrids;
            }

            @Override
            protected void onPostExecute(Student[] futureFrids) {
                if (futureFrids == null || futureFrids.length == 0) {
                    sendToast("No Matching Student");
                } else {
                    Bundle futureFridsBd = new Bundle();
                    System.out.println("the length of students to be shown on map is--> " + futureFrids.length);
                    futureFridsBd.putParcelableArray("matchingStudents", futureFrids);
                    Intent intent = new Intent(getActivity(), GoogleMapActivity.class);
                    intent.putExtras(futureFridsBd);
                    startActivity(intent);
                    sendToast("Redirecting...");
                }
            }

        }.execute();
    }


    public void createFutureFriendsTable(View v, LinearLayout ll, String fullName, String nationality, String gender, String favSport, String favUnit, String favMovie, int btnFavMovieId, String btnAddAsFrid) {
        CardView card = new CardView(v.getContext());

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(12, 12, 12, 12);
        card.setLayoutParams(lp);
        card.setContentPadding(12, 12, 12, 12);


        card.setCardBackgroundColor(Color.rgb(120, 210, 250));
        card.setRadius(10);

        LinearLayout cardLL = new LinearLayout(v.getContext());
        cardLL.setLayoutParams(lp);
        cardLL.setOrientation(LinearLayout.VERTICAL);


        TextView tvName = new TextView(v.getContext());
        tvName.setText(fullName);
        tvName.getPaint().setFakeBoldText(true);
        tvName.setTextColor(Color.WHITE);
        tvName.setTextSize(30);

        Button addAsFriendBtn = new Button(v.getContext());
        addAsFriendBtn.setTag(btnAddAsFrid);
        addAsFriendBtn.setText("Add Friend");
        addAsFriendBtn.setBackgroundColor(Color.YELLOW);

        TextView tvNation = new TextView(v.getContext());
        tvNation.setText("Nationality: " + nationality);
        tvNation.setTextColor(Color.WHITE);

        TextView tvGender = new TextView(v.getContext());
        tvGender.setText("Gender: " + gender);
        tvGender.setTextColor(Color.WHITE);

        TextView tvFavSport = new TextView(v.getContext());
        tvFavSport.setText("Favourite Sport: " + favSport);
        tvFavSport.setTextColor(Color.WHITE);

        TextView tvFavUnit = new TextView(v.getContext());
        tvFavUnit.setText("Favourite Unit: " + favUnit);
        tvFavUnit.setTextColor(Color.WHITE);

        TextView tvFavMovie = new TextView(v.getContext());
        tvFavMovie.setText("Favourite Movie: " + favMovie);
        tvFavMovie.setTextColor(Color.WHITE);

        Button btnFavMovie = new Button(v.getContext());
        btnFavMovie.setId(btnFavMovieId);
        btnFavMovie.setText("Search");


        cardLL.addView(tvName);
        cardLL.addView(addAsFriendBtn);
        cardLL.addView(tvGender);
        cardLL.addView(tvNation);
        cardLL.addView(tvFavSport);
        cardLL.addView(tvFavUnit);
        cardLL.addView(tvFavMovie);
        cardLL.addView(btnFavMovie);

        card.addView(cardLL);


        ll.addView(card, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        System.out.println("added CardView layout");
    }


    public void addFriend(final Student student, final JSONObject friend) {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return RestClient.postFriendship(student, friend);
            }

            @Override
            protected void onPostExecute(Boolean response) {
                if (response) {
                    sendToast("Add Friend Successfully");
                } else {
                    sendToast("You already have this friend.");
                }
            }
        }.execute();

    }


    // for error message
    public void sendToast(String msg) {
        Toast.makeText(this.getActivity(), msg, Toast.LENGTH_LONG).show();
    }

//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public JSONArray getFutureFriends(String searchContent, long stdId) {
//        try {
//            JSONArray futureFrid = RestClient.getResultStr("student", "/findFutureFriendsEnhancedByUsingQueryParam", searchContent);
//            JSONArray alreadyFrid = RestClient.getResultStr("friendship", "/findByStdId", "/" + stdId);
//
//            for (int i = 0; i< futureFrid.length(); i++){
//                long thisStdId = futureFrid.getJSONObject(i).getLong("stdId");
//                for (int j = 0; j < alreadyFrid.length(); j++){
//                   if (thisStdId ==  alreadyFrid.getJSONObject(j).getJSONObject("student1").getLong("stdId")){
//                       futureFrid.remove(i);
//                   }
//                }
//
//            }
//            return futureFrid;
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
//

//    }

}
