package GUI.Factory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import User_Interaction.FactoryInterplay;
import Main.Factory;
import Main.Byproduct;
import Main.InsufficientFundsException;
import Main.InsufficientResourcesException;
import Main.InvalidInputException;


public class FDispose extends JFrame {
    private Factory factory;
    private DefaultListModel<Byproduct> byproductListModel;
    private JList<Byproduct> byproductJList;
    private JTextField amountField;

    public FDispose(Factory factory) {
        this.factory = factory;
        setTitle(factory.getName() + " - Dispose Byproducts");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        byproductListModel = new DefaultListModel<>();
        loadByproducts();

        byproductJList = new JList<>(byproductListModel);
        JScrollPane scrollPane = new JScrollPane(byproductJList);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Amount:"));
        amountField = new JTextField(5);
        bottomPanel.add(amountField);
        JButton disposeButton = new JButton("Dispose");
        bottomPanel.add(disposeButton);

        add(new JLabel("Byproducts in storage:"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        disposeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Byproduct selected = byproductJList.getSelectedValue();
                if (selected == null) return;
                try {
                    FactoryInterplay.disposeByproduct(factory, selected.getName(), amountField.getText().trim());
                    JOptionPane.showMessageDialog(FDispose.this, "Disposal completed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadByproducts();
                } catch (InvalidInputException | InsufficientResourcesException | InsufficientFundsException ex) {
                    JOptionPane.showMessageDialog(FDispose.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    private void loadByproducts() {
        if (byproductListModel == null) return;
        byproductListModel.clear();
        for (Main.Item item : factory.getInventory()) {
            if (item instanceof Byproduct) {
                Byproduct bp = (Byproduct) item;
                if (bp.getQuantity() > 0) {
                    byproductListModel.addElement(bp);
                }
            }
        }
    }
}

