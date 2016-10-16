package squarerock.celluloid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import squarerock.celluloid.R;
import squarerock.celluloid.model.Cast;
import squarerock.celluloid.utils.Constants;
import squarerock.celluloid.utils.Utilities;

/**
 * Created by pranavkonduru on 10/16/16.
 */

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.CreditsViewHolder>{

    private List<Cast> castList;
    private Context context;

    public CreditsAdapter(Context context, List<Cast> castList) {
        this.castList = castList;
        this.context = context;
    }

    @Override
    public CreditsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_cast, null, false);
        return new CreditsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CreditsViewHolder holder, int position) {
        Cast cast = castList.get(position);
        Utilities.loadImage(context, Constants.IMAGE_BASE_URL+cast.getProfilePath(), holder.castImage);
        holder.originalName.setText(cast.getName());
        holder.movieName.setText(cast.getCharacter());
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public void updateData(List<Cast> cast) {
        castList.clear();
        castList.addAll(cast);
        notifyItemRangeChanged(0, cast.size());
    }

    public static class CreditsViewHolder extends RecyclerView.ViewHolder {
        private ImageView castImage;
        private TextView originalName, movieName;

        public CreditsViewHolder(View itemView) {
            super(itemView);
            castImage = (ImageView) itemView.findViewById(R.id.cast_image);
            originalName = (TextView) itemView.findViewById(R.id.cast_original_name);
            movieName = (TextView) itemView.findViewById(R.id.cast_movie_name);
        }
    }
}
