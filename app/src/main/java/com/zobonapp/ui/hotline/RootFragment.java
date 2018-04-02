package com.zobonapp.ui.hotline;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zobonapp.R;
import com.zobonapp.utils.BasicFragment;
import com.zobonapp.utils.QueryPreferences;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RootFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RootFragment extends BasicFragment
{
    private static final String ARG_CATEGORIES_ARGS="categoriesArgs";
    private static final String ARG_ITEMS_ARGS="itemsArgs";
    private static final String ARG_VIEW_TYPE_KEY="viewTypeKey";
    private static final String TAG = RootFragment.class.getSimpleName();

    private Fragment itemsFragment;
    private Fragment categoriesFragment;
    private String viewTypeKey;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RootFragment.
     */
    // TODO: Rename and change types and number of parameters


    public static RootFragment newInstance(Bundle categoriesArgs,Bundle itemsArgs,String viewTypeKey)
    {
        RootFragment fragment = new RootFragment();

        Bundle args = new Bundle();
        args.putString(ARG_VIEW_TYPE_KEY,viewTypeKey);
        args.putParcelable(ARG_CATEGORIES_ARGS,categoriesArgs);
        args.putParcelable(ARG_ITEMS_ARGS,itemsArgs);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args=(Bundle) getArguments().get(ARG_ITEMS_ARGS);
        itemsFragment=ItemsFragment.newInstance(args);
        args=(Bundle)getArguments().get(ARG_CATEGORIES_ARGS);
//        categoriesFragment=CategoriesFragment.newInstance(args);
        categoriesFragment=ItemsFragment.newInstance(args);
        viewTypeKey=getArguments().getString(ARG_VIEW_TYPE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View result= inflater.inflate(R.layout.fragment_root, container, false);
        final FloatingActionButton fab = (FloatingActionButton) result.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentTransaction transaction=getChildFragmentManager().beginTransaction();
                switch (QueryPreferences.getViewType(viewTypeKey))
                {
                    case CATEGORY:

                        transaction.replace(R.id.rootFrame, itemsFragment).commit();
                        QueryPreferences.setViewType(viewTypeKey,QueryPreferences.ViewType.ITEM);
                        break;
                    case ITEM:

                        transaction.replace(R.id.rootFrame, categoriesFragment).commit();
                        QueryPreferences.setViewType(viewTypeKey,QueryPreferences.ViewType.CATEGORY);
                        break;
                }
                updateSubtitle();

            }
        });
        FragmentTransaction transaction=getChildFragmentManager().beginTransaction();
        switch (QueryPreferences.getViewType(viewTypeKey))
        {
            case ITEM:
                transaction.replace(R.id.rootFrame, itemsFragment).commit();
                break;
            case CATEGORY:
                transaction.replace(R.id.rootFrame, categoriesFragment).commit();
                break;
        }
        return result;
    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu)
    {
        super.setHasOptionsMenu(hasMenu);
        itemsFragment.setHasOptionsMenu(hasMenu);
        categoriesFragment.setHasOptionsMenu(hasMenu);
    }
    public void updateSubtitle()
    {
        if(getUserVisibleHint())
        {
            switch (QueryPreferences.getViewType(viewTypeKey))
            {
                case ITEM:
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Items");
                    break;
                case CATEGORY:
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Categories");
                    break;
            }
        }

    }
}