package GUI.Market;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import User_Interaction.MarketInterplay;
import Main.Market;
import Main.Product;
import Main.InvalidInputException;


public class MSetPrice extends JFrame {
    private Market market;
    private DefaultListModel<Product> productListModel;
    private JList<Product> productJList;
    private JTextField priceField;

    public MSetPrice(Market market) {
        this.market = market;
        setTitle(market.getName() + " - Set Prices");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        productListModel = new DefaultListModel<>();
        loadProducts();

        productJList = new JList<>(productListModel);
        JScrollPane scrollPane = new JScrollPane(productJList);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("New Price:"));
        priceField = new JTextField(5);
        bottomPanel.add(priceField);
        JButton setButton = new JButton("Set Price");
        bottomPanel.add(setButton);

        add(new JLabel("Products in inventory:"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Product selected = productJList.getSelectedValue();
                if (selected == null) return;
                try {
                    MarketInterplay.setPrice(market, selected.getName(), priceField.getText().trim());
                    JOptionPane.showMessageDialog(MSetPrice.this, "Price updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadProducts();
                } catch (InvalidInputException ex) {
                    JOptionPane.showMessageDialog(MSetPrice.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void loadProducts() {
        if (productListModel == null) return;
        productListModel.clear();
        for (Main.Item item : market.getInventory()) {
            if (item instanceof Product) {
                productListModel.addElement((Product) item);
            }
        }
    }
}

