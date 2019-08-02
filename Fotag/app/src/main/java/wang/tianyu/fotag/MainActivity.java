package wang.tianyu.fotag;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView imageRatingListView;
    private RecyclerView.Adapter imageRatingListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ImageInfo> imageInfoList;
    private List<ImageInfo> displayList;
    private List<String> imageUrlList;
    private ImagePayload imagePayload; // used for communicating with ImageCard
    private boolean imageListVisible = false;
    private boolean loadingSpinnerVisible = false;
    private float filterValue;
    private int imageFocusIndex;

    private class FetchImages extends AsyncTask<List<String>, Void, List<ImageInfo>> {
        @Override
        protected List<ImageInfo> doInBackground(List<String>... params) {
            List<String> urlList = params[0];
            List<ImageInfo> result = new ArrayList<>();
            int id = 0;
            for (String url: urlList) {
                try {
                    URL iUrl = new URL(url);
                    Bitmap bmp = BitmapFactory.decodeStream(iUrl.openConnection().getInputStream());
                    ImageInfo info = new ImageInfo(bmp, id);
                    id += 1;
                    result.add(info);
                } catch (Exception e) {
                    Log.e("ImageFetchError", e.getMessage());
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            RecyclerView imageList = findViewById(R.id.imageListView);
            imageList.setVisibility(View.GONE);
            MainActivity.this.imageListVisible = false;
            MainActivity.this.loadingSpinnerVisible = true;
            FrameLayout spinner = findViewById(R.id.loadingSpinner);
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<ImageInfo> result) {
            RecyclerView imageList = findViewById(R.id.imageListView);
            imageList.setVisibility(View.VISIBLE);
            MainActivity.this.imageListVisible = true;
            MainActivity.this.loadingSpinnerVisible = false;
            FrameLayout spinner = findViewById(R.id.loadingSpinner);
            spinner.setVisibility(View.GONE);
            MainActivity.this.imageListUpdate(result);
        }
    }

    private void imageListUpdate(List<ImageInfo> list) {
        this.imageInfoList = list;
        this.updateDisplayList(true);
        Log.d("FetchImage", "Display: " + this.displayList.size());
    }

    private void configView(int span) {
        this.imageRatingListView = findViewById(R.id.imageListView);

        if (this.imageListVisible) {
            this.imageRatingListView.setVisibility(View.VISIBLE);
        } else {
            this.imageRatingListView.setVisibility(View.GONE);
        }

        if (this.loadingSpinnerVisible) {
            FrameLayout spinner = findViewById(R.id.loadingSpinner);
            spinner.setVisibility(View.VISIBLE);
        } else {
            FrameLayout spinner = findViewById(R.id.loadingSpinner);
            spinner.setVisibility(View.GONE);
        }

        // use a grid layout manager
        this.layoutManager = new GridLayoutManager(this, span);
        this.imageRatingListView.setLayoutManager(this.layoutManager);

        // specify an adapter
        this.imageRatingListAdapter = new ImageRatingListAdapter(this.displayList, this.imagePayload);
        this.imageRatingListView.setAdapter(this.imageRatingListAdapter);

        ImageButton refreshBtn = findViewById(R.id.refreshListBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BtnClick", "RefreshList");
                MainActivity.this.filterValue = 0;
                RatingBar filterBar = findViewById(R.id.filterRatingBar);
                filterBar.setRating(0);
                new FetchImages().execute(MainActivity.this.imageUrlList);
            }
        });

        ImageButton clearBtn = findViewById(R.id.clearListBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BtnClick", "ClearList");
                MainActivity.this.filterValue = 0;
                RatingBar filterBar = findViewById(R.id.filterRatingBar);
                filterBar.setRating(0);
                MainActivity.this.displayList.clear();
                MainActivity.this.imageInfoList.clear();
                RecyclerView imageList = findViewById(R.id.imageListView);
                imageList.setVisibility(View.GONE);
                MainActivity.this.imageListVisible = false;
                MainActivity.this.imageRatingListAdapter.notifyDataSetChanged();
            }
        });

        RatingBar filterBar = findViewById(R.id.filterRatingBar);
        Log.d("FilterValue", "Value: " + this.filterValue);
        filterBar.setRating(this.filterValue);
        filterBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d("FilterChange", "Value: " + rating);
                MainActivity.this.filterValue = rating;
                MainActivity.this.updateDisplayList(true);
            }
        });

        final ImageView imageFocusView = findViewById(R.id.imageFocusView);
        if (this.imageFocusIndex >= 0) {
            imageFocusView.setVisibility(View.VISIBLE);
            imageFocusView.setImageBitmap(this.imageInfoList.get(this.imageFocusIndex).image);
        } else {
            imageFocusView.setVisibility(View.GONE);
        }
        imageFocusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ImageFocus", "click");
                imageFocusView.setVisibility(View.GONE);
                MainActivity.this.imageFocusIndex = -1;
            }
        });
    }

    private void updateDisplayList(boolean refresh) {
        this.displayList.clear();
        for(ImageInfo i: this.imageInfoList) {
            if (i.rating >= this.filterValue) {
                this.displayList.add(i);
            }
        }
        if (refresh) { this.imageRatingListAdapter.notifyDataSetChanged(); }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_vertical);

        this.imageInfoList = new ArrayList<>();
        this.displayList = new ArrayList<>();

        this.imageFocusIndex = -1;

        this.imagePayload = new ImagePayload() {
            @Override
            public void ImageViewClick(int id) {
                Log.d("Main ImageClick", "Id: " + id);
                ImageView imageFocusView = findViewById(R.id.imageFocusView);
                imageFocusView.setImageBitmap(MainActivity.this.imageInfoList.get(id).image);
                imageFocusView.setVisibility(View.VISIBLE);
                MainActivity.this.imageFocusIndex = id;
            }

            @Override
            public void ImageRatingChange(int id, float rating) {
                Log.d("Main RatingBarChange", "Id: " + id + " Value: " + rating);
                MainActivity.this.imageInfoList.get(id).rating = rating;
                MainActivity.this.updateDisplayList(true);
            }
        };

        this.filterValue = 0;

        this.imageUrlList = new ArrayList<>();
        // Add urls to imageList to parse the image

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.configView(2);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.configView(1);
        }
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("Orientation", "Landscape");
            this.setContentView(R.layout.main_horizontal);
            this.configView(2);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.d("Orientation", "Portrait");
            this.setContentView(R.layout.main_vertical);
            this.configView(1);
        }
    }
}
