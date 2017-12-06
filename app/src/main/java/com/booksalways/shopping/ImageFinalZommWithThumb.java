package com.booksalways.shopping;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created by welcome on 13-10-2016.
 */
public class ImageFinalZommWithThumb extends AppCompatActivity {
    public static final String TAG = "GalleryActivity";

    ImageButton btn_close;
    ViewPager pager;
    LinearLayout thumbnails;
    String name = "";

    private GalleryPagerAdapter adapter;

    public static ArrayList<String> _images = new ArrayList<String>();
    ArrayList<String> _imagesSmall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinch_zoom_view);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            name = getIntent().getExtras().getString("productName");
            _images = (ArrayList<String>) getIntent().getStringArrayListExtra("imageListLarge");
            _imagesSmall = (ArrayList<String>) getIntent().getStringArrayListExtra("imageListSmall");
        }
        initToolbar();
        FetchXmlID();

        Assert.assertNotNull(_images);
        adapter= new GalleryPagerAdapter(this);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(6); // how many images to load into memory
        adapter.notifyDataSetChanged();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void FetchXmlID() {

        btn_close=(ImageButton)findViewById(R.id.btn_close);
        pager=(ViewPager)findViewById(R.id.pager);
        thumbnails=(LinearLayout)findViewById(R.id.thumbnails);
    }
    private void initToolbar() {



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText(name);
        setSupportActionBar(toolbar);



    }

    class GalleryPagerAdapter extends PagerAdapter {

        Context _context;
        LayoutInflater _inflater;

        public GalleryPagerAdapter(Context context) {
            _context = context;
            _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return _images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = _inflater.inflate(R.layout.pager_gallery_item, container, false);
            container.addView(itemView);


            int borderSize = thumbnails.getPaddingTop();


            int thumbnailSize = ((FrameLayout.LayoutParams)
                    pager.getLayoutParams()).bottomMargin - (borderSize * 2);

            // Set the thumbnail layout parameters. Adjust as required
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize);
            params.setMargins(0, 0, borderSize, 0);


            final ImageView thumbView = new ImageView(_context);
            thumbView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            thumbView.setLayoutParams(params);
            thumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Thumbnail clicked");

                    // Set the pager position when thumbnail clicked
                    pager.setCurrentItem(position);
                }
            });
            thumbnails.addView(thumbView);

            final customclass.TouchImageView imageView =
                    (customclass.TouchImageView) itemView.findViewById(R.id.image);

            // Asynchronously load the image and set the thumbnail and pager view
            Glide.with(_context)
                    .load(_images.get(position))
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            imageView.setImageBitmap(bitmap);
                            thumbView.setImageBitmap(bitmap);
                        }
                    });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        _images.clear();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
