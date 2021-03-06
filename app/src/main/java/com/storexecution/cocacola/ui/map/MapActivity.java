package com.storexecution.cocacola.ui.map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.storexecution.cocacola.adapter.SectorAdapter;
import com.storexecution.cocacola.event.DownloadFinishedEvent;
import com.storexecution.cocacola.event.LocationUpdatedEvent;
import com.storexecution.cocacola.event.MockPosition;
import com.storexecution.cocacola.model.Filter;
import com.storexecution.cocacola.model.Sector;
import com.storexecution.cocacola.network.ApiEndpointInterface;
import com.storexecution.cocacola.network.RetrofitClient;
import com.storexecution.cocacola.service.LocationUpdatesService;
import com.storexecution.cocacola.ui.newpos.NewSurveyActivity;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.adapter.SalepointAdapter;
import com.storexecution.cocacola.model.Commune;
import com.storexecution.cocacola.model.MyKmlStyler;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.model.Suivi;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.ui.rtm.RTMMapActivity;
import com.storexecution.cocacola.util.Base64Util;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.RecyclerItemClickListener;
import com.storexecution.cocacola.util.SalepointTypeUtils;
import com.storexecution.cocacola.util.Session;
import com.storexecution.cocacola.util.SharedPrefUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements Marker.OnMarkerClickListener, LocationListener {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.drawer_layout)
    androidx.drawerlayout.widget.DrawerLayout drawerLayout;
    @BindView(R.id.content_frame)
    FrameLayout contentFrame;
    @BindView(R.id.mvMap)
    org.osmdroid.views.MapView mvMap;
    @BindView(R.id.ivOpen)
    ImageView ivOpen;
    @BindView(R.id.liste)
    LinearLayout liste;
    @BindView(R.id.llEdited)
    LinearLayout llEdited;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btAll)
    Button btAll;
    @BindView(R.id.btSyncSectors)
    Button btSyncSectors;
    @BindView(R.id.btEdited)
    Button btEdited;
    @BindView(R.id.btEdite)
    Button btEdite;
    @BindView(R.id.tvCalendar)
    TextView tvCalendar;
    @BindView(R.id.rvSalepoints)
    androidx.recyclerview.widget.RecyclerView rvSalepoints;
    @BindView(R.id.ivMap)
    ImageView ivMap;
    @BindView(R.id.fabLocate)
    FloatingActionButton fabLocate;


    /**
     * ButterKnife Code
     **/
    @BindView(R.id.tvID)
    TextView tvID;
    @BindView(R.id.tvPos)
    TextView tvPos;
    @BindView(R.id.tvOwner)
    TextView tvOwner;
    @BindView(R.id.tvTypePos)
    TextView tvTypePos;
    @BindView(R.id.tvWilaya)
    TextView tvWilaya;
    @BindView(R.id.tvCommune)
    TextView tvCommune;
    @BindView(R.id.tvAdresse)
    TextView tvAdresse;
    @BindView(R.id.tvVan)
    TextView tvVan;
    @BindView(R.id.tvDay)
    TextView tvDay;
    @BindView(R.id.tvBarcode)
    TextView tvBarcode;
    @BindView(R.id.tvCluster)
    TextView tvCluster;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.btClosing)
    Button btClosing;
    @BindView(R.id.btRoute)
    Button btRoute;
    @BindView(R.id.btActivityChange)
    Button btActivityChange;
    @BindView(R.id.btDN)
    Button btDN;
    @BindView(R.id.btEdit)
    Button btEdit;
    @BindView(R.id.ivCancel)
    ImageView ivCancel;
    @BindView(R.id.lvDrawer)
    RecyclerView lvDrawer;

    @BindView(R.id.workDetails)
    CardView workDetails;
    IMapController mapController;
    private List<Marker> markers;
    private RealmList<Salepoint> salepoints;
    int currentPosition = 0;
    Salepoint currentSalepoint;
    boolean detailsShowing = false;
    SalepointAdapter salepointAdapter;

    Realm realm;
    Session session;
    Marker userMarker;
    String currentTiles;
    SharedPrefUtil sharedPreferences;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    LocationManager locationManager;
    User user;
    SweetAlertDialog pDialog;
    RealmList<Sector> sectors;

    Gson gson;
    Map<String, String> headers;
    SharedPrefUtil sharedPrefUtil;
    SectorAdapter sectorAdapter;
    int selectedSectorID = 0;


    ApiEndpointInterface service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();
        Configuration.getInstance().setUserAgentValue("Store Execution/3");
        gson = new Gson();
        service = RetrofitClient.getRetrofitInstance().create(ApiEndpointInterface.class);
        sharedPrefUtil = SharedPrefUtil.getInstance(getApplicationContext());
        headers = new HashMap<>();
        headers.put("Authorization", "bearer " + sharedPrefUtil.getString(Constants.ACCESS_TOKEN, ""));
        user = realm.where(User.class).findFirst();
        org.osmdroid.config.IConfigurationProvider osmConf = org.osmdroid.config.Configuration.getInstance();
        File basePath = new File(getCacheDir().getAbsolutePath(), "osmdroid");
        osmConf.setOsmdroidBasePath(basePath);
        File tileCache = new File(osmConf.getOsmdroidBasePath().getAbsolutePath(), "tile");
        osmConf.setOsmdroidTileCache(tileCache);
        sharedPreferences = SharedPrefUtil.getInstance(this);
        salepoints = new RealmList<>();
        sectors = new RealmList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        selectedSectorID = sharedPreferences.getInt("selectedSectorID", 0);
        sectors.addAll(realm.where(Sector.class).equalTo("user_id", user.getId()).and().equalTo("day", DateUtils.todayDate()).findAll());
        sectorAdapter = new SectorAdapter(this, sectors, selectedSectorID, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (sectors.get(position).getOpen() == 1) {
                    if (sectors.get(position).getLocalFile() != null && sectors.get(position).getLocalFile().length() > 0) {
                        sharedPreferences.putInt("selectedSectorID", sectors.get(position).getId());
                        sectorAdapter.setSelectedId(sectors.get(position).getId());
                        sectorAdapter.notifyDataSetChanged();
                        sharedPreferences.putString("paths", sectors.get(position).getLocalFile());
                        loadSector(sectors.get(position).getLocalFile());
                    } else {

                        pDialog = new SweetAlertDialog(MapActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("T??l??chargement Secteurs");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        Toasty.info(MapActivity.this, "T??l??chargement du secteur", 4000).show();
                        RealmList<Sector> toDownload = new RealmList<>();
                        toDownload.add(sectors.get(position));
                        downloadSectors(toDownload, 0);
                    }
                } else {
                    Toasty.warning(MapActivity.this, "Ce secteur n'est pas encore actif", 3000).show();
                }
            }
        });
        lvDrawer.setLayoutManager(new LinearLayoutManager(this));

        lvDrawer.setAdapter(sectorAdapter);


        salepoints.addAll(realm.where(Salepoint.class).sort("mobileModificationDate", Sort.DESCENDING).findAll());
        workDetails.setVisibility(View.INVISIBLE);
        //mvMap.setTileSource(TileSourceFactory.MAPNIK);
        currentTiles = Constants.TILES_BING;

        BingMapTileSource.retrieveBingKey(MapActivity.this);
        setTileSource();
        mvMap.setBuiltInZoomControls(true);
        mvMap.setMultiTouchControls(true);
        session = new Session(getApplicationContext());
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                UUID.randomUUID().toString());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        salepointAdapter = new SalepointAdapter(this, salepoints, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                onSalepoint(position);

            }
        });
        rvSalepoints.setLayoutManager(new LinearLayoutManager(this));
        rvSalepoints.setAdapter(salepointAdapter);


        markers = new ArrayList<>();
        loadMarkers();
        mapController = mvMap.getController();
    }

    private void loadMarkers() {
        mvMap.invalidate();
        mvMap.getOverlays().clear();
        markers.clear();

        for (Salepoint salepoint : salepoints) {
            Marker marker = new Marker(mvMap);
            GeoPoint geo = new GeoPoint(salepoint.getLatitude(), salepoint.getLongitude());
            marker.setPosition(geo);
            marker.setIcon(getResources().getDrawable(R.drawable.marker_red));
            mvMap.getOverlays().add(marker);
            marker.setOnMarkerClickListener(this);
            markers.add(marker);


        }
        loadUserPosition();
        String path = sharedPreferences.getString("paths", null);
        if (path != null) {
            loadSector(path);
        } else if (markers.size() > 0) {
            //    mvMap.getController().setCenter(markers.get(0).getPosition());
            mvMap.getController().animateTo(markers.get(0).getPosition(), 15.0, 1300l);
        } else {

            mvMap.getController().animateTo(new GeoPoint(36.0, 3.01), 14.0, 1300l);
        }


    }

    private void loadUserPosition() {
        Suivi suivi = realm.where(Suivi.class).sort("milis", Sort.DESCENDING).findFirst();
        if (suivi != null) {
            userMarker = new Marker(mvMap);
            GeoPoint geo = new GeoPoint(suivi.getLatitude(), suivi.getLongitude());
            userMarker.setIcon(getResources().getDrawable(R.drawable.person_marker));
            userMarker.setPosition(geo);
            mvMap.getOverlays().add(userMarker);

            mvMap.getController().animateTo(userMarker.getPosition(), 15.0, 1300l);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {

        onSalepoint(markers.indexOf(marker));
        return true;
    }

    @OnClick(R.id.btSyncSectors)
    public void syncSector() {

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitle("Synchronisation ... ");
        sweetAlertDialog.show();
        Filter filter = new Filter();
        filter.setUserId(user.getId());
        filter.setDay(DateUtils.todayDate());
        service.fetchSectors(headers, gson.toJson(filter)).enqueue(new Callback<ArrayList<Sector>>() {
            @Override
            public void onResponse(Call<ArrayList<Sector>> call, Response<ArrayList<Sector>> response) {
                Log.e("call", call.request().url().toString());

                if (response.code() == 200 && response.body() != null) {
                    Log.e("size", response.body().size() + " ");
                    realm.beginTransaction();
                    realm.insertOrUpdate(response.body());
                    realm.commitTransaction();
                    sectors.clear();
                    sectors.addAll(realm.where(Sector.class).equalTo("user_id", user.getId()).and().equalTo("day", DateUtils.todayDate()).findAll());
                    ;
                    sectorAdapter.notifyDataSetChanged();
                    int index = 0;
                    pDialog = new SweetAlertDialog(MapActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("T??l??chargement Secteurs");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    downloadSectors(sectors, 0);
                }
                sweetAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<Sector>> call, Throwable t) {
                sweetAlertDialog.dismiss();
                sectorAdapter.notifyDataSetChanged();
                Toasty.error(MapActivity.this, "Erreur de connexion", 3000).show();


            }
        });


    }


    public void onSalepoint(int position) {

        currentPosition = position;

        currentSalepoint = salepoints.get(position);
        currentPosition = position;
        // mapController.setCenter(markers.get(position).getPosition());
        mvMap.getController().animateTo(markers.get(position).getPosition(), 19.0, 1300l);

        //mapController.setZoom(17);
        loadDetails(currentSalepoint);
        if (!detailsShowing)
            showDetails();
    }

    private void loadDetails(Salepoint i) {


        tvPos.setText(i.getPosName());
        tvAdresse.setText(i.getLandmark());
        Commune commune = realm.where(Commune.class).equalTo("id", i.getCommune()).findFirst();
        if (commune != null)
            tvCommune.setText(commune.getName());

        tvTypePos.setText(SalepointTypeUtils.getType(i.getSalepointType()));
        tvOwner.setText(i.getOwnerName());
        tvBarcode.setText(i.getBarcode());


        Photo photo = realm.where(Photo.class).equalTo("TypeID", i.getMobile_id()).and().equalTo("Type", Constants.IMG_POS).findFirst();
        if (photo != null)
            ivImage.setImageBitmap(Base64Util.Base64ToBitmap(photo.getImage()));
        else
            ivImage.setImageBitmap(null);


        // if()

    }

    @OnClick(R.id.ivCancel)
    public void cancel() {

        detailsShowing = false;
        mvMap.setClickable(true);
        mvMap.requestDisallowInterceptTouchEvent(false);
        hideDetails();
        System.gc();

    }

    private void hideDetails() {
        YoYo.with(Techniques.SlideOutUp)
                .duration(700)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        workDetails.setVisibility(View.INVISIBLE);
                        // lvInvestigations.setItemChecked(lvInvestigations.getCheckedItemPosition(), false);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .playOn(workDetails)

        ;
    }


    private void showDetails() {

        detailsShowing = true;
        mvMap.setClickable(false);
        mvMap.requestDisallowInterceptTouchEvent(true);

        YoYo.with(Techniques.SlideInUp)
                .duration(700)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        workDetails.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(workDetails)

        ;
    }


    @OnClick(R.id.ivImage)
    public void ImagePreview() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_image, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {

                ImageView iv = (ImageView) dialog.findViewById(R.id.ivImage);

                Photo photo = realm.where(Photo.class).equalTo("TypeID", currentSalepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_POS).findFirst();
                if (photo != null)
                    iv.setImageBitmap(Base64Util.Base64ToBitmap(photo.getImage(), 2));


            }
        });

        dialog.show();
    }


    @OnClick(R.id.btEdit)
    public void edit() {
        session.setSalepoint(realm.copyFromRealm(currentSalepoint));
        startActivity(new Intent(this, NewSurveyActivity.class));

    }

    Polyline polyline;

    @OnClick(R.id.fabTracking)
    public void showTracking() {
        if (polyline != null) {
            mvMap.getOverlays().remove(polyline);
        }
        ArrayList<Suivi> trackings = new ArrayList<>(realm.where(Suivi.class).equalTo("jour", DateUtils.todayDate()).sort("milis", Sort.ASCENDING).findAll());
        polyline = new Polyline();
        for (Suivi tracking : trackings)
            polyline.addPoint(new GeoPoint(tracking.getLatitude(), tracking.getLongitude()));
        if (trackings.size() > 0) {
            mvMap.getController().animateTo(new GeoPoint(trackings.get(0).getLatitude(), trackings.get(0).getLongitude()));
            // polyline.setColor();

        }
        mvMap.getOverlays().add(polyline);

    }

    @OnClick(R.id.fabLocate)
    public void setLoation() {


        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsActive = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsActive) {

            pDialog = new SweetAlertDialog(MapActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Localisation");
            pDialog.setCancelable(false);
            pDialog.show();
            // btgps.showLoading();
//            gpsLocal();
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }

        } else {

            Toasty.error(MapActivity.this, "Veuillez activer votre GPS", 5000).show();
        }
    }

    @Subscribe
    public void onEvent(LocationUpdatedEvent event) {
        EventBus.getDefault().unregister(this);
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
        userMarker = new Marker(mvMap);
        GeoPoint geo = new GeoPoint(event.getLocation().getLatitude(), event.getLocation().getLongitude());
        userMarker.setIcon(getResources().getDrawable(R.drawable.person_marker));
        userMarker.setPosition(geo);
        mvMap.getOverlays().add(userMarker);
        // mvMap.getController().setCenter(userMarker.getPosition());
        double zoomlevel = mvMap.getZoomLevelDouble();
        if (mvMap.getZoomLevelDouble() < 18.0)
            zoomlevel = 18.0;


        mvMap.getController().animateTo(userMarker.getPosition(), zoomlevel, 1300l);

    }

    @Subscribe
    public void onEvent(MockPosition event) {
        EventBus.getDefault().unregister(this);
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();

        Toasty.error(this, "Fausse position detect??", Toasty.LENGTH_LONG).show();


    }


    @SuppressLint("MissingPermission")
    private void gpsLocal() {

        //makeToast("Vous Utiliser le GPS ,cela peut prendre plusieurs minutes");
        Toasty.info(MapActivity.this, "Vous Utiliser le GPS ,cela peut prendre plusieurs minutes").show();

        fabLocate.setEnabled(false);
        wakeLock.acquire();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, true);

        locationManager.requestLocationUpdates(provider, 0, 1, this);


    }

    @OnClick(R.id.ivMap)
    public void setTileSource() {

        if (currentTiles.equals(Constants.TILES_DEFAULT)) {
            try {
                BingMapTileSource.retrieveBingKey(this);
                String m_locale = Locale.getDefault().getDisplayName();
                BingMapTileSource bing = new BingMapTileSource(m_locale);
                bing.setStyle(BingMapTileSource.IMAGERYSET_AERIALWITHLABELS);
                mvMap.setTileSource(bing);
                mvMap.setMaxZoomLevel(19.0);
                currentTiles = Constants.TILES_BING;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // mvMap.setTileSource(TileSourceFactory.USGS_TOPO);

            mvMap.setTileSource(TileSourceFactory.MAPNIK);
            mvMap.setMaxZoomLevel(22.0);

            currentTiles = Constants.TILES_DEFAULT;
        }
        mvMap.invalidate();
    }

    int FILE_PICKER_REQUEST_CODE = 9381;


    @OnClick(R.id.fabSector)
    public void open() {
        drawerLayout.openDrawer(Gravity.RIGHT, true);
    }


    public void sector2() {

        String external = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();

        String external2 = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        //   String downloads = new File(external, "Downloads").getAbsolutePath();
//        new File(
//                getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
//                "Downloads");

        new MaterialFilePicker()
                // Pass a source of context. Can be:
                //    .withActivity(Activity activity)
                //    .withFragment(Fragment fragment)
                //    .withSupportFragment(androidx.fragment.app.Fragment fragment)
                .withActivity(this)
                // With cross icon on the right side of toolbar for closing picker straight away
                .withCloseMenu(true)
                //   .withPath(downloads)
                // Root path (user won't be able to come higher than it)
                .withRootPath(external2)

                // Entry point path (user will start from it)
                // .withPath(alarmsFolder.absolutePath)
                // Root path (user won't be able to come higher than it)
                //  .withRootPath(externalStorage.absolutePath)
                // Showing hidden files
                .withHiddenFiles(true)
                // Want to choose only jpg images
                .withFilter(Pattern.compile(".*\\.(kml)$"))
                // Don't apply filter to directories names
                .withFilterDirectories(false)
                .withTitle("Ouvrir un fichier secteur")

                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            importFile(filePath);
            Log.e("test", filePath);
            // Do anything with file
        }
    }


    public void importFile(String realPath) {
//        String fileName = getFileName(uri);
//        Log.e("kml", uri.getPath() + " ");
//        Log.e("realkml", FileUtils.getRealPath(this, uri) + " ");
//        getRealPathUtil.getRealPathFromURI(this, uri);
//        String selectedFilePath = FilePath.getPath(getActivity(), uri);
        //final File file = new File(realPath);


        // The temp file could be whatever you want
        loadSector(realPath);

        // Done!
    }

    FolderOverlay kmlOverlay;

    public void loadSector(String path) {
        Log.e("log", " " + path);
        // Toasty.warning(MapActivity.this, "loadSector", 2000).show();

        KmlDocument kmlDocument = new KmlDocument();
        try {

            File file = new File(path);
            if (kmlOverlay != null) {
                mvMap.getOverlays().remove(kmlOverlay);
            }
            if (file.exists()) {

                kmlDocument.parseKMLFile(file);
                KmlFeature.Styler styler = new MyKmlStyler();
                Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);
                Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
                Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 3.0f, 0x20CD7777);
                try {
                    kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mvMap, null, styler, kmlDocument);
                    mvMap.getOverlays().add(kmlOverlay);

                    BoundingBox bb = kmlDocument.mKmlRoot.getBoundingBox();
                    mvMap.getController().animateTo(bb.getCenter(), 15.0, 1200l);

                } catch (StackOverflowError e) {
                    Toasty.error(MapActivity.this, "Secteur invalide ", 10000).show();
                    loadUserPosition();
                }


            }
            // mapController.setZoom(14);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        Log.e("Tracking Service", "onLocationChanged " + location.getAccuracy());
        Suivi suivi;
        if (location.getAccuracy() != 0.0 && location.getAccuracy() < 25.0) {
            fabLocate.setEnabled(true);
            if (locationManager != null) {
                locationManager.removeUpdates(this);
                locationManager = null;
            }
            suivi = new Suivi();
            suivi.setMobile_Id(UUID.randomUUID().toString());
            suivi.setHeur(DateUtils.currentTime());
            suivi.setJour(DateUtils.todayDate());
            suivi.setType("mid");
            if (user != null)
                suivi.setUser(user.getId());
            suivi.setMilis(DateUtils.currentMilis());
            suivi.setLatitude(location.getLatitude());
            suivi.setLongitude(location.getLongitude());
            suivi.setAccurency(location.getAccuracy());
            suivi.setIsmock(location.isFromMockProvider());


            realm.beginTransaction();
            realm.insertOrUpdate(suivi);
            realm.commitTransaction();


            if (pDialog != null && pDialog.isShowing()) {

                pDialog.dismiss();

            }


            // Suivi suivi = realm.where(Suivi.class).sort("milis", Sort.DESCENDING).findFirst();
            if (suivi != null) {
                userMarker = new Marker(mvMap);
                GeoPoint geo = new GeoPoint(suivi.getLatitude(), suivi.getLongitude());
                userMarker.setIcon(getResources().getDrawable(R.drawable.person_marker));
                userMarker.setPosition(geo);
                mvMap.getOverlays().add(userMarker);
                // mvMap.getController().setCenter(userMarker.getPosition());
                mvMap.getController().animateTo(userMarker.getPosition(), 18.0, 1300l);


            }
        }


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void downloadSectors(RealmList<Sector> sectors, int position) {
        Sector sector;
        if (sectors.size() > position) {
            sector = sectors.get(position);

            String savePath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "cocacola" + File.separator + "sector" + File.separator;
            Log.e("Path", "Downloading ...");

            FileDownloader.getImpl().create(Constants.URL_SECTOR + sector.getFileLink())
                    .setPath(savePath + sector.getFileLink())
                    .setAutoRetryTimes(20)

                    .setListener(new FileDownloadListener() {
                        @Override
                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            Log.e("Downloading", "pending");
                        }

                        @Override
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        }

                        @Override
                        protected void completed(BaseDownloadTask task) {
                            Log.e("Downloading", "Path : " + task.getTargetFilePath() + " ");
                            realm.beginTransaction();
                            sectors.get(position).setLocalFile(task.getTargetFilePath());
                            realm.insertOrUpdate(sector);
                            sector.setLocalFile(task.getTargetFilePath());
                            realm.commitTransaction();

                            downloadSectors(sectors, (position + 1));
                        }

                        @Override
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            Log.e("Downloading", "paused");
                        }

                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {
                            Log.e("Downloading", "Error");
                            e.printStackTrace();
                            Toasty.error(MapActivity.this, "Erreur de connexion", 3000).show();

                        }

                        @Override
                        protected void warn(BaseDownloadTask task) {

                            Log.e("Downloading", "Warn");
                            task.getErrorCause().printStackTrace();
                        }
                    }).start();
        } else {
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();
            Toasty.success(MapActivity.this, "Les secteurs ont ??t?? t??l??charg??s", 3000).show();

        }

    }

    @Override
    protected void onDestroy() {

        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
        super.onDestroy();
    }
}
