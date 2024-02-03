package com.example.notefirebase;

public class Note {
    // Attributi della classe Note
    String id, titolo, contenuto, tipo;
        // Costruttore vuoto richiesto da Firebase
    public Note() {
        // Il costruttore vuoto è richiesto da Firebase per deserializzare gli oggetti quando vengono recuperati dal Db FireBase

    }
        // Costruttore della classe note
    public Note(String id, String titolo, String contenuto, String tipo) {
        this.id = id;
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.tipo = tipo;

    }
    // Imposto il getter per accedere agli attributi della classe Note
    public String getId(){return id;}
    public String getTitolo(){return titolo;}
    public String getContenuto(){return contenuto;}
    public String getTipo(){return tipo;}


}
