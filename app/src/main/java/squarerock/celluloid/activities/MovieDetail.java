package squarerock.celluloid.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import squarerock.celluloid.R;
import squarerock.celluloid.adapters.CreditsAdapter;
import squarerock.celluloid.adapters.listeners.ItemClickListener;
import squarerock.celluloid.adapters.TrailersAdapter;
import squarerock.celluloid.model.Credits;
import squarerock.celluloid.model.Movie;
import squarerock.celluloid.model.TrailerResults;
import squarerock.celluloid.model.Trailers;
import squarerock.celluloid.network.MoviesApiService;
import squarerock.celluloid.utils.Constants;
import squarerock.celluloid.utils.Utilities;

/**
 * Created by pranavkonduru on 10/15/16.
 */

public class MovieDetail extends AppCompatActivity implements
        ItemClickListener.OnItemClickListener,
        retrofit2.Callback<Trailers> {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.movie_background) ImageView movieBackgroundImage;
    @BindView(R.id.movie_detail_description) TextView movieDescription;
    @BindView(R.id.item_trailer) TextView trailerHeading;
    @BindView(R.id.rating_bar) RatingBar movieRating;
    @BindView(R.id.recycler_view_video_trailers) RecyclerView trailerRecyclerView;
    @BindView(R.id.recycler_view_credits) RecyclerView creditsRecyclerView;

    TrailersAdapter adapter;
    List<TrailerResults> trailerResults;
    Movie movieResult;
    MoviesApiService api;
    Credits credits;
    CreditsAdapter creditsAdapter;

    private static final String TAG = "MovieDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        initTrailerRecyclerView();
        initCreditsRecyclerView();

        processIntent(getIntent());
    }

    /**
     * Decides what to do based on the intent that started this activity
     * @param intent Intent that started this activity
     */
    private void processIntent(Intent intent) {
        Serializable movie = intent.getSerializableExtra(Constants.EXTRA_MOVIE);
        if (movie == null || !(movie instanceof Movie)) {
            Log.d(TAG, "onCreate: Finishing activity");
            finish();
        } else {
            movieResult = (Movie) movie;
            api = new MoviesApiService();
            api.getTrailerForMovie(movieResult.getId(), this);
        }
    }

    /**
     * Initialization of Credits recycler view
     */
    private void initCreditsRecyclerView() {
        creditsAdapter = new CreditsAdapter(this, new ArrayList<>());
        creditsRecyclerView.setAdapter(creditsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        creditsRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Initialization of trailers recycler view
     */
    private void initTrailerRecyclerView() {
        adapter = new TrailersAdapter(new ArrayList<>());
        trailerRecyclerView.setAdapter(adapter);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trailerRecyclerView.addOnItemTouchListener(new ItemClickListener(this, trailerRecyclerView, this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (movieResult != null) {
            Log.d(TAG, "onResume: Movie result is not null. Processing it");
            processMovieResult();
        }
    }

    /**
     * Processes the movie result
     */
    private void processMovieResult() {
        Log.d(TAG, "onCreate: movie instance of result for: "+movieResult.getId());

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        collapsingToolbarLayout.setTitle(movieResult.getTitle());

        String imagePath = Utilities.getImageBasedOnOrientation(this, movieResult);

        Picasso.with(this)
                .load(imagePath)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(movieBackgroundImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: ");
                        Bitmap bitmap = ((BitmapDrawable)movieBackgroundImage.getDrawable()).getBitmap();
                        Palette palette = Palette.from(bitmap).generate();

                        collapsingToolbarLayout
                                .setExpandedTitleColor(palette.getLightVibrantColor(getResources().getColor(R.color.white)));
                        collapsingToolbarLayout
                                .setContentScrimColor(palette.getLightVibrantColor(getResources().getColor(R.color.colorPrimary)));

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                            Window window = MovieDetail.this.getWindow();
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(palette.getLightVibrantColor(getResources().getColor(R.color.colorPrimary)));
                        }
                    }

                    @Override
                    public void onError() {
                        Picasso.with(MovieDetail.this)
                                .load(imagePath)
                                .error(R.drawable.movie_fallback)
                                .into(movieBackgroundImage);
                    }
                });


        movieDescription.setText(movieResult.getOverview());
        movieRating.setRating(movieResult.getVoteAverage().floatValue() / 2.0f);
    }

    /**
     * Called when Retrofit gives a response
     * @param call Callback
     * @param response Response object from the call
     */
    @Override
    public void onResponse(Call<Trailers> call, Response<Trailers> response) {
        Log.d(TAG, "onResponse: ");
        trailerResults = response.body().getTrailers();
        adapter.updateData(trailerResults);

        // If no trailers are not present, hide Trailers heading
        if(trailerResults.size() == 0){
            Log.d(TAG, "No trailers received. Hiding the trailers heading");
            trailerHeading.setVisibility(View.GONE);
        }

        // Fetch credits only if the earlier response was successful.
        fetchCredits();
    }

    /**
     * Fetches cast and crew
     */
    private void fetchCredits(){
        Log.d(TAG, "fetchCredits: ");
        api.getCreditsForMovie(movieResult.getId(), new retrofit2.Callback<Credits>() {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                Log.d(TAG, "onResponse: getting casts");
                credits = response.body();
                if(credits != null) {
                    creditsAdapter.updateData(credits.getCast());
                }
            }

            @Override
            public void onFailure(Call<Credits> call, Throwable t) {
                Log.d(TAG, "onFailure: getting casts");
            }
        });
    }

    /**
     * Called when call to Retrofit fails
     * @param call Callback
     * @param t Reason it failed
     */
    @Override
    public void onFailure(Call<Trailers> call, Throwable t) {
        Log.d(TAG, "onFailure: getting trailers: "+t.getMessage());
    }

    /**
     * Called when an item in Trailers view is clicked
     * @param view View that was clicked
     * @param position Position of that view
     */
    @Override
    public void onItemClick(View view, int position) {
        String videoId = trailerResults.get(position).getKey();
        Log.d(TAG, "onItemClick: "+videoId);
        playTrailer(videoId);
    }

    /**
     * Plays trailer for the video id that is given
     * @param videoId Video ID in Youtube-acceptable-format
     */
    private void playTrailer(String videoId){
        Intent intent = new Intent(this, QuickPlay.class);
        intent.putExtra(Constants.EXTRA_VIDEO_ID, videoId);
        startActivity(intent);
    }

}
