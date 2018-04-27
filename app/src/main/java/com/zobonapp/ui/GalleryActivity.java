package com.zobonapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zobonapp.R;
import com.zobonapp.utils.BasicActivity;

/**
 * Created by Admin on 4/9/2018.
 */

public class GalleryActivity extends BasicActivity
{
    private static final String ROOT_PATH="rootPath";
    private static final String PAGES="pages";
    private static final String IMAGE_TYPE="imageType";
    private String rootPath;
    private String imageType;
    private int pages;
    private RecyclerView gallery;
    public static Intent newIntent(Context ctx, String rootPath, int pages,String imageType)
    {
        Intent intent=new Intent(ctx,GalleryActivity.class);
        intent.putExtra(ROOT_PATH,rootPath);
        intent.putExtra(PAGES,pages);
        intent.putExtra(IMAGE_TYPE,imageType);
        return intent;
    }
    public static void start(Context ctx, String rootPath, int pages,String imageType)
    {
        ctx.startActivity(newIntent(ctx,rootPath,pages,imageType));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pages=getIntent().getIntExtra(PAGES,0);
        rootPath=getIntent().getStringExtra(ROOT_PATH);
        imageType=getIntent().getStringExtra(IMAGE_TYPE);
        setContentView(R.layout.activity_gallery);
        gallery=findViewById(R.id.gallery);
        gallery.setLayoutManager(new StaggeredGridLayoutManager(2,1));
//        gallery.setItemViewCacheSize(20);
//        gallery.setDrawingCacheEnabled(true);
//        gallery.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        gallery.setAdapter(new RecyclerView.Adapter<ImageHolder>()
        {
            @NonNull
            @Override
            public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {

                return new ImageHolder(parent,rootPath,imageType);
            }

            @Override
            public void onBindViewHolder(@NonNull ImageHolder holder, int position)
            {
                holder.bind(position);
            }

            @Override
            public int getItemCount()
            {
                return pages;
            }
        });
    }
}
