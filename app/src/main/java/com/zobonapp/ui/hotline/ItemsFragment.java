package com.zobonapp.ui.hotline;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.zobonapp.R;
import com.zobonapp.ui.AdapterFactory;
import com.zobonapp.ui.GenericPagerAdapter;
import com.zobonapp.ui.Paginator;
import com.srx.widget.PullToLoadView;
import com.zobonapp.utils.QueryPreferences;

//import android.support.v4.app.Fragment;

/**
 * Created by hasalem on 11/26/2017.
 */

public class ItemsFragment extends Fragment
{
    private static String TAG = ItemsFragment.class.getSimpleName();
    private static final String ARG_ADAPTER_CLASS = "adapterClass";
    private PullToLoadView pullToLoadView;
    private GenericPagerAdapter adapter;

        public static ItemsFragment newInstance(Bundle arguments, Class<? extends GenericPagerAdapter> clazz)
    {
        ItemsFragment fragment=new ItemsFragment();
        Bundle args=new Bundle();
        args.putAll(arguments);
        args.putString(ARG_ADAPTER_CLASS,clazz.getName());
        fragment.setArguments(args);
        return fragment;
    }
    public static ItemsFragment newInstance(Bundle arguments)
    {
        ItemsFragment fragment = new ItemsFragment();
        Bundle args = new Bundle();
        args.putAll(arguments);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            Class<? extends GenericPagerAdapter> clazz = Class.forName(getArguments().getString(ARG_ADAPTER_CLASS)).asSubclass(GenericPagerAdapter.class);
            adapter = AdapterFactory.getAdapter(clazz, getArguments());
        } catch (ClassNotFoundException e)
        {
            throw new IllegalArgumentException(e);
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_hotlines, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        String query = QueryPreferences.getSearchQuery(adapter.getSearchKey());
        if (!TextUtils.isEmpty(query))
        {
            searchView.setIconified(false);

            searchView.setQuery(query, true);
            adapter.setSearchQuery(query);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Log.d(TAG, "QueryTextSubmit:" + query);
                searchView.clearFocus();
                QueryPreferences.setSearchQuery(adapter.getSearchKey(), query);
                adapter.setSearchQuery(query);
                pullToLoadView.initLoad();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query)
            {
                Log.d(TAG, "QueryTextSubmit:" + query);
                QueryPreferences.setSearchQuery(adapter.getSearchKey(), query);
                adapter.setSearchQuery(query);
                pullToLoadView.initLoad();
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                if (!QueryPreferences.getSearchQuery().equals(searchView.getQuery().toString()))
                {
                    QueryPreferences.setSearchQuery(adapter.getSearchKey(), "");
                    adapter.setSearchQuery("");
                    pullToLoadView.initLoad();
                }
                return false;
            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View result = inflater.inflate(R.layout.fragment_hotlines, container, false);
        pullToLoadView = result.findViewById(R.id.pullToLoadView);

        new Paginator(pullToLoadView, adapter);

        return result;
    }


}
