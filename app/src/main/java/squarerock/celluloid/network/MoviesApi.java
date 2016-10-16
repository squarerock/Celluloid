package squarerock.celluloid.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import squarerock.celluloid.model.Credits;
import squarerock.celluloid.model.NowPlayingMovies;
import squarerock.celluloid.model.Trailers;

/**
 * Created by pranavkonduru on 10/11/16.
 */

public interface MoviesApi {

    @GET("3/movie/now_playing")
    Call<NowPlayingMovies> getNowPlayingMovies(@Query("api_key") String key);

    @GET("3/movie/{movie_id}/videos")
    Call<Trailers> getTrailerForMovie(@Path("movie_id") Integer id, @Query("api_key") String key);

    @GET("3/movie/{movie_id}/credits")
    Call<Credits> getCreditsForMovie(@Path("movie_id") Integer id, @Query("api_key") String key);
}
