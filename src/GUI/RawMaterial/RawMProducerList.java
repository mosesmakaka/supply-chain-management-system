package GUI.RawMaterial;

import javax.swing.*;

//main frame'den RawMaterial'a tıklayınca yeni frame açılır, frame burada oluşturulur. GUI bileşeni, layout burada define edilir
//her butona özel listener var, burada tanımlılar
//Var olan üreticileri listeler, Yeni Üretici'ye basıldığında RawMaterialForm'u açar.


import java.awt.*;
import java.awt.event.*;

import GUI.MainFrame;
import User_Interaction.RawMaterialInterplay;
import Main.RawMaterialProducer;

public class RawMProducerList extends JFrame {
    private DefaultListModel<RawMaterialProducer> producerListModel;
    private JList<RawMaterialProducer> producerJList;

    public RawMProducerList() {
        setTitle("Raw Material Producers");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        producerListModel = new DefaultListModel<>();

        for (RawMaterialProducer rp : RawMaterialInterplay.getProducers()) {
            producerListModel.addElement(rp);
        }
        producerJList = new JList<>(producerListModel);
        JScrollPane scrollPane = new JScrollPane(producerJList);
        producerJList.setBackground(new Color(200,255,255));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(155,225, 175));
        JButton openButton = new JButton("Open");
        openButton.setBackground(new Color(155,225, 175));
        JButton registerButton = new JButton("Register New Producer");
        registerButton.setBackground(new Color(155,225, 175));
        buttonPanel.setBackground(new Color(155, 225, 175));
        JButton backButton=new JButton("Back");

        backButton.setBackground(new Color(155, 225, 175));
        buttonPanel.add(openButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        setUndecorated(true);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RawMaterialProducer selected = producerJList.getSelectedValue();
                if (selected != null) {
                    new RawMProducerDetail(selected).setVisible(true);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {dispose();
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
