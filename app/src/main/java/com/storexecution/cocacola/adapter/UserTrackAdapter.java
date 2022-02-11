package com.storexecution.cocacola.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.model.UserTrack;
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.RecyclerItemClickListener;
import com.storexecution.cocacola.util.SalepointTypeUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class UserTrackAdapter extends RecyclerView.Adapter<UserTrackAdapter.UserTrackViewHolder> {

    Context context;
    RealmList<UserTrack> usersTracks;
    ArrayList<Integer> selected;

    RecyclerItemClickListener mListner;
    LayoutInflater mInflater;

    public UserTrackAdapter(Context context, RealmList<UserTrack> usersTracks, ArrayList<Integer> selected, RecyclerItemClickListener listner) {
        this.context = context;
        this.usersTracks = usersTracks;
        this.selected = selected;
        this.mListner = listner;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public UserTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_row_salepoint, parent, false);
        final UserTrackViewHolder viewHolder = new UserTrackViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListner.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    public void toggleSelect(int userId) {
        if (selected.contains(userId)) {
            selected.remove(Integer.valueOf(userId));
        } else {
            selected.add(userId);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull UserTrackViewHolder holder, int position) {
        holder.bind(context, usersTracks.get(position), position);
    }

    @Override
    public int getItemCount() {
        return usersTracks.size();
    }

    class UserTrackViewHolder extends RecyclerView.ViewHolder {
        /**
         * ButterKnife Code
         **/
        @BindView(R.id.llHolder)
        LinearLayout llHolder;
        @BindView(R.id.flSelect)
        FrameLayout flSelect;
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

        public UserTrackViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, UserTrack usersTrack, int index) {


            if (selected.contains(usersTrack.getUserId())) {

                flSelect.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));

            } else {
                flSelect.setBackgroundColor(context.getResources().getColor(R.color.transparent));

            }


        }
    }
}
