package com.storexecution.cocacola;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.ui.newpos.storeinformation.StoreInformationFragment;
import com.storexecution.cocacola.util.Session;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocalityFragment extends Fragment {


    Session session;
    Salepoint salepoint;

    public LocalityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_locality, container, false);
        ButterKnife.bind(this, v);
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();

        return v;
    }


    @OnClick(R.id.fabNext)
    public void save() {
        StoreInformationFragment fragment = new StoreInformationFragment();

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment, "storeInfo")
                .addToBackStack(getClass().getName())
                .commit();


    }

    @OnClick(R.id.fabPrev)
    public void prev() {

        getActivity().onBackPressed();


    }
}
