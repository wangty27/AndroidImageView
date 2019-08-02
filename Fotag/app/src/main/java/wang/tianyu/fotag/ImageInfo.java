package wang.tianyu.fotag;

import android.graphics.Bitmap;

public class ImageInfo {
    public int id;
    public float rating;
    public Bitmap image;

    public ImageInfo(Bitmap i, int id) {
        this.id = id;
        this.image = i;
        this.rating = 0;
    }
}
