package srowntree;


import javax.enterprise.context.ApplicationScoped;
import java.sql.*;

@ApplicationScoped
public class DatabaseProvider {

    private static final String DB_PATH = "jdbc:sqlite:text.db";

    public void addTextToDb(long pUId, String pText) {
        insert(pUId, pText);
    }

    public void insert(long pUId, String pText) {
        connect();
        createNewTableIfNotExists();
        String sql = "INSERT INTO text(UId,text) VALUES(?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, pUId);
            pstmt.setString(2, pText);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public String selectByID(long pUId){
        connect();
        createNewTableIfNotExists();
        String sql = "SELECT UId, text FROM text WHERE UId = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setLong(1,pUId);
            ResultSet rs  = pstmt.executeQuery();

            while (rs.next())
                return rs.getString("text");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public String selectAll(){
        connect();
        createNewTableIfNotExists();
        String output = "";
        String sql = "SELECT UId, text FROM text";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                output+=(rs.getLong("UId") +  "\t" +
                        rs.getString("text") + "\n");

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return output;
    }

    public void deleteAll() {
        connect();
        createNewTableIfNotExists();
        String sql = "DELETE FROM text";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createNewTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS text (\n"
                + "	UId integer PRIMARY KEY,\n"
                + "	text text NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_PATH);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_PATH);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }



}
