package it.dimatteoluca;

public class Persona {
    // ATTRIBUTI
    private String nome;
    private String cognome;
    private String indirizzo;
    private String telefono;
    private int eta;

    // COSTRUTTORE
    public Persona(String n, String c, String i, String t, int e) {
        nome = n;
        cognome = c;
        indirizzo = i;
        telefono = t;
        eta = e;
    }

    // FUNZIONI GET E SET
    public String getNome() {
        return nome;
    }

    public void setNome(String n) {
        nome = n;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String c) {
        cognome = c;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String i) {
        indirizzo = i;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String t) {
        telefono = t;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int e) {
        eta = e;
    }
}
