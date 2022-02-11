package com.storexecution.cocacola.ui.newpos.internalplv;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.storexecution.cocacola.BuildConfig;
import com.storexecution.cocacola.PdfViewer;
import com.storexecution.cocacola.PhotoFragmentDialog;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.adapter.PhotoAdapter;
import com.storexecution.cocacola.model.Notification;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.model.TagElement;
import com.storexecution.cocacola.model.ValidationConditon;
import com.storexecution.cocacola.util.Base64Util;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.ImageLoad;
import com.storexecution.cocacola.util.PrimaryKeyFactory;
import com.storexecution.cocacola.util.RecyclerItemClickListener;
import com.storexecution.cocacola.util.Session;
import com.storexecution.cocacola.util.UtilBase64;
import com.storexecution.cocacola.viewmodel.PhotoViewModel;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InternalPlvFragment extends Fragment implements Observer<String> {


    /**
     * ButterKnife Code
     **/
    @BindView(R.id.tgMetalRack)
    co.lujun.androidtagview.TagContainerLayout tgMetalRack;
    @BindView(R.id.ivAddMetalRack)
    Button ivAddMetalRack;
    @BindView(R.id.tgForexRack)
    co.lujun.androidtagview.TagContainerLayout tgForexRack;
    @BindView(R.id.ivAddForexRack)
    Button ivAddForexRack;
    @BindView(R.id.tgWrapedLinear)
    co.lujun.androidtagview.TagContainerLayout tgWrapedLinear;
    @BindView(R.id.ivAddWrapedLinear)
    Button ivAddWrapedLinear;
    @BindView(R.id.tgWrapedSkid)
    co.lujun.androidtagview.TagContainerLayout tgWrapedSkid;
    @BindView(R.id.ivAddWrapedSkid)
    Button ivAddWrapedSkid;
    @BindView(R.id.rgCombo)
    RadioGroup rgCombo;
    @BindView(R.id.rbComboYes)
    RadioButton rbComboYes;
    @BindView(R.id.rbComboNo)
    RadioButton rbComboNo;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.tvInternalPlv)
    TextView tvInternalPlv;
    @BindView(R.id.rvPhoto)
    RecyclerView rvPhoto;

    /** ButterKnife Code **/
    /**
     * ButterKnife Code
     **/
    SweetAlertDialog sweetAlertDialog;
    Session session;
    Salepoint salepoint;
    Realm realm;
    RealmList<TagElement> metalRacks;
    RealmList<TagElement> forexRacks;
    RealmList<TagElement> wrapedLinears;
    RealmList<TagElement> wrapedSkids;
    ArrayList<String> brands;
    Photo photo;
    PhotoAdapter photoAdapter;
    RealmList<Photo> photos;

    private PhotoViewModel photoViewModel;

    public InternalPlvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_internal_plv, container, false);
        ButterKnife.bind(this, view);
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();
        realm = Realm.getDefaultInstance();
        metalRacks = salepoint.getMetalRacks();
        forexRacks = salepoint.getForexRacks();
        wrapedLinears = salepoint.getWrapedLinear();
        wrapedSkids = salepoint.getWrapedSkids();
        brands = new ArrayList<>();
        brands.add("Marque");
        brands.add("Coca-Cola");
        brands.add("Pepsi");
        brands.add("Hamoud boualem");
        brands.add("Autre Boisson gazeuse");
        Log.e("REALM", Realm.getGlobalInstanceCount(Realm.getDefaultConfiguration()) + " ");

        if (salepoint.getCocacolaCombo() == 1)
            rbComboYes.setChecked(true);
        else if (salepoint.getCocacolaCombo() == 0)
            rbComboNo.setChecked(true);

        photos = new RealmList<>();


        long count = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_PLV_Internal).count();
        if (count > 0) {
            //   ivPhoto.setImageBitmap(Base64Util.Base64ToBitmap(photo.getImage()));
            photos.addAll(realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_PLV_Internal).findAll());
            //photos.add(photo);
        }
        photoAdapter = new PhotoAdapter(getActivity(), photos, salepoint.getNotificationId(), new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                takePhoto(Constants.IMG_PLV_Internal, salepoint.getMobile_id(), photos.get(position).getImageID());

            }
        });
        rvPhoto.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvPhoto.setAdapter(photoAdapter);
        setForexRackTags();
        setMetalRackTags();
        setWrapedLinearTags();
        setWrapedSkidTags();
        checkNotification();

        photoViewModel = new ViewModelProvider(requireActivity()).get(PhotoViewModel.class);

        photoViewModel.getPhoto().observe(requireActivity(), this);

        tgMetalRack.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                edit(tgMetalRack, metalRacks, position, Constants.IMG_IPLV_METAL_RACK);
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

        tgForexRack.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                edit(tgForexRack, forexRacks, position, Constants.IMG_IPLV_FOREX_RACK);
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

        tgWrapedLinear.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                edit(tgWrapedLinear, wrapedLinears, position, Constants.IMG_IPLV_LINEAR);
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

        tgWrapedSkid.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                edit(tgWrapedSkid, wrapedSkids, position, Constants.IMG_IPLV_SKID);
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


        return view;
    }

    @OnClick(R.id.ivPhoto)
    public void photo() {
        takePhoto(Constants.IMG_PLV_Internal, salepoint.getMobile_id(), null);


        //add(tgMetalRack, metalRacks);
    }

    private void setMetalRackTags() {
        tgMetalRack.removeAllTags();
        for (TagElement tg : metalRacks)
            tgMetalRack.addTag(tg.getName() + ":" + tg.getQuantity());
    }

    @OnClick(R.id.ivAddMetalRack)
    public void addMetalRack() {

        add(tgMetalRack, metalRacks, Constants.IMG_IPLV_METAL_RACK);

    }

    private void setForexRackTags() {
        tgForexRack.removeAllTags();
        for (TagElement tg : forexRacks)
            tgForexRack.addTag(tg.getName() + ":" + tg.getQuantity());
    }

    @OnClick(R.id.ivAddForexRack)
    public void addForexRack() {

        add(tgForexRack, forexRacks, Constants.IMG_IPLV_FOREX_RACK);

    }

    private void setWrapedLinearTags() {
        tgWrapedLinear.removeAllTags();
        for (TagElement tg : wrapedLinears)
            tgWrapedLinear.addTag(tg.getName() + ":" + tg.getQuantity());
    }

    @OnClick(R.id.ivAddWrapedLinear)
    public void addWrapedLinear() {

        add(tgWrapedLinear, wrapedLinears, Constants.IMG_IPLV_LINEAR);

    }

    private void setWrapedSkidTags() {
        tgWrapedSkid.removeAllTags();
        for (TagElement tg : wrapedSkids)
            tgWrapedSkid.addTag(tg.getName() + ":" + tg.getQuantity());
    }

    @OnClick(R.id.ivAddWrapedSkid)
    public void setIvAddWrapedSkid() {

        add(tgWrapedSkid, wrapedSkids, Constants.IMG_IPLV_SKID);

    }

    ImageView ivDialogPhoto;
    String id;
    String type;

    public void add(TagContainerLayout tagContainerLayout, RealmList<TagElement> tagElements, String type) {

        SecureRandom random = new SecureRandom();
        int num = random.nextInt(99);

        TagElement tagElement = new TagElement();
        tagElement.setId("PLVI_" + UUID.randomUUID());
        final Spinner spinner = new Spinner(getActivity());


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, brands);
        spinner.setAdapter(adapter);

        final EditText name = new EditText(getActivity());
        final EditText quantity = new EditText(getActivity());
        ivDialogPhoto = new ImageView(getActivity());

        ivDialogPhoto.setMaxHeight(300);
        ivDialogPhoto.setMaxWidth(300);
        ivDialogPhoto.setAdjustViewBounds(true);
        ivDialogPhoto.setImageResource(R.drawable.photo_red);
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

                String nameText = spinner.getSelectedItem().toString();
                String quantityText = quantity.getText().toString().trim();
                photo = realm.where(Photo.class).equalTo("TypeID", tagElement.getId()).and().equalTo("Type", type).findFirst();


                if (spinner.getSelectedItemPosition() > 0 && quantityText.length() > 0 && Integer.valueOf(quantityText) > 0 && photo != null) {
                    tagContainerLayout.addTag(spinner.getSelectedItem().toString() + " : " + Integer.valueOf(quantityText));
                    tagElement.setName(nameText);
                    tagElement.setQuantity(Integer.valueOf(quantityText));
                    tagElements.add(tagElement);
                    // Log.e("taglistLength",tags.size()+" ");
                    sweetAlertDialog.dismiss();


                } else {
                    Toasty.error(getActivity(), "Veillez remplir tous les champs", 5000).show();
                }
                // sweetAlertDialog.dismiss();
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
        linearLayout.addView(ivDialogPhoto, index + 1);

        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(quantity, index + 1);
        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(spinner, index + 1);

        ivDialogPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePhoto(type, tagElement.getId(), null);
            }
        });

    }

    private void edit(TagContainerLayout tagContainerLayout, RealmList<TagElement> tagElements, int position, String type) {

        TagElement tagElement = tagElements.get(position);
        final EditText name = new EditText(getActivity());
        final EditText quantity = new EditText(getActivity());

        final Spinner spinner = new Spinner(getActivity());


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, brands);
        spinner.setAdapter(adapter);

        int elementindex = brands.indexOf(tagElement.getName());
        if (elementindex != -1)
            spinner.setSelection(elementindex);
        else
            spinner.setSelection(4);
      //  Log.e("onChangeededit", tagElement.getId());
        name.setGravity(Gravity.CENTER);
        name.setHint("Marque");
        name.setText(tagElement.getName());
        quantity.setHint("Nombre");
        quantity.setText(tagElement.getQuantity() + "");
        quantity.setGravity(Gravity.CENTER);
        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        ivDialogPhoto = new ImageView(getActivity());

        ivDialogPhoto.setMaxHeight(300);
        ivDialogPhoto.setMaxWidth(300);
        ivDialogPhoto.setAdjustViewBounds(true);
        photo = realm.where(Photo.class).equalTo("TypeID", tagElement.getId()).findFirst();
        if (photo != null)
            ivDialogPhoto.setImageBitmap(Base64Util.Base64ToBitmap(photo.getImage(), 4));
        else
            ivDialogPhoto.setImageResource(R.drawable.photo_red);
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


                String nameText = spinner.getSelectedItem().toString();
                String quantityText = quantity.getText().toString().trim();
                if (spinner.getSelectedItemPosition() > 0 && quantityText.length() > 0 && Integer.parseInt(quantityText) > 0) {
                    tagContainerLayout.removeTag(position);
                    tagContainerLayout.addTag(nameText + " : " + Integer.parseInt(quantityText));

                    tagElements.remove(position);
                   // tagElements.add(new TagElement(nameText, Integer.parseInt(quantityText)));
                    tagElements.add(new TagElement(tagElement.getId(), nameText, Integer.parseInt(quantityText)));

                    // Log.e("taglistLength",tags.size()+" ");
                    sweetAlertDialog.dismiss();

                } else {
                    Toasty.error(getActivity(), "Veillez remplir tous les champs", 5000).show();
                }
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
        linearLayout.addView(ivDialogPhoto, index + 1);

        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(quantity, index + 1);

        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(spinner, index + 1);


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

        salepoint.setMetalRacks(metalRacks);
        salepoint.setForexRacks(forexRacks);
        salepoint.setWrapedLinear(wrapedLinears);
        salepoint.setWrapedSkids(wrapedSkids);
        if (rbComboYes.isChecked())
            salepoint.setCocacolaCombo(1);
        else if (rbComboNo.isChecked())
            salepoint.setCocacolaCombo(0);
        session.setSalepoint(salepoint);

    }

    private Uri fileUri;
    int MEDIA_TYPE_IMAGE = 1;

    public void takePhoto(String type, String id, @Nullable String imageid) {
        Log.e("onChangeed5", type + " ");
        Log.e("onChangeed4", id + " ");
        PhotoFragmentDialog photoFragmentDialog = new PhotoFragmentDialog();
        photoFragmentDialog.setMobile_id(salepoint.getMobile_id());
        photoFragmentDialog.setPrefix("plv");
        photoFragmentDialog.setType(type);
        photoFragmentDialog.setImage_id(imageid);
        photoFragmentDialog.setType_id(id);
        showFragment(photoFragmentDialog, getFragmentManager(), true, "photoframgent");


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.type = type;
        this.id = id;
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        Log.e("fileuri", fileUri.getPath() + " ");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//        intent.putExtra("type", type);
//        intent.putExtra("id", id);
       /* intent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +".provider",
                        new File(fileUri.getPath())));*/
        // start the image capture Intent
        //  this.startActivityForResult(intent, 1234);
    }

    public Uri getOutputMediaFileUri(int type) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return Uri.fromFile(ImageLoad.getOutputMediaFile3(type, salepoint.getMobile_id(), Constants.IMG_PLV_Internal));
        } else

            return FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", ImageLoad.getOutputMediaFile(getContext(), type, salepoint.getMobile_id(), Constants.IMG_PLV_Internal));

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
        Log.e("dataextra", type + " " + id);

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
            if (type.equals(Constants.IMG_PLV_Internal)) {
                ivPhoto.setImageBitmap(mutableBitmap);

            } else {
                if (sweetAlertDialog.isShowing()) {
                    ivDialogPhoto.setImageBitmap(mutableBitmap);

                }
            }
            //   ivPhoto.setImageBitmap(mutableBitmap);
            String imageBase64 = UtilBase64.bitmapToBase64String(mutableBitmap);

            // fridge.setPhotoFridge(imageBase64);
            photo = realm.where(Photo.class).equalTo("TypeID", id).and().equalTo("Type", type).findFirst();

            realm.beginTransaction();
            if (photo == null) {
                photo = realm.createObject(Photo.class, PrimaryKeyFactory.nextKey(Photo.class));

                photo.setImageID("plv_" + UUID.randomUUID());

                photo.setTypeID(id);
                photo.setType(type);
            }
            photo.setDate(DateUtils.todayDateTime() + "");
            photo.setImage(imageBase64);
            photo.setSynced(false);


            realm.commitTransaction();


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ivInfo)
    public void info() {
        Intent intent = new Intent(getActivity(), PdfViewer.class);
        intent.putExtra("source", "plvi");
        getActivity().startActivity(intent);
    }

    private void checkNotification() {
        Notification notification;
        if (salepoint.getNotificationId() != 0)
            notification = realm.where(Notification.class).equalTo("id", salepoint.getNotificationId()).findFirst();
        else
            notification = null;
        if (notification != null) {
            for (ValidationConditon validationConditon : notification.getConditions()) {

                if (validationConditon.getStatus() == 0 && validationConditon.getDataType().equals(Constants.IMG_PLV_Internal)) {
                    tvInternalPlv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_action_warning, 0);
                    Log.e("notification", validationConditon.getDataType() + " ");
                }

            }


        }

    }


    @Override
    public void onChanged(String s) {

        Photo photo = realm.where(Photo.class).equalTo("ImageID", s).findFirst();
        Log.e("onChangeed", s);
        Log.e("onChangeed1", photo.getTypeID());
        Log.e("onChangeed2", photo.getType());
        Log.e("onChangeedocunt", realm.where(Photo.class).equalTo("ImageID", s).count() + " ");
        Bitmap bitmap = Base64Util.Base64ToBitmap(photo.getImage());
        if (photo.getType().equals(Constants.IMG_PLV_Internal)) {
            //  ivPhoto.setImageBitmap(bitmap);
            photos.clear();
            photos.addAll(realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_PLV_Internal).findAll());

            photoAdapter.notifyDataSetChanged();

        } else {
            if (sweetAlertDialog.isShowing()) {
                ivDialogPhoto.setImageBitmap(bitmap);

            }
        }
    }

    public static void showFragment(Fragment fragment, FragmentManager fragmentManager, boolean withAnimation, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (withAnimation) {
            transaction.setCustomAnimations(R.anim.slide_up_anim, R.anim.slide_down_anim);
        } else {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        transaction.add(android.R.id.content, fragment, tag).commitAllowingStateLoss();
    }
}
