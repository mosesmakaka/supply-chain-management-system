package GUI.Factory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Main.Factory;


public class FDetail extends JFrame {
    private Factory factory;
    private JLabel fundsLabel;
    private JLabel storageLabel;

    public FDetail(Factory factory) {
        this.factory = factory;
        setTitle("Factory: " + factory.getName());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 1, 5, 5));

        // Display basic info
        fundsLabel = new JLabel("Funds: " + String.format("%.2f", factory.getFunds()));
        storageLabel = new JLabel("Storage: " + factory.getUsedCapacity() + " / " + factory.getMaxCapacity());
        add(fundsLabel);
        add(storageLabel);

        // Action buttons for factory operations
        JButton buyButton = new JButton("Buy Materials/Products");
        JButton produceButton = new JButton("Produce Products");
        JButton designButton = new JButton("Design New Product");
        JButton disposeButton = new JButton("Dispose Byproduct");
        JButton inventoryButton = new JButton("Check Inventory");

        add(buyButton);
        add(produceButton);
        add(designButton);
        add(disposeButton);
        add(inventoryButton);

        buyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FBuy(factory).setVisible(true);
            }
        });
        produceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FProduce(factory).setVisible(true);
            }
        });
        designButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FDesign(factory).setVisible(true);
            }
        });
        disposeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FDispose(factory).setVisible(true);
            }
        });
        inventoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FInventory(factory).setVisible(true);
            }
        });
    }
}
