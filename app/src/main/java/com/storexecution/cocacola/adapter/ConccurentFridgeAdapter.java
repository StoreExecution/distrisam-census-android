package com.storexecution.cocacola.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.ConcurrentFridge;
import com.storexecution.cocacola.model.Fridge;
import com.storexecution.cocacola.util.RecyclerItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class ConccurentFridgeAdapter extends RecyclerView.Adapter<ConccurentFridgeAdapter.FridgeViewHolder> {
    Context context;
    RealmList<ConcurrentFridge> fridges;
    RecyclerItemClickListener mListner;
    LayoutInflater mInflater;
    int fridgeIcon;

    public ConccurentFridgeAdapter(Context context, RealmList<ConcurrentFridge> fridges, int fridgeIcon, @Nullable RecyclerItemClickListener listner) {
        this.context = context;
        this.fridges = fridges;
        this.mListner = listner;
        this.fridgeIcon = fridgeIcon;
        this.mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public FridgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_fridge, parent, false);
        final FridgeViewHolder viewHolder = new FridgeViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListner.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FridgeViewHolder holder, int position) {
        holder.bind(context, position);
    }

    @Override
    public int getItemCount() {
        return fridges.size();
    }

    class FridgeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivFridge)
        ImageView ivFridge;


        public FridgeViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, int position) {

            ivFridge.setImageResource(fridgeIcon);

        }
    }
}
