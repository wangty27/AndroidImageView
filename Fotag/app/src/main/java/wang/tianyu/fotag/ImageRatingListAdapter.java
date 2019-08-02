package wang.tianyu.fotag;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ImageRatingListAdapter extends RecyclerView.Adapter<ImageRatingCard> {
    private List<ImageInfo> imageList;
    private ImagePayload p;

    public ImageRatingListAdapter(List<ImageInfo> l, ImagePayload p) {
        Log.d("Adapter", "constructor");
        this.imageList = l;
        this.p = p;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ImageRatingCard onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("Adapter", "onCreateViewHolder");
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_card, parent, false);
        ImageRatingCard card = new ImageRatingCard(v, this.p);
        return card;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ImageRatingCard holder, int position) {
        Log.d("Adapter", "onBindViewHolder: " + position);
        holder.imageView.setImageBitmap(this.imageList.get(position).image);
        holder.ratingBar.setRating(this.imageList.get(position).rating);
        holder.id = this.imageList.get(position).id;
        Log.d("Adapter", "onBindViewHolder target Id: " + holder.id);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.imageList.size();
    }
}
