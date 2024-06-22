package MySQL;

public class Note {
    private int id;
    private String ueberschrift;
    private String notizText;

    // Konstruktor, Getter und Setter
    public Note(int id, String ueberschrift, String notizText) {
        this.id = id;
        this.ueberschrift = ueberschrift;
        this.notizText = notizText;
    }

    // Getter und Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUeberschrift() {
        return ueberschrift;
    }

    public void setUeberschrift(String ueberschrift) {
        this.ueberschrift = ueberschrift;
    }

    public String getNotizText() {
        return notizText;
    }

    public void setNotizText(String notizText) {
        this.notizText = notizText;
    }
}
