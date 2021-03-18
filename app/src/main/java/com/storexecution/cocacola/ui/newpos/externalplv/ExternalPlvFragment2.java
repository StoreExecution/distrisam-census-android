package com.storexecution.cocacola.ui.newpos.externalplv;

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
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.storexecution.cocacola.BuildConfig;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.model.TagElement;
import com.storexecution.cocacola.util.Base64Util;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.ImageLoad;
import com.storexecution.cocacola.util.PrimaryKeyFactory;
import com.storexecution.cocacola.util.Session;
import com.storexecution.cocacola.util.UtilBase64;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import io.realm.Realm;
import io.realm.RealmList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExternalPlvFragment2 extends Fragment {

    @BindView(R.id.tgSideWalk)
    co.lujun.androidtagview.TagContainerLayout tgSideWalk;
    @BindView(R.id.btAddSideWalk)
    Button btAddSideWalk;
    @BindView(R.id.tgTables)
    co.lujun.androidtagview.TagContainerLayout tgTables;
    @BindView(R.id.btAddTables)
    Button btAddTables;
    @BindView(R.id.tgChair)
    co.lujun.androidtagview.TagContainerLayout tgChair;
    @BindView(R.id.btAddChair)
    Button btAddChair;
    @BindView(R.id.tgParasol)
    co.lujun.androidtagview.TagContainerLayout tgParasol;
    @BindView(R.id.btAddParasol)
    Button btAddParasol;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;

    Realm realm;

    SweetAlertDialog sweetAlertDialog;
    Salepoint salepoint;
    Session session;
    RealmList<TagElement> sideWalk;
    RealmList<TagElement> tables;
    RealmList<TagElement> chairs;
    RealmList<TagElement> parasols;
    Photo photo;



    public ExternalPlvFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_external_plv2, container, false);
        ButterKnife.bind(this, v);
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();
        realm = Realm.getDefaultInstance();
        photo = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_PLV_EXTERNAL2).findFirst();
        if (photo != null)
            ivPhoto.setImageBitmap(Base64Util.Base64ToBitmap(photo.getImage()));
        sideWalk = salepoint.getSidewalkStop();
        tables = salepoint.getTables();
        chairs = salepoint.getChaires();
        parasols = salepoint.getParasols();

        setParasolTags();
        setChairsTags();
        settablesTags();
        setsidewalkTags();


        tgChair.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                edit(tgChair, chairs, position);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
        tgSideWalk.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                edit(tgSideWalk, sideWalk, position);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

        tgTables.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                edit(tgTables, tables, position);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

        tgParasol.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                edit(tgParasol, parasols, position);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
        return v;
    }

    private void settablesTags() {
        tgTables.removeAllTags();
        for (TagElement tg : tables)
            tgTables.addTag(tg.getName() + ":" + tg.getQuantity());
    }

    @OnClick(R.id.btAddTables)
    public void btAddTables() {

        add(tgTables, tables);
    }

    private void setChairsTags() {
        tgChair.removeAllTags();
        for (TagElement tg : chairs)
            tgChair.addTag(tg.getName() + ":" + tg.getQuantity());
    }

    @OnClick(R.id.btAddChair)
    public void btAddChair() {

        add(tgChair, chairs);
    }

    private void setParasolTags() {
        tgParasol.removeAllTags();
        for (TagElement tg : parasols)
            tgParasol.addTag(tg.getName() + ":" + tg.getQuantity());
    }

    @OnClick(R.id.btAddParasol)
    public void btAddParasol() {

        add(tgParasol, parasols);
    }

    private void setsidewalkTags() {
        tgSideWalk.removeAllTags();
        for (TagElement tg : sideWalk)
            tgSideWalk.addTag(tg.getName() + ":" + tg.getQuantity());
    }

    @OnClick(R.id.btAddSideWalk)
    public void btAddSideWalk() {

        add(tgSideWalk, sideWalk);
    }


    public void add(TagContainerLayout tagContainerLayout, RealmList<TagElement> tagElements) {

        SecureRandom random = new SecureRandom();
        int num = random.nextInt(99);


        final EditText name = new EditText(getActivity());
        final EditText quantity = new EditText(getActivity());
        name.setGravity(Gravity.CENTER);
        name.setHint("Marque");
        quantity.setHint("Nombre");
        quantity.setGravity(Gravity.CENTER);
        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);

        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitle("Marque");
        sweetAlertDialog.setContentText("Entrer le nom de la marque  ");
        sweetAlertDialog.setConfirmButton("Oui", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                String nameText = name.getText().toString();
                String quantityText = quantity.getText().toString().trim();
                if (nameText.length() > 0 && quantityText.length() > 0 && Integer.valueOf(quantityText) > 0) {
                    tagContainerLayout.addTag(name.getText().toString() + " : " + Integer.valueOf(quantityText));
                    tagElements.add(new TagElement(nameText, Integer.valueOf(quantityText)));
                    // Log.e("taglistLength",tags.size()+" ");


                }
                sweetAlertDialog.dismiss();
            }
        });
        sweetAlertDialog.setCancelButton("Annuler", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }

        });

        sweetAlertDialog.show();
        LinearLayout linearLayout = (LinearLayout) sweetAlertDialog.findViewById(R.id.loading);
        int index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(quantity, index + 1);

        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(name, index + 1);

    }

    private void edit(TagContainerLayout tagContainerLayout, RealmList<TagElement> tagElements, int position) {

        TagElement tagElement = tagElements.get(position);
        final EditText name = new EditText(getActivity());
        final EditText quantity = new EditText(getActivity());
        name.setGravity(Gravity.CENTER);
        name.setHint("Marque");
        name.setText(tagElement.getName());
        quantity.setHint("Nombre");
        quantity.setText(tagElement.getQuantity() + "");
        quantity.setGravity(Gravity.CENTER);
        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);

        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitle("Marque");
        sweetAlertDialog.setContentText("Entrer le nom de la marque  ");
        sweetAlertDialog.setNeutralButton("Annuler", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        sweetAlertDialog.setConfirmButton("Oui", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();

                String nameText = name.getText().toString();
                String quantityText = quantity.getText().toString().trim();
                if (nameText.length() > 0 && quantityText.length() > 0 && Integer.parseInt(quantityText) > 0) {
                    tagContainerLayout.removeTag(position);
                    tagContainerLayout.addTag(name.getText().toString() + " : " + Integer.parseInt(quantityText));

                    tagElements.remove(position);
                    tagElements.add(new TagElement(nameText, Integer.parseInt(quantityText)));

                    // Log.e("taglistLength",tags.size()+" ");


                }
                sweetAlertDialog.dismiss();
            }
        });
        sweetAlertDialog.setCancelButton("Supprimer", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                tagElements.remove(position);
                tagContainerLayout.removeTag(position);

                sweetAlertDialog.dismiss();

            }

        });

        sweetAlertDialog.show();
        LinearLayout linearLayout = (LinearLayout) sweetAlertDialog.findViewById(R.id.loading);
        int index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(quantity, index + 1);

        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(name, index + 1);


    }

    @OnClick(R.id.fabNext)
    public void save() {

        setData();
        getActivity().finish();


    }

    @OnClick(R.id.fabPrev)
    public void prev() {
        setData();
        getActivity().onBackPressed();


    }

    private void setData() {

        salepoint.setSidewalkStop(sideWalk);
        salepoint.setChaires(chairs);
        salepoint.setTables(tables);
        salepoint.setParasols(parasols);
        session.setSalepoint(salepoint);

    }

    private Uri fileUri;
    int MEDIA_TYPE_IMAGE = 1;

    public void takePhoto() {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        Log.e("fileuri", fileUri.getPath() + " ");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
       /* intent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +".provider",
                        new File(fileUri.getPath())));*/
        // start the image capture Intent
        this.startActivityForResult(intent, 1234);
    }

    public Uri getOutputMediaFileUri(int type) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return Uri.fromFile(ImageLoad.getOutputMediaFile3(type));
        } else

            return FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", ImageLoad.getOutputMediaFile(getContext(),type));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  Log.e("resultcode",resultCode+" /");
        if (resultCode == Activity.RESULT_OK) {
            // successfully captured the image
            // display it in image view
            //   Uri uri = data.getData();
            if (data != null)
                previewCapturedImage(requestCode, data.getData());
            else {
                previewCapturedImage(requestCode, null);
            }
        } else {
            // failed to capture image
            Toast.makeText(getActivity(),
                    "veuillez réessayer", Toast.LENGTH_SHORT)
                    .show();
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
            paint.setTextSize(24);
            paint.setTextAlign(Paint.Align.LEFT);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());

            canvas.drawText("Date: " + currentDateandTime, 20, 35, paint);

       /*     ivPlv.setImageResource(android.R.color.transparent);
            PicassoSingleton.with(getActivity())
                    .load("file://" + fileUri.getPath())
                    .resize(250, 250)
                    .noFade()
                    .into(ivPlv);*/
            ivPhoto.setImageBitmap(mutableBitmap);
            String imageBase64 = UtilBase64.bitmapToBase64String(mutableBitmap);

            // fridge.setPhotoFridge(imageBase64);
            realm.beginTransaction();
            if(photo==null)
             photo = realm.createObject(Photo.class, PrimaryKeyFactory.nextKey(Photo.class));

            photo.setImageID("plv2_" + UUID.randomUUID());
            photo.setDate(System.currentTimeMillis() / 1000 + "");
            photo.setTypeID(salepoint.getMobile_id());
            photo.setImage(imageBase64);
            photo.setType(Constants.IMG_PLV_EXTERNAL2);
            realm.commitTransaction();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ivPhoto)
    public void photo() {

        takePhoto();
    }
}
