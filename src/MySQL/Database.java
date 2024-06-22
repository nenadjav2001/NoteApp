package MySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

  final static String URL = "jdbc:mysql://localhost:3306/notes";
  final static String USERNAME = "root";
  final static String PASSWORD = "root";



    public void insertData(String ueberschrift, String notizText) {
        String sql = "INSERT INTO notes (überschrift, notizText) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, ueberschrift);
            preparedStatement.setString(2, notizText);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                System.out.println("Eingefügte ID: " + generatedId);
            } else {
                throw new SQLException("Keine automatisch generierte ID erhalten.");
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Fehler beim Einfügen der Daten: " + e.getMessage(), e);
        }
    }


  public String getDataFromTableFromID(String ueberschrift){

      String noteText = "";
      String sql = "SELECT notizText from notes WHERE überschrift=?";
      try(Connection connection = DriverManager.getConnection(URL , USERNAME , PASSWORD)){

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1 , ueberschrift);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()){
        noteText = resultSet.getString("notizText");
        } else if (!resultSet.next()){
            return null;
        }


      }catch (SQLException e){
          e.printStackTrace();
      }
      return noteText;
  }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String ueberschrift = resultSet.getString("überschrift");
                String notizText = resultSet.getString("notizText");

                Note note = new Note(id, ueberschrift, notizText);
                notes.add(note);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Hier sollten Sie eine angemessene Fehlerbehandlung durchführen
        }

        return notes;
    }

    public Note getNoteByTitle(String title) {
        String sql = "SELECT * FROM notes WHERE überschrift = ?";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String ueberschrift = resultSet.getString("überschrift");
                String notizText = resultSet.getString("notizText");
                return new Note(id, ueberschrift, notizText);
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Fehler beim Abrufen der Notiz: " + e.getMessage());
        }
    }
    public void deleteNoteById(int noteId) {
        String sql = "DELETE FROM notes WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, noteId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Notiz erfolgreich gelöscht.");
            } else {
                System.out.println("Keine Notiz mit dieser ID gefunden.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Fehler beim Löschen der Notiz: " + e.getMessage());
        }
    }




}