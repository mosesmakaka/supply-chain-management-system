package GUI.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Main.Customer;


public class CDetail extends JFrame {
    private Customer customer;
    private JLabel moneyLabel;

    public CDetail(Customer customer) {
        this.customer = customer;
        setTitle("Customer: " + customer.getName());
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 5, 5));

        moneyLabel = new JLabel("Money: " + String.format("%.2f", customer.getMoney()));
        add(moneyLabel);

        JButton shopButton = new JButton("Shop");
        JButton inventoryButton = new JButton("View Inventory");
        add(shopButton);
        add(inventoryButton);

        shopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                new CShop(customer).setVisible(true);
            }
        });
        inventoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                new CInventory(customer).setVisible(true);
            }
        });
    }
}
