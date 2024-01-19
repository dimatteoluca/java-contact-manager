package it.dimatteoluca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class Rubrica {

    // ATTRIBUTI
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JFrame editorFrame;
    private Persona personaSelezionata;
    private Vector<Persona> elenco;
    private Dimension screenSize;

    // COSTRUTTORE
    public Rubrica() {

        // Crea un nuovo frame
        frame = new JFrame("Rubrica");
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width / 3;
        int y = screenSize.height / 3;
        frame.setLocation(x, y);
        frame.setSize(x, y);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crea un modello per la tabella
        model = new DefaultTableModel();
        model.addColumn("NOME");
        model.addColumn("COGNOME");
        model.addColumn("TELEFONO");

        // Carica i dati dal file di testo
        elenco = new Vector<Persona>();
        File file = new File("informazioni.txt");
        popolaElencoETabella(file);

        // Crea la tabella e imposta il modello
        table = new JTable(model);

        // Crea uno JScrollPane e aggiungi la tabella
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Crea una JToolBar
        JToolBar toolBar = new JToolBar();

        // Crea i bottoni
        ImageIcon addIcon = new ImageIcon(getClass().getResource("/images/add.png"));
        JButton nuovoButton = new JButton(addIcon);
        nuovoButton.setToolTipText("Aggiungi nuovo contatto");
        ImageIcon editIcon = new ImageIcon(getClass().getResource("/images/edit.png"));
        JButton modificaButton = new JButton(editIcon);
        modificaButton.setToolTipText("Modifica contatto");
        ImageIcon trashIcon = new ImageIcon(getClass().getResource("/images/trash.png"));
        JButton eliminaButton = new JButton(trashIcon);
        eliminaButton.setToolTipText("Elimina contatto");

        // Aggiungi un componente invisibile che "spinge" i bottoni a destra
        toolBar.add(Box.createHorizontalGlue());
        // Aggiungi i bottoni alla JToolBar
        toolBar.add(nuovoButton);
        toolBar.add(modificaButton);
        toolBar.add(eliminaButton);

        // Aggiungi i listener ai bottoni
        nuovoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                personaSelezionata = null;
                apriEditorPersona(null);
            }
        });

        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata = table.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frame, "Per favore, seleziona un contatto prima di premere 'Modifica'.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                }
                else {
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
                    JOptionPane.showMessageDialog(frame, "Per favore, seleziona un contatto prima di premere 'Elimina'.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    int conferma = JOptionPane.showConfirmDialog(frame, "Eliminare il contatto '"+(String) model.getValueAt(rigaSelezionata, 0)+" "+(String) model.getValueAt(rigaSelezionata, 1)+"'?", "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
                    if (conferma == JOptionPane.YES_OPTION) {
                        eliminaPersona((String) model.getValueAt(rigaSelezionata, 0), (String) model.getValueAt(rigaSelezionata, 1), (String) model.getValueAt(rigaSelezionata, 2));
                        aggiornaFile();
                        model.setRowCount(0);
                        popolaTabella();

                    }
                }
            }
        });

        // Aggiungi la JToolBar al frame
        frame.getContentPane().add(toolBar, BorderLayout.SOUTH);
        // Mostra il frame
        frame.setVisible(true);
    
    }

    // METODI
    private void apriEditorPersona(Persona persona) {
        
        int x = screenSize.width / 3;
        int y = screenSize.height / 3;
        editorFrame = new JFrame("Editor Persona");
        editorFrame.setLocation(x * 5 / 4, y * 5 / 4);
        editorFrame.setSize(x/2, y*2/3);

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

        ImageIcon diskIcon = new ImageIcon(getClass().getResource("/images/disk.png"));
        JButton salvaButton = new JButton(diskIcon);
        salvaButton.setToolTipText("Salva contatto");
        salvaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (nomeField.getText().equals("") || cognomeField.getText().equals("") || indirizzoField.getText().equals("") || telefonoField.getText().equals("") || etaField.getText().equals("")) {
                        throw new EccezionePersonalizzata("Non è possibile lasciare campi vuoti.");
                    }
                    else if (nomeField.getText().contains(";") || cognomeField.getText().contains(";") || indirizzoField.getText().contains(";") || telefonoField.getText().contains(";") || etaField.getText().contains(";")) {
                        throw new EccezionePersonalizzata("Non è possibile utilizzare il carattere ';' nei campi dati.");
                    }
                    else if (!telefonoField.getText().matches("\\+?\\d+")) {
                        throw new EccezionePersonalizzata("Il numero di telefono deve essere una sequenza\n" + //
                                "di cifre eventualmente preceduta dal carattere '+'.");
                    }
                    else if (Integer.parseInt(etaField.getText()) < 0) {
                        throw new EccezionePersonalizzata("L'età deve essere un numero intero positivo.");
                    }
                    else if (personaSelezionata != null) {
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
                    ordinaElenco();
                    aggiornaFile();
                    model.setRowCount(0);
                    popolaTabella();
                    editorFrame.dispose();
                } catch (EccezionePersonalizzata ex) {
                    JOptionPane.showMessageDialog(editorFrame, ex.getMessage(), "Attenzione", JOptionPane.WARNING_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(editorFrame, "L'età deve essere un numero.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        ImageIcon undoIcon = new ImageIcon(getClass().getResource("/images/undo.png"));
        JButton annullaButton = new JButton(undoIcon);
        annullaButton.setToolTipText("Annulla operazione");
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorFrame.dispose();
            }
        });

        // Crea una JToolBar
        JToolBar toolBar = new JToolBar();

        // Aggiungi un componente invisibile che "spinge" i bottoni a destra
        toolBar.add(Box.createHorizontalGlue());
        // Aggiungi i bottoni alla JToolBar
        toolBar.add(salvaButton);
        toolBar.add(annullaButton);

        editorFrame.getContentPane().add(panel);
        // Aggiungi la JToolBar al frame
        editorFrame.getContentPane().add(toolBar, BorderLayout.SOUTH);
        editorFrame.setVisible(true);
    
    }

    private void popolaElencoETabella(File file) {
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String riga;
                while ((riga = reader.readLine()) != null) {
                    String[] dati = riga.split(";");
                    Persona persona = new Persona(dati[0], dati[1], dati[2], dati[3], Integer.parseInt(dati[4]));
                    elenco.add(persona);
                }
                ordinaElenco();
                popolaTabella();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void popolaTabella(){
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

    private void aggiornaFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("informazioni.txt", false))) {
            for (Persona persona : elenco) {
                String nome = persona.getNome();
                String cognome = persona.getCognome();
                String indirizzo = persona.getIndirizzo();
                String telefono = persona.getTelefono();
                int eta = persona.getEta();
                writer.println(nome + ";" + cognome + ";" + indirizzo + ";" + telefono + ";" + eta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ordina l'elenco in ordine alfabetico in base al nome
    private void ordinaElenco() {
        Collections.sort(elenco, new Comparator<Persona>() {
            public int compare(Persona p1, Persona p2) {
                return p1.getNome().compareTo(p2.getNome());
            }
        });
    }

}
