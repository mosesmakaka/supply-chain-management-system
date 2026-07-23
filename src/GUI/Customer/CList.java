package GUI.Customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import GUI.MainFrame;
import GUI.Utils.*;
import User_Interaction.CustomerInterplay;
import Main.Customer;

public class CList extends JFrame {
    private DefaultListModel<Customer> customerListModel;
    private JList<Customer> customerJList;

    public CList() {
        setTitle("Customers");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setUndecorated(false);
        getContentPane().setBackground(ModernColors.BACKGROUND);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.BOTH;

        // Header
        ModernPanel headerPanel = new ModernPanel(ModernColors.PRIMARY);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel(Icons.CUSTOMER + "Customers");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        gbc.gridx=0; gbc.gridy=0; gbc.weightx=1.0; gbc.weighty=0.0;
        add(headerPanel, gbc);

        // List
        customerListModel = new DefaultListModel<>();
        for (Customer c : CustomerInterplay.getCustomers()) customerListModel.addElement(c);
        customerJList = new JList<>(customerListModel);
        customerJList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        customerJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerJList.setBackground(ModernColors.SURFACE);
        customerJList.setSelectionBackground(ModernColors.PRIMARY_LIGHT);
        customerJList.setSelectionForeground(Color.WHITE);
        customerJList.setFixedCellHeight(40);

        JScrollPane scrollPane = new JScrollPane(customerJList);
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER));

        gbc.gridx=0; gbc.gridy=1; gbc.weightx=1.0; gbc.weighty=1.0;
        add(scrollPane, gbc);

        // Buttons
        ModernPanel buttonPanel = new ModernPanel(ModernColors.SURFACE_DARK);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(10,15,10,15));

        ModernButton openButton = new ModernButton(Icons.OPEN + "Open", ModernColors.PRIMARY);
        ModernButton registerButton = new ModernButton(Icons.ADD + "New Customer", ModernColors.SUCCESS);
        ModernButton deleteButton = new ModernButton(Icons.DELETE + "Delete", ModernColors.DANGER);
        ModernButton backButton = new ModernButton(Icons.BACK + "Back", ModernColors.TEXT_SECONDARY);

        buttonPanel.add(backButton); buttonPanel.add(deleteButton); buttonPanel.add(registerButton); buttonPanel.add(openButton);

        gbc.gridx=0; gbc.gridy=2; gbc.weightx=1.0; gbc.weighty=0.0;
        add(buttonPanel, gbc);

        // Event listeners
        backButton.addActionListener(e -> { dispose(); new MainFrame().setVisible(true); });

        deleteButton.addActionListener(e -> {
            Customer sel = customerJList.getSelectedValue();
            if (sel != null) {
                int response = JOptionPane.showConfirmDialog(null, "Delete this customer?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    CustomerInterplay.deleteCustomer(sel);
                    customerListModel.removeElement(sel);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a customer first", "No Selection", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        openButton.addActionListener(e -> {
            Customer selected = customerJList.getSelectedValue();
            if (selected != null) new CDetail(selected).setVisible(true);
            else JOptionPane.showMessageDialog(null, "Please select a customer first", "No Selection", JOptionPane.INFORMATION_MESSAGE);
        });

        registerButton.addActionListener(e -> new CForm(customerListModel).setVisible(true));
    }
}

