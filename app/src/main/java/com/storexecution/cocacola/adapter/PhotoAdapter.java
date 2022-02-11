package com.storexecution.cocacola.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Notification;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.ValidationConditon;
import com.storexecution.cocacola.util.Base64Util;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.RecyclerItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {


    Context context;
    RealmList<Photo> photos;
    RecyclerItemClickListener recyclerItemClickListener;
    int salepointNotificationId;
    Realm realm;

    public PhotoAdapter(Context context, RealmList<Photo> photos, int salepointNotificationId, RecyclerItemClickListener recyclerItemClickListener) {
        this.context = context;
        this.photos = photos;
        this.salepointNotificationId = salepointNotificationId;
        this.recyclerItemClickListener = recyclerItemClickListener;
        realm = Realm.getDefaultInstance();
    }

    @NonNull
    @NotNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_photo, parent, false);
        PhotoViewHolder photoViewHolder = new PhotoViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerItemClickListener.onItemClick(v, photoViewHolder.getAdapterPosition());
            }
        });

        return photoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PhotoViewHolder holder, int position) {
        holder.bind(context, photos.get(position));
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;
        @BindView(R.id.ivWarning)
        ImageView ivWarning;

        public PhotoViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, Photo photo) {

            ivPhoto.setImageBitmap(Base64Util.Base64ToBitmap(photo.getImage()));
            boolean hasNotification = false;
            Notification notification;
            if (salepointNotificationId != 0)
                notification = realm.where(Notification.class).equalTo("id", salepointNotificationId).findFirst();
            else
                notification = null;

            if (notification != null) {
                for (ValidationConditon validationConditon : notification.getConditions()) {

                    if (validationConditon.getStatus() == 0 && validationConditon.getDataId().equals(photo.getImageID())) {
                        hasNotification = true;
                    }

                }


            }

            if (hasNotification)
                ivWarning.setVisibility(View.VISIBLE);
            else
                ivWarning.setVisibility(View.GONE);

        }
    }
}
