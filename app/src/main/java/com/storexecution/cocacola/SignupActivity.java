package com.storexecution.cocacola;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.storexecution.cocacola.model.RequestResponse;
import com.storexecution.cocacola.model.Signup;
import com.storexecution.cocacola.model.Wilaya;
import com.storexecution.cocacola.network.ApiEndpointInterface;
import com.storexecution.cocacola.network.RetrofitClient;

import net.khirr.android.privacypolicy.PrivacyPolicyDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.rlTinda)
    RelativeLayout rlTinda;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etMail)
    EditText etMail;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etPasssword)
    EditText etPasssword;
    @BindView(R.id.spWilaya)
    Spinner spWilaya;
    @BindView(R.id.btSignUp)
    Button btSignUp;
    @BindView(R.id.tvVersion)
    TextView tvVersion;
    /**
     * ButterKnife Code
     **/

    ArrayList<String> wilayas;
    RealmList<Wilaya> wilayasRepo;

    Realm realm;
    PrivacyPolicyDialog dialog;
    ApiEndpointInterface service;
    String wilaya = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        wilayas = new ArrayList<>();
        wilayas.add("Wilaya");
        realm = Realm.getDefaultInstance();
        service = RetrofitClient.getRetrofitInstance().create(ApiEndpointInterface.class);

        wilayasRepo = new RealmList<>();
        wilayasRepo.addAll(realm.where(Wilaya.class).findAll());
        for (Wilaya wilaya : wilayasRepo)
            wilayas.add(wilaya.getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wilayas);
        spWilaya.setAdapter(adapter);

        //  Params: context, termsOfService url, privacyPolicyUrl
        dialog = new PrivacyPolicyDialog(this,
                "https://localhost",
                "https://localhost", true);

        dialog.forceReset();

        // final Intent intent = new Intent(this, SecondActivity.class);

        dialog.setOnClickListener(new PrivacyPolicyDialog.OnClickListener() {
            @Override
            public void onAccept(boolean isFirstTime) {
                Log.e("MainActivity", "Policies accepted");

                storeUser();

            }

            @Override
            public void onCancel() {
                Log.e("MainActivity", "Policies not accepted");
                finish();
            }
        });



        //  Set Europe only
        //  dialog.setEuropeOnly(true);

        // dialog.show();
       // dialog.show();
    }

    public void  setUpDialog(){

        dialog.setTitle("Accord et Conditions d???utilisation  ");

        dialog.addPoliceLine("Entre les deux co-contractants:\n<br/>" +
                "La soci??t?? <b> Sarl Store Ex??cution </b>, ci-apr??s d??nomm?? l???employeur, ??lisant domicile au<b> Bois des cars 02 villa n??03 Delly Ibrahim</b>\n<br/>" +
                "Et :\n <br/>" +
                "<b>Le salari??</b>, celui qu???il l???appose d???une <b>signature ??lectronique </b> et manifeste son consentement sur les dispositions contenues aux articles cit??s et les obligations qui d??coule, sont applicables ?? cet accord sous forme ??lectronique\n<br/>");
        dialog.addPoliceLine("A ??t?? convenu ce qui suit :");
        dialog.addPoliceLine("<br/><b> <font color='red'>Article 01 : Objet de l???accord </font></b>");
        dialog.addPoliceLine("Le pr??sent accord a pour objet de fixer les r??gles et conditions applicables ?? la relation de travail liant <b>le salari?? </b> ?? la soci??t?? <b>Sarl Store Ex??cution</b>, conform??ment aux dispositions l??gales et r??glementaires en vigueur.");
        dialog.addPoliceLine("<br/><b><font color='red'>Article 02 : fonction de l???employ??</font></b>");
        dialog.addPoliceLine("Le salari?? est recrut?? en qualit?? <b>d???Agent d???enqu??te</b> sur la wilaya de <b>" + wilaya + "</b> au sein de la soci??t?? pour effectuer les t??ches suivantes <b>op??ration census Coca-Cola </b>;");
        //  Customizing (Optional)

        dialog.addPoliceLine("<br/><b><font color='red'>Article 03 : Taches & obligation ?? accomplir  :  </font></b>");
        dialog.addPoliceLine("<b>Le salari?? </b> est tenu de produire un rendement conforme aux objectifs qui lui sont assign??s : ");
        dialog.addPoliceLine("<ul>" +
                "<li> &nbsp; L???enqu??teur doit expliquer le but de sa visite au point de vent ; </li> " +
                "<li> &nbsp; Ne doit pas manquer de respecter au point de vente  </li> " +
                "<li> &nbsp; Remonter les informations du point de vente, via l???application </li> " +
                "<li> &nbsp; Visiter 30 Channel par jour  </li> " +
                "<li> &nbsp; Utiliser la Map mobil lors de ses d??placements, et pour la remont??e des informations </li> " +
                "<li> &nbsp; Mettre son ??quipement lors de l???op??ration ;</li> " +
                "<li> &nbsp; Etre r??active et assidus dans son travail </li> " +
                "<li> &nbsp; Ne pas manger et effectu?? des appels t??l??phoniques personnels, durant les heures de travail </li> " +
                "<li> &nbsp; Ne pas mettre un jeans trou??, surv??tement, short, et claquette, durant les heures de travail;</li> " +
                "<li> &nbsp; Ne pas fumer et manger ?? l???int??rieur du point de point </li> " +
                "<li> &nbsp; Ne pas quitter son poste pr??matur??ment lors du recensement </li> " +
                "<li> &nbsp; Alerter son responsable en cas de probl??me sur le terrain </li> " +
                "<li> &nbsp; Enlever son ??quipement lors de la pause d??jeuner ;</li> " +
                "<li> &nbsp; Rendre les ??quipements mis ?? sa disposition </li> " +
                "<li> &nbsp; Coll?? un code ?? barre pour chaque frigo, coca-cola  </li> " +
                "<li> &nbsp; Coll?? un code ?? barre ?? l???int??rieur de chaque pos  </li> " +
                "<li> &nbsp; Ratisser les secteurs qui lui son affecter.   </li> " +
                "<li> &nbsp; Recenser les points de vente suivants </li> " +
                "<li> &nbsp; <b>Alimentations g??n??ral </b>,<b> superettes, restaurants </b>,<b> fastfood, caf??t??rias</b>,<b> bureaux de tabac </b>, <b>vendeurs de th??</b> et <b>p??tisseries</b>    </li> " +


                "</ul>");

        dialog.addPoliceLine("En Cas de rupture de la relation de travail, l???enqu??teur doit restituer l?????quipement en bon ??tat. Tout salari?? ayant commis des d??g??ts au mat??riel de l???entreprise est tenue de rembourser la totalit?? des frais.");
        dialog.addPoliceLine("<br/><b><font color='red'>Article 04 : dur??e de l???accord </font></b>");
        dialog.addPoliceLine("Le pr??sent accord ?? dur??e d??termin??e <b> de dix (10) jours</b>, peut faire l???objet d???un renouvellement d???une m??me p??riode, si l???employeur le juge appropri??\n" +
                "Le salari?? est soumis ?? une p??riode d???essai, durant cette p??riode, le pr??sent accord peut ??tre r??sili??, ?? tout moment, ?? l???initiative de chaque partie, sans indemnit?? ni pr??avis \n");
        dialog.addPoliceLine("<br/><b><font color='red'>Article 05 : jours et horaires de travail </font></b>");
        dialog.addPoliceLine("L???enqu??teur s???engage ?? intervenir dans les secteurs qui lui sont affect??s dans l???horaire fixer, et ?? dur??e de 10 jours.");
        dialog.addPoliceLine("<br/><b><font color='red'>Article 06 : R??mun??ration</font></b>");

        dialog.addPoliceLine("En contrepartie,  l???enqu??teur percevra 500 DA net/Jr, ?? condition que l???enqu??teur recense au moins <b>5 PDV </b>  dans la dur??e l??gal de du travail qui est de <b>(08) huit heurs</font></b>");
        dialog.addPoliceLine("Au-del??s de des 5 premier point de vente recens?? l???enqu??teur percevra :  ");
        dialog.addPoliceLine("<ul>" +
                "<li> &nbsp; <b>2000 DA/jour </b> ?? 25 points de vente dans les secteurs a fort potentiel  </li> " +
                "<li> &nbsp; <b>1500 DA/jour  </b> sur les secteur a faible potentiel avec un objectif de 15 POS    </li> " +
                "<li> &nbsp; Les points de vente recens?? comme <b>refus</b> ou <b>ferm??</b> <b>ne sont pas r??mun??r??</b>  </li> " +

                "</ul>");

        dialog.addPoliceLine("Un syst??me de contr??le de pr??sence est mise en place et la pr??sence est constat??e par tous moyens, <b>l???enqu??teur ne peut ??tre  r??mun??r?? pour une p??riode non travaill??</font></b>");

        dialog.addPoliceLine("<br/><b><font color='red'>Article 07 : Utilisations des ??quipements mis ?? disposition</font></b>");


        dialog.addPoliceLine("Le salari?? disposera d???un ??quipement lors de l???accomplissement de ses t??ches, il doit les utiliser avec soin, responsabilit?? et s???assurer de leur bon entretien, en aucun cas les utiliser pour des fins personnelles");
        dialog.addPoliceLine("Les agissements contraires aux pr??sentes dispositions, exposent leur auteur ?? des <b>mesures et sanctions disciplinaires </b>");


        dialog.addPoliceLine("<br/><b><font color='red'>Article 08 : Force majeure et dispositions g??n??rales</font></b>");
        dialog.addPoliceLine("On entend par cas de force majeure, tout acte ou ??v??nement impr??visible, irr??sistible et ind??pendant de la volont?? des Parties qui rend impossible totalement ou partiellement l???ex??cution, par l???une ou les deux Parties, de ses (leurs) obligations contractuelles.");
        dialog.addPoliceLine("Tout ce qui n???est pas express??ment pr??vu par le pr??sent accord, les deux parties s???engagent ?? r??gler ?? l???amiable les litiges pouvant naitre de l???application du pr??sent accord.");

        dialog.addPoliceLine("<br/><b><font color='red'>Article 09 : Rupture de la Relation de travail</font></b>");
        dialog.addPoliceLine("Le pr??sent accord de travail est r??sili?? dans les cas suivants :");

        dialog.addPoliceLine("<ul>" +
                "<li> &nbsp; A l???initiative de l???employeur en cas de manquement par le salari?? ?? ses obligations et taches professionnelles cit??es ?? <b>l???article 03</b> cit?? ci-dessus  </li> " +
                "<li> &nbsp; Le licenciement pour faute professionnelle grave  </li> " +
                "<li> &nbsp; D??sistement  </li> " +
                "<li> &nbsp; Le pr??sent accord peut ??tre rompu, avant son terme, suite ?? un arr??t pr??matur?? des prestations, ind??pendant de la volont?? de la <b>sarl Store execution</b></li> " +
                "<li> &nbsp; Force majeure </li> " +
                "<li> &nbsp; La nullit?? ou de l???abrogation l??gale du pr??sent accord  </li> " +
                "<li> &nbsp; Toute absence non autoris??e ou non justifi?? dans les <b>quarante-huit (48) heures </b> qui suivent constitue une absence irr??guli??re du salari?? entrainant la mise en ??uvre de la proc??dure dont les modalit??s sont fix??es;" +


                "</ul>");

        dialog.setTitleTextColor(Color.parseColor("#222222"));
        dialog.setAcceptButtonColor(ContextCompat.getColor(this, R.color.colorAccent));

        //  Title


        //  {terms}Terms of Service{/terms} is replaced by a link to your terms
        //  {privacy}Privacy Policy{/privacy} is replaced by a link to your privacy policy
        dialog.setTermsOfServiceSubtitle("En cliquant sur  {accept}, vous accepter toutes les condition et les terms de l'utilisation");
    }

    @OnClick(R.id.btSignUp)
    public void signup() {

        if (validation()) {
            //  Signup signup = new Signup();
            wilaya = spWilaya.getSelectedItem().toString();
            setUpDialog();
            dialog.show();


        } else {
            Toasty.error(this, "Veuillez verifier vos informations", 3000).show();

        }


    }

    private void storeUser() {
        service.signup(etName.getText().toString(),
                etUserName.getText().toString(),
                etPhone.getText().toString(),
                etMail.getText().toString(),
                spWilaya.getSelectedItemPosition(),
                etPasssword.getText().toString(),
                5
        ).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {


                if (response.code() == 200) {
                    Toasty.success(SignupActivity.this, "Votre compte a ??t?? cr??e avec success", 3000).show();

                    SignupActivity.this.finish();
                } else {
                    Toasty.warning(SignupActivity.this, "Veuillez verifier vos informations", 3000).show();

                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                t.printStackTrace();
                Toasty.error(SignupActivity.this, "Erreur", 3000).show();

            }
        });

    }


    private boolean validation() {

        Boolean valid = true;

        if (etUserName.getText().toString().length() < 3) {
            valid = false;
            etUserName.setError("Veuillez entrer un nom d'utilisateur valide");

        }
        if (etName.getText().toString().length() < 3) {
            valid = false;
            etName.setError("Veuillez entrer un nom  valide");

        }

        if (etPasssword.getText().toString().length() < 3) {
            valid = false;
            etPasssword.setError("Veuillez entrer un mot de passe valide");

        }
        if (etPasssword.getText().toString().length() < 3) {
            valid = false;
            etPasssword.setError("Veuillez entrer un mot de passe valide");

        }
        if (TextUtils.isEmpty(etMail.getText().toString()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(etMail.getText().toString()).matches()) {
            valid = false;
            etMail.setError("Veuillez entrer un mot de passe valide");

        }

        if (etPhone.getText().toString().length() == 10 && (etPhone.getText().toString().startsWith("05") || etPhone.getText().toString().startsWith("06") || etPhone.getText().toString().startsWith("07"))) {
            etPhone.setError(null);

        } else {

            valid = false;
            etPhone.setError("Veuillez entrer un num??ro valide");
        }

        if (spWilaya.getSelectedItemPosition() == 0) {
            valid = false;
            TextView errorText = (TextView) spWilaya.getSelectedView();
            errorText.setError("Veuillez choisir une wilaya");

        }

        return valid;

    }
}