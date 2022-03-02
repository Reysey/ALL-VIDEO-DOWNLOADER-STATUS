package com.infusiblecoder.allinonevideodownloader.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.infusiblecoder.allinonevideodownloader.databinding.ActivityFullViewBinding;


public class FullImageActivity extends AppCompatActivity {

    private ActivityFullViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityFullViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initViews();

        if (getIntent().getStringExtra("myimgfile") != null && getIntent().getStringExtra("isfbimage") != null) {
            String filepath = getIntent().getStringExtra("myimgfile");
            String is = getIntent().getStringExtra("isfbimage");
            if (is.equals("true")) {
                showImage(filepath);
            } else {
                Glide.with(this)
                        .load(filepath)
                        .into(binding.imageviewFullimg);
            }
        } else if (getIntent().getStringExtra("myimgfile") != null) {
            String filepath = getIntent().getStringExtra("myimgfile");
            Glide.with(this)
                    .load(filepath)
                    .into(binding.imageviewFullimg);

        }


    }


    public void showImage(String str) {
        Glide.with(this).asBitmap().load(str).addListener(new RequestListener<Bitmap>() {
            public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
                Toast.makeText(FullImageActivity.this, "Cant get image", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                FullImageActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        binding.imageviewFullimg.setImageBitmap(resource);
                    }
                });
                return false;
            }


        }).submit();
    }

    public void initViews() {
        binding.imgclose.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
