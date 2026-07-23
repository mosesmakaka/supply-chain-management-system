package GUI.Factory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import GUI.MainFrame;
import User_Interaction.FactoryInterplay;
import Main.Factory;
import GUI.util.LayoutUtils;


//Sistemdeki tüm ManufacturedProduct tasarımlarını ve fabrika stokunu listeler.
//diğerlerinde olduğu gibi GUI bunun üstüne kuruludur


public class FList extends JFrame {
    private DefaultListModel<Factory> factoryListModel;
    private JList<Factory> factoryJList;

    public FList() {
        setTitle("Factories");
        setSize(420, 340);
        setLocationRelativeTo(null);

        factoryListModel = new DefaultListModel<>();
        for (Factory f : FactoryInterplay.getFactories()) {
            factoryListModel.addElement(f);
        }
        factoryJList = new JList<>(factoryListModel);
        JScrollPane scrollPane = new JScrollPane(factoryJList);
        factoryJList.setBackground(new Color(200, 255, 255));

        setUndecorated(true);

        setLayout(new GridBagLayout());
        GridBagConstraints c = LayoutUtils.gbc(0,0,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH);
        add(scrollPane, c);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(155,225,175));
        GridBagConstraints bc = LayoutUtils.gbc(0,0);
        JButton openButton = new JButton("Open");
        openButton.setBackground(new Color(155,225,175));
        buttonPanel.add(openButton, bc);
        bc.gridx = 1;
        JButton registerButton = new JButton("Register New Factory");
        registerButton.setBackground(new Color(155,225,175));
        buttonPanel.add(registerButton, bc);
        bc.gridx = 2;
        JButton buttonBack = new JButton("Back");
        buttonBack.setBackground(new Color(155,225,175));
        buttonPanel.add(buttonBack, bc);

        c = LayoutUtils.gbc(0,1,1,1,0.0,0.0,GridBagConstraints.SOUTH,GridBagConstraints.HORIZONTAL);
        add(buttonPanel, c);
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
