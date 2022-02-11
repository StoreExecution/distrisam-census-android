package com.storexecution.cocacola;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.ImageLoad;
import com.storexecution.cocacola.util.PrimaryKeyFactory;
import com.storexecution.cocacola.util.UtilBase64;
import com.storexecution.cocacola.viewmodel.PhotoViewModel;

import java.text.SimpleDateFormat;
import java.util.UUID;

import butterknife.ButterKnife;
import io.realm.Realm;

public class PhotoFragmentDialog extends DialogFragment {


    Photo photo;
    private Uri fileUri;
    int MEDIA_TYPE_IMAGE = 1;
    Realm realm;
    String type_id;
    String image_id;
    String type;
    String mobile_id;
    String prefix;
    User user;
    private PhotoViewModel photoViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_photo_dialog, container, false);

        ButterKnife.bind(this, v);
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        photoViewModel = new ViewModelProvider(requireActivity()).get(PhotoViewModel.class);

        takePhoto(type, type_id);
        Log.e("REALM", Realm.getGlobalInstanceCount(Realm.getDefaultConfiguration()) + " ");

        return v;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile_id() {
        return mobile_id;
    }

    public void setMobile_id(String mobile_id) {
        this.mobile_id = mobile_id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void takePhoto(String type, String id) {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        this.type = type;
//        this.id = id;
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        Log.e("fileuri", fileUri.getPath() + " ");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//        intent.putExtra("type", type);
//        intent.putExtra("id", id);
       /* intent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +".provider",
                        new File(fileUri.getPath())));*/
        // start the image capture Intent
        this.startActivityForResult(intent, 1234);
    }

    public Uri getOutputMediaFileUri(int type) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return Uri.fromFile(ImageLoad.getOutputMediaFile3(type, mobile_id, Constants.IMG_PLV_Internal));
        } else

            return FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", ImageLoad.getOutputMediaFile(getContext(), type, mobile_id, Constants.IMG_PLV_Internal));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  Log.e("resultcode",resultCode+" /");
        if (resultCode == Activity.RESULT_OK) {
            // successfully captured the image
            // display it in image view
            //   Uri uri = data.getData();
            if (data != null) {

                String id = data.getExtras().getString("id");
                String type = data.getExtras().getString("type");


                previewCapturedImage(requestCode, data.getData());

            } else {
                previewCapturedImage(requestCode, null);
            }
        } else {
            // failed to capture image
            Toast.makeText(getActivity(),
                    "veuillez réessayer", Toast.LENGTH_SHORT)
                    .show();
            realm.close();
            dismiss();
        }

    }


    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void previewCapturedImage(int requestCode, Uri uri) {
        Log.e("dataextra", type + " " + type_id);

        try {
            //Log.e("fileuri2",uri.getPath()+" ");
            //Log.e("fileuri3",fileUri.getPath()+" ");

            // Log.e("fileuri3",getRealPathFromURI(fileUri));
            String path;
            if (uri == null)
                path = fileUri.getPath();
            else
                fileUri = uri;


            //    plvi.setToken(plv.getToken());

            //   if (uri != null)
            // path = getRealPathFromURI(uri);

            // Log.e("uri", path);
            final Bitmap resized = ImageLoad.decodeScaledBitmapFromSdCard(fileUri, 800, 600, getActivity());

            Bitmap mutableBitmap = resized.copy(Bitmap.Config.RGB_565, true);


            Canvas canvas = new Canvas(mutableBitmap);
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            paint.setTextSize(60);
            paint.setTextAlign(Paint.Align.LEFT);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");


            canvas.drawText("Date: " + DateUtils.todayDateTime(), 35, 65, paint);

       /*     ivPlv.setImageResource(android.R.color.transparent);
            PicassoSingleton.with(getActivity())
                    .load("file://" + fileUri.getPath())
                    .resize(250, 250)
                    .noFade()
                    .into(ivPlv);*/

            //   ivPhoto.setImageBitmap(mutableBitmap);
            String imageBase64 = UtilBase64.bitmapToBase64String(mutableBitmap);

            // fridge.setPhotoFridge(imageBase64);
            String photoId = prefix + "_" + UUID.randomUUID();
            realm.beginTransaction();
            if (image_id == null) {
                photo = realm.createObject(Photo.class, PrimaryKeyFactory.nextKey(Photo.class));

                photo.setImageID(photoId);


                photo.setTypeID(type_id);
                photo.setType(type);
            } else {
                photo = realm.where(Photo.class).equalTo("ImageID", image_id).findFirst();

            }
            photo.setUser_id(user.getId());
            photo.setDate(DateUtils.todayDateTime() + "");
            photo.setImage(imageBase64);
            photo.setSynced(false);


            realm.commitTransaction();
            realm.close();
            Log.e("dialog", photo.getImageID());
            photoViewModel.setPhoto(photo.getImageID());
            dismiss();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}