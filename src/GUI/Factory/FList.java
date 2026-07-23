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
        setUndecorated(false);
        getContentPane().setBackground(ModernColors.BACKGROUND);

        // Use GridBagLayout for consistent structure
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Header
        ModernPanel headerPanel = new ModernPanel(ModernColors.PRIMARY);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel(Icons.FACTORY + "Factories");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.weighty = 0.0;
        add(headerPanel, gbc);

        // List
        factoryListModel = new DefaultListModel<>();
        for (Factory f : FactoryInterplay.getFactories()) factoryListModel.addElement(f);
        factoryJList = new JList<>(factoryListModel);
        factoryJList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        factoryJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        factoryJList.setBackground(ModernColors.SURFACE);
        factoryJList.setSelectionBackground(ModernColors.PRIMARY_LIGHT);
        factoryJList.setSelectionForeground(Color.WHITE);
        factoryJList.setFixedCellHeight(40);

        JScrollPane scrollPane = new JScrollPane(factoryJList);
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER));

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 1.0; gbc.weighty = 1.0;
        add(scrollPane, gbc);

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

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 1.0; gbc.weighty = 0.0;
        add(buttonPanel, gbc);

        // Event listeners
        backButton.addActionListener(evt -> { dispose(); new MainFrame().setVisible(true); });

        openButton.addActionListener(e -> {
            Factory selected = factoryJList.getSelectedValue();
            if (selected != null) new FDetail(selected).setVisible(true);
            else JOptionPane.showMessageDialog(null, "Please select a factory first", "No Selection", JOptionPane.INFORMATION_MESSAGE);
        });

        registerButton.addActionListener(e -> new FForm(factoryListModel).setVisible(true));
    }
}
