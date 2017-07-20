package com.example.yuanzhan.monashff.MainActivityRepository;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuanzhan.monashff.EntityRepository.Student;
import com.example.yuanzhan.monashff.MainActivityRepository.GoogleMapActivity.GoogleMapActivity;
import com.example.yuanzhan.monashff.R;
import com.example.yuanzhan.monashff.RESTClients.RestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YuanZhan on 29/04/2017.
 */

public class FriendFragment extends Fragment {
    View vFriend;

    LinearLayout friendsLL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vFriend = inflater.inflate(R.layout.fragment_friend, container, false);

        final Student currStd = (Student) getActivity().getIntent().getParcelableExtra("loggedStd");
        final long id = currStd.getStdId();

        //initialization
        friendsLL = (LinearLayout) vFriend.findViewById(R.id.friendsLL);


        // first, get all friends
        new AsyncTask<Long, Void, JSONArray>() {

            @Override
            protected JSONArray doInBackground(Long... params) {
                JSONArray friendsJArrayFirst = RestClient.getResultStr("friendship", "/findByStdId", "/" + id);
                JSONArray friendsJArraySecond = RestClient.getResultStr("friendship", "/findByFriendId", "/" + id);

                try {
                    return concatArray(friendsJArrayFirst, friendsJArraySecond);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return  null;
                }
            }

            @Override
            protected void onPostExecute(final JSONArray friends) {
                if (friends == null) {
                    createFriendsTable(vFriend, friendsLL, "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "error");
                    System.out.println("No future Friend");
                } else {
                    System.out.println("Have future friends");
                    int numOFFF = friends.length();
                    System.out.println("Have future friends___>ininini" + numOFFF );
                    for (int i = 0; i < numOFFF; i++) {
                        try {
                            System.out.println("Have future friends___>inside" );
                            final String theStdStr;
                            if (id == friends.getJSONObject(i).getJSONObject("student1").getLong("stdId")) {
                                theStdStr = "student";
                            } else {
                                theStdStr = "student1";
                            }

                            final long fridId = friends.getJSONObject(i).getJSONObject(theStdStr).getLong("stdId");

                            String fullName = friends.getJSONObject(i).getJSONObject(theStdStr).getString("fname") + " " + friends.getJSONObject(i).getJSONObject(theStdStr).getString("lname");
                            String nationality = friends.getJSONObject(i).getJSONObject(theStdStr).getString("nationality");
                            String gender;
                            if (friends.getJSONObject(i).getJSONObject(theStdStr).getBoolean("gender")) {
                                gender = "male";
                            } else {
                                gender = "female";
                            }
                            String favSport = friends.getJSONObject(i).getJSONObject(theStdStr).getString("favouriteSport");
                            String favUnit = friends.getJSONObject(i).getJSONObject(theStdStr).getString("favouriteUnit");
                            final String favMovie = friends.getJSONObject(i).getJSONObject(theStdStr).getString("favouriteMovie");
                            createFriendsTable(vFriend, friendsLL, fullName, nationality, gender, favSport, favUnit, favMovie, "DeleteBtn" + i);

                            // then handle the onclick method for delete a friend here
                            final Button deleteFridBtn = (Button) vFriend.findViewWithTag("DeleteBtn" + i);
                            deleteFridBtn.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(final View v) {
                                    System.out.println("delete a friend btn clicked-->" + "AddFrid" + fridId + "<--" + fridId);

                                    // delete friend
                                    if (id < fridId){
                                        deleteFriend(id, fridId);
                                    } else {
                                        deleteFriend(fridId, id);
                                    }

                                    deleteFridBtn.setText("DELETED");
                                }
                            });


                            // then handle the onlick method for show the friends location
                            final Button shownOnMap = (Button) vFriend.findViewById(R.id.showAllFriendsOnMapBtn);
                            shownOnMap.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    new AsyncTask<Void, Void, Void>() {

                                        @Override
                                        protected Void doInBackground(Void... params) {


                                            System.out.println("show on map btn clicked-->");
                                            Student[] allFriends = new Student[friends.length()];
                                            System.out.println("show on map btn clicked-->" + "frrrrriend length" + friends.length());
                                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();

                                            for (int i = 0; i < allFriends.length; i++) {
                                                try {
                                                    System.out.println("show on map btn clicked-->" + "frrrrriend email" + friends.getJSONObject(i).getJSONObject(theStdStr).getString("emailAddr"));
                                                    Student curStd = gson.fromJson(
                                                            RestClient.getResultStr("student", "/findByEmailAddr", "/" + friends.getJSONObject(i).getJSONObject(theStdStr).getString("emailAddr")).getJSONObject(0).toString(),
                                                            Student.class);
                                                    allFriends[i] = curStd;
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            // shown on map
                                            Bundle futureFridsBd = new Bundle();
                                            System.out.println("the length of students to be shown on map is--> " + allFriends.length);
                                            futureFridsBd.putParcelableArray("matchingStudents", allFriends);
                                            Intent intent = new Intent(getActivity(), GoogleMapActivity.class);
                                            intent.putExtras(futureFridsBd);
                                            startActivity(intent);
                                            return null;
                                        }
                                    }.execute();


                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            createFriendsTable(vFriend, friendsLL, "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "error");
                        }
                    }
                }
            }

        }.execute();


        return vFriend;
    }


    public void createFriendsTable(View v, LinearLayout ll, String fullName, String nationality, String gender, String favSport, String favUnit, String favMovie, String btnAddAsFrid) {

        CardView card = new CardView(v.getContext());

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.setMargins(12,12,12,12);
        card.setLayoutParams(lp);
        card.setContentPadding(12,12,12,12);


        card.setCardBackgroundColor(Color.rgb(120,210,250));
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
        addAsFriendBtn.setText("Delete Friend");

        TextView tvNation = new TextView(v.getContext());
        tvNation.setText("Nationality: " + nationality);
        tvNation.setTextColor(Color.WHITE);

        TextView tvGender = new TextView(v.getContext());
        tvGender.setText("Gender: " + gender);
        tvGender.setTextColor(Color.WHITE);

        TextView tvFavSport = new TextView(v.getContext());
        tvFavSport.setText("Favourite Sport: " +favSport);
        tvFavSport.setTextColor(Color.WHITE);

        TextView tvFavUnit = new TextView(v.getContext());
        tvFavUnit.setText("Favourite Unit: " +favUnit);
        tvFavUnit.setTextColor(Color.WHITE);

        TextView tvFavMovie = new TextView(v.getContext());
        tvFavMovie.setText("Favourite Movie: " +favMovie);
        tvFavMovie.setTextColor(Color.WHITE);




        cardLL.addView(tvName);
        cardLL.addView(addAsFriendBtn);
        cardLL.addView(tvGender);
        cardLL.addView(tvNation);
        cardLL.addView(tvFavSport);
        cardLL.addView(tvFavUnit);
        cardLL.addView(tvFavMovie);

        card.addView(cardLL);


        ll.addView(card, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        System.out.println("added CardView layout");

    }


    // for error message
    public void sendToast(String msg) {
        Toast.makeText(this.getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public void deleteFriend(final long stdId, final long friendId) {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return RestClient.deleteFriend(stdId, friendId);
            }

            @Override
            protected void onPostExecute(Boolean response) {
                if (response) {
                    sendToast("Delete Friend Successfully");
                } else {
                    sendToast("Errors during deleting");
                }
            }
        }.execute();

    }

    private JSONArray concatArray(JSONArray arr1, JSONArray arr2)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (int i = 0; i < arr1.length(); i++) {
            result.put(arr1.get(i));
        }
        for (int i = 0; i < arr2.length(); i++) {
            result.put(arr2.get(i));
        }
        return result;
    }
}