	package GUI.RawMaterial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import User_Interaction.RawMaterialInterplay;
import Main.RawMaterialProducer;
import Main.InvalidInputException;

//RawMaterialForm, yeni RawMaterialProducer nesnesi oluşturur


public class RawMProducerForm extends JFrame {
    private JTextField nameField;
    private JTextField materialField;
    private JTextField genCostField;
    private JTextField sellPriceField;
    private JTextField capacityField;
    private JTextField fundsField;
    private DefaultListModel<RawMaterialProducer> listModel;

    public RawMProducerForm(DefaultListModel<RawMaterialProducer> listModel) {
        this.listModel = listModel;
        setTitle("New Raw Material Producer");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 5, 5));

        add(new JLabel("Producer Name:"));
        nameField = new JTextField();
        add(nameField);
        add(new JLabel("Material Name:"));
        materialField = new JTextField();
        add(materialField);
        add(new JLabel("Generation Cost per Unit:"));
        genCostField = new JTextField();
        add(genCostField);
        add(new JLabel("Selling Price per Unit:"));
        sellPriceField = new JTextField();
        add(sellPriceField);
        add(new JLabel("Max Storage Capacity:"));
        capacityField = new JTextField();
        add(capacityField);
        add(new JLabel("Initial Funds:"));
        fundsField = new JTextField();
        add(fundsField);

        JButton createButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");
        add(createButton);
        add(cancelButton);

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    RawMaterialProducer producer = RawMaterialInterplay.registerProducer(
                            nameField.getText().trim(),
                            materialField.getText().trim(),
                            genCostField.getText().trim(),
                            sellPriceField.getText().trim(),
                            capacityField.getText().trim(),
                            fundsField.getText().trim()
                    );

                    listModel.addElement(producer);
                    JOptionPane.showMessageDialog(RawMProducerForm.this, "Producer registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (InvalidInputException ex) {
                    JOptionPane.showMessageDialog(RawMProducerForm.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
