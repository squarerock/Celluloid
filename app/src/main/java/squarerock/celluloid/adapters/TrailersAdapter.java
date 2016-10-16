package squarerock.celluloid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import squarerock.celluloid.R;
import squarerock.celluloid.model.TrailerResults;

/**
 * Created by pranavkonduru on 10/15/16.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder>{

    private List<TrailerResults> listOfTrailers;

    public TrailersAdapter(List<TrailerResults> trailers) {
        this.listOfTrailers = trailers;
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_trailer, null, false);
        return new TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        TrailerResults result = listOfTrailers.get(position);
        holder.trailerName.setText(String.format("%s: %s", result.getType(), result.getName()));
        holder.trailerLanguage.setText(result.getIso6391());
    }

    @Override
    public int getItemCount() {
        return listOfTrailers.size();
    }

    public static class TrailersViewHolder extends RecyclerView.ViewHolder {
        public TextView trailerName, trailerLanguage;
        public TrailersViewHolder(View itemView) {
            super(itemView);
            trailerName = (TextView) itemView.findViewById(R.id.item_trailer_name);
            trailerLanguage = (TextView) itemView.findViewById(R.id.item_trailer_language);
        }
    }

    public void updateData(List<TrailerResults> trailers) {
        listOfTrailers.clear();
        listOfTrailers.addAll(trailers);
        notifyItemRangeChanged(0, trailers.size());
    }
}
