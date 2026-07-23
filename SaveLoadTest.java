import Main.DBHelper;
import Main.Factory;
import Main.BusinessEntity;
import java.util.List;

public class SaveLoadTest {
    public static void main(String[] args) {
        try {
            DBHelper.initDatabase();
            Factory f = new Factory("TF-1", 50, 200.0);
            long id = DBHelper.saveEntityWithType("Factory", f);
            System.out.println("Saved id=" + id);
            List<BusinessEntity> list = DBHelper.loadEntitiesByType("Factory");
            System.out.println("Loaded factories: " + list.size());
            for (BusinessEntity be : list) {
                System.out.println(" - " + be.getName() + " funds=" + be.getFunds());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
