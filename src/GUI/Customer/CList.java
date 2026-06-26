package GUI.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import GUI.MainFrame;
import User_Interaction.CustomerInterplay;
import Main.Customer;


public class CList extends JFrame {
    private DefaultListModel<Customer> customerListModel;
    private JList<Customer> customerJList;

    public CList() {
        setTitle("Customers");
        setSize(550, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        customerListModel = new DefaultListModel<>();
        for (Customer c : CustomerInterplay.getCustomers()) {
            customerListModel.addElement(c);
        }
        customerJList = new JList<>(customerListModel);
        JScrollPane scrollPane = new JScrollPane(customerJList);
        customerJList.setBackground(new Color(200, 255, 255));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(155,225,175));
        JButton openButton = new JButton("Open");
        openButton.setBackground(new Color(155,225,175));
        JButton registerButton = new JButton("Register New Customer");
        JButton backButton = new JButton("Back");
        JButton deleteButton = new JButton("Delete Customer");
        deleteButton.setBackground(new Color(155, 225, 175));
        backButton.setBackground(new Color(155, 225, 175));
        registerButton.setBackground(new Color(155,225,175));
        buttonPanel.add(openButton);
        buttonPanel.add(registerButton);
        buttonPanel.setBackground(new Color(155,225,175));
        buttonPanel.add(backButton);
        buttonPanel.add(deleteButton);

        setUndecorated(true);


        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainFrame().setVisible(true);
            }
        });

        deleteButton.addActionListener(e -> {
            Customer sel = customerJList.getSelectedValue();
            if (sel != null) {
                CustomerInterplay.deleteCustomer(sel);
                customerListModel.removeElement(sel);
            }
        });


        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Customer selected = customerJList.getSelectedValue();
                if (selected != null) {
                    new CDetail(selected).setVisible(true);
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CForm(customerListModel).setVisible(true);
            }
        });
    }
}
