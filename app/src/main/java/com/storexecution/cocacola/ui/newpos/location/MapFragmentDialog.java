package com.storexecution.cocacola.ui.newpos.location;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.pappin.mbs.MaterialBarcodeScanner;
import com.github.pappin.mbs.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
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
import com.storexecution.cocacola.ui.map.MapActivity;
import com.storexecution.cocacola.ui.newpos.fridge.FridgeDialogSurvey;
import com.storexecution.cocacola.util.Base64Util;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.ImageLoad;
import com.storexecution.cocacola.util.PrimaryKeyFactory;
import com.storexecution.cocacola.util.RecyclerItemClickListener;
import com.storexecution.cocacola.util.Session;
import com.storexecution.cocacola.util.UtilBase64;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.io.File;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
public class MapFragmentDialog extends DialogFragment {

    public MapFragmentDialog() {
        setCancelable(false);
    }

    /**
     * ButterKnife Code
     **/
    @BindView(R.id.mvMap)
    org.osmdroid.views.MapView mvMap;
    @BindView(R.id.btNo)
    Button btNo;
    @BindView(R.id.btYes)
    Button btYes;

    String currentTiles;
    /**
     * ButterKnife Code
     **/

    Location location;
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.map_fragment_dialog, container, false);
        ButterKnife.bind(this, v);
        Configuration.getInstance().setUserAgentValue("Store Execution/2");


        org.osmdroid.config.IConfigurationProvider osmConf = org.osmdroid.config.Configuration.getInstance();
        File basePath = new File(getActivity().getCacheDir().getAbsolutePath(), "osmdroid");
        osmConf.setOsmdroidBasePath(basePath);
        File tileCache = new File(osmConf.getOsmdroidBasePath().getAbsolutePath(), "tile");
        osmConf.setOsmdroidTileCache(tileCache);
        currentTiles = Constants.TILES_BING;

        BingMapTileSource.retrieveBingKey(getActivity());
        setTileSource();
        mvMap.setBuiltInZoomControls(true);
        mvMap.setMultiTouchControls(true);
        Marker marker = new Marker(mvMap);
        marker.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
        marker.setIcon(getResources().getDrawable(R.drawable.person_marker));
        mvMap.getOverlays().add(marker);
        mvMap.getController().animateTo(marker.getPosition(), 20.0, 1000l);

        return v;


    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @OnClick(R.id.ivMap)
    public void setTileSource() {

        if (currentTiles.equals(Constants.TILES_DEFAULT)) {
            try {
                BingMapTileSource.retrieveBingKey(getActivity());
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

    @OnClick(R.id.btYes)
    public void validaLocation() {

        LocationValidationCallbackInterface callbackInterfrae = (LocationValidationCallbackInterface) getTargetFragment();
        callbackInterfrae.setValidLocation(location);
        getFragmentManager().popBackStack();

    }

    @OnClick(R.id.btNo)
    public void dissmissLocation() {

        LocationValidationCallbackInterface callbackInterfrae = (LocationValidationCallbackInterface) getTargetFragment();
        callbackInterfrae.unsetLocation();

        getFragmentManager().popBackStack();

    }

}
