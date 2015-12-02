package com.r_mobile.phasebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.r_mobile.Category;

import java.util.List;

/**
 * Created by nemol on 12.11.2015.
 */
public class CategoryAdapter extends BaseAdapter {
    Context cntx;
    LayoutInflater lInflater;
    List<Category> categoryList;

    CategoryAdapter (Context context, List<Category> categories) {
        cntx = context;
        categoryList = categories;
        lInflater = (LayoutInflater) cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = lInflater.inflate(R.layout.categoryitem, parent, false);

        Category cat = getCategory(position);

        ((TextView) view.findViewById(R.id.tvCategoryItem)).setText(cat.getLabel());
        return view;
    }

    public Category getCategory(int position) {
        return ((Category) getItem(position));
    }
}
