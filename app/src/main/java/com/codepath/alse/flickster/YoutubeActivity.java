package com.codepath.alse.flickster;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

//Youtube Activity to play youtube videos in Youtube player
public class YoutubeActivity extends YouTubeBaseActivity {

    public static final String VIDEO_KEY = "video_key";
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer mYouTubePlayer;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        final Intent intent = getIntent();
        String apiKey = BuildConfig.YOUTUBE_API_KEY;

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.player);
        youTubePlayerView.initialize(apiKey, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                if (!wasRestored) {
                    mYouTubePlayer = youTubePlayer;
                    youTubePlayer.loadVideo(intent.getStringExtra(VIDEO_KEY));
                }
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

    }
}


