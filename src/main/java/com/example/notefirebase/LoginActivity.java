package com.example.notefirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText Email, Password;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Inizializza FireBaseAuth

        firebaseAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (view.getId() == R.id.btnLogin){
                    // Ottieni email e password inseriti dall'utente
                    String email = Email.getText().toString().trim();
                    String password = Password.getText().toString().trim();
                    //Verifica che email e password non sono vuote
                    if (email.isEmpty() ||password.isEmpty()){
                        Toast.makeText(LoginActivity.this,"Inserisci Email e Password", Toast.LENGTH_SHORT).show();
                    }else{
                        // effettua l'autenticazione con email e password
                        loginUserWithEmailAndPassword(email, password);

                    }
                }
            }
        });
    }
    private void loginUserWithEmailAndPassword(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    // Se l'autenticazione è avvenuta con successo reindirizza l'utente alla MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    //Autenticazione ha fallito
                    Toast.makeText(LoginActivity.this, "Errore di autenticazione", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}