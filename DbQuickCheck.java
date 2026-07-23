import java.sql.*;

public class DbQuickCheck {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:db/supplychain.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Connected to: " + url);
            try (Statement stmt = conn.createStatement()) {
                System.out.println("Tables:");
                try (ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name")) {
                    while (rs.next()) {
                        System.out.println(" - " + rs.getString(1));
                    }
                }
                try (ResultSet rs2 = stmt.executeQuery("SELECT count(*) FROM business_entity")) {
                    if (rs2.next()) {
                        System.out.println("business_entity rows: " + rs2.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("DB check failed:");
            e.printStackTrace();
            System.exit(2);
        }
    }
}
