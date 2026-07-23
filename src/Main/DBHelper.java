package Main;

import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private static final String DB_DIR = "db";
    private static final String DB_FILE = DB_DIR + File.separator + "supplychain.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;

    public static void initDatabase() {
        try {
            File dir = new File(DB_DIR);
            if (!dir.exists()) dir.mkdirs();
            try (Connection conn = getConnection()) {
                if (conn != null) {
                    try (Statement stmt = conn.createStatement()) {
                        // add a "type" column so we can rehydrate subclasses
                        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS business_entity (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, type TEXT, funds REAL, max_capacity INTEGER, used_capacity INTEGER);");
                        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS item (id INTEGER PRIMARY KEY AUTOINCREMENT, entity_id INTEGER, name TEXT, quantity INTEGER, FOREIGN KEY(entity_id) REFERENCES business_entity(id));");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Save an entity and its inventory. Returns the generated entity id.
    public static long saveEntityWithType(String type, BusinessEntity e) throws SQLException {
        String sql = "INSERT INTO business_entity(name,type,funds,max_capacity,used_capacity) VALUES(?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getName());
            ps.setString(2, type);
            ps.setDouble(3, e.getFunds());
            ps.setInt(4, e.getMaxCapacity());
            ps.setInt(5, e.getUsedCapacity());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    // persist inventory right away
                    saveInventory(id, e.getInventory());
                    return id;
                }
            }
        }
        return -1;
    }

    public static void saveInventory(long entityId, java.util.List<Item> items) throws SQLException {
        String del = "DELETE FROM item WHERE entity_id = ?";
        String sql = "INSERT INTO item(entity_id,name,quantity) VALUES(?,?,?)";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement delPs = conn.prepareStatement(del)) {
                delPs.setLong(1, entityId);
                delPs.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Item it : items) {
                    ps.setLong(1, entityId);
                    ps.setString(2, it.getName());
                    ps.setInt(3, it.getQuantity());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            conn.commit();
        }
    }

    // Load entities of a given type (e.g., "Factory", "Market") and rehydrate basic inventory
    public static List<BusinessEntity> loadEntitiesByType(String type) throws SQLException {
        List<BusinessEntity> out = new ArrayList<>();
        String q = "SELECT id,name,funds,max_capacity,used_capacity FROM business_entity WHERE type = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    String name = rs.getString("name");
                    double funds = rs.getDouble("funds");
                    int maxCap = rs.getInt("max_capacity");
                    int used = rs.getInt("used_capacity");

                    BusinessEntity e = null;
                    if ("Factory".equalsIgnoreCase(type)) {
                        e = new Factory(name, maxCap, funds);
                    } else if ("Market".equalsIgnoreCase(type)) {
                        e = new Market(name, maxCap, funds);
                    } else if ("RawMaterialProducer".equalsIgnoreCase(type)) {
                        // RawMaterialProducer requires materialName, generationCost, sellingPrice, capacity, initialFunds
                        // we don't have those fields in the DB yet, use sensible defaults for rehydration
                        e = new RawMaterialProducer(name, "", 0.0, 0.0, maxCap, funds);
                    } else if ("Customer".equalsIgnoreCase(type)) {
                        // Customer is not a BusinessEntity subclass in this project; skip rehydration here
                        continue;
                    } else {
                        // fallback to a generic Factory so object can hold inventory
                        e = new Factory(name, maxCap, funds);
                    }

                    // set protected fields directly (same package)
                    e.usedCapacity = used;

                    // load inventory
                    String iq = "SELECT name,quantity FROM item WHERE entity_id = ?";
                    try (PreparedStatement ips = conn.prepareStatement(iq)) {
                        ips.setLong(1, id);
                        try (ResultSet irs = ips.executeQuery()) {
                            while (irs.next()) {
                                String iname = irs.getString("name");
                                int qty = irs.getInt("quantity");
                                // rehydrate as Product with price 0 — better type inference can be added later
                                Product p = new Product(iname, qty, 0);
                                e.inventory.add(p);
                            }
                        }
                    }

                    out.add(e);
                }
            }
        }
        return out;
    }
}

