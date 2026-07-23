package GUI.Market;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import GUI.MainFrame;
import GUI.Utils.*;
import User_Interaction.MarketInterplay;
import Main.Market;

public class MList extends JFrame {
    private DefaultListModel<Market> marketListModel;
    private JList<Market> marketJList;

    public MList() {
        setTitle("Markets");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setUndecorated(false);
        getContentPane().setBackground(ModernColors.BACKGROUND);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.BOTH;

        ModernPanel headerPanel = new ModernPanel(ModernColors.INFO);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel(Icons.MARKET + "Markets");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        gbc.gridx=0; gbc.gridy=0; gbc.weightx=1.0; gbc.weighty=0.0;
        add(headerPanel, gbc);

        marketListModel = new DefaultListModel<>();
        for (Market m : MarketInterplay.getMarkets()) marketListModel.addElement(m);
        marketJList = new JList<>(marketListModel);
        marketJList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        marketJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        marketJList.setBackground(ModernColors.SURFACE);
        marketJList.setSelectionBackground(new Color(100, 180, 255));
        marketJList.setSelectionForeground(Color.WHITE);
        marketJList.setFixedCellHeight(40);

        JScrollPane scrollPane = new JScrollPane(marketJList);
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER));

        gbc.gridx=0; gbc.gridy=1; gbc.weightx=1.0; gbc.weighty=1.0;
        add(scrollPane, gbc);

        ModernPanel buttonPanel = new ModernPanel(ModernColors.SURFACE_DARK);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(10,15,10,15));

        ModernButton openButton = new ModernButton(Icons.OPEN + "Open", ModernColors.INFO);
        ModernButton registerButton = new ModernButton(Icons.ADD + "New Market", ModernColors.SUCCESS);
        ModernButton backButton = new ModernButton(Icons.BACK + "Back", ModernColors.TEXT_SECONDARY);

        buttonPanel.add(backButton); buttonPanel.add(registerButton); buttonPanel.add(openButton);

        gbc.gridx=0; gbc.gridy=2; gbc.weightx=1.0; gbc.weighty=0.0;
        add(buttonPanel, gbc);

        // Event listeners
        openButton.addActionListener(e -> {
            Market selected = marketJList.getSelectedValue();
            if (selected != null) new MDetail(selected).setVisible(true);
            else JOptionPane.showMessageDialog(null, "Please select a market first", "No Selection", JOptionPane.INFORMATION_MESSAGE);
        });

        backButton.addActionListener(evt -> { dispose(); new MainFrame().setVisible(true); });
        registerButton.addActionListener(e -> new MForm(marketListModel).setVisible(true));
    }
}
