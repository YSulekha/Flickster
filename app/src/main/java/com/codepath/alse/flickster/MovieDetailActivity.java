package com.codepath.alse.flickster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.codepath.alse.flickster.Models.Movie;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;


//Activity to display movie details
public class MovieDetailActivity extends AppCompatActivity {

    //Extra data sent from intent
    public static String MOVIE = "movie_object";

    public static String thumbnail_url = "http://img.youtube.com/vi/";
    Movie m;

    @BindView(R.id.detail_ImageView)
    ImageView posterImage;
    @BindView(R.id.detail_popular)
    TextView popular;
    @BindView(R.id.detail_ratingBar)
    RatingBar stars;
    @BindView(R.id.detail_overview)
    TextView overview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        m = (Movie) Parcels.unwrap(intent.getParcelableExtra(MOVIE));
        if(m.getBackdropPath()==null){
            Picasso.with(this).load(m.getPosterPath()).
                    placeholder(R.drawable.ic_movie_red_400_48dp).into(posterImage);
        }
        else {
            Picasso.with(this).load(m.getBackdropPath()).
                    placeholder(R.drawable.ic_movie_red_400_48dp).into(posterImage);
        }
        popular.setText(String.valueOf(m.getPopularity().intValue()));
        float rating = (float) ((m.getRating() * 5) / 10);
        stars.setRating(rating);
        overview.setText(m.getOverview());
        setTitle(m.getMovieName());

    }

    //When clicked on image, play trailer in youtube player
    public void onClickPic(View v) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        MovieActivity mA = new MovieActivity();
        Intent intent = new Intent(MovieDetailActivity.this, YoutubeActivity.class);
        mA.asyncVideosApi(m, mRequestQueue, intent, this);
    }
}
