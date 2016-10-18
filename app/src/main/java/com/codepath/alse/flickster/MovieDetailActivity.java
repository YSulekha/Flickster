package com.codepath.alse.flickster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.codepath.alse.flickster.Models.Movie;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class MovieDetailActivity extends AppCompatActivity {

    public static String POSTER_PATH = "poster_path";
    public static String RATING = "rating";
    public static String MOVIE_NAME = "name";
    public static String OVERVIEW = "overview";
    public static String DATE = "date";
    public static String TRAILER = "trailer";
    public static String MOVIE = "movie_object";

    public static String thumbnail_url = "http://img.youtube.com/vi/";
    Movie m;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        m = (Movie) Parcels.unwrap(intent.getParcelableExtra(MOVIE));

       ImageView posterImage = (ImageView)findViewById(R.id.detail_ImageView);
       /* Uri uri = Uri.parse(thumbnail_url).buildUpon().appendPath(intent.getStringExtra(TRAILER)).
                appendPath("0.jpg").build();
        Log.v("Uri",uri.toString());
        Picasso.with(this).load(intent.getStringExtra(POSTER_PATH)).
                placeholder(R.drawable.ic_movie_red_400_48dp).into(posterImage);*/
        TextView date = (TextView)findViewById(R.id.detail_release_date);
    //    date.setText(intent.getStringExtra(DATE));
        RatingBar stars = (RatingBar)findViewById(R.id.detail_ratingBar);
    //    stars.setRating((float) intent.getDoubleExtra(RATING,0.0));
        TextView overview = (TextView)findViewById(R.id.detail_overview);
     //   overview.setText(intent.getStringExtra(OVERVIEW));*/
        //  setTitle(intent.getStringExtra(MOVIE_NAME));

        Picasso.with(this).load(m.getPosterPath()).
                placeholder(R.drawable.ic_movie_red_400_48dp).into(posterImage);
        date.setText(m.getRealeaseDate());
        float rating = (float)((m.getRating()*5)/10);
        stars.setRating(rating);
        Log.v("rating",String.valueOf(m.getRating()));
        overview.setText(m.getOverview());
        setTitle(m.getMovieName());

    }

    public void onClickPic(View v){
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        MovieActivity mA = new MovieActivity();
        Intent intent = new Intent(MovieDetailActivity.this, YoutubeActivity.class);
        mA.asyncVideosApi(m,mRequestQueue,intent,this);
    }
}
