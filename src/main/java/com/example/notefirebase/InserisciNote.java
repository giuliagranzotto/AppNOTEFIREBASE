package com.example.notefirebase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InserisciNote extends AppCompatActivity {
    // Dichiarazione delle variabili per gli elementi dell'interfaccia utente
    EditText nuovoTitolo;
    EditText nuovoContenuto;
    Spinner scegliTipo;
    Button btnInserisciNote;
    //Riferimento al nodo "Note" nel DataBase FireBase
    DatabaseReference noteDbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserisci_note);
        // Inizializzo gli elementi dell'interfaccia utente
        nuovoTitolo = findViewById(R.id.nuovoTitolo);
        nuovoContenuto = findViewById(R.id.nuovoContenuto);
        scegliTipo = findViewById(R.id.scegliTipo);
        btnInserisciNote = findViewById(R.id.btnInserisciNote);
        // Ottengo il riferimento al nodo "Note" nel DataBase FireBase
        noteDbRef = FirebaseDatabase.getInstance().getReference("Note");
        // Imposto un Listener per il click del pulsante btInserisciNote


        btnInserisciNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Controlla se l'utente è autorizzato
                if (isUtenteAutorizzato()) {
                    // Se autorizzato, inserisce i dati della nota
                    inseriscDatiNote();

                } else {
                    //Altrimenti mostra un messaggio, non sei autorizzato
                    Toast.makeText(InserisciNote.this, "Non sei autorizzato", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
        // Metodo per verificare se l'utente è autorizzato ad inserire nuove note
    private boolean isUtenteAutorizzato() {
        //Verifica se l'utente è loggato
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            // Verifica se l'utente ha la mail autorizzata come admi
            String email = firebaseAuth.getCurrentUser().getEmail();
            return email != null && email.equals("gmoi.c87@gmail.com");

        }
        return false;

    }

    private void inseriscDatiNote() {
        // ottieni i dati inseriti dall'utente
        String titolo = nuovoTitolo.getText().toString();
        String contenuto = nuovoContenuto.getText().toString();
        String tipo = scegliTipo.getSelectedItem().toString();
        //Genera un nuovo id univoco per la nota
        String id = noteDbRef.push().getKey();
        //Crea un oggetto di tipo note con i dati inseriti
        Note dati = new Note(id, titolo, contenuto, tipo);
        //INserisci i dati nel Database FireBase usando il nuovo id
        assert id != null;
        noteDbRef.child(id).setValue(dati);
        //Mostra un messaggio di successo
        Toast.makeText(InserisciNote.this, "Dati inseriti con successo", Toast.LENGTH_SHORT).show();

    }


}
