package com.zobonapp.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.transition.GestureTransitions;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;
import com.alexvasilkov.gestures.transition.tracker.SimpleTracker;
import com.zobonapp.R;
import com.zobonapp.utils.BasicActivity;

/**
 * Created by Admin on 4/17/2018.
 */

public class GalleryActivity3 extends BasicActivity
{


    private static final String ROOT_PATH="rootPath";
    private static final String PAGES="pages";
    private static final String IMAGE_TYPE="imageType";
    private static final String OFFER_NAME="offerName";

    private String rootPath;
    private String imageType;
    private int pages;
    private String offerName;

    private RecyclerView list;
    private ViewPager pager;
    private View background;
    private PagerAdapter pagerAdapter;
    private ViewsTransitionAnimator<Integer> animator;


    public static void start(Context ctx, String baseUrl, String offerName, int pages, String imgType)
    {
        Intent intent=new Intent(ctx, GalleryActivity3.class);
        intent.putExtra(ROOT_PATH,baseUrl);
        intent.putExtra(PAGES,pages);
        intent.putExtra(IMAGE_TYPE,imgType);
        intent.putExtra(OFFER_NAME,offerName);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery3);

        pages=getIntent().getIntExtra(PAGES,0);
        rootPath=getIntent().getStringExtra(ROOT_PATH);
        imageType=getIntent().getStringExtra(IMAGE_TYPE);
        offerName=getIntent().getStringExtra(OFFER_NAME);

        // Initializing ListView
        list = findViewById(R.id.recycler_list);
        list.setLayoutManager(new StaggeredGridLayoutManager(2,1));
        list.setAdapter(new RecyclerAdapter(pages,rootPath, this::onPaintingClick));

        // Initializing ViewPager
        pager = findViewById(R.id.recycler_pager);
        pagerAdapter = new PagerAdapter(pager, pages,rootPath);
        pager.setAdapter(pagerAdapter);
        pager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.view_pager_margin));
        pager.setOffscreenPageLimit(3);

        // Initializing images animator. It requires us to provide FromTracker and IntoTracker items
        // that are used to find images views for particular item IDs in the list and in the pager
        // to keep them in sync.
        // In this example we will use SimpleTracker which will track images by their positions,
        // if you have a more complicated case see further examples.
        final SimpleTracker listTracker = new SimpleTracker() {
            @Override
            public View getViewAt(int position) {
                RecyclerView.ViewHolder holder = list.findViewHolderForLayoutPosition(position);
                return holder == null ? null : RecyclerAdapter.getImageView(holder);
            }
        };

        final SimpleTracker pagerTracker = new SimpleTracker() {
            @Override
            public View getViewAt(int position) {
                RecyclePagerAdapter.ViewHolder holder = pagerAdapter.getViewHolder(position);
                return holder == null ? null : PagerAdapter.getImageView(holder);
            }
        };

        animator = GestureTransitions.from(list, listTracker).into(pager, pagerTracker);

        // Setting up background animation during image transition
        background = findViewById(R.id.recycler_full_background);
        animator.addPositionUpdateListener((pos, isLeaving) -> applyImageAnimationState(pos));
    }
    @Override
    public void onBackPressed() {
        // We should leave full image mode instead of closing the screen
        if (!animator.isLeaving()) {
            animator.exit(true);
        } else {
            super.onBackPressed();
        }
    }

    private void applyImageAnimationState(float position) {
        background.setVisibility(position == 0f ? View.INVISIBLE : View.VISIBLE);
        background.setAlpha(position);
    }
    private void onPaintingClick(int position) {
        // Animating image transition from given list position into pager
        animator.enter(position, true);
    }
}
