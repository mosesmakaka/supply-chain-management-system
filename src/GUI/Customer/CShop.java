package GUI.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import User_Interaction.CustomerInterplay;
import User_Interaction.FactoryInterplay;
import Main.Customer;
import Main.InsufficientFundsException;
import Main.InsufficientResourcesException;
import Main.InvalidInputException;


public class CShop extends JFrame {
    private Customer customer;
    private DefaultListModel<FactoryInterplay.AvailableItem> availableListModel;
    private JList<FactoryInterplay.AvailableItem> availableJList;
    private JTextField quantityField;

    public CShop(Customer customer) {
        this.customer = customer;
        setTitle(customer.getName() + " - Shop");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        availableListModel = new DefaultListModel<>();
        refreshAvailableList();

        availableJList = new JList<>(availableListModel);
        JScrollPane scrollPane = new JScrollPane(availableJList);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(5);
        bottomPanel.add(quantityField);
        JButton purchaseButton = new JButton("Purchase");
        bottomPanel.add(purchaseButton);

        add(new JLabel("Products available in markets:"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        purchaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FactoryInterplay.AvailableItem selected = availableJList.getSelectedValue();
                if (selected == null) return;
                try {
                    CustomerInterplay.purchaseItem(customer, selected, quantityField.getText().trim());
                    JOptionPane.showMessageDialog(CShop.this, "Purchase successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshAvailableList();
                } catch (InvalidInputException | InsufficientFundsException | InsufficientResourcesException ex) {
                    JOptionPane.showMessageDialog(CShop.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void refreshAvailableList() {
        if (availableListModel == null) return;
        availableListModel.clear();
        for (FactoryInterplay.AvailableItem item : CustomerInterplay.listAvailableProductsForShopping()) {
            availableListModel.addElement(item);
        }
    }
}

