package GUI.Factory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import User_Interaction.FactoryInterplay;
import Main.Factory;
import Main.InvalidInputException;


public class FForm extends JFrame {
    private JTextField nameField;
    private JTextField capacityField;
    private JTextField fundsField;
    private DefaultListModel<Factory> listModel;

    public FForm(DefaultListModel<Factory> listModel) {
        this.listModel = listModel;
        setTitle("New Factory");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Factory Name:"));
        nameField = new JTextField();
        add(nameField);
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
                    Factory factory = FactoryInterplay.registerFactory(
                            nameField.getText().trim(),
                            capacityField.getText().trim(),
                            fundsField.getText().trim()
                    );
                    listModel.addElement(factory);
                    JOptionPane.showMessageDialog(FForm.this, "Factory registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (InvalidInputException ex) {
                    JOptionPane.showMessageDialog(FForm.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
