package com.storexecution.cocacola.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.Base64Util;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.RecyclerItemClickListener;
import com.storexecution.cocacola.util.SalepointTypeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;

public class SynchAdapter extends RecyclerView.Adapter<SynchAdapter.SalepointViewHolder> {

    Context context;
    RealmList<Salepoint> salepoints;

    RecyclerItemClickListener mListner;
    LayoutInflater mInflater;
    Realm realm;

    public SynchAdapter(Context context, RealmList<Salepoint> salepoints, RecyclerItemClickListener listner) {
        this.context = context;
        this.salepoints = salepoints;
        this.mListner = listner;
        this.mInflater = LayoutInflater.from(context);
        realm = Realm.getDefaultInstance();
    }

    @NonNull
    @Override
    public SalepointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_row_sync, parent, false);
        final SalepointViewHolder viewHolder = new SalepointViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListner.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SalepointViewHolder holder, int position) {
        holder.bind(context, salepoints.get(position), position);
    }

    @Override
    public int getItemCount() {
        return salepoints.size();
    }

    class SalepointViewHolder extends RecyclerView.ViewHolder {
        /**
         * ButterKnife Code
         **/
        @BindView(R.id.llHolder)
        androidx.cardview.widget.CardView llHolder;
        @BindView(R.id.ivStoreIcon)
        ImageView ivStoreIcon;
        @BindView(R.id.tvNum)
        TextView tvNum;
        @BindView(R.id.tvSalepointName)
        TextView tvSalepointName;
        @BindView(R.id.tvOwnerName)
        TextView tvOwnerName;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.flSynced)
        FrameLayout flSynced;

        /**
         * ButterKnife Code
         **/

        public SalepointViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, Salepoint salepoint, int index) {

            tvNum.setText("");
            // tvNum.setText(new StringBuilder().append(String.valueOf(index + 1)).append("- ").toString());
            tvSalepointName.setText(salepoint.getPosName());
            tvOwnerName.setText(salepoint.getOwnerName());
            ivStoreIcon.setImageDrawable(context.getResources().getDrawable(SalepointTypeUtils.getIcon(salepoint.getSalepointType())));
            tvDate.setText(DateUtils.getDateTime(salepoint.getMobileModificationDate()));
            if (checkAllImages(salepoint.getMobile_id()) && salepoint.isSynced()) {
                flSynced.setBackground(context.getDrawable(R.drawable.green_circle));
            } else {
                flSynced.setBackground(context.getDrawable(R.drawable.red_circle));


            }

            if(salepoint.isError())
                flSynced.setBackground(context.getDrawable(R.drawable.yellow_circle));


        }


        private boolean checkAllImages(String mobile_id) {
            int synced = 0;
            int total = 0;
            Photo IMG_PLV_EXTERNAL = realm.where(Photo.class).equalTo("TypeID", mobile_id).and().equalTo("Type", Constants.IMG_PLV_EXTERNAL).findFirst();
            if (IMG_PLV_EXTERNAL != null) {
                total++;
                if (IMG_PLV_EXTERNAL.isSynced())
                    synced++;

            }

            Photo IMG_PLV_EXTERNAL2 = realm.where(Photo.class).equalTo("TypeID", mobile_id).and().equalTo("Type", Constants.IMG_PLV_EXTERNAL2).findFirst();
            if (IMG_PLV_EXTERNAL2 != null) {
                total++;
                if (IMG_PLV_EXTERNAL2.isSynced())
                    synced++;

            }

            Photo TAG_INTERNALPLV = realm.where(Photo.class).equalTo("TypeID", mobile_id).and().equalTo("Type", Constants.TAG_INTERNALPLV).findFirst();
            if (TAG_INTERNALPLV != null) {
                total++;
                if (TAG_INTERNALPLV.isSynced())
                    synced++;

            }

            Photo IMG_BARCODE = realm.where(Photo.class).equalTo("TypeID", mobile_id).and().equalTo("Type", Constants.IMG_BARCODE).findFirst();
            if (IMG_BARCODE != null) {
                total++;
                if (IMG_BARCODE.isSynced())
                    synced++;

            }

            Photo IMG_POS = realm.where(Photo.class).equalTo("TypeID", mobile_id).and().equalTo("Type", Constants.IMG_POS).findFirst();
            if (IMG_POS != null) {
                total++;
                if (IMG_POS.isSynced())
                    synced++;

            }

            return synced == total;


        }


    }
}
