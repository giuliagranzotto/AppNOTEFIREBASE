package com.example.notefirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button InserisciNotebtn;
    Button RiceviNotebtn;
    Button btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InserisciNotebtn = findViewById(R.id.InserisciNotebtn);
        RiceviNotebtn = findViewById(R.id.RiceviNotebtn);
        InserisciNotebtn.setOnClickListener(this);
        RiceviNotebtn.setOnClickListener(this);

        btnLogOut = findViewById(R.id.btnLogout);
        btnLogOut.setOnClickListener(this);
        // inizializza firebase Auth
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // Verifica che l'utente è già autenticato
        if (firebaseAuth.getCurrentUser() == null) {
            //L'utente non è autenticato, reindirizzalo alla LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        InserisciNotebtn.setOnClickListener(this);
        RiceviNotebtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.InserisciNotebtn) {
            startActivity(new Intent(MainActivity.this, InserisciNote.class));
        } else if (view.getId() == R.id.RiceviNotebtn) {
            startActivity(new Intent(MainActivity.this, RiceviNote.class));


        } else if (view.getId() == R.id.btnLogout) {
            //Esegui il logout dell'utente
            FirebaseAuth.getInstance().signOut();
            // Reindirizza al LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();


        }

    }
}