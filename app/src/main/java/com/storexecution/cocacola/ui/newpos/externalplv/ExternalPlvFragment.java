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
import com.storexecution.cocacola.PdfViewer;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.model.TagElement;
import com.storexecution.cocacola.util.Base64Util;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.GsonUtils;
import com.storexecution.cocacola.util.ImageLoad;
import com.storexecution.cocacola.util.PrimaryKeyFactory;
import com.storexecution.cocacola.util.Session;
import com.storexecution.cocacola.util.UtilBase64;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
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
public class ExternalPlvFragment extends Fragment {


    /**
     * ButterKnife Code
     **/
    @BindView(R.id.tgTinda)
    co.lujun.androidtagview.TagContainerLayout tgTinda;
    @BindView(R.id.btAddTinda)
    Button btAddTinda;
    @BindView(R.id.tgEar)
    co.lujun.androidtagview.TagContainerLayout tgEar;
    @BindView(R.id.btAddEar)
    Button btAddEar;
    @BindView(R.id.tgPotence)
    co.lujun.androidtagview.TagContainerLayout tgPotence;
    @BindView(R.id.btAddPotence)
    Button btAddPotence;
    @BindView(R.id.tgWindow)
    co.lujun.androidtagview.TagContainerLayout tgWindow;
    @BindView(R.id.tgAddWindow)
    Button tgAddWindow;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;


    /**
     * ButterKnife Code
     **/
    SweetAlertDialog sweetAlertDialog;


    RealmList<TagElement> tindas;
    RealmList<TagElement> ears;
    RealmList<TagElement> potence;
    RealmList<TagElement> windows;


    Salepoint salepoint;
    Session session;
    Realm realm;
    Photo photo;

    public ExternalPlvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_external_plv, container, false);


        ButterKnife.bind(this, v);
        realm = Realm.getDefaultInstance();
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();
        tindas = new RealmList<>();
        ears = new RealmList<>();
        potence = new RealmList<>();
        windows = new RealmList<>();
        realm = Realm.getDefaultInstance();
        tindas.addAll(salepoint.getTindas());
        ears.addAll(salepoint.getEars());
        potence.addAll(salepoint.getPotence());
        windows.addAll(salepoint.getWindowwrap());
        photo = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_PLV_EXTERNAL).findFirst();
        if (photo != null)
            ivPhoto.setImageBitmap(Base64Util.Base64ToBitmap(photo.getImage()));
        setEarsTags();
        setPotanceTags();
        setWindowTags();
        settindaTags();


        tgEar.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                edit(tgEar, ears, position);
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


        tgPotence.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                edit(tgPotence, potence, position);
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


        tgWindow.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                edit(tgWindow, windows, position);
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

        tgTinda.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                edit(tgTinda, tindas, position);
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


    private void setEarsTags() {
        tgEar.removeAllTags();
        for (TagElement tg : ears)
            tgEar.addTag(tg.getName() + ":" + tg.getQuantity());
    }

    @OnClick(R.id.btAddEar)
    public void btAddEar() {

        add(tgEar, ears);
    }


    private void setPotanceTags() {
        tgPotence.removeAllTags();
        for (TagElement tg : potence)
            tgPotence.addTag(tg.getName() + ":" + tg.getQuantity());
    }

    @OnClick(R.id.btAddPotence)
    public void btAddPotence() {

        add(tgPotence, potence);
    }

    private void settindaTags() {
        tgTinda.removeAllTags();
        for (TagElement tg : tindas)
            tgTinda.addTag(tg.getName() + ":" + tg.getQuantity());
    }

    @OnClick(R.id.btAddTinda)
    public void btAddTinda() {

        add(tgTinda, tindas);
    }

    private void setWindowTags() {
        tgWindow.removeAllTags();
        for (TagElement tg : windows)
            tgWindow.addTag(tg.getName() + ":" + tg.getQuantity());
    }


    @OnClick(R.id.tgAddWindow)
    public void tgAddWindow() {

        add(tgWindow, windows);
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

        if (salepoint.getSalepointType() == Constants.TYPE_CAFE || salepoint.getSalepointType() == Constants.TYPE_FASTFOOD || salepoint.getSalepointType() == Constants.TYPE_RESTAURANT) {
            ExternalPlvFragment2 fragment = new ExternalPlvFragment2();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, fragment, "storeInfo")
                    .addToBackStack(getClass().getName())
                    .commit();
        } else
            getActivity().finish();


    }

    @OnClick(R.id.fabPrev)
    public void prev() {
        setData();

        getActivity().onBackPressed();


    }

    private void setData() {

        salepoint.setEars(ears);
        salepoint.setTindas(tindas);
        salepoint.setPotence(potence);
        salepoint.setWindowwrap(windows);

        Log.e("salepoint", GsonUtils.salepointToJson(salepoint));
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

            return FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", ImageLoad.getOutputMediaFile(getContext(), type));

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
            if (photo == null)
                photo = realm.createObject(Photo.class, PrimaryKeyFactory.nextKey(Photo.class));

            photo.setImageID("plv_" + UUID.randomUUID());
            photo.setDate(System.currentTimeMillis() / 1000 + "");
            photo.setTypeID(salepoint.getMobile_id());
            photo.setImage(imageBase64);
            photo.setType(Constants.IMG_PLV_EXTERNAL);
            realm.commitTransaction();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ivPhoto)
    public void photo() {

        takePhoto();
    }

    @OnClick(R.id.ivInfo)
    public void info() {
        Intent intent = new Intent(getActivity(), PdfViewer.class);
        intent.putExtra("source", "plve");
        getActivity().startActivity(intent);
    }

}
