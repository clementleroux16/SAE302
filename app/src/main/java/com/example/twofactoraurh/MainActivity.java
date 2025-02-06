package com.example.twofactoraurh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements OnClickListener{
    TextView code_genere;
    EditText username;
    Button generer;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        generer = findViewById(R.id.generer);

        generer.setOnClickListener(this);
    }

    //fonction qui genere le code de verification pseudo aleatoire
    private String generateCode(String username) {
        long timestamp = System.currentTimeMillis() / 1000 / 30;
        return String.valueOf((username.hashCode() + timestamp) % 1000000);
    }

    public void onClick(View v){
        if(!username.getText().toString().equals("")){
            Toast.makeText(this,"Veuillez entrer un code !", Toast.LENGTH_SHORT).show();
        } else {
            code = generateCode(username.toString());           //on appelle la fonction qui genere le code quand l'utilisateur clique sur le bouton

            //on envoie via un intent les informations "username" et "code" dans la 2nd activite
            Intent intent = new Intent(MainActivity.this, activity_auth.class);
            intent.putExtra("username", username.getText().toString());
            intent.putExtra("code", code);

            startActivity(intent);
        }
    }

}