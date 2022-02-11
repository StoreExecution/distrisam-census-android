package com.storexecution.cocacola.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.RecyclerItemClickListener;
import com.storexecution.cocacola.util.SalepointTypeUtils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class ImageSyncAdapter extends RecyclerView.Adapter<ImageSyncAdapter.ImageSyncViewHolder> {

    Context context;
    RealmList<Photo> photos;

    RecyclerItemClickListener mListner;
    LayoutInflater mInflater;

    public ImageSyncAdapter(Context context, RealmList<Photo> photos, RecyclerItemClickListener listner) {
        this.context = context;
        this.photos = photos;
        this.mListner = listner;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ImageSyncViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_row_sync_photo, parent, false);
        final ImageSyncViewHolder viewHolder = new ImageSyncViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListner.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSyncViewHolder holder, int position) {
        holder.bind(context, photos.get(position), position);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class ImageSyncViewHolder extends RecyclerView.ViewHolder {
        /**
         * ButterKnife Code
         **/
        @BindView(R.id.llHolder)
        androidx.cardview.widget.CardView llHolder;
        @BindView(R.id.ivThumbnail)
        ImageView ivThumbnail;
        @BindView(R.id.tvNum)
        TextView tvNum;
        @BindView(R.id.tvImageType)
        TextView tvImageType;
        @BindView(R.id.tvSize)
        TextView tvSize;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.flSynced)
        FrameLayout flSynced;

        /**
         * ButterKnife Code
         **/
        public ImageSyncViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, Photo photo, int index) {

            tvNum.setText(new StringBuilder().append(String.valueOf(index + 1)).append("- ").toString() + "" + photo.getType() + " ");

            long bitmapSize = photo.getImage().getBytes().length;
            long compressedSize = photo.getImage().getBytes().length;

            bitmapSize = ((bitmapSize * 3) / 4) - (photo.getImage().endsWith("==") ? 2 : 1);

            tvSize.setText(humanReadableByteCountSI(bitmapSize));
            if (photo.getImage().getBytes().length >= (1024 * 500) || photo.isError()) {
                Bitmap img = Base64Util.Base64ToBitmap(photo.getImage(), 1);
                img = getResizedBitmap(img, 700);
                compressedSize = Base64Util.bitmapToBase64String(img, 90).getBytes().length;
                compressedSize = ((compressedSize * 3) / 4) - (photo.getImage().endsWith("==") ? 2 : 1);
                tvSize.setText(humanReadableByteCountSI(bitmapSize) + " ( " + humanReadableByteCountSI(compressedSize) + " )");
            }


            //  tvImageType.setText(photo.getType());
            //   ivThumbnail.setImageBitmap(Base64Util.Base64ToBitmap(photo.getImage()));

            ivThumbnail.setImageBitmap(null);
            if (photo.isSynced()) {
                flSynced.setBackground(context.getDrawable(R.drawable.green_circle));
            } else {
                flSynced.setBackground(context.getDrawable(R.drawable.red_circle));
            }

            if (photo.isError()) {
                flSynced.setBackground(context.getDrawable(R.drawable.yellow_circle));
            }
        }

        public String humanReadableByteCountBin(long bytes) {
            long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
            if (absB < 1024) {
                return bytes + " B";
            }
            long value = absB;
            CharacterIterator ci = new StringCharacterIterator("KMGTPE");
            for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
                value >>= 10;
                ci.next();
            }
            value *= Long.signum(bytes);
            return String.format("%.1f %ciB", value / 1024.0, ci.current());
        }
    }

    public static String humanReadableByteCountSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
