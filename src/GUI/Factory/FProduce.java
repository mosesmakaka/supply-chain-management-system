package GUI.Factory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import User_Interaction.FactoryInterplay;
import Main.Factory;
import Main.InsufficientResourcesException;
import Main.InsufficientStorageException;
import Main.InvalidInputException;


public class FProduce extends JFrame {
    private Factory factory;
    private JComboBox<String> designComboBox;
    private JTextField quantityField;
    private JLabel fundsLabel;
    private JLabel storageLabel;

    public FProduce(Factory factory) {
        this.factory = factory;
        setTitle(factory.getName() + " - Produce Products");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("Design:"));
        String[] designNames = factory.getDesigns().stream().map(d -> d.getProductName()).toArray(String[]::new);
        designComboBox = new JComboBox<>(designNames);
        add(designComboBox);

        add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        add(quantityField);


        add(new JLabel("Current Funds:"));
        fundsLabel = new JLabel(String.format("%.2f", factory.getFunds()));
        add(fundsLabel);
        add(new JLabel("Available Storage:"));
        storageLabel = new JLabel((factory.getMaxCapacity() - factory.getUsedCapacity()) + " free of " + factory.getMaxCapacity());
        add(storageLabel);

        JButton produceButton = new JButton("Produce");
        add(produceButton);
        add(new JLabel());

        produceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String productName = (String) designComboBox.getSelectedItem();
                if (productName == null) return;
                try {
                    FactoryInterplay.produceProducts(factory, productName, quantityField.getText().trim());
                    JOptionPane.showMessageDialog(FProduce.this, "Production completed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    storageLabel.setText((factory.getMaxCapacity() - factory.getUsedCapacity()) + " free of " + factory.getMaxCapacity());
                } catch (InvalidInputException | InsufficientResourcesException | InsufficientStorageException ex) {
                    JOptionPane.showMessageDialog(FProduce.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

