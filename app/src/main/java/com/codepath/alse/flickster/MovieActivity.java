package com.codepath.alse.flickster;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codepath.alse.flickster.Adapters.MovieArrayAdapter;
import com.codepath.alse.flickster.Models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    @BindView(R.id.movies_list)
    ListView moviesList;
    MovieArrayAdapter movieAdapter;
    @BindView(R.id.movies_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private RequestQueue mRequestQueue;
    boolean isDetail = false;
    Context mContext;
    @BindString(R.string.no_videos)
    String toastString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        moviesList.setAdapter(movieAdapter);
        mRequestQueue = Volley.newRequestQueue(this);
        //Asynchronous call to MovieDB Api
        asyncMoviesApi();
        //Refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                asyncMoviesApi();
            }
        });
        mContext = this;
        moviesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Movie m = (Movie) adapterView.getItemAtPosition(position);
                //If rating is greater than 5, open youtube activity
                if (m.getRating() > 5.0) {
                    Intent intent = new Intent(MovieActivity.this, YoutubeActivity.class);
                    asyncVideosApi(m, mRequestQueue, intent, mContext);
                } else {
                    Intent intent = new Intent(MovieActivity.this, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailActivity.MOVIE, Parcels.wrap(m));
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_refresh:
                swipeRefreshLayout.setRefreshing(true);
                asyncMoviesApi();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//asyncMovies using AsyncHttpClient
    /*   public void asyncMoviesApi(){
           String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
           AsyncHttpClient client = new AsyncHttpClient();
           client.get(url,new JsonHttpResponseHandler(){

               @Override
               public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                   JSONArray resultArray = null;
                   try {
                       resultArray = response.getJSONArray("results");
                       movies.clear();
                       movies.addAll(Movie.getMovieArray(resultArray));
                       movieAdapter.notifyDataSetChanged();
                       Log.v("InsideOnSuccess","success");
                       swipeRefreshLayout.setRefreshing(false);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }

               @Override
               public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                   super.onFailure(statusCode, headers, responseString, throwable);
               }
           });
       }*/

    //Method to download data from MovieDBAPI
    public void asyncMoviesApi() {
        String apiKey = BuildConfig.MOVIE_DB_API_KEY;
        String url = "https://api.themoviedb.org/3/movie/now_playing";
        Uri uri = Uri.parse(url).buildUpon().appendQueryParameter("api_key", apiKey).build();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, uri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray resultArray = null;
                        try {
                            resultArray = response.getJSONArray("results");
                            movies.clear();
                            movies.addAll(Movie.getMovieArray(resultArray));
                            movieAdapter.notifyDataSetChanged();
                            Log.v("InsideOnSuccess", "success");
                            swipeRefreshLayout.setRefreshing(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error in Network call", error.getMessage());
            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }
//Method to get the trailer videos from MovieDB Api using AsyncHttpClient
    /*public  void asyncVideosApi(final Movie movie){
        String url = "https://api.themoviedb.org/3/movie";
        Uri u = Uri.parse(url).buildUpon().appendPath(movie.getMovieid()).appendPath("videos").
                appendQueryParameter("api_key","a07e22bc18f5cb106bfe4cc1f83ad8ed").build();
        AsyncHttpClient client = new AsyncHttpClient();
        Log.v("MovieActivity",u.toString());
        client.get(u.toString(),new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray resultArray = null;
                try {
                    resultArray = response.getJSONArray("results");
                    for(int i=0;i<resultArray.length();i++){
                        JSONObject onj = (JSONObject) resultArray.get(i);
                        String type = onj.getString("type");
                        if(type.equals("Trailer")){
                            Log.v("MovieActivity","dfasdf");
                            movie.setTrailer(onj.getString("key"));
                            Intent intent = new Intent(MovieActivity.this, YoutubeActivity.class);
                            intent.putExtra(YoutubeActivity.VIDEO_KEY,movie.getTrailer());
                            startActivity(intent);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }*/

    ///Method to get the trailer videos from MovieDB Api using Volley API
    public void asyncVideosApi(final Movie movie, RequestQueue requestQueue, final Intent intent, final Context context) {
        String url = "https://api.themoviedb.org/3/movie";
        String apiKey = BuildConfig.MOVIE_DB_API_KEY;
        Uri u = Uri.parse(url).buildUpon().appendPath(movie.getMovieid()).appendPath("videos").
                appendQueryParameter("api_key", apiKey).build();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, u.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray resultArray = null;
                        try {
                            resultArray = response.getJSONArray("results");
                            if (resultArray.length() == 0) {
                                Toast toast = Toast.makeText(context, toastString, Toast.LENGTH_LONG);
                                toast.show();
                            }
                            for (int i = 0; i < resultArray.length(); i++) {
                                JSONObject onj = (JSONObject) resultArray.get(i);
                                String type = onj.getString("type");
                                if (type.equals("Trailer")) {
                                    movie.setTrailer(onj.getString("key"));
                                    intent.putExtra(YoutubeActivity.VIDEO_KEY, movie.getTrailer());
                                    context.startActivity(intent);
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error in Network call", error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}
