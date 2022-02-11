package com.storexecution.cocacola.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.storexecution.cocacola.model.Sector;
import com.storexecution.cocacola.util.RecyclerItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;

public class SectorAdapter extends RecyclerView.Adapter<SectorAdapter.SectorViewHolder> {
    Context context;
    RealmList<Sector> sectors;
    RecyclerItemClickListener mListner;
    LayoutInflater mInflater;
    Realm realm;
    int selectedId;

    public SectorAdapter(Context context, RealmList<Sector> sectors, int selectedId, RecyclerItemClickListener mListner) {
        this.context = context;
        this.sectors = sectors;
        this.mListner = mListner;
        this.selectedId = selectedId;

        this.mInflater = LayoutInflater.from(context);
        realm = Realm.getDefaultInstance();
    }

    @NonNull
    @Override
    public SectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_row_sector, parent, false);

        SectorViewHolder sectorViewHolder = new SectorViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListner.onItemClick(v, sectorViewHolder.getAdapterPosition());
            }
        });

        return sectorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SectorViewHolder holder, int position) {
        holder.bind(context, sectors.get(position), position);
    }

    @Override
    public int getItemCount() {
        return sectors.size();
    }

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    class SectorViewHolder extends RecyclerView.ViewHolder {


        /**
         * ButterKnife Code
         **/
        @BindView(R.id.llHolder)
        LinearLayout llHolder;
        @BindView(R.id.ivSectorIcon)
        ImageView ivSectorIcon;
        @BindView(R.id.tvNum)
        TextView tvNum;
        @BindView(R.id.tvSectorName)
        TextView tvSectorName;
        @BindView(R.id.tvPosCount)
        TextView tvPosCount;
        @BindView(R.id.flOpen)
        FrameLayout flOpen;

        /**
         * ButterKnife Code
         **/


        public SectorViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, Sector sector, int position) {

            tvNum.setText(new StringBuilder().append(String.valueOf(position + 1)).append(" - ").toString());
            tvSectorName.setText(sector.getName());

            if (sector.getOpen() == 1) {
                flOpen.setBackground(context.getDrawable(R.drawable.green_circle));
            } else {
                flOpen.setBackground(context.getDrawable(R.drawable.red_circle));


            }
            if (selectedId == sector.getId()) {
                tvNum.setTextColor(context.getColor(R.color.colorWhite));
                tvSectorName.setTextColor(context.getColor(R.color.colorWhite));
                tvPosCount.setTextColor(context.getColor(R.color.colorWhite));
                llHolder.setBackgroundColor(context.getColor(R.color.colorAccent));
                ivSectorIcon.clearColorFilter();
            } else {
                tvNum.setTextColor(context.getColor(R.color.black));
                tvSectorName.setTextColor(context.getColor(R.color.black));
                tvPosCount.setTextColor(context.getColor(R.color.black));
                llHolder.setBackgroundColor(context.getColor(R.color.white));
                ivSectorIcon.setColorFilter(Color.argb(255, 255, 255, 255));

            }


        }
    }
}
