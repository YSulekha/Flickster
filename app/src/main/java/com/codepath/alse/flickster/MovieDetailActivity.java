package com.codepath.alse.flickster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    public static String POSTER_PATH = "poster_path";
    public static String RATING = "rating";
    public static String MOVIE_NAME = "name";
    public static String OVERVIEW = "overview";
    public static String DATE = "date";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        ImageView posterImage = (ImageView)findViewById(R.id.detail_ImageView);
        Picasso.with(this).load(intent.getStringExtra(POSTER_PATH)).
                placeholder(R.drawable.ic_theaters_red_200_36dp).into(posterImage);
        TextView date = (TextView)findViewById(R.id.detail_release_date);
        date.setText(intent.getStringExtra(DATE));
        RatingBar stars = (RatingBar)findViewById(R.id.detail_ratingBar);
        stars.setRating((float) intent.getDoubleExtra(RATING,0.0));
        TextView overview = (TextView)findViewById(R.id.detail_overview);
        overview.setText(intent.getStringExtra(OVERVIEW));

        setTitle(intent.getStringExtra(MOVIE_NAME));


    }
}
