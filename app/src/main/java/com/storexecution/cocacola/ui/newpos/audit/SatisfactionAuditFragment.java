package com.storexecution.cocacola.ui.newpos.audit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog;
import com.anurag.multiselectionspinner.MultiSpinner;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.hsalf.smileyrating.SmileyRating;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.Session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SatisfactionAuditFragment extends Fragment {

    /**
     * ButterKnife Code
     **/
    @BindView(R.id.rlTinda)
    RelativeLayout rlTinda;
    @BindView(R.id.etDeliveryCompany)
    EditText etDeliveryCompany;
    @BindView(R.id.smile_rating)
    com.hsalf.smileyrating.SmileyRating smileRating;
    @BindView(R.id.spRating)
    Spinner spRating;
    @BindView(R.id.btAddComment)
    Button btAddComment;
    @BindView(R.id.tgComments)
    co.lujun.androidtagview.TagContainerLayout tgComments;
    @BindView(R.id.llNavigation)
    LinearLayout llNavigation;
    @BindView(R.id.fabPrev)
    ImageView fabPrev;
    @BindView(R.id.fabNext)
    ImageView fabNext;
    /**
     * ButterKnife Code
     **/

    Salepoint salepoint;
    Session session;

    ArrayList<Integer> selected;
    ArrayList<MultiSelectModel> comments;
    List<String> ressourceComments;


    public SatisfactionAuditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_satisfaction_audit, container, false);
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();
        ButterKnife.bind(this, v);

        init();
        selected = new ArrayList<>();
        smileRating.setSmileySelectedListener(new SmileyRating.OnSmileySelectedListener() {
            @Override
            public void onSmileySelected(SmileyRating.Type type) {

            }
        });


        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selected.addAll(salepoint.getDeliveryPoints());
        etDeliveryCompany.setText(salepoint.getBestDeliveryCompany());
        Log.e("rating", salepoint.getDeliveryRating() + " ");

        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                return false;
            }
        }).postDelayed(new Runnable() {
            @Override
            public void run() {
                smileRating.setRating(salepoint.getSmileyRating(), true);
            }
        }, 300);

        spRating.setSelection(salepoint.getDeliveryRating());

        setTags();
    }

    private void init() {
        comments = new ArrayList<>();

        ressourceComments = Arrays.asList(getActivity().getResources().getStringArray(R.array.audit));


        int i = 1;
        for (String r : ressourceComments) {
            comments.add(new MultiSelectModel(i, r));
            i++;

        }

        smileRating.setTitle(SmileyRating.Type.TERRIBLE, "Trés Mauvais");
        smileRating.setTitle(SmileyRating.Type.BAD, "Mauvais");
        smileRating.setTitle(SmileyRating.Type.OKAY, "Neutre");
        smileRating.setTitle(SmileyRating.Type.GOOD, "Bon");
        smileRating.setTitle(SmileyRating.Type.GREAT, "Trés Bon");

    }

    @OnClick(R.id.btAddComment)
    public void add() {
        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Commentaire POS") //setting title for dialog
                .titleSize(25)
                .positiveText("OK")
                .negativeText("Annuler")

                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)

                .setMaxSelectionLimit(comments.size()) //you can set maximum checkbox selection limit (Optional)
                .preSelectIDsList(selected) //List of ids that you need to be selected
                .multiSelectList(comments) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        //will return list of selected IDS
                        for (int i = 0; i < selectedIds.size(); i++) {
//                            Toast.makeText(getActivity(), "Selected Ids : " + selectedIds.get(i) + "\n" +
//                                    "Selected Names : " + selectedNames.get(i) + "\n" +
//                                    "DataString : " + dataString, Toast.LENGTH_SHORT).show();

                            setTags();
                        }


                    }

                    @Override
                    public void onCancel() {
                        Log.d("TAG", "Dialog cancelled");
                    }


                });

        multiSelectDialog.show(getActivity().getSupportFragmentManager(), "multiSelectDialog");
    }

    @OnClick(R.id.fabNext)
    public void save() {
        setData();
        getActivity().finish();
    }

    private void setTags() {
        tgComments.removeAllTags();
        for (int i : selected)
            tgComments.addTag(ressourceComments.get(i - 1));
    }

    private void setData() {

        Log.e("rating", smileRating.getSelectedSmiley() + " / " + smileRating.getSelectedSmiley().getRating());

        salepoint.setBestDeliveryCompany(etDeliveryCompany.getText().toString());
        salepoint.setDeliveryRating(spRating.getSelectedItemPosition());
        salepoint.setSmileyRating(smileRating.getSelectedSmiley().getRating());
        RealmList<Integer> selectedlist = new RealmList<>();
        selectedlist.addAll(selected);
        salepoint.setDeliveryPoints(selectedlist);
        session.setSalepoint(salepoint);
    }
}
