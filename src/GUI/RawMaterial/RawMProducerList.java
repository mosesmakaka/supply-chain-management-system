package GUI.RawMaterial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import GUI.MainFrame;
import GUI.Utils.*;
import User_Interaction.RawMaterialInterplay;
import Main.RawMaterialProducer;

public class RawMProducerList extends JFrame {
    private DefaultListModel<RawMaterialProducer> producerListModel;
    private JList<RawMaterialProducer> producerJList;

    public RawMProducerList() {
        setTitle("Raw Material Producers");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(false);

        getContentPane().setBackground(ModernColors.BACKGROUND);

        // Header
        ModernPanel headerPanel = new ModernPanel(ModernColors.SUCCESS);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel(Icons.PRODUCER + "Raw Material Producers");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // List
        producerListModel = new DefaultListModel<>();
        for (RawMaterialProducer rp : RawMaterialInterplay.getProducers()) {
            producerListModel.addElement(rp);
        }
        producerJList = new JList<>(producerListModel);
        producerJList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        producerJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        producerJList.setBackground(ModernColors.SURFACE);
        producerJList.setSelectionBackground(ModernColors.SUCCESS_LIGHT);
        producerJList.setSelectionForeground(Color.WHITE);
        producerJList.setFixedCellHeight(40);
        
        JScrollPane scrollPane = new JScrollPane(producerJList);
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
        
        ModernButton openButton = new ModernButton(Icons.OPEN + "Open", ModernColors.SUCCESS);
        ModernButton registerButton = new ModernButton(Icons.ADD + "New Producer", ModernColors.SUCCESS);
        ModernButton backButton = new ModernButton(Icons.BACK + "Back", ModernColors.TEXT_SECONDARY);

        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(openButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event listeners
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RawMaterialProducer selected = producerJList.getSelectedValue();
                if (selected != null) {
                    new RawMProducerDetail(selected).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a producer first", "No Selection", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainFrame().setVisible(true);
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RawMProducerForm(producerListModel).setVisible(true);
            }
        });
    }
}
