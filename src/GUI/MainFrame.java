package GUI;

import GUI.Customer.CList;
import GUI.Factory.FList;
import GUI.Market.MList;
import GUI.RawMaterial.RawMProducerList;
import GUI.Utils.*;

import Main.DBHelper;
import GUI.util.SwingWorkers;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import User_Interaction.FactoryInterplay;
import User_Interaction.MarketInterplay;
import User_Interaction.RawMaterialInterplay;

public class MainFrame extends JFrame {
    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setUndecorated(false);

        getContentPane().setBackground(ModernColors.BACKGROUND);

        // Modern buttons with icons
        ModernButton producerButton = new ModernButton(Icons.PRODUCER + "Raw Materials", ModernColors.SUCCESS);
        ModernButton factoryButton = new ModernButton(Icons.FACTORY + "Factories", ModernColors.PRIMARY);
        ModernButton marketButton = new ModernButton(Icons.MARKET + "Markets", ModernColors.INFO);
        ModernButton customerButton = new ModernButton(Icons.CUSTOMER + "Customers", ModernColors.PRIMARY_LIGHT);
        ModernButton exitButton = new ModernButton(Icons.EXIT + "Exit", ModernColors.DANGER);
        ModernButton saveButton = new ModernButton(Icons.SAVE + "Save All", ModernColors.ACCENT);
        ModernButton loadButton = new ModernButton(Icons.LOAD + "Load", ModernColors.ACCENT);

        setLayout(new BorderLayout());

        // Modern header
        JPanel topPanel = new ModernPanel(ModernColors.PRIMARY);
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel(Icons.HOME + "Supply Chain Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.WEST);

        JPanel exitPanel = new JPanel();
        exitPanel.setOpaque(false);
        exitPanel.add(saveButton);
        exitPanel.add(loadButton);
        exitPanel.add(exitButton);
        topPanel.add(exitPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Modern content panel
        ModernPanel contentPanel = new ModernPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Set button sizes
        Dimension buttonSize = new Dimension(200, 60);
        producerButton.setPreferredSize(buttonSize);
        factoryButton.setPreferredSize(buttonSize);
        marketButton.setPreferredSize(buttonSize);
        customerButton.setPreferredSize(buttonSize);

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(producerButton, gbc);

        gbc.gridy = 1;
        contentPanel.add(factoryButton, gbc);

        gbc.gridy = 2;
        contentPanel.add(marketButton, gbc);

        gbc.gridy = 3;
        contentPanel.add(customerButton, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // Event listeners
        producerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RawMProducerList().setVisible(true);
            }
        });
        factoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FList().setVisible(true);
            }
        });
        marketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MList().setVisible(true);
            }
        });
        customerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CList().setVisible(true);
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Save and Load actions use background workers so EDT isn't blocked
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveButton.setEnabled(false);
                SwingWorkers.run(new java.util.concurrent.Callable<Integer>() {
                    public Integer call() throws Exception {
                        int saved = 0;
                        for (Main.Factory f : FactoryInterplay.getFactories()) {
                            DBHelper.saveEntityWithType("Factory", f);
                            saved++;
                        }
                        for (Main.Market m : MarketInterplay.getMarkets()) {
                            DBHelper.saveEntityWithType("Market", m);
                            saved++;
                        }
                        for (Main.RawMaterialProducer r : RawMaterialInterplay.getProducers()) {
                            DBHelper.saveEntityWithType("RawMaterialProducer", r);
                            saved++;
                        }
                        return saved;
                    }
                }, new java.util.function.Consumer<Integer>() {
                    public void accept(Integer result) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Saved " + result + " entities.");
                        saveButton.setEnabled(true);
                    }
                }, new java.util.function.Consumer<Exception>() {
                    public void accept(Exception ex) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Save failed: " + ex.getMessage());
                        saveButton.setEnabled(true);
                    }
                });
            }
        });

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadButton.setEnabled(false);
                SwingWorkers.run(new java.util.concurrent.Callable<int[]>() {
                    public int[] call() throws Exception {
                        java.util.List<Main.BusinessEntity> factories = DBHelper.loadEntitiesByType("Factory");
                        java.util.List<Main.BusinessEntity> markets = DBHelper.loadEntitiesByType("Market");
                        java.util.List<Main.BusinessEntity> producers = DBHelper.loadEntitiesByType("RawMaterialProducer");
                        java.util.List<Main.Factory> f2 = new java.util.ArrayList<Main.Factory>();
                        for (Main.BusinessEntity be : factories) if (be instanceof Main.Factory) f2.add((Main.Factory) be);
                        java.util.List<Main.Market> m2 = new java.util.ArrayList<Main.Market>();
                        for (Main.BusinessEntity be : markets) if (be instanceof Main.Market) m2.add((Main.Market) be);
                        java.util.List<Main.RawMaterialProducer> r2 = new java.util.ArrayList<Main.RawMaterialProducer>();
                        for (Main.BusinessEntity be : producers) if (be instanceof Main.RawMaterialProducer) r2.add((Main.RawMaterialProducer) be);
                        FactoryInterplay.replaceFactories(f2);
                        MarketInterplay.replaceMarkets(m2);
                        RawMaterialInterplay.replaceProducers(r2);
                        return new int[] {f2.size(), m2.size(), r2.size()};
                    }
                }, new java.util.function.Consumer<int[]>() {
                    public void accept(int[] res) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Loaded: factories=" + res[0] + " markets=" + res[1] + " producers=" + res[2]);
                        loadButton.setEnabled(true);
                    }
                }, new java.util.function.Consumer<Exception>() {
                    public void accept(Exception ex) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Load failed: " + ex.getMessage());
                        loadButton.setEnabled(true);
                    }
                });
            }
        });
    }

    public static void main(String[] args) {
            // Initialize SQLite database
            DBHelper.initDatabase();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                }
            });
        }
}
