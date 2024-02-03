package com.example.notefirebase;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Note> {
    // Definisco il ListAdapter
    private final Activity mContext;
    private List<Note> noteList;

    //Costruttore della ListAdapter
    public ListAdapter(Activity mContext, List<Note> noteList) {
        //Richiama il costruttore della classe base(ArrayAdapter)
        //Passo il layout dell'elemento della lista(R.layout.list_item) e la lista di dati(noteList)
        super(mContext, R.layout.list_item, noteList);
        this.mContext = mContext;
        this.noteList = noteList;

    }

    //Mantengo i riferimenti agli elementi della lista
    private static class ViewHolder {
        TextView mostraTitolo;
        TextView mostraContenuto;
        TextView mostraTipo;

    }

    //Metodo per creare la Vista di ogni elemento della Lista
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        // Se convertView è null crea una nuova Vista usando il layout personalizzato(R.layout.list_item)
        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            // Creo un nuovo ViewHolder e collego gli elementi della Vista ai suoi membri
            viewHolder = new ViewHolder();
            viewHolder.mostraTitolo = convertView.findViewById(R.id.mostraTitolo);
            viewHolder.mostraContenuto = convertView.findViewById(R.id.mostraContenuto);
            viewHolder.mostraTipo = convertView.findViewById(R.id.mostraTipo);
            // Imposto il ViewHolder come tag della Vista convertita
            convertView.setTag(viewHolder);
        } else {
            // Se convertView non è null ottengo il viewHolder dalla vista convertita
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Ottengo l'oggetto note dalla lista in base alla posizione
        Note note = noteList.get(position);
        // Imposto i valori dell'oggetto note nei textView corrispondenti nel layout(R.layout.list_item)
        viewHolder.mostraTitolo.setText(note.getTitolo());
        viewHolder.mostraContenuto.setText(note.getContenuto());
        viewHolder.mostraTipo.setText(note.getTipo());
        // Restituisco la Vista convertita con i dati impostati
        return convertView;


    }
        // Ottengo il numero di elementi nella Lista
    @Override
    public int getCount() {
        return noteList.size();
    }

}
