public class RunInit {
    public static void main(String[] args) throws Exception {
        Main.DBHelper.initDatabase();
        // then inspect
        DBInspect.main(args);
    }
}
