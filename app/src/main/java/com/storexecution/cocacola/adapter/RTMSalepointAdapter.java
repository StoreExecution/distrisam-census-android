package com.storexecution.cocacola.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.ActivityChange;
import com.storexecution.cocacola.model.RTMSalepoint;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.RecyclerItemClickListener;
import com.storexecution.cocacola.util.SalepointTypeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;

public class RTMSalepointAdapter extends RecyclerView.Adapter<RTMSalepointAdapter.SalepointViewHolder> {

    Context context;
    RealmList<RTMSalepoint> salepoints;

    RecyclerItemClickListener mListner;
    LayoutInflater mInflater;
    Realm realm;

    public RTMSalepointAdapter(Context context, RealmList<RTMSalepoint> salepoints, RecyclerItemClickListener listner) {
        this.context = context;
        this.salepoints = salepoints;
        this.mListner = listner;
        this.mInflater = LayoutInflater.from(context);
        realm = Realm.getDefaultInstance();
    }

    @NonNull
    @Override
    public SalepointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_row_salepoint, parent, false);
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
        LinearLayout llHolder;
        @BindView(R.id.tvNum)
        TextView tvNum;
        @BindView(R.id.tvSalepointName)
        TextView tvSalepointName;
        @BindView(R.id.tvOwnerName)
        TextView tvOwnerName;
        @BindView(R.id.tvDate)
        TextView tvDate;

        /**
         * ButterKnife Code
         **/

        public SalepointViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, RTMSalepoint salepoint, int index) {

            tvNum.setText(new StringBuilder().append(String.valueOf(index + 1)).append("- ").toString() + " ");
            tvSalepointName.setText(salepoint.getSalepoint_name());
            tvOwnerName.setText(salepoint.getOwner_name());
            tvDate.setText(salepoint.getAffected_date());

            if (realm.where(Salepoint.class).equalTo("rtmId", salepoint.getId()).findFirst() != null || salepoint.isDone())
                llHolder.setBackgroundColor(context.getResources().getColor(R.color.main_green_color));
            else if (realm.where(ActivityChange.class).equalTo("rtmId", salepoint.getId()).findFirst() != null)
                llHolder.setBackgroundColor(context.getResources().getColor(R.color.main_orange_color));
            else
                llHolder.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));


        }
    }
}
