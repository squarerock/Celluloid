package squarerock.celluloid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import squarerock.celluloid.R;
import squarerock.celluloid.model.Movie;
import squarerock.celluloid.model.Trailers;
import squarerock.celluloid.network.MoviesApiService;
import squarerock.celluloid.utils.Constants;

/**
 * Created by pranavkonduru on 10/15/16.
 */

public class QuickPlay extends YouTubeBaseActivity implements Callback<Trailers> {

    private static final String TAG = "QuickPlay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_quick_play);
        processIntent(getIntent());
    }

    /**
     * Processes the intent that was used to start this Activity
     * @param intent Intent that was used to start this Activity
     */
    private void processIntent(Intent intent) {
        Serializable movie = intent.getSerializableExtra(Constants.EXTRA_MOVIE);
        String videoId = intent.getStringExtra(Constants.EXTRA_VIDEO_ID);

        if(movie != null && movie instanceof Movie){
            fetchTrailers((Movie) movie);
        } else if(videoId != null){
            playVideo(videoId, false);
        }
    }

    /**
     * Fetches trailers for the movie that is specified
     * @param movie Movie for which trailers need to be fetched
     */
    private void fetchTrailers(Movie movie) {
        Log.d(TAG, "onCreate: Playing video trailer for movie: "+movie.getTitle());
        MoviesApiService api = new MoviesApiService();
        api.getTrailerForMovie(movie.getId(), this);
    }

    /**
     * Plays video for the video ID provided.
     * @param videoId Video that needs to be played. Should be in Youtube-acceptable-format
     * @param playImmediately If true, will play the video. If false, will cue the video.
     */
    private void playVideo(final String videoId, final boolean playImmediately) {
        Log.d(TAG, "playVideo: "+videoId);
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.player);

        youTubePlayerView.initialize(Constants.YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        if(playImmediately){
                            Log.d(TAG, "onInitializationSuccess: Playing Video");
                            youTubePlayer.loadVideo(videoId);
                        } else {
                            Log.d(TAG, "onInitializationSuccess: Cue-ing the video");
                            youTubePlayer.cueVideo(videoId);
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        Log.e(TAG, "onInitializationFailure: ");
                    }
                });
    }

    /**
     * Called by Retrofit when it successfully fetches trailers
     * @param call Callback
     * @param response Response which contains the trailers data
     */
    @Override
    public void onResponse(Call<Trailers> call, Response<Trailers> response) {
        Log.d(TAG, "onResponse: ");
        Trailers trailers = response.body();
        playVideo(trailers.getTrailers().get(0).getKey(), true);
    }

    /**
     * Called by Retrofit when it fails fetching trailers
     * @param call Callback
     * @param t Reason it failed
     */
    @Override
    public void onFailure(Call<Trailers> call, Throwable t) {
        Log.d(TAG, "onFailure: "+t.getMessage());
    }
}
