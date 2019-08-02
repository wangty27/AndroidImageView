package wang.tianyu.fotag;

import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

public class ImageRatingCard extends RecyclerView.ViewHolder {
    public CardView cardView;
    public ImageView imageView;
    public RatingBar ratingBar;
    public int id;
    private ImagePayload p;
    public ImageRatingCard(CardView v, ImagePayload p) {
        super(v);
        this.cardView = v;
        LinearLayout L = (LinearLayout) v.getChildAt(0);
        this.imageView = (ImageView) L.getChildAt(0);
        this.ratingBar = (RatingBar) L.getChildAt(1);
        this.p = p;

        this.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d("Card RatingChange", "Id: " + ImageRatingCard.this.id);
                if (fromUser) {
                    ImageRatingCard.this.p.ImageRatingChange(ImageRatingCard.this.id, rating);
                }
            }
        });

        this.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageRatingCard.this.p.ImageViewClick(ImageRatingCard.this.id);
            }
        });
    }
}
