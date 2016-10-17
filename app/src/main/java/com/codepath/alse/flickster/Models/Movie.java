package com.codepath.alse.flickster.Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aharyadi on 10/14/16.
 */

public class Movie {

    String movieName;
    String posterPath;
    String overview;
    String backdropPath;
    double rating;
    String releaseDate;

    public String getTrailer() {
        return trailer;
    }

    String trailer;

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }



    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    String movieid;

    private final String IMAGE_BASEURL = "http://image.tmdb.org/t/p/w185/";

    public String getMovieName() {
        return movieName;
    }

    public double getRating(){
        return rating;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w500/%s",posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780/%s",backdropPath);
    }

    public String getRealeaseDate() {
        return releaseDate;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.movieName = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.rating = jsonObject.getDouble("vote_average");
        this.releaseDate = jsonObject.getString("release_date");
        this.movieid = jsonObject.getString("id");

        Log.v("Name+backdrop",this.movieName+this.backdropPath);
    }

    public static ArrayList<Movie> getMovieArray(JSONArray resultsArray) throws JSONException {
        ArrayList<Movie> movieArray = new ArrayList<Movie>();

        for(int i=0;i<resultsArray.length();i++){
            JSONObject movieObject = resultsArray.getJSONObject(i);
            movieArray.add(new Movie(movieObject));
        }
        return movieArray;
    }


}
