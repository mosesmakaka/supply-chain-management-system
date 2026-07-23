package Main;

public class DBColumns {
    public enum Tables {
        BUSINESS_ENTITY("business_entity"),
        ITEM("item");
        public final String name;
        Tables(String name) { this.name = name; }
    }

    public enum BusinessEntity {
        COL_ID("id"),
        COL_NAME("name"),
        COL_TYPE("type"),
        COL_FUNDS("funds"),
        COL_MAX_CAPACITY("max_capacity"),
        COL_USED_CAPACITY("used_capacity");
        public final String col;
        BusinessEntity(String col) { this.col = col; }
    }

    public enum Item {
        COL_ID("id"),
        COL_ENTITY_ID("entity_id"),
        COL_NAME("name"),
        COL_QUANTITY("quantity");
        public final String col;
        Item(String col) { this.col = col; }
    }
}
