package com.codepath.alse.flickster;

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

import com.codepath.alse.flickster.Adapters.MovieArrayAdapter;
import com.codepath.alse.flickster.Models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    ListView moviesList;
    MovieArrayAdapter movieAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        movies = new ArrayList<>();
        moviesList = (ListView)findViewById(R.id.movies_list);
        movieAdapter = new MovieArrayAdapter(this,movies);
        moviesList.setAdapter(movieAdapter);
        asyncMoviesApi();
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.movies_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.v("InsideOnrefresh","dsfsd");
                asyncMoviesApi();
            }
        });
        moviesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Movie m = (Movie) adapterView.getItemAtPosition(position);
                if(m.getRating() > 5.0) {
                    asyncVideosApi(m);
                }
                else{
                      Intent intent = new Intent(MovieActivity.this,MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.MOVIE_NAME,m.getMovieName());
                intent.putExtra(MovieDetailActivity.DATE,m.getRealeaseDate());
                intent.putExtra(MovieDetailActivity.RATING,(m.getRating()*5)/10);
                intent.putExtra(MovieDetailActivity.OVERVIEW,m.getOverview());
                intent.putExtra(MovieDetailActivity.POSTER_PATH,m.getPosterPath());
                startActivity(intent);
                }
            }
        });

        //asyncMoviesApi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menu_refresh:
                swipeRefreshLayout.setRefreshing(true);
                asyncMoviesApi();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void asyncMoviesApi(){
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
    }
    public void asyncVideosApi(final Movie movie){
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

    }
}
