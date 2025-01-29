package com.example.twofactoraurh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_auth extends AppCompatActivity {
    Button verif_code;
    EditText entree_code;
    TextView tv_verif;
    String username, code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        verif_code = (Button) findViewById(R.id.verif_code);
        entree_code = (EditText) findViewById(R.id.code);
        tv_verif = (TextView) findViewById(R.id.tv_verif);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        code = intent.getStringExtra("code");

        tv_verif.setText(username);
    }
}