package com.zobonapp.ui.hotline;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srx.widget.PullToLoadView;
import com.zobonapp.R;
import com.zobonapp.ui.AdapterFactory;
import com.zobonapp.ui.Paginator;
import com.zobonapp.utils.QueryPreferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment
{
    private static final String TAG=CategoriesFragment.class.getSimpleName();
    private PullToLoadView pullToLoadView;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View result=inflater.inflate(R.layout.fragment_categories, container, false);
        pullToLoadView=result.findViewById(R.id.pullToLoadView);

        new Paginator(pullToLoadView,AdapterFactory.getAdapter(CategoryAdapter.class,getArguments()));
        return result;
    }

}
