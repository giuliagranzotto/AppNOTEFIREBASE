package com.example.notefirebase;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RiceviNote extends AppCompatActivity {
    ListView myListView;
    List<Note> noteList;
    ListAdapter adapter;
    DatabaseReference noteDbRef;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricevi_note);

        // Imposto un id per ricevere la Lista di Viste
        myListView = findViewById(R.id.myListView);
        noteList = new ArrayList<>();
        // Inizializzo un' Adapter per recuperare i dati dalla Lista
        adapter = new ListAdapter(this, noteList);
        myListView.setAdapter(adapter);
        // Inizializzo i riferimenti a Firebase
        noteDbRef = FirebaseDatabase.getInstance().getReference("Note");
        firebaseAuth = FirebaseAuth.getInstance();

        noteDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noteList.clear();
                // Iteriamo tra le note nel DB e le aggungiamo al noteList
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    Note note = noteSnapshot.getValue(Note.class);
                    noteList.add(note);
                }
                // Notifichiamo all'Adapter che i dati sono cambiati in modo che aggiorni la listView
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });
        // imposto un Listener per i LongClick su i vari Item che si trovano sulla listView
        myListView.setOnItemLongClickListener((parent, view, position, id) -> {
            Note note = noteList.get(position);
            // Se l'utente è autorizzato mostriamo i dialog di aggiornamento con i dati esistenti della nota
            if (isUtenteAutorizzato()) {
                showUpdateDialog(note.getId(), note.getTitolo(), note.getContenuto());

            } else {
                // Se l'utente non è autorizzato mostriamo un Toast con un messaggio
                showToast("Non sei autorizzato a modificare o a eliminare la nota");

            }
            // Restituisci True per indicare l'evento di Click è stato gestito correttamente
            return true;

        });
    }

    private void showUpdateDialog(final String id, String titolo, String contenuto) {
        // Creo un AlertDialog per il Dialog di aggiornamento che in questo è mDialog
        final AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View mDialogView = inflater.inflate(R.layout.update_dialog, null);
        mDialog.setView(mDialogView);
        // Creo i riferimenti alle Viste nel mDialog
        final EditText aggiornaTitolo = mDialogView.findViewById(R.id.aggiornaTitolo);
        final EditText aggiornaContenuto = mDialogView.findViewById(R.id.aggiornaContenuto);
        final Spinner aggiornaTipo = mDialogView.findViewById(R.id.aggiornaTipo);
        Button btnUpdate = mDialogView.findViewById(R.id.btnUpdate);
        Button btnDelete = mDialogView.findViewById(R.id.btnDelete);
        //Imposto il titolo e il contenuto corrente della nota nei campi editText
        aggiornaTitolo.setText(titolo);
        aggiornaContenuto.setText(contenuto);
        mDialog.setTitle("aggiornato " + titolo);
        final AlertDialog alertDialog = mDialog.create();
        alertDialog.show();
        //Verifico se l'utente è autorizzato a eliminare il record
        if (isUtenteAutorizzato()) {
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }
        // Imposto i clickListener per i pulsanti del Dialog
        btnUpdate.setOnClickListener(view -> {
            //qui aggiorneremo i dati nel Db ottenendo i valori dalle Viste nel Dialog
            String nuovoTitolo = aggiornaTitolo.getText().toString();
            String nuovoContenuto = aggiornaContenuto.getText().toString();
            String nuovoTipo = aggiornaTipo.getSelectedItem().toString();
            // Verifico se l'utente è autorizzato ad aggiornare i record
            if (!isUtenteAutorizzato()) {
                showToast("Non sei autorizzato a modificare la nota");
                alertDialog.dismiss();
                return;

            }
            // Chiamo il metodo updateData per aggiornare i dati nel db FireBase

            updateData(id, nuovoTitolo, nuovoContenuto, nuovoTipo);
            // Se il record è aggiornato mostra un messaggio con un Toast
            Toast.makeText(RiceviNote.this, "Record Aggiornato", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });
        btnDelete.setOnClickListener(view -> {
            // Verifico se l'utente è autorizzato ad eliminare il record dal db FireBase
            if (!isUtenteAutorizzato()) {
                showToast("Non sei autorizzato a eliminare la nota");
                alertDialog.dismiss();
                return;
            }
            // Chiamo il metodo deleteRecod per eliminare il recordo tramite id nel db FireBase
            deleteRecord(id);
            // Mostra un messaggio Toast che indica che il record è stato eliminato e chiude il Dialog
            alertDialog.dismiss();
        });
    }

    private boolean isUtenteAutorizzato() {
        // Verifica se l'utente è loggato e non ha l'indirizzo email autorizzato
        if (firebaseAuth.getCurrentUser() != null) {
            String email = firebaseAuth.getCurrentUser().getEmail();
            return email != null && email.equals("gmoi.c87@gmail.com");
        }
        return false;
    }

    private void showToast(String message) {
        // Mostra un messaggio Toast utilizzando la String message che recupera il contenuto (come testo) dei vari show Toast
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    private void deleteRecord(String id) {
        // Crea un riferimento al db per l'id della nota specificata
        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("Note").child(id);

        // usa il metodo removeValue per eliminare il record
        Task<Void> mTask = DbRef.removeValue();
        mTask.addOnSuccessListener(aVoid -> showToast("Cancellato")).addOnFailureListener(e -> showToast("Errore durante la cancellazione"));
    }

    private void updateData(String id, String titolo, String contenuto, String tipo) {
        // Creo riferimento al db per l'id della nota specificata
        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("Note").child(id);
        //creo un nuovo oggetto note con i dati aggiornati
        Note note = new Note(id, titolo, contenuto, tipo);
        // Imposto i dati aggiornati nel db utilizzando setValue
        DbRef.setValue(note);

    }
}
