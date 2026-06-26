package GUI.Factory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import GUI.MainFrame;
import User_Interaction.FactoryInterplay;
import Main.Factory;


//Sistemdeki tüm ManufacturedProduct tasarımlarını ve fabrika stokunu listeler.
//diğerlerinde olduğu gibi GUI bunun üstüne kuruludur


public class FList extends JFrame {
    private DefaultListModel<Factory> factoryListModel;
    private JList<Factory> factoryJList;

    public FList() {
        setTitle("Factories");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        factoryListModel = new DefaultListModel<>();
        for (Factory f : FactoryInterplay.getFactories()) {
            factoryListModel.addElement(f);
        }
        factoryJList = new JList<>(factoryListModel);
        JScrollPane scrollPane = new JScrollPane(factoryJList);
        factoryJList.setBackground(new Color(200, 255, 255));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(155,225,175));
        JButton openButton = new JButton("Open");
        openButton.setBackground(new Color(155,225,175));
        JButton registerButton = new JButton("Register New Factory");
        registerButton.setBackground(new Color(155,225,175));
        buttonPanel.add(openButton);
        buttonPanel.add(registerButton);
        buttonPanel.setBackground(new Color(155,225, 175));
        JButton buttonBack =new JButton("Back");
        buttonBack.setBackground(new Color(155,225,175));
        buttonPanel.add(buttonBack);

        setUndecorated(true);



        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        buttonBack.addActionListener(new ActionListener() {
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
