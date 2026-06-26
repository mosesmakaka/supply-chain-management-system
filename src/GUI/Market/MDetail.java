package GUI.Market;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Main.Market;


public class MDetail extends JFrame {
    private Market market;
    private JLabel fundsLabel;

    public MDetail(Market market) {
        this.market = market;
        setTitle("Market: " + market.getName());
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 5, 5));

        fundsLabel = new JLabel("Funds: " + String.format("%.2f", market.getFunds()));
        add(fundsLabel);

        JButton buyButton = new JButton("Buy Products");
        JButton setPriceButton = new JButton("Set Product Prices");
        add(buyButton);
        add(setPriceButton);

        buyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MBuy(market).setVisible(true);
            }
        });
        setPriceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MSetPrice(market).setVisible(true);
            }
        });
    }
}
