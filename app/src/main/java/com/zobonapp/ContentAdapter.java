package com.zobonapp;

import android.app.Activity;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.zobonapp.ui.hotline.EntityAdapter;
import com.zobonapp.ui.hotline.CategoryAdapter;
import com.zobonapp.ui.hotline.FavoriteEntityAdapter;
import com.zobonapp.ui.hotline.ItemsFragment;
import com.zobonapp.ui.hotline.MenuAdapter;
import com.zobonapp.ui.hotline.OfferCategoryAdapter;
import com.zobonapp.ui.hotline.OffersAdapter;
import com.zobonapp.ui.hotline.RootFragment;
import com.zobonapp.utils.BasicActivity;
import com.zobonapp.utils.BasicFragment;
import com.zobonapp.utils.ZobonApp;


/**
 * Created by hasalem on 11/12/2017.
 */

public class ContentAdapter extends FragmentStatePagerAdapter
{
    Fragment fragments[]={
            RootFragment.newInstance(CategoryAdapter.newArguments(1001,"cat_hotline"), EntityAdapter.newArguments(null,"root"),"hotlines"),
            RootFragment.newInstance( OfferCategoryAdapter.newArguments(1001,"cat_offer"), OffersAdapter.newArguments(1),"offers"),
            RootFragment.newInstance( CategoryAdapter.newArguments(0,"cat_menus"), MenuAdapter.newArguments(1),"menus"),
//            RootFragment.newInstance( FavoriteCategoryAdapter.newArguments(2000),FavoriteEntityAdapter.newArguments(null,"favorite"),"menus"),
            ItemsFragment.newInstance(FavoriteEntityAdapter.newArguments(null,"favorite"))

//            FavoriteFragment.newInstance( CategoryAdapter.newArguments(0),FavoriteEntityAdapter.newArguments(null,"favorite")),
//            FavoriteFragment.newInstance(),
//            ItemsFragment.newInstance(FavoriteEntityAdapter.newArguments(null,"menu"),FavoriteEntityAdapter.class),
//            ContentFragment.newInstance("file:///android_asset/misc/initial2.json"),


    };
    private Fragment currentFragment;
    Activity ctx;
    private String items[] = ZobonApp.getContext().getResources().getStringArray(R.array.titles);

    public ContentAdapter(BasicActivity ctx)
    {
        super(ctx.getSupportFragmentManager());

        this.ctx = ctx;
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments[position];
    }

    @Override
    public int getCount()
    {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return items[position];
    }

    @Override
    public int getItemPosition(Object object)
    {
        return super.getItemPosition(object);
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object)
    {
        super.setPrimaryItem(container, position, object);
        if(((Fragment)object).getActivity()==null)return;
        if(currentFragment!=object)
        {
            if(currentFragment!=null)
                currentFragment.setHasOptionsMenu(false);
            currentFragment=(Fragment)object;
            currentFragment.setHasOptionsMenu(true);
            currentFragment.getActivity().invalidateOptionsMenu();
            if(currentFragment instanceof BasicFragment)
            {
                ((BasicFragment)currentFragment).updateSubtitle();
            }

        }
    }
}
