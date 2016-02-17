package com.peltashield.inferno.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.peltashield.inferno.MainActivity;

/**
 * Created by javier on 12/16/14.
 */
public class ImageAdapter extends BaseAdapter {

    private MainActivity mContext;
    private int[] mCardIds;

    public ImageAdapter(Context c, int[] ids) {
        mContext = (MainActivity) c;
        mCardIds = ids;
    }

    @Override
    public int getCount() {
        return mCardIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mCardIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) { // if it's not recycled
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }
        mContext.displayCardImage(mCardIds[position], imageView, false, true);
        return imageView;
    }

    public void remove(int position) {
        mCardIds = ArrayCommons.remove(mCardIds, position);
        notifyDataSetChanged();
    }

}
