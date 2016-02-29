package com.r_mobile.phasebook.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.r_mobile.phasebook.R;
import com.r_mobile.phasebook.greenDao.Category;

import java.util.List;

/**
 * Created by AlanMalone on 27.02.2016.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.DataObjectHolder> {

    private List<Category> mDataset;
    private View.OnClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView tvCategoryItem;
        LinearLayout categoryCard;
        View holdingView;

        public DataObjectHolder(View itemView) {
            super(itemView);
            tvCategoryItem = (TextView) itemView.findViewById(R.id.tvCategoryItem);
            categoryCard = (LinearLayout) itemView.findViewById(R.id.categoryCard);
            Log.i("phrasebook", "Adding Listener");
            holdingView = itemView;
        }
    }

    public void setOnItemClickListener(View.OnClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public CategoriesAdapter(List<Category> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categoryitem, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.tvCategoryItem.setText(mDataset.get(position).getLabel());
        holder.categoryCard.setOnClickListener(myClickListener);

        holder.holdingView.setTag(mDataset.get(position).getId());
    }

    public void addItem(Category dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public void setmDataset(List<Category> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
