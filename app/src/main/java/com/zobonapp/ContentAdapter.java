package com.zobonapp;

import android.app.Activity;
import android.app.Fragment;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.zobonapp.ui.hotline.BusinessEntityAdapter;
import com.zobonapp.ui.hotline.CategoryAdapter;
import com.zobonapp.ui.hotline.FavoriteEntityAdapter;
import com.zobonapp.ui.hotline.RootFragment;
import com.zobonapp.utils.ZobonApp;


/**
 * Created by hasalem on 11/12/2017.
 */

public class ContentAdapter extends FragmentStatePagerAdapter
{
    Fragment fragments[]={
            RootFragment.newInstance(CategoryAdapter.newArguments(1001),BusinessEntityAdapter.newArguments(null,"root"),"hotlines"),
            RootFragment.newInstance( CategoryAdapter.newArguments(1),FavoriteEntityAdapter.newArguments(null,"favorite"),"menus"),
            RootFragment.newInstance( CategoryAdapter.newArguments(0),FavoriteEntityAdapter.newArguments(null,"favorite"),"favorites"),
//            FavoriteFragment.newInstance( CategoryAdapter.newArguments(0),FavoriteEntityAdapter.newArguments(null,"favorite")),
//            FavoriteFragment.newInstance(),
//            ItemsFragment.newInstance(FavoriteEntityAdapter.newArguments(null,"category"),FavoriteEntityAdapter.class),
//            ContentFragment.newInstance("file:///android_asset/misc/initial2.json"),
            ContentFragment.newInstance("file:///android_asset/misc/initial2.json")

    };
    private Fragment currentFragment;
    Activity ctx;
    private String items[] = ZobonApp.getContext().getResources().getStringArray(R.array.titles);

    public ContentAdapter(Activity ctx)
    {
        super(ctx.getFragmentManager());

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
        }
    }
}
