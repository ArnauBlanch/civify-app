package com.civify.activity.registration;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.civify.model.ProfileIcon;

import java.util.ArrayList;

public class AvatarAdapter extends BaseAdapter {

    private static final int WIDTH = 250;

    private Context mContext;
    private ArrayList<ProfileIcon> mProfileIcons;

    public AvatarAdapter(Context context, ArrayList<ProfileIcon> profileIcons) {
        mContext = context;
        mProfileIcons = profileIcons;
        mProfileIcons.remove(0);
    }

    @Override
    public int getCount() {
        return mProfileIcons.size();
    }

    @Override
    public Object getItem(int position) {
        return mProfileIcons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(WIDTH, WIDTH));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mProfileIcons.get(position).getIcon());
        return imageView;
    }
}
