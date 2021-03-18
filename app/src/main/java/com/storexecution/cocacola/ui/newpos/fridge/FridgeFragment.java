package com.storexecution.cocacola.ui.newpos.fridge;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.storexecution.FridgeDialogCallbackInterface;
import com.storexecution.cocacola.BuildConfig;
import com.storexecution.cocacola.PdfViewer;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.adapter.ConccurentFridgeAdapter;
import com.storexecution.cocacola.adapter.FridgeAdapter;
import com.storexecution.cocacola.model.ConcurrentFridge;
import com.storexecution.cocacola.model.Fridge;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.model.TagElement;
import com.storexecution.cocacola.util.Base64Util;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.ImageLoad;
import com.storexecution.cocacola.util.PrimaryKeyFactory;
import com.storexecution.cocacola.util.RecyclerItemClickListener;
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
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FridgeFragment extends Fragment implements FridgeDialogCallbackInterface {


    /**
     * ButterKnife Code
     **/
    @BindView(R.id.rlTinda)
    RelativeLayout rlTinda;
    @BindView(R.id.ivInfo)
    ImageView ivInfo;
    @BindView(R.id.etFridgeCount)
    EditText etFridgeCount;
    @BindView(R.id.add)
    Button add;
    @BindView(R.id.rvFridges)
    androidx.recyclerview.widget.RecyclerView rvFridges;
    @BindView(R.id.addPepsi)
    Button addPepsi;
    @BindView(R.id.rvFridgesPepsi)
    androidx.recyclerview.widget.RecyclerView rvFridgesPepsi;
    @BindView(R.id.addHammoud)
    Button addHammoud;
    @BindView(R.id.rvFridgesHammoud)
    androidx.recyclerview.widget.RecyclerView rvFridgesHammoud;
    @BindView(R.id.addOther)
    Button addOther;
    @BindView(R.id.rvFridgesOther)
    androidx.recyclerview.widget.RecyclerView rvFridgesOther;
    @BindView(R.id.llNavigation)
    LinearLayout llNavigation;
    @BindView(R.id.fabPrev)
    ImageView fabPrev;
    @BindView(R.id.fabNext)
    ImageView fabNext;
    /**
     * ButterKnife Code
     **/
    Session session;
    Salepoint salepoint;
    RealmList<Fridge> fridges;
    RealmList<ConcurrentFridge> fridgesPepsi;
    RealmList<ConcurrentFridge> fridgesHammoud;
    RealmList<ConcurrentFridge> fridgesOther;


    FridgeAdapter fridgeAdapter;


    ConccurentFridgeAdapter fridgePepsiAdapter;
    ConccurentFridgeAdapter fridgeHammoudAdapter;
    ConccurentFridgeAdapter fridgeOtherAdapter;
    LinearLayoutManager horizontalLayout;

    SweetAlertDialog sweetAlertDialog;
    TagContainerLayout mTagContainerLayout;
    ArrayList<TagElement> tags;
    Realm realm;

    public FridgeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fridge, container, false);
        ButterKnife.bind(this, v);
        fridges = new RealmList<>();
        realm = Realm.getDefaultInstance();
        fridgesPepsi = new RealmList<>();
        fridgesHammoud = new RealmList<>();
        fridgesOther = new RealmList<>();
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();


        fridges.addAll(salepoint.getCocaColaFridges());
        fridgesPepsi.addAll(salepoint.getPepsiFridges());
        fridgesHammoud.addAll(salepoint.getHamoudFridges());
        fridgesOther.addAll(salepoint.getOtherFridges());

        etFridgeCount.setText(salepoint.getFridgeCount());


        fridgeAdapter = new FridgeAdapter(getActivity(), fridges, R.drawable.fridge_coca, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                FridgeDialogSurvey fridgeDialogSurvey = new FridgeDialogSurvey();
                // fridgeDialogSurvey.setFridge(fridges.get(position));

                fridgeDialogSurvey.setFridges(fridges);
                fridgeDialogSurvey.setIndex(position);
                fridgeDialogSurvey.setTargetFragment(FridgeFragment.this, 1223);
                fridgeDialogSurvey.setCancelable(false);

                showFragment(fridgeDialogSurvey, getActivity().getSupportFragmentManager(), true);

            }
        });

        fridgePepsiAdapter = new ConccurentFridgeAdapter(getActivity(), fridgesPepsi, R.drawable.fridge_pepsi, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                edit(fridgesPepsi, position, false, fridgePepsiAdapter);

            }
        });


        fridgeHammoudAdapter = new ConccurentFridgeAdapter(getActivity(), fridgesHammoud, R.drawable.fridge_hamoud, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                edit(fridgesHammoud, position, false, fridgeHammoudAdapter);

            }
        });

        fridgeOtherAdapter = new ConccurentFridgeAdapter(getActivity(), fridgesOther, R.drawable.frigo_other, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                edit(fridgesOther, position, true, fridgeOtherAdapter);

            }
        });


        horizontalLayout
                = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        rvFridgesPepsi.setLayoutManager(horizontalLayout);
        rvFridgesHammoud.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false));
        rvFridgesOther.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false));
        rvFridges.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false));

        rvFridges.setAdapter(fridgeAdapter);
        rvFridgesPepsi.setAdapter(fridgePepsiAdapter);
        rvFridgesHammoud.setAdapter(fridgeHammoudAdapter);
        rvFridgesOther.setAdapter(fridgeOtherAdapter);

        return v;
    }

    @OnClick(R.id.ivInfo)
    public void info() {
        Intent intent = new Intent(getActivity(), PdfViewer.class);
        intent.putExtra("source", "fridge");
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.add)
    public void AddElement() {
        //  add(mTagContainerLayout);
        // showFragment(new FridgeDialogSurvey(), MainActivity.this.getSupportFragmentManager(), true);

        Fridge fridge = new Fridge();
        fridge.setSalepointMobileId(salepoint.getMobile_id());
        fridge.setMobileId("fridge_" + UUID.randomUUID().toString());
        fridges.add(fridge);

        FridgeDialogSurvey fridgeDialogSurvey = new FridgeDialogSurvey();
        // fridgeDialogSurvey.setFridge(fridges.get(fridges.size()-1));
        fridgeDialogSurvey.setFridges(fridges);
        fridgeDialogSurvey.setIndex(fridges.size() - 1);
        fridgeDialogSurvey.setCancelable(false);
        fridgeDialogSurvey.setTargetFragment(FridgeFragment.this, 1223);

        showFragment(fridgeDialogSurvey, getActivity().getSupportFragmentManager(), true);
        fridgeAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.addPepsi)
    public void addPepsi() {

        add(fridgesPepsi, "Pepsi", fridgePepsiAdapter);
        //fridgesPepsi.add(new ConcurrentFridge());
        fridgePepsiAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.addHammoud)
    public void addHammoud() {
        add(fridgesHammoud, "Hammoud Boulam", fridgeHammoudAdapter);
        // fridgesHammoud.add(new ConcurrentFridge());
        fridgeHammoudAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.addOther)
    public void addOther() {
        add(fridgesOther, null, fridgeOtherAdapter);
        //  fridgesOther.add(new ConcurrentFridge());
        fridgeOtherAdapter.notifyDataSetChanged();
    }

    public static void showFragment(Fragment fragment, FragmentManager fragmentManager, boolean withAnimation) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (withAnimation) {
            transaction.setCustomAnimations(R.anim.slide_up_anim, R.anim.slide_down_anim);
        } else {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        transaction.add(android.R.id.content, fragment).addToBackStack(null).commitAllowingStateLoss();
    }

    @OnClick(R.id.fabNext)
    public void save() {
        setData();
        setData();
        getActivity().finish();


    }

    @OnClick(R.id.fabPrev)
    public void prev() {
        setData();
        getActivity().onBackPressed();


    }

    ImageView ivDialogPhoto;
    String id;
    String type;

    public void add(RealmList<ConcurrentFridge> list, @Nullable String brand, ConccurentFridgeAdapter adapter) {

        SecureRandom random = new SecureRandom();
        int num = random.nextInt(99);
        ConcurrentFridge concurrentFridge = new ConcurrentFridge();
        concurrentFridge.setMobile_id("cfridge_" + UUID.randomUUID());


        final EditText name = new EditText(getActivity());
        final RadioGroup type = new RadioGroup(getActivity());

        final TextView textviewType = new TextView(getActivity());
        textviewType.setText("Type de frigo");
        textviewType.setTextColor(getResources().getColor(R.color.colorAccent));
        textviewType.setTypeface(null, Typeface.BOLD);
        final RadioButton typeOneDoor = new RadioButton(getActivity());
        typeOneDoor.setText("1 Porte");
        final RadioButton typeTwoDoor = new RadioButton(getActivity());
        typeTwoDoor.setText("2 Porte");
        type.setOrientation(RadioGroup.HORIZONTAL);
        type.addView(typeOneDoor);
        type.addView(typeTwoDoor);

        name.setGravity(Gravity.CENTER);
        type.setGravity(Gravity.CENTER);
        textviewType.setGravity(Gravity.CENTER);

        ivDialogPhoto = new ImageView(getActivity());

        ivDialogPhoto.setMaxHeight(80);
        ivDialogPhoto.setMaxWidth(80);
        ivDialogPhoto.setImageResource(R.drawable.photo_red);

        final TextView textviewex = new TextView(getActivity());
        textviewex.setText("Frigo en exterieur");
        textviewex.setTextColor(getResources().getColor(R.color.colorAccent));
        textviewex.setTypeface(null, Typeface.BOLD);

        final RadioGroup external = new RadioGroup(getActivity());
        final RadioButton externalYes = new RadioButton(getActivity());
        externalYes.setText("Oui");
        final RadioButton externalNo = new RadioButton(getActivity());
        externalNo.setText("Non");
        external.setOrientation(RadioGroup.HORIZONTAL);
        external.addView(externalYes);
        external.addView(externalNo);


        textviewex.setGravity(Gravity.CENTER);
        external.setGravity(Gravity.CENTER);


        name.setHint("Marque du frigo");
        if (brand != null) {
            name.setText(brand);
            name.setEnabled(false);
        }


        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitle("Brand");
        sweetAlertDialog.setContentText("Entrer le nom de la marque  ");
        sweetAlertDialog.setConfirmButton("Oui", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                String nameText = name.getText().toString();
                Photo photo = realm.where(Photo.class).equalTo("TypeID", concurrentFridge.getMobile_id()).and().equalTo("Type", Constants.IMG_CFRIDGE).findFirst();

                // String quantityText = quantity.getText().toString().trim();
                if (nameText.length() > 0 && type.getCheckedRadioButtonId() != -1 && external.getCheckedRadioButtonId() != -1 && photo != null) {
                    int typechecked = 2;
                    if (typeOneDoor.isChecked())
                        typechecked = 1;

                    int isExternal = 0;
                    if (externalYes.isChecked())
                        isExternal = 1;

                    concurrentFridge.setBrand(nameText);
                    concurrentFridge.setExternal(isExternal);
                    concurrentFridge.setType(typechecked);

                    list.add(concurrentFridge);
                    // Log.e("taglistLength",tags.size()+" ");
                    adapter.notifyDataSetChanged();
                    sweetAlertDialog.dismiss();

                } else {
                    Toasty.error(getActivity(), "Veillez remplir tous les champs", 5000).show();
                }

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
        linearLayout.addView(external, index + 1);
        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(textviewex, index + 1);
        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(type, index + 1);


        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(textviewType, index + 1);

        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(name, index + 1);

        ivDialogPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePhoto(Constants.IMG_CFRIDGE, concurrentFridge.getMobile_id());
            }
        });

    }

    private void edit(RealmList<ConcurrentFridge> fridges, int position, boolean editname, ConccurentFridgeAdapter adapter) {

        ConcurrentFridge fridge = fridges.get(position);
        final EditText name = new EditText(getActivity());
        final RadioGroup type = new RadioGroup(getActivity());
        if (fridge.getMobile_id() == null || fridge.getMobile_id().length() == 0)
            fridge.setMobile_id("cfridge_" + UUID.randomUUID());


        final TextView textviewType = new TextView(getActivity());
        textviewType.setText("Type de frigo");
        textviewType.setTextColor(getResources().getColor(R.color.colorAccent));
        textviewType.setTypeface(null, Typeface.BOLD);
        final RadioButton typeOneDoor = new RadioButton(getActivity());
        typeOneDoor.setText("1 Porte");
        final RadioButton typeTwoDoor = new RadioButton(getActivity());
        typeTwoDoor.setText("2 Porte");
        type.setOrientation(RadioGroup.HORIZONTAL);
        type.addView(typeOneDoor);
        type.addView(typeTwoDoor);

        name.setGravity(Gravity.CENTER);
        type.setGravity(Gravity.CENTER);
        textviewType.setGravity(Gravity.CENTER);

        ivDialogPhoto = new ImageView(getActivity());

        ivDialogPhoto.setMaxHeight(100);
        ivDialogPhoto.setMaxWidth(100);

        Photo photo = realm.where(Photo.class).equalTo("TypeID", fridge.getMobile_id()).and().equalTo("Type", Constants.IMG_CFRIDGE).findFirst();
        if (photo != null)
            ivDialogPhoto.setImageBitmap(Base64Util.Base64ToBitmap(photo.getImage(), 4));
        else
            ivDialogPhoto.setImageResource(R.drawable.photo_red);

        final TextView textviewex = new TextView(getActivity());
        textviewex.setText("Frigo en exterieur");
        textviewex.setTextColor(getResources().getColor(R.color.colorAccent));
        textviewex.setTypeface(null, Typeface.BOLD);

        final RadioGroup external = new RadioGroup(getActivity());
        final RadioButton externalYes = new RadioButton(getActivity());
        externalYes.setText("Oui");
        final RadioButton externalNo = new RadioButton(getActivity());
        externalNo.setText("Non");
        external.setOrientation(RadioGroup.HORIZONTAL);
        external.addView(externalYes);
        external.addView(externalNo);


        textviewex.setGravity(Gravity.CENTER);
        external.setGravity(Gravity.CENTER);


        name.setGravity(Gravity.CENTER);
        name.setHint("Marque du frigo");
        name.setText(fridge.getBrand());
        if (!editname) {
            name.setEnabled(false);
        }

        if (fridge.getType() == 1) {
            typeOneDoor.setChecked(true);
        } else if (fridge.getType() == 2) {
            typeTwoDoor.setChecked(true);
        }
        if (fridge.getExternal() == 1) {
            externalYes.setChecked(true);
        } else if (fridge.getExternal() == 0) {
            externalNo.setChecked(true);
        }

        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitle("Brand");
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
                int checked = type.getCheckedRadioButtonId();
                int chekcedExternal = external.getCheckedRadioButtonId();

                if (nameText.length() > 0 && checked != -1 && chekcedExternal != -1 && photo != null) {

                    int typechecked = 2;
                    if (typeOneDoor.isChecked())
                        typechecked = 1;

                    int isExternal = 0;
                    if (externalYes.isChecked())
                        isExternal = 1;
                    fridges.remove(position);
                    fridge.setBrand(nameText);
                    fridge.setExternal(isExternal);
                    fridge.setType(typechecked);
                    fridges.add(fridge);

                    // Log.e("taglistLength",tags.size()+" ");
                    adapter.notifyDataSetChanged();
                    sweetAlertDialog.dismiss();

                } else {

                    Toasty.warning(getContext(), "Veuillez remplir tous les champs", 5000).show();
                }
                //  sweetAlertDialog.dismiss();
            }
        });
        sweetAlertDialog.setCancelButton("Supprimer", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                fridges.remove(position);
                adapter.notifyDataSetChanged();

                sweetAlertDialog.dismiss();

            }

        });

        sweetAlertDialog.show();
        LinearLayout linearLayout = (LinearLayout) sweetAlertDialog.findViewById(R.id.loading);
        int index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(ivDialogPhoto, index + 1);
        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(external, index + 1);
        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(textviewex, index + 1);
        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(type, index + 1);


        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(textviewType, index + 1);

        index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));

        linearLayout.addView(name, index + 1);
        ivDialogPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePhoto(Constants.IMG_CFRIDGE, fridge.getMobile_id());
            }
        });


    }

    @Override
    public void setFridge(Fridge fridge, int index) {
        fridges.set(index, fridge);
        Log.e("fridge", new Gson().toJson(fridge));
        fridgeAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteFridge(int index) {
        fridges.remove(index);
        fridgeAdapter.notifyDataSetChanged();

    }

    private void setData() {
        salepoint.setCocaColaFridgesCount(fridges.size());
        salepoint.setCocaColaFridges(fridges);
        salepoint.setPepsiFridges(fridgesPepsi);
        salepoint.setHamoudFridges(fridgesHammoud);
        salepoint.setOtherFridges(fridgesOther);
        salepoint.setFridgeCount(etFridgeCount.getText().toString());
        session.setSalepoint(salepoint);


    }


    private Uri fileUri;
    int MEDIA_TYPE_IMAGE = 1;

    public void takePhoto(String type, String id) {


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

            if (sweetAlertDialog.isShowing()) {
                ivDialogPhoto.setImageBitmap(mutableBitmap);


            }
            //   ivPhoto.setImageBitmap(mutableBitmap);
            String imageBase64 = UtilBase64.bitmapToBase64String(mutableBitmap);

            // fridge.setPhotoFridge(imageBase64);
            realm.beginTransaction();


            Photo photo = realm.createObject(Photo.class, PrimaryKeyFactory.nextKey(Photo.class));

            photo.setImageID("cfridge_" + UUID.randomUUID());
            photo.setDate(System.currentTimeMillis() / 1000 + "");
            if (type.equals(Constants.IMG_PLV_Internal)) {
                photo.setTypeID(salepoint.getMobile_id());
            } else {
                photo.setTypeID(id);
            }
            photo.setImage(imageBase64);
            photo.setType(type);

            realm.commitTransaction();


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


}
