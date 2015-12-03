package com.r_mobile.phasebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.r_mobile.Category;

import java.util.List;

/**
 * Created by nemol on 03.12.2015.
 */
public class SpinnerAddAdapter extends ArrayAdapter<Category> {

    private List<Category> listCategories;
    private Context context;
    LayoutInflater layoutInflater;
    int resource;

    public SpinnerAddAdapter(Context context, int resource, List<Category> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listCategories = objects;
        this.resource = resource;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        // TODO: convert view (Nikita)
        View row = layoutInflater.inflate(resource, viewGroup, false);

        Category cat = listCategories.get(position);

        ((TextView) row.findViewById(android.R.id.text1)).setText(cat.getLabel());

        row.setTag(cat.getId());

        return row;
    }
}
