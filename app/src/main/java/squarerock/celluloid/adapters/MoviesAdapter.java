package squarerock.celluloid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import squarerock.celluloid.R;
import squarerock.celluloid.model.Movie;
import squarerock.celluloid.utils.Utilities;

/**
 * Created by pranavkonduru on 10/12/16.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>{

    private List<Movie> moviesPlaying;
    private Context context;
    private static final String TAG = "MoviesAdapter";
    private final int POPULAR_MOVIE = 1;
    private final int NOT_POPULAR_MOVIE = 2;

    public MoviesAdapter(List<Movie> moviesPlaying, Context context) {
        this.moviesPlaying = moviesPlaying;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (moviesPlaying.get(position).getVoteAverage() <= 5) {
            return NOT_POPULAR_MOVIE;
        } else {
            return POPULAR_MOVIE;
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: viewType: "+viewType);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View movieItemView;
        if (viewType == NOT_POPULAR_MOVIE) {
            movieItemView = inflater.inflate(R.layout.item_movie_not_popular, parent, false);
        } else {
            movieItemView = inflater.inflate(R.layout.item_movie_popular, parent, false);
        }

        return new MovieViewHolder(movieItemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movieResult = moviesPlaying.get(position);

        holder.title.setText(movieResult.getTitle());
        holder.language.setText(movieResult.getOriginalLanguage());
        holder.genres.setText(Utilities.getGenreString(movieResult.getGenreIds()));
        holder.description.setText(movieResult.getOverview());

        String imagePath = Utilities.getImageBasedOnOrientation(context, movieResult);
        Utilities.loadImage(context, imagePath, holder.coverImage);

        holder.rating.setText(String.format("%s/10 based on %s votes", movieResult.getVoteAverage().toString(), movieResult.getVoteCount().toString()));
    }

    @Override
    public int getItemCount() {
        return moviesPlaying.size();
    }

    public void updateData(List<Movie> movies) {
        moviesPlaying.clear();
        moviesPlaying.addAll(movies);
        notifyItemRangeChanged(0, movies.size());
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder{

        public ImageView coverImage;
        public TextView title, genres, rating, language, description;

        public MovieViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.movie_title);
            language = (TextView) itemView.findViewById(R.id.movie_language);
            genres = (TextView) itemView.findViewById(R.id.movie_genres);
            description = (TextView) itemView.findViewById(R.id.movie_description);
            coverImage = (ImageView) itemView.findViewById(R.id.movie_poster);
            rating = (TextView) itemView.findViewById(R.id.movie_rating);
        }
    }
}
