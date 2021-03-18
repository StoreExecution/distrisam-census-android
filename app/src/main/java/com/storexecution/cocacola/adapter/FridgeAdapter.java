package com.storexecution.cocacola.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Fridge;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.RecyclerItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;

public class FridgeAdapter extends RecyclerView.Adapter<FridgeAdapter.FridgeViewHolder> {
    Context context;
    RealmList<Fridge> fridges;
    RecyclerItemClickListener mListner;
    LayoutInflater mInflater;
    int fridgeIcon;
    Realm realm;

    public FridgeAdapter(Context context, RealmList<Fridge> fridges, int fridgeIcon, @Nullable RecyclerItemClickListener listner) {
        this.context = context;
        this.fridges = fridges;
        this.mListner = listner;
        this.fridgeIcon = fridgeIcon;
        this.mInflater = LayoutInflater.from(context);
        realm = Realm.getDefaultInstance();
    }


    @NonNull
    @Override
    public FridgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_fridge_indicator, parent, false);
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
        holder.bind(context, fridges.get(position));
    }

    @Override
    public int getItemCount() {
        return fridges.size();
    }

    class FridgeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivFridge)
        ImageView ivFridge;
        @BindView(R.id.flIndicator)
        FrameLayout flIndicator;

        public FridgeViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, Fridge fridge) {

            ivFridge.setImageResource(fridgeIcon);
            validateCocaColaFridge(context, fridge, flIndicator);

        }
    }

    private void validateCocaColaFridge(Context context, Fridge fridge, FrameLayout frameLayout) {
        boolean valid = true;
        boolean started = false;
        Log.e("started",started+" ");
        if (fridge.getAbused() >= 0) {
            started = true;
        } else {
            valid = false;
        }
        Log.e("started",started+" ");
        if (fridge.getFridgeOwner() > 0) {
            started = true;
        } else {
            valid = false;
        }
        Log.e("started",started+" ");
        if (fridge.getFridgeModel() > 0) {
            started = true;
        } else {
            valid = false;
        }
//        Log.e("started",started+" ");
//        if (fridge.getFridgeSerial().length() > 0) {
//            started = true;
//        } else {
//            valid = false;
//        }
        Log.e("started",started+" ");
        if (fridge.getBarCode().length() > 0) {
            started = true;
        } else {
            valid = false;
        }
        Log.e("started",started+" ");
        if (fridge.getFridgeState() > 0) {
            started = true;
        } else {
            valid = false;
        }
        Log.e("started",started+" ");
        if (fridge.getFridgeState() == 2) {
            if (fridge.getBreakDownType() > 0) {
                started = true;
            } else {
                valid = false;
            }
        }
        Log.e("started",started+" ");
        if (fridge.getIsOn() >= 0) {

            started = true;
        } else {
            valid = false;
        }
//        if (fridge.getIsOn() == 1) {
//            if (fridge.getFridgeTemp().length() > 0) {
//                started = true;
//            } else {
//                valid = false;
//            }
//        }
        if (fridge.getExternal() >= 0) {

            started = true;
        } else {
            valid = false;
        }

        Photo photo = realm.where(Photo.class).equalTo("TypeID", fridge.getMobileId()).and().equalTo("Type", Constants.IMG_FRIDGE).findFirst();
        if (photo == null) {
            valid = false;
        } else {
            started = true;
        }

        if (valid)
            frameLayout.setBackgroundColor(context.getResources().getColor(R.color.colorGreen2, null));
        else if (started)
            frameLayout.setBackgroundColor(context.getResources().getColor(R.color.colorYellow, null));

        else
            frameLayout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent, null));


    }
}
