package GUI.Factory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import User_Interaction.FactoryInterplay;
import Main.Factory;
import Main.InsufficientFundsException;
import Main.InsufficientResourcesException;
import Main.InsufficientStorageException;
import Main.InvalidInputException;


//RawMaterialProducer'lardan RawMaterial almamızı sağlar


public class FBuy extends JFrame {
    private Factory factory;
    private DefaultListModel<FactoryInterplay.AvailableItem> availableListModel;
    private JList<FactoryInterplay.AvailableItem> availableJList;
    private JTextField quantityField;

    public FBuy(Factory factory) {
        this.factory = factory;
        setTitle(factory.getName() + " - Buy Materials/Products");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        availableListModel = new DefaultListModel<>();
        refreshAvailableList();

        availableJList = new JList<>(availableListModel);
        JScrollPane scrollPane = new JScrollPane(availableJList);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(5);
        bottomPanel.add(quantityField);
        JButton buyButton = new JButton("Buy");
        bottomPanel.add(buyButton);

        add(new JLabel("Available items to buy:"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        buyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FactoryInterplay.AvailableItem selected = availableJList.getSelectedValue();
                if (selected == null) return;
                try {
                    FactoryInterplay.buyItem(factory, selected, quantityField.getText().trim());
                    JOptionPane.showMessageDialog(FBuy.this, "Purchase successful.", "Success", JOptionPane.INFORMATION_MESSAGE);

                    refreshAvailableList();
                } catch (InvalidInputException | InsufficientFundsException | InsufficientStorageException | InsufficientResourcesException ex) {
                    JOptionPane.showMessageDialog(FBuy.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    private void refreshAvailableList() {
        if (availableListModel == null) return;
        availableListModel.clear();
        for (FactoryInterplay.AvailableItem item : FactoryInterplay.listAvailableItemsForPurchase(factory)) {
            availableListModel.addElement(item);
        }
    }
}

