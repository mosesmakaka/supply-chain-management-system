package GUI.Market;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import User_Interaction.MarketInterplay;
import Main.Market;
import Main.InvalidInputException;


public class MForm extends JFrame {
    private JTextField nameField;
    private JTextField capacityField;
    private JTextField fundsField;
    private DefaultListModel<Market> listModel;

    public MForm(DefaultListModel<Market> listModel) {
        this.listModel = listModel;
        setTitle("New Market");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Market Name:"));
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
                    Market market = MarketInterplay.registerMarket(
                            nameField.getText().trim(),
                            capacityField.getText().trim(),
                            fundsField.getText().trim()
                    );
                    listModel.addElement(market);
                    JOptionPane.showMessageDialog(MForm.this, "Market registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (InvalidInputException ex) {
                    JOptionPane.showMessageDialog(MForm.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
