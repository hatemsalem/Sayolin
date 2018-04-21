package com.zobonapp.ui;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zobonapp.BuildConfig;
import com.zobonapp.R;
import com.zobonapp.utils.ZobonApp;

import java.util.Locale;

/**
 * Created by Admin on 4/9/2018.
 */

public class ImageHolder extends ViewHolder<Integer>
{
    ImageView imgPage;
    private String rootPath;
    private String imgType;
    public ImageHolder(ViewGroup parent,String rootPath,String imgType)
    {
        super(parent,R.layout.cell_gallery);
        imgPage=itemView.findViewById(R.id.imgPage);
        this.rootPath=rootPath;
        this.imgType=imgType;
    }

    @Override
    public void bind(Integer position)
    {

        Uri uri=Uri.parse(String.format(Locale.US,"%s%03d.%s",rootPath,position+1,imgType));
        ZobonApp.getPicasso()
                .load(uri)
                .resize(280,0)
                .centerInside()
                .error(R.drawable.notfoundimage)
                .placeholder(R.drawable.placeholder   )
                .into(imgPage);
    }
}
