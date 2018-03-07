package com.zobonapp.ui;

import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;

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

    public Paginator(PullToLoadView pullToLoadView, GenericPagerAdapter adapter)
    {
        this.adapter=adapter;
        this.pullToLoadView = pullToLoadView;
        RecyclerView recyclerView =pullToLoadView.getRecyclerView();
        recyclerView.setLayoutManager(new GridLayoutManager(pullToLoadView.getContext(),5));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        initializePaginator();

    }
    private void initializePaginator()
    {
        pullToLoadView.isLoadMoreEnabled(true);
        pullToLoadView.setLoadMoreOffset(40);
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
                return adapter.loadData(page);
            }

            @Override
            protected void onPostExecute(List<?> entities)
            {
                //UPDATE PROPETIES
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

}
