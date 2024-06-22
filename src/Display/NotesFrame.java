package Display;

import MySQL.Database;
import MySQL.Note;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import java.util.Stack;

public class NotesFrame extends JFrame {

    private final int screenWidth = 500;
    private final int screenHeight = 800;

    private JButton createNote;
    private JButton findNote;
    private JButton saveNote;
    private JButton clearNoteText;
    private JButton clearTitle;
    private JButton search;
    private JButton deleteNote;

    // TextArea for note text
    JTextArea noteText;

    // TextFields for input
    JTextField titleField;
    JTextField findNoteFromTitle;

    // Class Objects
    Database database;
    JScrollPane noteScroller;
    JPanel notesPanel;
    Stack<JTextArea> stack;

    public NotesFrame() {
        super("Notiz-App");
        createNote = new JButton("Neue Notiz");
        findNote = new JButton("Notiz finden");
        saveNote = new JButton("Notiz speichern");
        clearNoteText = new JButton("Textfeld leeren");
        clearTitle = new JButton("Überschrift leeren");
        deleteNote = new JButton("Notiz Löschen");
        search = new JButton("Finde");
        noteText = new JTextArea();
        titleField = new JTextField("Überschrift");
        findNoteFromTitle = new JTextField("Notizüberschrift");

        editButtons();
        initTextArea();
        initTextFields();

        database = new Database();

        notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));

        noteScroller = new JScrollPane(notesPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        stack = new Stack<>();

        initNoteScroller();
    }

    public void initGui() {
        this.setSize(screenWidth, screenHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(Color.LIGHT_GRAY);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void editButtons() {
        JButton[] taskButtons = {createNote, findNote, saveNote, clearNoteText, clearTitle, search , deleteNote};
        for (JButton b : taskButtons) {
            b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
            b.setFocusable(false);
            b.setBackground(Color.WHITE);
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonListener(e);
                }
            });
            addingComponents(b);
        }

        createNote.setEnabled(false);
        findNote.setEnabled(false);
        boundsForButtons();
    }

    private void boundsForButtons() {
        createNote.setBounds(0, 0, 100, 30);
        findNote.setBounds(0, 40, 100, 30);
        saveNote.setBounds(0, 250, 150, 30);
        clearTitle.setBounds(350, 0, 120, 30);
        search.setBounds(350, 40, 120, 30);
        clearNoteText.setBounds(330, 250, 150, 30);
        deleteNote.setBounds(190 , 250 , 100 , 30);
    }

    private void initTextArea() {
        noteText.setBounds(0, 100, 480, 150);
        noteText.setBorder(BorderFactory.createTitledBorder("Notiz Text"));
        noteText.setFont(new Font("Arial", Font.BOLD, 15));
        addingComponents(noteText);
    }

    private void initTextFields() {
        JTextField[] fields = {titleField, findNoteFromTitle};
        for (JTextField f : fields) {
            f.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    f.setText("");
                }

                @Override
                public void focusLost(FocusEvent e) {
                    // Nothing
                }
            });
        }
        titleField.setBounds(120, 0, 200, 30);
        titleField.setBackground(Color.WHITE);
        titleField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        addingComponents(titleField);

        findNoteFromTitle.setBounds(120, 40, 200, 30);
        findNoteFromTitle.setBackground(Color.WHITE);
        findNoteFromTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        addingComponents(findNoteFromTitle);
    }

    private void buttonListener(ActionEvent e) {
        if (e.getSource() == clearNoteText) {
            noteText.setText("");
        } else if (e.getSource() == clearTitle) {
            titleField.setText("");
        } else if (e.getSource() == saveNote) {
            checkFieldsText();
        } else if (e.getSource() == search) {
            searchNote(findNoteFromTitle.getText());
        } else if(e.getSource() == deleteNote){
            deleteSelectedNote();
        }
    }

    private void checkFieldsText() {
        if (titleField.getText().equals("") || noteText.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Felder sind leer", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!titleField.getText().equals("") && !noteText.getText().equals("")) {
            database.insertData(titleField.getText(), noteText.getText());

            noteText.setText("");
            titleField.setText("");

            updateNoteScroller();
        }
    }

    private void searchNote(String noteTitle) {
        if (!findNoteFromTitle.getText().equals("")) {
            Note note = database.getNoteByTitle(noteTitle);
            if (note != null) {
                String findNoteText = "ID: " + note.getId() + "\nÜberschrift: " + note.getUeberschrift() + "\nNotiz: " + note.getNotizText();
                JOptionPane.showMessageDialog(null, findNoteText, "Notiz", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Notiz nicht gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Fehler", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void initNoteScroller() {
        noteScroller.setBounds(0, 300, 490, 450);
        addingComponents(noteScroller);

        updateNoteScroller();
    }

    private void updateNoteScroller() {
        notesPanel.removeAll();

        List<Note> allNotes = database.getAllNotes();
        for (Note note : allNotes) {
            JTextArea textArea = new JTextArea(note.getNotizText());
            textArea.setPreferredSize(new Dimension(300, 70));
            textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true));
            textArea.setText("ID: " + note.getId() + "\nÜberschrift: " + note.getUeberschrift() + "\nNotiz: " + note.getNotizText());

            notesPanel.add(textArea);
        }

        notesPanel.revalidate();
        notesPanel.repaint();
    }

    private void deleteSelectedNote() {
        String noteTitle = findNoteFromTitle.getText();
        if (!noteTitle.equals("")) {
            int confirm = JOptionPane.showConfirmDialog(null, "Möchten Sie diese Notiz wirklich löschen?", "Bestätigung", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Note noteToDelete = database.getNoteByTitle(noteTitle); // Annahme: Eine Methode, die eine Notiz anhand des Titels findet
                if (noteToDelete != null) {
                    database.deleteNoteById(noteToDelete.getId());
                    updateNoteScroller(); // Aktualisiere die Anzeige
                    titleField.setText("");
                    noteText.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Notiz nicht gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Bitte geben Sie die Überschrift der zu löschenden Notiz ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void addingComponents(Component component) {
        if (component != null) {
            this.add(component);
        }
    }

    public static void main(String[] args) {
        NotesFrame notesFrame = new NotesFrame();
        notesFrame.initGui();

    }
}
