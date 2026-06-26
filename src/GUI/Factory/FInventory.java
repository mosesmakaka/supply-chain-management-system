package GUI.Factory;

import javax.swing.*;
import java.awt.*;
import Main.Factory;
import Main.Item;
import Main.Product;
import Main.Byproduct;


public class FInventory extends JFrame {
    public FInventory(Factory factory) {
        setTitle(factory.getName() + " - Inventory");
        setSize(300, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DefaultListModel<String> inventoryModel = new DefaultListModel<>();
        for (Item item : factory.getInventory()) {
            if (item instanceof Byproduct) {
                inventoryModel.addElement(item.getName() + " (byproduct) x " + item.getQuantity());
            } else if (item instanceof Product) {
                inventoryModel.addElement(item.getName() + " (product) x " + item.getQuantity());
            } else {
                // Raw material or other
                inventoryModel.addElement(item.getName() + " x " + item.getQuantity());
            }
        }
        JList<String> inventoryList = new JList<>(inventoryModel);
        add(new JScrollPane(inventoryList), BorderLayout.CENTER);
    }
}
