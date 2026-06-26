package GUI.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import User_Interaction.CustomerInterplay;
import Main.Customer;
import Main.InvalidInputException;


public class CForm extends JFrame {
    private JTextField nameField;
    private JTextField moneyField;
    private DefaultListModel<Customer> listModel;

    public CForm(DefaultListModel<Customer> listModel) {
        this.listModel = listModel;
        setTitle("New Customer");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        add(nameField);
        add(new JLabel("Initial Money:"));
        moneyField = new JTextField();
        add(moneyField);

        JButton createButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");
        add(createButton);
        add(cancelButton);

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Customer customer = CustomerInterplay.registerCustomer(
                            nameField.getText().trim(),
                            moneyField.getText().trim()
                    );
                    listModel.addElement(customer);
                    JOptionPane.showMessageDialog(CForm.this, "Customer registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (InvalidInputException ex) {
                    JOptionPane.showMessageDialog(CForm.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
