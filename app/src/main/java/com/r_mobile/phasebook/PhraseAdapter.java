package com.r_mobile.phasebook;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.r_mobile.DaoSession;
import com.r_mobile.Phrase;
import com.r_mobile.PhraseBookApp;
import com.r_mobile.PhraseDao;

import java.util.List;

/**
 * Created by Admin on 18.11.2015.
 */
public class PhraseAdapter extends RecyclerView.Adapter<PhraseAdapter.DataObjectHolder> {

    private List<Phrase> mDataset;
    private View.OnClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView tvPhrase;
        TextView tvTranscription;
        TextView tvTranslate;
        ImageView ivFavorite;
        View holdingView;

        public DataObjectHolder(View itemView) {
            super(itemView);
            tvPhrase = (TextView) itemView.findViewById(R.id.tvPhrase);
            tvTranscription = (TextView) itemView.findViewById(R.id.tvTranscription);
            tvTranslate = (TextView) itemView.findViewById(R.id.tvTranslate);
            ivFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);
            Log.i("phrasebook", "Adding Listener");
            holdingView = itemView;
        }
    }

    public void setOnItemClickListener(View.OnClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public PhraseAdapter(List<Phrase> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phrasecard, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.tvPhrase.setText(mDataset.get(position).getPhrase());
        holder.tvTranscription.setText(mDataset.get(position).getTranslateList().get(0).getTranscription());
        holder.tvTranslate.setText(mDataset.get(position).getTranslateList().get(0).getContent());

        if (mDataset.get(position).getFavorite().equals(1)) {
            holder.ivFavorite.setImageResource(android.R.drawable.star_on);
        } else {
            holder.ivFavorite.setImageResource(android.R.drawable.star_off);
        }

        holder.ivFavorite.setOnClickListener(myClickListener);
        holder.holdingView.setTag(mDataset.get(position).getId());
    }

    public void addItem(Phrase dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
