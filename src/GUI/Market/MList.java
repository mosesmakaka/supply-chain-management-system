package GUI.Market;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import GUI.MainFrame;
import User_Interaction.MarketInterplay;
import Main.Market;

public class MList extends JFrame {
    private DefaultListModel<Market> marketListModel;
    private JList<Market> marketJList;

    public MList() {
        setTitle("Markets");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        marketListModel = new DefaultListModel<>();
        for (Market m : MarketInterplay.getMarkets()) {
            marketListModel.addElement(m);
        }
        marketJList = new JList<>(marketListModel);
        JScrollPane scrollPane = new JScrollPane(marketJList);
        marketJList.setBackground(new Color(200, 255, 255));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(155,225,175));
        JButton openButton = new JButton("Open");
        openButton.setBackground(new Color(155,225,175));
        JButton registerButton = new JButton("Register New Market");
        registerButton.setBackground(new Color(155,225,175));
        buttonPanel.add(openButton);
        buttonPanel.add(registerButton);
        buttonPanel.setBackground(new Color(155,225,175));
        JButton buttonBack=new JButton("Back");
        buttonBack.setBackground(new Color(155,225,175));
        buttonPanel.add(buttonBack);

        setUndecorated(true);


        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Market selected = marketJList.getSelectedValue();
                if (selected != null) {
                    new MDetail(selected).setVisible(true);
                }
            }
        });

        buttonBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dispose();
                new MainFrame().setVisible(true);
            }
        });
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MForm(marketListModel).setVisible(true);
            }
        });
    }
}
