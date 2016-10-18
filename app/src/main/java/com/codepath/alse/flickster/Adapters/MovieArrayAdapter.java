package com.codepath.alse.flickster.Adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.alse.flickster.Models.Movie;
import com.codepath.alse.flickster.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


//Adapter for the listView
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    public static final int ITEM_BACKDROP = 0;
    public static final int ITEM_DETAILS = 1;
    Context mContext = getContext();

    public MovieArrayAdapter(Context context, List<Movie> movies) {

        super(context, 0,movies);
    }

    @Override
    public int getItemViewType(int position) {
        Movie movie = getItem(position);
        double rating = movie.getRating();
        if(rating > 5.0){
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        int type = getItemViewType(position);
        int orientation = mContext.getResources().getConfiguration().orientation;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch(type){
            case ITEM_BACKDROP:
                    ViewHolderBackdrop viewHolderBackdrop;
                    if(convertView == null){

                        convertView = inflater.inflate(R.layout.movie_listitem_rating,parent,false);
                        viewHolderBackdrop = new ViewHolderBackdrop(convertView);
                     //   viewHolderBackdrop.backdrop = (ImageView)convertView.findViewById(list_rating_backdrop);
                        convertView.setTag(viewHolderBackdrop);
                    }
                    else{
                        viewHolderBackdrop = (ViewHolderBackdrop)convertView.getTag();
                    }
                    Picasso.with(getContext()).load(movie.getBackdropPath()).
                            transform(new RoundedCornersTransformation(30,5)).
                            placeholder(R.drawable.ic_movie_red_400_48dp).into(viewHolderBackdrop.backdrop);
                    Log.v("Name+backdrop+Adap",movie.getMovieName()+movie.getBackdropPath());

                return convertView;
            case ITEM_DETAILS:
                ViewHolderDetail viewHolder;
                if(convertView == null){
                    if(position % 2 ==0) {
                        convertView = inflater.inflate(R.layout.movie_listitem1, parent, false);
                    }
                    else{
                        convertView = inflater.inflate(R.layout.movie_listitem2, parent, false);
                    }
                    viewHolder = new ViewHolderDetail(convertView);
                 /*   viewHolder.poster = (ImageView)convertView.findViewById(R.id.list_profile);
                    viewHolder.title = (TextView)convertView.findViewById(list_title);
                    viewHolder.overview = (TextView)convertView.findViewById(list_overview);*/
                    convertView.setTag(viewHolder);

                }
                else{
                    viewHolder = (ViewHolderDetail)convertView.getTag();
                }
                String imageUrl = null;
                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    imageUrl = movie.getPosterPath();
                }
                else{
                    imageUrl = movie.getBackdropPath();
                }
                viewHolder.poster.setImageResource(0);
                Picasso.with(getContext()).load(imageUrl).
                        transform(new RoundedCornersTransformation(20,10)).
                        placeholder(R.drawable.ic_movie_red_400_48dp).into(viewHolder.poster);

                viewHolder.title.setText(movie.getMovieName());
                viewHolder.overview.setText(movie.getOverview());
                return convertView;

            }
        return null;
        }

    public static class ViewHolderDetail{
        @BindView(R.id.list_profile) ImageView poster;
        @BindView(R.id.list_title) TextView title;
        @BindView(R.id.list_overview) TextView overview;

        public ViewHolderDetail(View v){
            ButterKnife.bind(this, v);
        }
    }
    public static class ViewHolderBackdrop{
        @BindView(R.id.list_rating_backdrop) ImageView backdrop;

        public ViewHolderBackdrop(View v){
            ButterKnife.bind(this, v);
        }
    }
}
