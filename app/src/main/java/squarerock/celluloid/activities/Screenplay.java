package squarerock.celluloid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import squarerock.celluloid.R;
import squarerock.celluloid.adapters.listeners.ItemClickListener;
import squarerock.celluloid.adapters.MoviesAdapter;
import squarerock.celluloid.model.Movie;
import squarerock.celluloid.model.NowPlayingMovies;
import squarerock.celluloid.network.MoviesApiService;
import squarerock.celluloid.utils.Constants;

/**
 * Created by pranavkonduru on 10/11/16.
 */

public class Screenplay extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        ItemClickListener.OnItemClickListener,
        Callback<NowPlayingMovies>{

    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView moviesView;
    @BindView(R.id.no_internet) TextView noInternetView;

    MoviesAdapter adapter;
    MoviesApiService movieApiService;
    private List<Movie> moviesNowPlaying;

    private static final String TAG = "Screenplay";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_screenplay);
        ButterKnife.bind(this);

        initSwipeToRefresh();
        initRecyclerView();

        movieApiService = new MoviesApiService();

        fetchMovies();
    }

    private void initSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        moviesView.setLayoutManager(new LinearLayoutManager(this));
        moviesView.addOnItemTouchListener(new ItemClickListener(this, moviesView, this));
        adapter = new MoviesAdapter(new ArrayList<>(), this);
        moviesView.setAdapter(adapter);
    }

    /**
     * Called when swipe-to-refresh is executed.
     */
    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: Fetching movies again");
        fetchMovies();
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Fetches movies and calls onResponse with the data.
     * In case of error onFailure will be called.
     */
    private void fetchMovies() {
        Log.d(TAG, "fetchMovies: ");
        movieApiService.getNowPlayingMovies(this);
    }

    /**
     * Called when an item in the recycler view is clicked
     * @param view the view that was clicked
     * @param position the position of that view
     */
    @Override
    public void onItemClick(View view, int position) {
        Movie movie = moviesNowPlaying.get(position);
        if(movie != null) {
            if(movie.getVoteAverage() <= 5 ) {
                Log.d(TAG, "Showing movie detail for: " + movie.getTitle());
                showMovieDetail(movie);
            } else {
                Log.d(TAG, "Playing video for: "+ movie.getTitle());
                playTrailer(movie);
            }
        }
    }

    /**
     * Launches the movie detail page
     * @param movie The movie for which the detail page needs to be shown
     */
    private void showMovieDetail(Movie movie) {
        Log.d(TAG, "showMovieDetail: For movie: "+movie.getTitle());
        Intent intent = new Intent(this, MovieDetail.class);
        intent.putExtra(Constants.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    /**
     * Plays a trailer for the movie
     * @param movie The movie for which the trailer needs to be played
     */
    private void playTrailer(Movie movie){
        Log.d(TAG, "playTrailer: For movie: "+movie.getTitle());
        Intent intent = new Intent(this, QuickPlay.class);
        intent.putExtra(Constants.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    /**
     * Called once Retrofit returns the data.
     * @param call Callback
     * @param response Response from Retrofit
     */
    @Override
    public void onResponse(Call<NowPlayingMovies> call, Response<NowPlayingMovies> response) {
        Log.d(TAG, "onResponse: ");
        moviesNowPlaying = response.body().getMovies();
        adapter.updateData(moviesNowPlaying);
    }

    /**
     * Called when Retrofit call fails
     * @param call Callback
     * @param t error
     */
    @Override
    public void onFailure(Call<NowPlayingMovies> call, Throwable t) {
        Log.e(TAG, "onFailure: "+t.getMessage());
        noInternetView.setVisibility(View.VISIBLE);
    }
}
