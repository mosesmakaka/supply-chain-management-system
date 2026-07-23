package GUI.RawMaterial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Main.RawMaterialProducer;
import Main.InsufficientFundsException;
import Main.InsufficientStorageException;
import Main.InvalidInputException;
import User_Interaction.RawMaterialInterplay;


//Listede üretilen halihazırda tek bir rawmaterial producer'ının güncel bakiyesini, depo durumunu vesaire gösterir	


public class RawMProducerDetail extends JFrame {
    private RawMaterialProducer producer;
    private JLabel fundsLabel;
    private JLabel storageLabel;
    private JTextField amountField;

    public RawMProducerDetail(RawMaterialProducer producer) {
        this.producer = producer;
        setTitle("Producer: " + producer.getName());
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 5, 5));


        add(new JLabel("Funds:"));
        fundsLabel = new JLabel(String.format("%.2f", producer.getFunds()));
        add(fundsLabel);

        add(new JLabel("Storage (used / capacity):"));
        storageLabel = new JLabel(producer.getUsedCapacity() + " / " + producer.getMaxCapacity());
        add(storageLabel);

        add(new JLabel("Generate Amount:"));
        amountField = new JTextField();
        add(amountField);

        JButton generateButton = new JButton("Generate");
        add(generateButton);
        add(new JLabel());

        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    RawMaterialInterplay.generateMaterials(producer, amountField.getText().trim());

                    fundsLabel.setText(String.format("%.2f", producer.getFunds()));
                    storageLabel.setText(producer.getUsedCapacity() + " / " + producer.getMaxCapacity());
                    JOptionPane.showMessageDialog(RawMProducerDetail.this, "Material generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (InvalidInputException | InsufficientFundsException | InsufficientStorageException ex) {

                    JOptionPane.showMessageDialog(RawMProducerDetail.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

