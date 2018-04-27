package com.zobonapp.ui;

import android.os.AsyncTask;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;

import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;
import com.zobonapp.utils.QueryPreferences;
import com.zobonapp.utils.ZobonApp;

import java.util.List;

/**
 * Created by hasalem on 24/12/2017.
 */

public class Paginator
{

    private PullToLoadView pullToLoadView;
    private GenericPagerAdapter adapter;
    private boolean isLoading;
    private boolean hasLoadingAll;
    private int nextPage;
    private int columnWidth;

    public Paginator(PullToLoadView pullToLoadView, GenericPagerAdapter adapter,RecyclerView.LayoutManager layoutManager)
    {
        this.adapter=adapter;
        this.pullToLoadView = pullToLoadView;
        this.columnWidth=columnWidth;
        RecyclerView recyclerView =pullToLoadView.getRecyclerView();
//        layoutManager=new GridLayoutManager(pullToLoadView.getContext(), ZobonApp.calculateColumns(columnWidth));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_DEFAULT
        );
        initializePaginator();

    }
    private void initializePaginator()
    {
        pullToLoadView.isLoadMoreEnabled(true);
        pullToLoadView.setLoadMoreOffset(50);
        pullToLoadView.setPullCallback(new PullCallback()
        {
            @Override
            public void onLoadMore()
            {
                loadData(nextPage);
            }

            @Override
            public void onRefresh()
            {
                adapter.clear();
                hasLoadingAll=false;
                loadData(0);
            }

            @Override
            public boolean isLoading()
            {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems()
            {
                return hasLoadingAll;
            }
        });
        pullToLoadView.initLoad();
    }
    private  void  loadData(final int page)
    {
        isLoading=true;


        new AsyncTask<Void, Void, List<?>>()
        {
            @Override
            protected List<?> doInBackground(Void... voids)
            {
                Log.e("Search Issue:",String.format("Query:%s----Page:%d",adapter.searchQuery,page));
                return adapter.loadData(page);

            }

            @Override
            protected void onPostExecute(List<?> entities)
            {
                isLoading = false;


                if (entities.size() > 0)
                {
                    adapter.add(entities);
                    nextPage = page + 1;
                }

                //                pullToLoadView.setComplete();
                //TODO: to be removed. this postdelayed is used because the post of setcomplet in the pulltoload
                pullToLoadView.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        pullToLoadView.setComplete();
                    }
                }, 100);

            }
        }.execute();
    }
//    private class LoaderTask extends AsyncTask<Void, Void, List<?>>
//    {
//        @Override
//        protected List<?> doInBackground(Void... voids)
//        {
//            return adapter.loadData(page);
//
//        }
//
//        @Override
//        protected void onPostExecute(List<?> items)
//        {
//            isLoading = false;
//
//
//            if (items.size() > 0)
//            {
//                adapter.add(items);
//                nextPage = page + 1;
//            }
//
//            //                pullToLoadView.setComplete();
//            //TODO: to be removed. this postdelayed is used because the post of setcomplet in the pulltoload
//            pullToLoadView.postDelayed(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    pullToLoadView.setComplete();
//                }
//            }, 100);
//
//        }
//    }
}
