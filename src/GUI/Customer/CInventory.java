package GUI.Customer;

import javax.swing.*;
import java.awt.*;
import Main.Customer;
import Main.Product;


public class CInventory extends JFrame {
    public CInventory(Customer customer) {
        setTitle(customer.getName() + " - Purchase History");
        setSize(300, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DefaultListModel<String> inventoryModel = new DefaultListModel<>();
        for (Product p : customer.getPurchasedItems()) {
            inventoryModel.addElement(p.getName() + " x " + p.getQuantity());
        }
        JList<String> inventoryList = new JList<>(inventoryModel);
        add(new JScrollPane(inventoryList), BorderLayout.CENTER);
    }
}

