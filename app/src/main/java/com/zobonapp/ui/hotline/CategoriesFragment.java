package com.zobonapp.ui.hotline;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.srx.widget.PullToLoadView;
import com.zobonapp.R;
import com.zobonapp.ui.AdapterFactory;
import com.zobonapp.ui.GenericPagerAdapter;
import com.zobonapp.ui.Paginator;
import com.zobonapp.utils.QueryPreferences;
import com.zobonapp.utils.ZobonApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String TAG=CategoriesFragment.class.getSimpleName();
    private static final String ARG_ADAPTER_CLASS = "adapterClass";
    private PullToLoadView pullToLoadView;
    private Paginator paginator;
    private GenericPagerAdapter adapter;
    public static CategoriesFragment newInstance(Bundle arguments)
    {
        CategoriesFragment fragment = new CategoriesFragment();

        Bundle args=new Bundle();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View result=inflater.inflate(R.layout.fragment_categories, container, false);
        pullToLoadView=result.findViewById(R.id.pullToLoadView);
        paginator= new Paginator(pullToLoadView,AdapterFactory.getAdapter(CategoryAdapter.class,getArguments()));
        ZobonApp.getContext().getPrefs().registerOnSharedPreferenceChangeListener(this);
        return result;

    }

    @Override
    public void onDestroyView()
    {
        ZobonApp.getContext().getPrefs().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroyView();
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if(TextUtils.equals(key,"showLargeDisplay"))
        {
            paginator.reLayout();
        }
    }
}
