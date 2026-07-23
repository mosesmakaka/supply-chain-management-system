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
                        // create tables if they don't exist
                        String beTable = DBColumns.Tables.BUSINESS_ENTITY.name;
                        String itTable = DBColumns.Tables.ITEM.name;

                        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + beTable + " ("
                                + DBColumns.BusinessEntity.COL_ID.col + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + DBColumns.BusinessEntity.COL_NAME.col + " TEXT NOT NULL, "
                                + DBColumns.BusinessEntity.COL_TYPE.col + " TEXT, "
                                + DBColumns.BusinessEntity.COL_FUNDS.col + " REAL, "
                                + DBColumns.BusinessEntity.COL_MAX_CAPACITY.col + " INTEGER, "
                                + DBColumns.BusinessEntity.COL_USED_CAPACITY.col + " INTEGER);");

                        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + itTable + " ("
                                + DBColumns.Item.COL_ID.col + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + DBColumns.Item.COL_ENTITY_ID.col + " INTEGER, "
                                + DBColumns.Item.COL_NAME.col + " TEXT, "
                                + DBColumns.Item.COL_QUANTITY.col + " INTEGER, FOREIGN KEY(" + DBColumns.Item.COL_ENTITY_ID.col + ") REFERENCES " + beTable + "(" + DBColumns.BusinessEntity.COL_ID.col + "));");

                        // Ensure 'type' column exists (safety migration for older DBs)
                        boolean hasType = false;
                        try (ResultSet cols = stmt.executeQuery("PRAGMA table_info('" + beTable + "')")) {
                            while (cols.next()) {
                                String colName = cols.getString("name");
                                if (DBColumns.BusinessEntity.COL_TYPE.col.equalsIgnoreCase(colName)) { hasType = true; break; }
                            }
                        }
                        if (!hasType) {
                            stmt.executeUpdate("ALTER TABLE " + beTable + " ADD COLUMN " + DBColumns.BusinessEntity.COL_TYPE.col + " TEXT;");
                        }

                        // Enable WAL journal mode for better concurrency
                        try {
                            stmt.execute("PRAGMA journal_mode=WAL;");
                        } catch (SQLException ignore) {
                            // ignore if pragma unsupported
                        }
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
        String beTable = DBColumns.Tables.BUSINESS_ENTITY.name;
        String itTable = DBColumns.Tables.ITEM.name;

        // Try to find existing entity by name+type to perform update instead of blind insert
        String find = "SELECT " + DBColumns.BusinessEntity.COL_ID.col + " FROM " + beTable + " WHERE " + DBColumns.BusinessEntity.COL_NAME.col + " = ? AND " + DBColumns.BusinessEntity.COL_TYPE.col + " = ? LIMIT 1";
        String insert = "INSERT INTO " + beTable + "(" + DBColumns.BusinessEntity.COL_NAME.col + "," + DBColumns.BusinessEntity.COL_TYPE.col + "," + DBColumns.BusinessEntity.COL_FUNDS.col + "," + DBColumns.BusinessEntity.COL_MAX_CAPACITY.col + "," + DBColumns.BusinessEntity.COL_USED_CAPACITY.col + ") VALUES(?,?,?,?,?)";
        String update = "UPDATE " + beTable + " SET " + DBColumns.BusinessEntity.COL_FUNDS.col + " = ?, " + DBColumns.BusinessEntity.COL_MAX_CAPACITY.col + " = ?, " + DBColumns.BusinessEntity.COL_USED_CAPACITY.col + " = ? WHERE " + DBColumns.BusinessEntity.COL_ID.col + " = ?";

        int attempts = 0;
        while (true) {
            attempts++;
            try (Connection conn = getConnection()) {
                conn.setAutoCommit(false);
                Long existingId = null;
                try (PreparedStatement fps = conn.prepareStatement(find)) {
                    fps.setString(1, e.getName());
                    fps.setString(2, type);
                    try (ResultSet rs = fps.executeQuery()) {
                        if (rs.next()) existingId = rs.getLong(DBColumns.BusinessEntity.COL_ID.col);
                    }
                }
                if (existingId == null) {
                    try (PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, e.getName());
                        ps.setString(2, type);
                        ps.setDouble(3, e.getFunds());
                        ps.setInt(4, e.getMaxCapacity());
                        ps.setInt(5, e.getUsedCapacity());
                        ps.executeUpdate();
                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            if (rs.next()) existingId = rs.getLong(1);
                        }
                    }
                } else {
                    try (PreparedStatement ups = conn.prepareStatement(update)) {
                        ups.setDouble(1, e.getFunds());
                        ups.setInt(2, e.getMaxCapacity());
                        ups.setInt(3, e.getUsedCapacity());
                        ups.setLong(4, existingId);
                        ups.executeUpdate();
                    }
                }

                if (existingId != null) {
                    saveInventory(existingId, e.getInventory());
                }
                conn.commit();
                return existingId == null ? -1 : existingId;
            } catch (SQLException ex) {
                // retry on database lock
                if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("database is locked") && attempts < 5) {
                    try { Thread.sleep(100 * attempts); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                    continue;
                }
                throw ex;
            }
        }
    }

    public static void saveInventory(long entityId, java.util.List<Item> items) throws SQLException {
        String itTable = DBColumns.Tables.ITEM.name;
        String del = "DELETE FROM " + itTable + " WHERE " + DBColumns.Item.COL_ENTITY_ID.col + " = ?";
        String sql = "INSERT INTO " + itTable + "(" + DBColumns.Item.COL_ENTITY_ID.col + "," + DBColumns.Item.COL_NAME.col + "," + DBColumns.Item.COL_QUANTITY.col + ") VALUES(?,?,?)";
        int attempts = 0;
        while (true) {
            attempts++;
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
                return;
            } catch (SQLException ex) {
                if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("database is locked") && attempts < 5) {
                    try { Thread.sleep(100 * attempts); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                    continue;
                }
                throw ex;
            }
        }
    }

    // Load entities of a given type (e.g., "Factory", "Market") and rehydrate basic inventory
    public static List<BusinessEntity> loadEntitiesByType(String type) throws SQLException {
        List<BusinessEntity> out = new ArrayList<>();
        String beTable = DBColumns.Tables.BUSINESS_ENTITY.name;
        String q = "SELECT " + DBColumns.BusinessEntity.COL_ID.col + "," + DBColumns.BusinessEntity.COL_NAME.col + "," + DBColumns.BusinessEntity.COL_FUNDS.col + "," + DBColumns.BusinessEntity.COL_MAX_CAPACITY.col + "," + DBColumns.BusinessEntity.COL_USED_CAPACITY.col + " FROM " + beTable + " WHERE " + DBColumns.BusinessEntity.COL_TYPE.col + " = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong(DBColumns.BusinessEntity.COL_ID.col);
                    String name = rs.getString(DBColumns.BusinessEntity.COL_NAME.col);
                    double funds = rs.getDouble(DBColumns.BusinessEntity.COL_FUNDS.col);
                    int maxCap = rs.getInt(DBColumns.BusinessEntity.COL_MAX_CAPACITY.col);
                    int used = rs.getInt(DBColumns.BusinessEntity.COL_USED_CAPACITY.col);

                    BusinessEntity e = null;
                    if ("Factory".equalsIgnoreCase(type)) {
                        e = new Factory(name, maxCap, funds);
                    } else if ("Market".equalsIgnoreCase(type)) {
                        e = new Market(name, maxCap, funds);
                    } else if ("RawMaterialProducer".equalsIgnoreCase(type)) {
                        e = new RawMaterialProducer(name, "", 0.0, 0.0, maxCap, funds);
                    } else if ("Customer".equalsIgnoreCase(type)) {
                        continue; // Customer persistence handled separately
                    } else {
                        e = new Factory(name, maxCap, funds);
                    }

                    e.usedCapacity = used;

                    // load inventory
                    String itTable = DBColumns.Tables.ITEM.name;
                    String iq = "SELECT " + DBColumns.Item.COL_NAME.col + "," + DBColumns.Item.COL_QUANTITY.col + " FROM " + itTable + " WHERE " + DBColumns.Item.COL_ENTITY_ID.col + " = ?";
                    try (PreparedStatement ips = conn.prepareStatement(iq)) {
                        ips.setLong(1, id);
                        try (ResultSet irs = ips.executeQuery()) {
                            while (irs.next()) {
                                String iname = irs.getString(DBColumns.Item.COL_NAME.col);
                                int qty = irs.getInt(DBColumns.Item.COL_QUANTITY.col);
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

