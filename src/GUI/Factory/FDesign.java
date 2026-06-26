package GUI.Factory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import User_Interaction.FactoryInterplay;
import User_Interaction.RawMaterialInterplay;
import Main.Factory;
import Main.ManufacturedProduct;
import Main.RawMaterialProducer;


//Yeni design sayfasını açan kısımdır burası, ürünün design'i bu framede yapılır

public class FDesign extends JFrame {
    private Factory factory;
    private DefaultListModel<String> availableListModel;
    private DefaultListModel<String> chosenListModel;
    private JList<String> availableList;
    private JList<String> chosenList;
    private JTextField amountField;
    private JTextField productNameField;
    private JTextField productPriceField;
    private JTextField byproductNameField;
    private JTextField byproductAmountField;
    private JTextField byproductCostField;
    private List<ManufacturedProduct.InputRequirement> chosenInputs;

    public FDesign(Factory factory) {
        this.factory = factory;
        this.chosenInputs = new ArrayList<>();
        setTitle(factory.getName() + " - Design New Product");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        availableListModel = new DefaultListModel<>();
        for (RawMaterialProducer rp : RawMaterialInterplay.getProducers()) {
            String matName = rp.getMaterialName();
            if (!availableListModel.contains(matName)) {
                availableListModel.addElement(matName);
            }
        }
        for (String prodType : FactoryInterplay.getProductTypes()) {
            if (!availableListModel.contains(prodType)) {
                availableListModel.addElement(prodType);
            }
        }
        for (ManufacturedProduct d : factory.getDesigns()) {
            String prodName = d.getProductName();
            if (!availableListModel.contains(prodName)) {
                availableListModel.addElement(prodName);
            }
        }
        availableList = new JList<>(availableListModel);
        chosenListModel = new DefaultListModel<>();
        chosenList = new JList<>(chosenListModel);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        centerPanel.add(new JScrollPane(availableList));

        JPanel addPanel = new JPanel(new FlowLayout());
        amountField = new JTextField(3);
        JButton addButton = new JButton("Add");
        addPanel.add(new JLabel("Amount:"));
        addPanel.add(amountField);
        addPanel.add(addButton);

        centerPanel.add(addPanel);
        centerPanel.add(new JScrollPane(chosenList));
        add(centerPanel, BorderLayout.CENTER);

        productNameField     = new JTextField();
        productPriceField    = new JTextField();
        byproductNameField   = new JTextField();
        byproductAmountField = new JTextField();
        byproductCostField   = new JTextField();
        JButton saveButton   = new JButton("Save Design");

        JPanel bottomPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        bottomPanel.add(productNameField);
        bottomPanel.add(productPriceField);
        bottomPanel.add(byproductNameField);
        bottomPanel.add(byproductAmountField);
        bottomPanel.add(byproductCostField);
        bottomPanel.add(saveButton);
        bottomPanel.add(new JLabel("Product Name:"));
        bottomPanel.add(new JLabel("Product Price:"));
        bottomPanel.add(new JLabel("Byproduct Name:"));
        bottomPanel.add(new JLabel("Byproduct Amount:"));
        bottomPanel.add(new JLabel("Byproduct Cost:"));
        bottomPanel.add(new JLabel());
        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            String selected = availableList.getSelectedValue();
            if (selected == null) return;
            try {
                int amount = Integer.parseInt(amountField.getText().trim());
                if (amount <= 0) throw new NumberFormatException();
                chosenInputs.add(new ManufacturedProduct.InputRequirement(selected, amount));
                chosenListModel.addElement(selected + " x " + amount);
                availableListModel.removeElement(selected);
                amountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid positive integer amount.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        saveButton.addActionListener(e -> {
            try {
                FactoryInterplay.createDesign(factory,
                        productNameField.getText().trim(),
                        productPriceField.getText().trim(),
                        byproductNameField.getText().trim(),
                        byproductAmountField.getText().trim(),
                        byproductCostField.getText().trim(),
                        chosenInputs
                );
                JOptionPane.showMessageDialog(this,
                        "Design saved successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
