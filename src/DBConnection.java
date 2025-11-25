// Save as: DBConnection.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConnection {

    // Default values (will be overridden by db.properties if present)
    private static String URL = "jdbc:mysql://localhost:3306/exam_system";
    private static String USER = "root";        // change if needed
    private static String PASSWORD = "";    // change if needed

    static {
        // Try to load database configuration from db.properties at project root
        Properties p = new Properties();
        try (InputStream in = new FileInputStream("db.properties")) {
            p.load(in);
            URL = p.getProperty("db.url", URL);
            USER = p.getProperty("db.user", USER);
            PASSWORD = p.getProperty("db.password", PASSWORD);
        } catch (IOException ignored) {
            // No properties file found — fall back to defaults
            // Keep defaults above. To customize, create a db.properties file in project root.
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
