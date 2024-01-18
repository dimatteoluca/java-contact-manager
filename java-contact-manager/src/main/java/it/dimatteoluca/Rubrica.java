package it.dimatteoluca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class Rubrica {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JFrame editorFrame;
    private Persona personaSelezionata;

    public Rubrica(Vector<Persona> elenco) {
        // Crea un nuovo frame
        frame = new JFrame("Rubrica");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Crea un modello per la tabella
        model = new DefaultTableModel();
        model.addColumn("Nome");
        model.addColumn("Cognome");
        model.addColumn("Telefono");

        // Popola la tabella con l'elenco di persone
        for (Persona persona : elenco) {
            model.addRow(new Object[]{persona.getNome(), persona.getCognome(), persona.getTelefono()});
        }

        // Crea la tabella e imposta il modello
        table = new JTable(model);

        // Crea uno JScrollPane e aggiungi la tabella
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Crea i bottoni
        JButton nuovoButton = new JButton("Nuovo");
        JButton modificaButton = new JButton("Modifica");
        JButton eliminaButton = new JButton("Elimina");

        // Aggiungi i listener ai bottoni
        // TODO: fare in modo che la creazione di un nuovo contatto non sovrascriva la riga selezionata
        nuovoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apriEditorPersona(null);
            }
        });

        /* TODO:
         * - non creare una nuova persona, cercarla nell'elenco
         * - mostrare un messaggio d'errore se non si ha prima selezionato una riga
         */
        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata = table.getSelectedRow();
                if (rigaSelezionata != -1) {
                    Persona persona = new Persona(
                            (String) model.getValueAt(rigaSelezionata, 0),
                            (String) model.getValueAt(rigaSelezionata, 1),
                            (String) model.getValueAt(rigaSelezionata, 2),
                            "prova",
                            0
                    );
                    personaSelezionata = persona;
                    apriEditorPersona(persona);
                }
            }
        });

        /* TODO:
         * - mostrare un messaggio di conferma prima dell'effettiva eliminazione
         * - eliminare la persona anche dall'elenco
         * - mostrare un messaggio d'errore se non si ha prima selezionato una riga
         */
        eliminaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata = table.getSelectedRow();
                if (rigaSelezionata != -1) {
                    model.removeRow(rigaSelezionata);
                }
            }
        });

        // Crea un pannello per i bottoni e aggiungilo al frame
        JPanel panel = new JPanel();
        panel.add(nuovoButton);
        panel.add(modificaButton);
        panel.add(eliminaButton);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);

        // Mostra il frame
        frame.setVisible(true);
    }

    private void apriEditorPersona(Persona persona) {
        editorFrame = new JFrame("Editor Persona");
        editorFrame.setSize(300, 200);

        JPanel panel = new JPanel(new GridLayout(6, 2));

        final JTextField nomeField = new JTextField();
        final JTextField cognomeField = new JTextField();
        final JTextField indirizzoField = new JTextField();
        final JTextField telefonoField = new JTextField();
        final JTextField etaField = new JTextField();

        if (persona != null) {
            nomeField.setText(persona.getNome());
            cognomeField.setText(persona.getCognome());
            indirizzoField.setText(persona.getIndirizzo());
            telefonoField.setText(persona.getTelefono());
            etaField.setText(String.valueOf(persona.getEta()));
        }

        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Cognome:"));
        panel.add(cognomeField);
        panel.add(new JLabel("Indirizzo:"));
        panel.add(indirizzoField);
        panel.add(new JLabel("Telefono:"));
        panel.add(telefonoField);
        panel.add(new JLabel("Età:"));
        panel.add(etaField);

        // TODO: salvare le modifiche anche nell'elenco
        JButton salvaButton = new JButton("Salva");
        salvaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (personaSelezionata != null) {
                    int rigaSelezionata = table.getSelectedRow();
                    model.setValueAt(nomeField.getText(), rigaSelezionata, 0);
                    model.setValueAt(cognomeField.getText(), rigaSelezionata, 1);
                    model.setValueAt(telefonoField.getText(), rigaSelezionata, 2);
                    personaSelezionata = null;
                } else {
                    model.addRow(new Object[]{nomeField.getText(), cognomeField.getText(), telefonoField.getText()});
                }
                editorFrame.dispose();
            }
        });

        JButton annullaButton = new JButton("Annulla");
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorFrame.dispose();
            }
        });

        panel.add(salvaButton);
        panel.add(annullaButton);

        editorFrame.getContentPane().add(panel);
        editorFrame.setVisible(true);
    }

}
