import java.sql.*;
public class DBInspect {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:sqlite:db/supplychain.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Connected");
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("PRAGMA table_info('business_entity')")) {
                    System.out.println("business_entity columns:");
                    while (rs.next()) {
                        System.out.println(rs.getInt("cid") + ": " + rs.getString("name") + " type=" + rs.getString("type"));
                    }
                }
                try (ResultSet rs2 = stmt.executeQuery("SELECT sql FROM sqlite_master WHERE name='business_entity'")) {
                    while (rs2.next()) {
                        System.out.println("create sql: " + rs2.getString(1));
                    }
                }
                try (ResultSet rs3 = stmt.executeQuery("PRAGMA journal_mode")) {
                    if (rs3.next()) System.out.println("journal_mode: " + rs3.getString(1));
                }
            }
        }
    }
}
