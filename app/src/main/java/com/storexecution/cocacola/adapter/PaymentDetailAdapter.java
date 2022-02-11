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
import com.storexecution.cocacola.model.PaymentDetail;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.RecyclerItemClickListener;
import com.storexecution.cocacola.util.SalepointTypeUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class PaymentDetailAdapter extends RecyclerView.Adapter<PaymentDetailAdapter.SalepointViewHolder> {

    Context context;
    ArrayList<PaymentDetail> paymentDetails;

    RecyclerItemClickListener mListner;
    LayoutInflater mInflater;

    public PaymentDetailAdapter(Context context, ArrayList<PaymentDetail> paymentDetails, RecyclerItemClickListener listner) {
        this.context = context;
        this.paymentDetails = paymentDetails;
        this.mListner = listner;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SalepointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_row_payment, parent, false);
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
        holder.bind(context, paymentDetails.get(position), position);
    }

    @Override
    public int getItemCount() {
        return paymentDetails.size();
    }

    class SalepointViewHolder extends RecyclerView.ViewHolder {
        /**
         * ButterKnife Code
         **/
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvTotalPos)
        TextView tvTotalPos;
        @BindView(R.id.tvTotalValid)
        TextView tvTotalValid;
        @BindView(R.id.tvTotalAmount)
        TextView tvTotalAmount;

        /**
         * ButterKnife Code
         **/


        public SalepointViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, PaymentDetail paymentDetail, int index) {


            tvDate.setText(paymentDetail.getDate());
            tvTotalPos.setText(paymentDetail.getPosCount() );
            tvTotalValid.setText(paymentDetail.getValid() );
            tvTotalAmount.setText(String.format("%,.2f", Double.valueOf(paymentDetail.getAmount())) + " DZD");

        }
    }
}
