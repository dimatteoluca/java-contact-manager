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
    private Vector<Persona> elenco;

    public Rubrica(Vector<Persona> e) {

        elenco = e;

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
        popolaTabella(elenco);

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
        nuovoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apriEditorPersona(null);
            }
        });

        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata = table.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frame, "Per favore, seleziona una riga prima di premere 'Modifica'", "Attenzione", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    /*Persona persona = new Persona(
                            (String) model.getValueAt(rigaSelezionata, 0),
                            (String) model.getValueAt(rigaSelezionata, 1),
                            (String) model.getValueAt(rigaSelezionata, 2),
                            "prova",
                            0
                    );*/
                    /*Persona persona = null;
                    for (Persona p : elenco) {
                        if (p.getNome().equals((String) model.getValueAt(rigaSelezionata, 0)) && p.getCognome().equals(model.getValueAt(rigaSelezionata, 1)) && p.getTelefono().equals(model.getValueAt(rigaSelezionata, 2))) {
                            persona = p;
                            break;
                        }
                    }*/
                    Persona persona = trovaPersona((String) model.getValueAt(rigaSelezionata, 0), (String) model.getValueAt(rigaSelezionata, 1), (String) model.getValueAt(rigaSelezionata, 2));
                    personaSelezionata = persona;
                    apriEditorPersona(persona);
                }
            }
        });

        eliminaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata = table.getSelectedRow();
                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frame, "Per favore, seleziona una riga prima di premere 'Elimina'", "Attenzione", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    int conferma = JOptionPane.showConfirmDialog(frame, "Eliminare la persona "+(String) model.getValueAt(rigaSelezionata, 0)+" "+(String) model.getValueAt(rigaSelezionata, 1)+"?", "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
                    if (conferma == JOptionPane.YES_OPTION) {
                        eliminaPersona((String) model.getValueAt(rigaSelezionata, 0), (String) model.getValueAt(rigaSelezionata, 1), (String) model.getValueAt(rigaSelezionata, 2));
                        model.setRowCount(0);
                        popolaTabella(elenco);

                    }
                        //model.removeRow(rigaSelezionata);
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

        JButton salvaButton = new JButton("Salva");
        salvaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*if (personaSelezionata != null) {
                    int rigaSelezionata = table.getSelectedRow();
                    model.setValueAt(nomeField.getText(), rigaSelezionata, 0);
                    model.setValueAt(cognomeField.getText(), rigaSelezionata, 1);
                    model.setValueAt(telefonoField.getText(), rigaSelezionata, 2);
                    personaSelezionata = null;
                } else {
                    model.addRow(new Object[]{nomeField.getText(), cognomeField.getText(), telefonoField.getText()});
                }*/
                if (personaSelezionata != null) {
                    for (Persona persona : elenco) {
                        if (persona.getNome().equals(personaSelezionata.getNome()) && persona.getCognome().equals(personaSelezionata.getCognome()) && persona.getTelefono().equals(personaSelezionata.getTelefono())) {
                            persona.setNome(nomeField.getText());
                            persona.setCognome(cognomeField.getText());
                            persona.setIndirizzo(indirizzoField.getText());
                            persona.setTelefono(telefonoField.getText());
                            persona.setEta(Integer.parseInt(etaField.getText()));
                            break;
                        }
                    }
                    personaSelezionata = null;
                }
                else {
                    elenco.add(new Persona(nomeField.getText(), cognomeField.getText(), indirizzoField.getText(), telefonoField.getText(), Integer.parseInt(etaField.getText())));
                }
                model.setRowCount(0);
                popolaTabella(elenco);
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

    private void popolaTabella(Vector<Persona> elenco){
        for (Persona persona : elenco) {
            model.addRow(new Object[]{persona.getNome(), persona.getCognome(), persona.getTelefono()});
        }
    }

    private Persona trovaPersona(String nome, String cognome, String telefono) {
        Persona persona = null;
        for (Persona p : elenco) {
            if (p.getNome().equals(nome) && p.getCognome().equals(cognome) && p.getTelefono().equals(telefono)) {
                persona = p;
                break;
            }
        }
        return persona;
    }

    private void eliminaPersona(String nome, String cognome, String telefono) {
        for (int i=0; i<elenco.size(); i++) {
            if (elenco.get(i).getNome().equals(nome) && elenco.get(i).getCognome().equals(cognome) && elenco.get(i).getTelefono().equals(telefono)) {
                elenco.remove(i);
                break;
            }
        }
    }

}
