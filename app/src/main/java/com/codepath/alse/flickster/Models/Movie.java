package com.codepath.alse.flickster.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

//Mode class for movie
@Parcel
public class Movie {

    String movieName;
    String posterPath;
    String overview;
    String backdropPath;
    double rating;
    String releaseDate;



    String trailer;
    double popularity;
    String movieid;

    private final String IMAGE_BASEURL = "http://image.tmdb.org/t/p/w185/";
    //Default constructor for Butterknife API
    public Movie(){

    }
    public Double getPopularity() {
        return popularity;
    }

    public String getTrailer() {
        return trailer;
    }
    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getMovieid() {
        return movieid;
    }



    public String getMovieName() {
        return movieName;
    }

    public double getRating(){
        return rating;
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

    public String getOverview() {
        return overview;
    }


    public Movie(JSONObject jsonObject) throws JSONException {
        //Parse JSONObject to get movie details
        this.posterPath = jsonObject.getString("poster_path");
        this.movieName = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.rating = jsonObject.getDouble("vote_average");
        this.releaseDate = jsonObject.getString("release_date");
        this.movieid = jsonObject.getString("id");
        this.popularity=jsonObject.getDouble("popularity");
    }

    //Method to get parse JSONObject
    public static ArrayList<Movie> getMovieArray(JSONArray resultsArray) throws JSONException {
        ArrayList<Movie> movieArray = new ArrayList<Movie>();
        for(int i=0;i<resultsArray.length();i++){
            JSONObject movieObject = resultsArray.getJSONObject(i);
            movieArray.add(new Movie(movieObject));
        }
        return movieArray;
    }
}
