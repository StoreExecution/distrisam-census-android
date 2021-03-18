package com.storexecution.cocacola;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.storexecution.cocacola.adapter.FridgeAdapter;
import com.storexecution.cocacola.model.Fridge;
import com.storexecution.cocacola.model.TagElement;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.RecyclerItemClickListener;
import com.storexecution.cocacola.util.SharedPrefUtil;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import es.dmoral.toasty.Toasty;

public class MainActivity extends BaseActivity {


    SharedPrefUtil sharedPrefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefUtil = SharedPrefUtil.getInstance(getApplicationContext());
        if (sharedPrefUtil.getBoolean(Constants.LOGGED, false) == false) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }


    }


}
