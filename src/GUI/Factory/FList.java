package GUI.Factory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import GUI.MainFrame;
import GUI.Utils.*;
import User_Interaction.FactoryInterplay;
import Main.Factory;


public class FList extends JFrame {
    private DefaultListModel<Factory> factoryListModel;
    private JList<Factory> factoryJList;

    public FList() {
        setTitle("Factories");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(false);

        getContentPane().setBackground(ModernColors.BACKGROUND);

        // Header
        ModernPanel headerPanel = new ModernPanel(ModernColors.PRIMARY);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel(Icons.FACTORY + "Factories");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // List
        factoryListModel = new DefaultListModel<>();
        for (Factory f : FactoryInterplay.getFactories()) {
            factoryListModel.addElement(f);
        }
        factoryJList = new JList<>(factoryListModel);
        factoryJList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        factoryJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        factoryJList.setBackground(ModernColors.SURFACE);
        factoryJList.setSelectionBackground(ModernColors.PRIMARY_LIGHT);
        factoryJList.setSelectionForeground(Color.WHITE);
        factoryJList.setFixedCellHeight(40);
        
        JScrollPane scrollPane = new JScrollPane(factoryJList);
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER));
        scrollPane.setBackground(ModernColors.SURFACE);
        
        ModernPanel contentPanel = new ModernPanel();
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        // Buttons panel
        ModernPanel buttonPanel = new ModernPanel(ModernColors.SURFACE_DARK);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        ModernButton openButton = new ModernButton(Icons.OPEN + "Open", ModernColors.PRIMARY);
        ModernButton registerButton = new ModernButton(Icons.ADD + "New Factory", ModernColors.SUCCESS);
        ModernButton backButton = new ModernButton(Icons.BACK + "Back", ModernColors.TEXT_SECONDARY);

        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(openButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event listeners
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dispose();
                new MainFrame().setVisible(true);
            }
        });

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Factory selected = factoryJList.getSelectedValue();
                if (selected != null) {
                    new FDetail(selected).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a factory first", "No Selection", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FForm(factoryListModel).setVisible(true);
            }
        });
    }
}
