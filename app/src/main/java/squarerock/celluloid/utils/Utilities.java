package squarerock.celluloid.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import squarerock.celluloid.R;
import squarerock.celluloid.model.Movie;

/**
 * Created by pranavkonduru on 10/16/16.
 */

public class Utilities {

    private static final String TAG = "Utilities";

    /**
     * Loads Image into ImageView using Picasso. Images will be cached upon downloading.
     * @param context Context that Picasso uses
     * @param imagePath Full URL of Image
     * @param view ImageView where image will be loaded
     */
    public static final void loadImage(Context context, String imagePath, ImageView view){
        Log.d(TAG, "loadImage: ");
        Picasso.with(context)
                .load(imagePath)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .error(R.drawable.movie_fallback)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: ");
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "onError: ");
                        Picasso.with(context)
                                .load(imagePath)
                                .error(R.drawable.movie_fallback)
                                .into(view);
                    }
                });
    }


    /**
     * Get the genres for the integers
     * @param genres Genre Integer array
     * @return
     */
    public static String getGenreString(List<Integer> genres){
        StringBuilder stringBuilder = new StringBuilder();
        for(Integer integer : genres){
            stringBuilder.append(Utilities.getGenreForCategory(integer));
            stringBuilder.append(", ");
        }

        if(stringBuilder.length() > 2){
            stringBuilder.deleteCharAt(stringBuilder.length() -1);
            stringBuilder.deleteCharAt(stringBuilder.length() -1);
        }
        return stringBuilder.toString();
    }

    /**
     * If the orientation is portrait, poster image will be given. If Landscape, backdrop will be give.
     * @param context Context
     * @param movie Movie for which images need to be given back
     * @return Image based on orientation
     */
    public static String getImageBasedOnOrientation(Context context, Movie movie){
        if(Configuration.ORIENTATION_PORTRAIT == getOrientation(context)){
            return  Constants.IMAGE_BASE_URL+movie.getPosterPath();
        } else {
            return Constants.IMAGE_BASE_URL+movie.getBackdropPath();
        }
    }

    /**
     * Gives the orientation of the device. If API < 17, Portrait is returned.
     * @param context Context
     * @return Orientation of device
     */
    private static int getOrientation(Context context){
        int orientation = Configuration.ORIENTATION_PORTRAIT;
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            orientation = context.getResources().getConfiguration().orientation;
        }
        return orientation;
    }

    /**
     * Mapping of Genre ID to Genre category
     * @param genreInteger ID
     * @return Genre
     */
    private static String getGenreForCategory(Integer genreInteger){
        switch (genreInteger){
            case 28:
                return "Action";
            case 12:
                return "Adventure";
            case 16:
                return "Animation";
            case 99:
                return "Documentary";
            case 35:
                return "Comedy";
            case 80:
                return "Crime";
            case 18:
                return "Drama";
            case 10751:
                return "Family";
            case 14:
                return "Fantasy";
            case 36:
                return "History";
            case 27:
                return "Horror";
            case 10402:
                return "Music";
            case 9648:
                return "Mystery";
            case 10749:
                return "Romance";
            case 878:
                return "Science Fiction";
            case 10770:
                return "TV Movie";
            case 53:
                return "Thriller";
            case 10752:
                return "War";
            case 37:
                return "Western";
            default:
                Log.d(TAG, "getGenreForCategory: Unknown for: "+genreInteger);
                return "Unknown";
        }
    }
}
