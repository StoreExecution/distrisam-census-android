package com.storexecution.cocacola.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.RecyclerItemClickListener;
import com.storexecution.cocacola.util.SalepointTypeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class SalepointAdapter extends RecyclerView.Adapter<SalepointAdapter.SalepointViewHolder> {

    Context context;
    RealmList<Salepoint> salepoints;

    RecyclerItemClickListener mListner;
    LayoutInflater mInflater;

    public SalepointAdapter(Context context, RealmList<Salepoint> salepoints, RecyclerItemClickListener listner) {
        this.context = context;
        this.salepoints = salepoints;
        this.mListner = listner;
        this.mInflater = LayoutInflater.from(context);
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

        public void bind(Context context, Salepoint salepoint, int index) {

            tvNum.setText(new StringBuilder().append(String.valueOf(index + 1)).append("- ").toString() + "" + SalepointTypeUtils.getType(salepoint.getSalepointType())+" ");
            tvSalepointName.setText(salepoint.getPosName());
            tvOwnerName.setText(salepoint.getOwnerName());
            tvDate.setText(DateUtils.getDateTime(salepoint.getMobileModificationDate()));

        }
    }
}
