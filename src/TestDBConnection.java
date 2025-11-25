import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDBConnection {
    public static void main(String[] args) {
        try (Connection c = DBConnection.getConnection()) {
            System.out.println("Got connection: " + (c != null));
            try (Statement st = c.createStatement()) {
                ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users");
                if (rs.next()) System.out.println("users count: " + rs.getInt(1));
            }
        } catch (Exception e) {
            System.err.println("DB test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
        System.out.println("DB test OK");
    }
}
