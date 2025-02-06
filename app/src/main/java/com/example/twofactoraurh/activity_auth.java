package com.example.twofactoraurh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.Manifest;

import android.view.View.OnClickListener;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class activity_auth extends AppCompatActivity implements OnClickListener {
    Button verif_code;
    EditText entree_code;
    TextView tv_verif, affiche_code;
    String username, code;
    LocationManager locationManager;
    double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        verif_code = findViewById(R.id.verif_code);
        entree_code = findViewById(R.id.code);
        tv_verif = findViewById(R.id.tv_verif);
        affiche_code = findViewById(R.id.affiche_code);
        verif_code.setOnClickListener(this);

        //on recupere les informations envoyees depuis la premiere activite
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        code = intent.getStringExtra("code");
        affiche_code.setText(getString(R.string.affiche_code,code));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    public void onClick(View v) {
        String code_entree = entree_code.getText().toString();
        //permet de verifier les autorisations de l'application
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // renvoie la position de l'utilisateur
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            latitude = location.getLatitude();         //permet de récupérer la latitude actuelle de l'utilisateur
            longitude = location.getLongitude();       //permet de récupérer la longitude actuelle de l'utilisateur
        }

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        //UserPrefs correspond au nom de fichier dans lesquelles les données sont stockées et MODE_PRIVATE veut dire que seulement notre appli y aura acces

        String savedUsername = sharedPreferences.getString("username_" + username, null);
        //avec cette ligne on accede a la valeur stockées derriere la cle "username_" + variable username, et si il n'y a rien la valeur par defaut est null

        if (savedUsername != null && savedUsername.equals(username)) {

            //cette partie permet de comparer les coordonnees gps si l'utilisateur est deja connu par l'appli
            float savedLatitude = sharedPreferences.getFloat("latitude_" + username, 0);      //on recupere la latitude sauvegardee dans les shared preferences
            float savedLongitude = sharedPreferences.getFloat("longitude_" + username, 0);    //pareil pour la longitude

            //on appelle la methode qui permet de calculer la distance entre 2 points en entrant les 4 coordonnees gps en parametre d'entree
            float distance = calculeDistance(savedLatitude, savedLongitude, latitude, longitude);

            if ((distance < 500) && (code.compareTo(code_entree) == 0)) { // si la distance est plus petite que 500 metres et que le code est le bon
                tv_verif.setText(getString(R.string.ok_connection));
            } else {
                tv_verif.setText(getString(R.string.code_gps_wrong));
            }
        } else {
            //si l'utilisateur n'est pas connu par l'application, alors on l'enregistre via un shared preferences
            if (code.compareTo(code_entree) == 0) { // Vérification que le code entrée est correct
                SharedPreferences.Editor editor = sharedPreferences.edit();    //permet d'acceder a l'editeur de sharedPreferences
                editor.putString("username_" + username, username);
                editor.putFloat("latitude_" + username, (float) latitude);
                editor.putFloat("longitude_" + username, (float) longitude);
                editor.apply();         //on applique les modifications
                tv_verif.setText(getString(R.string.user_registered,username));
            }
            else {
                tv_verif.setText(getString(R.string.wrong_code));
                entree_code.setText("");
            }
        }
    }

    private float calculeDistance(double lat1, double lon1, double lat2, double lon2) {
        // on utilise un tableau ici car la fonction Location.distanceBetween ne return rien (void) et donc on stocke le resultat du calcul dans le tableau
        float[] resultats = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, resultats);
        return resultats[0]; //resultat du calcul de distance entre les 2 positions
    }
}