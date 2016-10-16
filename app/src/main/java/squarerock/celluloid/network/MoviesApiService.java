package squarerock.celluloid.network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import squarerock.celluloid.model.Credits;
import squarerock.celluloid.model.NowPlayingMovies;
import squarerock.celluloid.model.Trailers;
import squarerock.celluloid.utils.Constants;

/**
 * Created by pranavkonduru on 10/15/16.
 */

public class MoviesApiService {

    private MoviesApi api;

    public MoviesApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.MOVIE_DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(MoviesApi.class);
    }

    public void getNowPlayingMovies(Callback<NowPlayingMovies> nowPlayingResponse){
        Call<NowPlayingMovies> callback = api.getNowPlayingMovies(Constants.MOVIE_DB_API_KEY);
        callback.enqueue(nowPlayingResponse);
    }

    public void getTrailerForMovie(Integer id, Callback<Trailers> movieTrailerResponse){
        Call<Trailers> callback = api.getTrailerForMovie(id, Constants.MOVIE_DB_API_KEY);
        callback.enqueue(movieTrailerResponse);
    }

    public void getCreditsForMovie(Integer id, Callback<Credits> creditsCallback){
        Call<Credits> callback = api.getCreditsForMovie(id, Constants.MOVIE_DB_API_KEY);
        callback.enqueue(creditsCallback);
    }
}
