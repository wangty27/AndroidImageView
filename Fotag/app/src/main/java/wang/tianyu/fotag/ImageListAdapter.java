package wang.tianyu.fotag;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import java.util.List;

public class ImageListAdapter extends BaseAdapter {
    private List<ImageInfo> imageList;
    private ImagePayload p;
    private LayoutInflater inflater;

    public ImageListAdapter(Context applicationContext, List<ImageInfo> l, ImagePayload p) {
        this.imageList = l;
        this.p = p;
        this.inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return this.imageList.size();
    }

    @Override
    public ImageInfo getItem(int i) {
        return this.imageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.imageList.get(i).id;
    }

    @Override
    public CardView getView(int index, View view, ViewGroup parent) {
        CardView cardView = (CardView) this.inflater.inflate(R.layout.image_card, null);
        LinearLayout L = (LinearLayout) cardView.getChildAt(0);
        ImageView imageView = (ImageView) L.getChildAt(0);
        RatingBar ratingBar = (RatingBar) L.getChildAt(1);
        final int id = this.imageList.get(index).id;
        imageView.setImageBitmap(this.imageList.get(index).image);
        ratingBar.setRating(this.imageList.get(index).rating);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d("Card RatingChange", "Id: " + id);
                ImageListAdapter.this.p.ImageRatingChange(id, rating);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageListAdapter.this.p.ImageViewClick(id);
            }
        });

        return cardView;
    }

}