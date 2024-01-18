package it.dimatteoluca;

import java.util.Vector;

public class App {
    public static void main( String[] args ) {

        // Crea un Vector di Persona
        Vector<Persona> rubrica = new Vector<Persona>();

        // Crea alcune persone
        Persona persona1 = new Persona("Steve", "Jobs", "Via Cupertino, 13", "0612344", 56);
        Persona persona2 = new Persona("Bill", "Gates", "Via Redmond, 10", "06688989", 60);
        Persona persona3 = new Persona("Babbo", "Natale", "Via del Polo Nord", "00000111", 99);

        // Aggiungi le persone al Vector
        rubrica.add(persona1);
        rubrica.add(persona2);
        rubrica.add(persona3);

        // Modifica gli attributi di una specifica persona
        for (Persona persona : rubrica) {
            if (persona.getNome().equals("Bill") && persona.getCognome().equals("Gates") && persona.getIndirizzo().equals("Via Redmond, 10") && persona.getTelefono().equals("06688989") && persona.getEta()==60) {
                persona.setEta(61);
                break;
            }
        }

        // Elimina una specifica persona
        for (int i=0; i<rubrica.size(); i++) {
            if (rubrica.get(i).getNome().equals(persona3.getNome()) && rubrica.get(i).getCognome().equals(persona3.getCognome()) && rubrica.get(i).getIndirizzo().equals(persona3.getIndirizzo()) && rubrica.get(i).getTelefono().equals(persona3.getTelefono()) && rubrica.get(i).getEta()==persona3.getEta()) {
                rubrica.remove(i);
                break;
            }
        }

        // Stampa le informazioni delle persone nel Vector
        for (Persona persona : rubrica) {
            System.out.println("Nome: " + persona.getNome());
            System.out.println("Cognome: " + persona.getCognome());
            System.out.println("Indirizzo: " + persona.getIndirizzo());
            System.out.println("Telefono: " + persona.getTelefono());
            System.out.println("Età: " + persona.getEta());
            System.out.println("-------------------");
        }

    }

}
