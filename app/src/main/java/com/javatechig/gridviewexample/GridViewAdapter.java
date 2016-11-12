package com.javatechig.gridviewexample;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class GridViewAdapter extends ArrayAdapter<Movies> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Movies> mGridData = new ArrayList<Movies>();

    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<Movies> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }

    public void setGridData(ArrayList<Movies> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Movies item = mGridData.get(position);
        if (layoutResourceId == R.layout.grid_item_layout) {
            holder.titleTextView.setText(item.getTitle());
            Picasso.with(mContext).load(item.getPosterPath())
                    .placeholder(R.drawable.user_placeholder_error)
                    .error(R.drawable.user_placeholder)
                    .into(holder.imageView);
        }
        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}