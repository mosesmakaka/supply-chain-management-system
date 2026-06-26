package GUI.Market;

import javax.swing.*;

//seçili ürünlerden fabrikadan product alım yeri

import java.awt.*;
import java.awt.event.*;

import User_Interaction.FactoryInterplay;
import User_Interaction.MarketInterplay;
import Main.Market;
import Main.InsufficientFundsException;
import Main.InsufficientResourcesException;
import Main.InsufficientStorageException;
import Main.InvalidInputException;


public class MBuy extends JFrame {
    private Market market;
    private DefaultListModel<FactoryInterplay.AvailableItem> availableListModel;
    private JList<FactoryInterplay.AvailableItem> availableJList;
    private JTextField quantityField;

    public MBuy(Market market) {
        this.market = market;
        setTitle(market.getName() + " - Buy Products");
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
        JButton buyButton = new JButton("Buy");
        bottomPanel.add(buyButton);

        add(new JLabel("Available products to buy:"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        buyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FactoryInterplay.AvailableItem selected = availableJList.getSelectedValue();
                if (selected == null) return;
                try {
                    MarketInterplay.buyItem(market, selected, quantityField.getText().trim());
                    JOptionPane.showMessageDialog(MBuy.this, "Purchase successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshAvailableList();
                } catch (InvalidInputException | InsufficientFundsException | InsufficientStorageException | InsufficientResourcesException ex) {
                    JOptionPane.showMessageDialog(MBuy.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void refreshAvailableList() {
        if (availableListModel == null) return;
        availableListModel.clear();
        for (FactoryInterplay.AvailableItem item : MarketInterplay.listAvailableItemsForPurchase(market)) {
            availableListModel.addElement(item);
        }
    }
}
