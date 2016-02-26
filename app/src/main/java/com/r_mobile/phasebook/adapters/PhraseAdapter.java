package com.r_mobile.phasebook.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.r_mobile.phasebook.greenDao.Phrase;
import com.r_mobile.phasebook.R;

import java.util.List;

/**
 * Created by r-mobile on 18.11.2015.
 */
public class PhraseAdapter extends RecyclerView.Adapter<PhraseAdapter.DataObjectHolder> {

    private List<Phrase> mDataset;
    private View.OnClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView tvPhrase;
        TextView tvTranscription;
        TextView tvTranslate;
        ImageView ivFavorite;
        ImageView ivMoreOptions;
        RelativeLayout rlSpeak;
        View holdingView;

        public DataObjectHolder(View itemView) {
            super(itemView);
            tvPhrase = (TextView) itemView.findViewById(R.id.tvPhrase);
            tvTranscription = (TextView) itemView.findViewById(R.id.tvTranscription);
            tvTranslate = (TextView) itemView.findViewById(R.id.tvTranslate);
            ivFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);
            ivMoreOptions = (ImageView) itemView.findViewById(R.id.ivMoreOptions);
            rlSpeak = (RelativeLayout) itemView.findViewById(R.id.rlSpeak);
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
        holder.tvTranscription.setText("[ " + mDataset.get(position).getTranslateList().get(0).getTranscription() + " ]");
        holder.tvTranslate.setText(mDataset.get(position).getTranslateList().get(0).getContent());

        if (mDataset.get(position).getFavorite().equals(1)) {
            holder.ivFavorite.setImageResource(R.drawable.ic_star);
        } else {
            holder.ivFavorite.setImageResource(R.drawable.ic_star_outline);
        }

        holder.ivFavorite.setOnClickListener(myClickListener);
        holder.rlSpeak.setOnClickListener(myClickListener);
        holder.ivMoreOptions.setOnClickListener(myClickListener);
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

    public void setmDataset(List<Phrase> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
