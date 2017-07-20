package com.example.yuanzhan.monashff.Constants;

import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuanzhan.monashff.APIMethods.APIMethods;
import com.example.yuanzhan.monashff.R;
import com.example.yuanzhan.monashff.RESTClients.RestClient;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by YuanZhan on 30/04/2017.
 */

public class SearchMovieDialogFragment extends DialogFragment {

    public TextView movieNameTV;
    public TextView movieDescTV;
    public ImageView movieImageIV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final View view = inflater.inflate(R.layout.dialog_movie_info, container);
        final String movieName = getArguments().getString("MovieName");

        movieNameTV = (TextView) view.findViewById(R.id.movieNameTV);
        movieDescTV = (TextView) view.findViewById(R.id.movieDescTV);
        movieImageIV = (ImageView) view.findViewById(R.id.movieImageIV);

        movieNameTV.setText(movieName);

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                final String movieDesc = APIMethods.getMovieDesc(movieName);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        movieDescTV.setText(movieDesc);
                    }
                });

                return null;
            }

        }.execute();



        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                final String movieImageURL = APIMethods.getMoviePicSrc(movieName);
                URL url = null;
                try {
                    url = new URL(movieImageURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }


                final Bitmap finalBmp = bmp;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        movieImageIV.setImageBitmap(finalBmp);
                    }
                });

                return null;
            }

        }.execute();


        return view;
    }

}
