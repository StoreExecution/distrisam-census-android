package com.storexecution.cocacola.adapter;

import android.content.Context;
import android.util.Log;
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
import com.storexecution.cocacola.model.Notification;
import com.storexecution.cocacola.model.ValidationConditon;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.RecyclerItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;

public class ConccurentFridgeAdapter extends RecyclerView.Adapter<ConccurentFridgeAdapter.FridgeViewHolder> {
    Context context;
    RealmList<ConcurrentFridge> fridges;
    RecyclerItemClickListener mListner;
    LayoutInflater mInflater;
    int fridgeIcon;
    Realm realm;
    Notification notification;

    public ConccurentFridgeAdapter(Context context, RealmList<ConcurrentFridge> fridges, int fridgeIcon, Notification notification, @Nullable RecyclerItemClickListener listner) {
        this.context = context;
        this.fridges = fridges;
        this.mListner = listner;
        this.fridgeIcon = fridgeIcon;
        this.notification = notification;
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
        @BindView(R.id.ivWarning)
        ImageView ivWarning;


        public FridgeViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, int position) {

            ivFridge.setImageResource(fridgeIcon);
            if (notification != null) {
                if (checkNotification(fridges.get(position).getMobile_id()))
                    ivWarning.setVisibility(View.VISIBLE);
                else
                    ivWarning.setVisibility(View.GONE);

            } else {
                ivWarning.setVisibility(View.GONE);
            }

        }

        private boolean checkNotification(String fridgeId) {
            for (ValidationConditon validationConditon : notification.getConditions()) {

                if (validationConditon.getStatus() == 0 && validationConditon.getDataType().equals(Constants.IMG_CFRIDGE) && validationConditon.getDataId().equals(fridgeId)) {

                    return true;
                }

            }
            return false;
        }

    }
}
