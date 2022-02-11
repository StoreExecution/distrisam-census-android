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

        dialog.setTitle("Accord et Conditions d’utilisation  ");

        dialog.addPoliceLine("Entre les deux co-contractants:\n<br/>" +
                "La société <b> Sarl Store Exécution </b>, ci-après dénommé l’employeur, élisant domicile au<b> Bois des cars 02 villa n°03 Delly Ibrahim</b>\n<br/>" +
                "Et :\n <br/>" +
                "<b>Le salarié</b>, celui qu’il l’appose d’une <b>signature électronique </b> et manifeste son consentement sur les dispositions contenues aux articles cités et les obligations qui découle, sont applicables à cet accord sous forme électronique\n<br/>");
        dialog.addPoliceLine("A été convenu ce qui suit :");
        dialog.addPoliceLine("<br/><b> <font color='red'>Article 01 : Objet de l’accord </font></b>");
        dialog.addPoliceLine("Le présent accord a pour objet de fixer les règles et conditions applicables à la relation de travail liant <b>le salarié </b> à la société <b>Sarl Store Exécution</b>, conformément aux dispositions légales et réglementaires en vigueur.");
        dialog.addPoliceLine("<br/><b><font color='red'>Article 02 : fonction de l’employé</font></b>");
        dialog.addPoliceLine("Le salarié est recruté en qualité <b>d’Agent d’enquête</b> sur la wilaya de <b>" + wilaya + "</b> au sein de la société pour effectuer les tâches suivantes <b>opération census Coca-Cola </b>;");
        //  Customizing (Optional)

        dialog.addPoliceLine("<br/><b><font color='red'>Article 03 : Taches & obligation à accomplir  :  </font></b>");
        dialog.addPoliceLine("<b>Le salarié </b> est tenu de produire un rendement conforme aux objectifs qui lui sont assignés : ");
        dialog.addPoliceLine("<ul>" +
                "<li> &nbsp; L’enquêteur doit expliquer le but de sa visite au point de vent ; </li> " +
                "<li> &nbsp; Ne doit pas manquer de respecter au point de vente  </li> " +
                "<li> &nbsp; Remonter les informations du point de vente, via l’application </li> " +
                "<li> &nbsp; Visiter 30 Channel par jour  </li> " +
                "<li> &nbsp; Utiliser la Map mobil lors de ses déplacements, et pour la remontée des informations </li> " +
                "<li> &nbsp; Mettre son équipement lors de l’opération ;</li> " +
                "<li> &nbsp; Etre réactive et assidus dans son travail </li> " +
                "<li> &nbsp; Ne pas manger et effectué des appels téléphoniques personnels, durant les heures de travail </li> " +
                "<li> &nbsp; Ne pas mettre un jeans troué, survêtement, short, et claquette, durant les heures de travail;</li> " +
                "<li> &nbsp; Ne pas fumer et manger à l’intérieur du point de point </li> " +
                "<li> &nbsp; Ne pas quitter son poste prématurément lors du recensement </li> " +
                "<li> &nbsp; Alerter son responsable en cas de problème sur le terrain </li> " +
                "<li> &nbsp; Enlever son équipement lors de la pause déjeuner ;</li> " +
                "<li> &nbsp; Rendre les équipements mis à sa disposition </li> " +
                "<li> &nbsp; Collé un code à barre pour chaque frigo, coca-cola  </li> " +
                "<li> &nbsp; Collé un code à barre à l’intérieur de chaque pos  </li> " +
                "<li> &nbsp; Ratisser les secteurs qui lui son affecter.   </li> " +
                "<li> &nbsp; Recenser les points de vente suivants </li> " +
                "<li> &nbsp; <b>Alimentations général </b>,<b> superettes, restaurants </b>,<b> fastfood, cafétérias</b>,<b> bureaux de tabac </b>, <b>vendeurs de thé</b> et <b>pâtisseries</b>    </li> " +


                "</ul>");

        dialog.addPoliceLine("En Cas de rupture de la relation de travail, l’enquêteur doit restituer l’équipement en bon état. Tout salarié ayant commis des dégâts au matériel de l’entreprise est tenue de rembourser la totalité des frais.");
        dialog.addPoliceLine("<br/><b><font color='red'>Article 04 : durée de l’accord </font></b>");
        dialog.addPoliceLine("Le présent accord à durée déterminée <b> de dix (10) jours</b>, peut faire l’objet d’un renouvellement d’une même période, si l’employeur le juge approprié\n" +
                "Le salarié est soumis à une période d’essai, durant cette période, le présent accord peut être résilié, à tout moment, à l’initiative de chaque partie, sans indemnité ni préavis \n");
        dialog.addPoliceLine("<br/><b><font color='red'>Article 05 : jours et horaires de travail </font></b>");
        dialog.addPoliceLine("L’enquêteur s’engage à intervenir dans les secteurs qui lui sont affectés dans l’horaire fixer, et à durée de 10 jours.");
        dialog.addPoliceLine("<br/><b><font color='red'>Article 06 : Rémunération</font></b>");

        dialog.addPoliceLine("En contrepartie,  l’enquêteur percevra 500 DA net/Jr, à condition que l’enquêteur recense au moins <b>5 PDV </b>  dans la durée légal de du travail qui est de <b>(08) huit heurs</font></b>");
        dialog.addPoliceLine("Au-delàs de des 5 premier point de vente recensé l’enquêteur percevra :  ");
        dialog.addPoliceLine("<ul>" +
                "<li> &nbsp; <b>2000 DA/jour </b> à 25 points de vente dans les secteurs a fort potentiel  </li> " +
                "<li> &nbsp; <b>1500 DA/jour  </b> sur les secteur a faible potentiel avec un objectif de 15 POS    </li> " +
                "<li> &nbsp; Les points de vente recensé comme <b>refus</b> ou <b>fermé</b> <b>ne sont pas rémunéré</b>  </li> " +

                "</ul>");

        dialog.addPoliceLine("Un système de contrôle de présence est mise en place et la présence est constatée par tous moyens, <b>l’enquêteur ne peut être  rémunéré pour une période non travaillé</font></b>");

        dialog.addPoliceLine("<br/><b><font color='red'>Article 07 : Utilisations des équipements mis à disposition</font></b>");


        dialog.addPoliceLine("Le salarié disposera d’un équipement lors de l’accomplissement de ses tâches, il doit les utiliser avec soin, responsabilité et s’assurer de leur bon entretien, en aucun cas les utiliser pour des fins personnelles");
        dialog.addPoliceLine("Les agissements contraires aux présentes dispositions, exposent leur auteur à des <b>mesures et sanctions disciplinaires </b>");


        dialog.addPoliceLine("<br/><b><font color='red'>Article 08 : Force majeure et dispositions générales</font></b>");
        dialog.addPoliceLine("On entend par cas de force majeure, tout acte ou événement imprévisible, irrésistible et indépendant de la volonté des Parties qui rend impossible totalement ou partiellement l’exécution, par l’une ou les deux Parties, de ses (leurs) obligations contractuelles.");
        dialog.addPoliceLine("Tout ce qui n’est pas expressément prévu par le présent accord, les deux parties s’engagent à régler à l’amiable les litiges pouvant naitre de l’application du présent accord.");

        dialog.addPoliceLine("<br/><b><font color='red'>Article 09 : Rupture de la Relation de travail</font></b>");
        dialog.addPoliceLine("Le présent accord de travail est résilié dans les cas suivants :");

        dialog.addPoliceLine("<ul>" +
                "<li> &nbsp; A l’initiative de l’employeur en cas de manquement par le salarié à ses obligations et taches professionnelles citées à <b>l’article 03</b> cité ci-dessus  </li> " +
                "<li> &nbsp; Le licenciement pour faute professionnelle grave  </li> " +
                "<li> &nbsp; Désistement  </li> " +
                "<li> &nbsp; Le présent accord peut être rompu, avant son terme, suite à un arrêt prématuré des prestations, indépendant de la volonté de la <b>sarl Store execution</b></li> " +
                "<li> &nbsp; Force majeure </li> " +
                "<li> &nbsp; La nullité ou de l’abrogation légale du présent accord  </li> " +
                "<li> &nbsp; Toute absence non autorisée ou non justifié dans les <b>quarante-huit (48) heures </b> qui suivent constitue une absence irrégulière du salarié entrainant la mise en œuvre de la procédure dont les modalités sont fixées;" +


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
                    Toasty.success(SignupActivity.this, "Votre compte a été crée avec success", 3000).show();

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
            etPhone.setError("Veuillez entrer un numéro valide");
        }

        if (spWilaya.getSelectedItemPosition() == 0) {
            valid = false;
            TextView errorText = (TextView) spWilaya.getSelectedView();
            errorText.setError("Veuillez choisir une wilaya");

        }

        return valid;

    }
}