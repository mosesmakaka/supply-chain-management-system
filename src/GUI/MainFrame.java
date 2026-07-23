package GUI;

import GUI.Customer.CList;
import GUI.Factory.FList;
import GUI.Market.MList;
import GUI.RawMaterial.RawMProducerList;

import Main.DBHelper;
import GUI.util.SwingWorkers;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import User_Interaction.FactoryInterplay;
import User_Interaction.MarketInterplay;
import User_Interaction.RawMaterialInterplay;

public class MainFrame extends JFrame {
    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setUndecorated(true);

        getContentPane().setBackground(new Color(220, 230, 241));

        JButton producerButton = new JButton("Raw Material ");
        producerButton.setBackground(new Color(155, 225, 175));
        JButton factoryButton = new JButton("Factories");
        factoryButton.setBackground(new Color(155, 225, 175));
        JButton marketButton = new JButton("Markets");
        marketButton.setBackground(new Color(155, 225, 175));
        JButton customerButton = new JButton("Customers");
        customerButton.setBackground(new Color(155, 225, 175));
        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(255, 179, 179));

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(220, 230, 241));
        GridBagConstraints tc = new GridBagConstraints();
        tc.insets = new Insets(4,6,4,6);
        tc.gridx = 0; tc.gridy = 0; tc.weightx = 1.0; tc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Supply Chain Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        topPanel.add(titleLabel, tc);

        tc.gridx = 1; tc.weightx = 0; tc.anchor = GridBagConstraints.EAST;
        JButton saveButton = new JButton("Save All");
        saveButton.setBackground(new Color(200, 230, 200));
        topPanel.add(saveButton, tc);

        tc.gridx = 2; JButton loadButton = new JButton("Load");
        loadButton.setBackground(new Color(200, 230, 200));
        topPanel.add(loadButton, tc);

        tc.gridx = 3; topPanel.add(exitButton, tc);
        add(topPanel, BorderLayout.NORTH);

        JPanel defaultPanel = new JPanel(new GridBagLayout());
        defaultPanel.setBackground(new Color(155, 225, 175));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,10,6,10);
        c.gridx = 0; c.gridy = 0; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1.0;
        defaultPanel.add(producerButton, c);
        c.gridy = 1; defaultPanel.add(factoryButton, c);
        c.gridy = 2; defaultPanel.add(marketButton, c);
        c.gridy = 3; defaultPanel.add(customerButton, c);
        add(defaultPanel, BorderLayout.CENTER);

        // Save and Load actions use background workers so EDT isn't blocked
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveButton.setEnabled(false);
                SwingWorkers.run(() -> {
                    // persist all BusinessEntity-backed lists
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
                }, (Integer result) -> {
                    JOptionPane.showMessageDialog(MainFrame.this, "Saved " + result + " entities.");
                    saveButton.setEnabled(true);
                }, (Exception ex) -> {
                    JOptionPane.showMessageDialog(MainFrame.this, "Save failed: " + ex.getMessage());
                    saveButton.setEnabled(true);
                });
            }
        });

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadButton.setEnabled(false);
                SwingWorkers.run(() -> {
                    java.util.List<Main.BusinessEntity> factories = DBHelper.loadEntitiesByType("Factory");
                    java.util.List<Main.BusinessEntity> markets = DBHelper.loadEntitiesByType("Market");
                    java.util.List<Main.BusinessEntity> producers = DBHelper.loadEntitiesByType("RawMaterialProducer");
                    // Convert typed lists
                    java.util.List<Main.Factory> f2 = new java.util.ArrayList<>();
                    for (Main.BusinessEntity be : factories) if (be instanceof Main.Factory) f2.add((Main.Factory) be);
                    java.util.List<Main.Market> m2 = new java.util.ArrayList<>();
                    for (Main.BusinessEntity be : markets) if (be instanceof Main.Market) m2.add((Main.Market) be);
                    java.util.List<Main.RawMaterialProducer> r2 = new java.util.ArrayList<>();
                    for (Main.BusinessEntity be : producers) if (be instanceof Main.RawMaterialProducer) r2.add((Main.RawMaterialProducer) be);
                    FactoryInterplay.replaceFactories(f2);
                    MarketInterplay.replaceMarkets(m2);
                    RawMaterialInterplay.replaceProducers(r2);
                    return new int[] {f2.size(), m2.size(), r2.size()};
                }, (int[] res) -> {
                    JOptionPane.showMessageDialog(MainFrame.this, "Loaded: factories=" + res[0] + " markets=" + res[1] + " producers=" + res[2]);
                    loadButton.setEnabled(true);
                }, (Exception ex) -> {
                    JOptionPane.showMessageDialog(MainFrame.this, "Load failed: " + ex.getMessage());
                    loadButton.setEnabled(true);
                });
            }
        });

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











/*Inheritance, BusinessEntity ve Item sınıfları super class, bağlı olanlar subclass, BusinessEntity'de ortak entity'lerin attribute'larını
//	depolamaya, envanteki product'ları depolamaya yarar, Item'da ise ortak attribute quantity ve name'i constructor'unda barındırır,
//bu sayede üst sınfın constructor'unu kullanabiliriz

//Polymorphism, özellikle interplay'lerde downcasting yapıyoruz. Market ve Factory'de de yapıyoruz (özellikle marketinterplayde 97. satır),  amaç BusinessEntity ve Item 
//superclasslardaki metodlara ve alanlara erişebilmek, "sellerEntity instanceof Factory" ve "it instanceof RawMaterial" örnekleri (rawmaterial producer, 57. satır)


//Abstract classes, BusinessEntity ve Item sınıfları abstract class'lar, abstract classtan nesne oluşturmak mümkün değil soyut sınıflar,
//bir nesne ve attribute temsil etmiyorlar tek başına direkt onlardan nesne oluşturmak istemeyiz bu nedenle abstract olarak tanımlanmışlar
//aynı zamanda da bazı attribute'lar ortak, superclass'ın constructorını kullanabilmemizi sağlar


//Interfaces: Producer sınıfı interface'imiz, burada produce metoduna erişip bütün 4 tane Exception'a erişmeyi sağlıyor, bu sayede 
//exceptionhandling'e takılıyor



//Exception Handling, 4 tane ana exceptionhandling sınıfımız var, ekstra producer void produce() metodunda 4 tane sınıfa atıfta bulunuyor


//GUI, her arayüz için ayrı frame açılıyor, her sınıfın ayrı package'ları var, genel olarak detail form ve list sınıfları içeriyor hepsi


//MVC pattern,kullanıcı arayüzü ve iş mantığını birbirinden ayırarak üç bileşene bölünmüş bir mimaridir:
//model Uygulamanın “veri”sini ve “iş kuralları”nı tutar. view kullanıcıya gösterilen rakamlar, tablolar ekranlar ve  
// Controller ise Model ile View arasında köprü olur, kullanıcı eylemlerini yakalar ve uygun main metodlarını çağırır, sonucu GUI'ye yansıtır




//good programming style. private değişkenler, constructorlar, farklı package kullanımları gibi


//Controllerlar ne yapar?
///View (GUI) ile Model arasındaki köprü
///Girdi Parse & Doğrulama :
///GUI’den gelen tüm String girdileri (miktar, fiyat, isim, kapasite, bakiye) uygun tiplere (int, double) dönüştürür.
///Exception Handling & Geri Bildirim
///Özet Akış (ör. FactoryInterplay)
View: Kullanıcı “Buy” butonuna tıklar.

Controller (FactoryInterplay.buyItem):

String quantity → int parse, pozitif kontrolü.

buyer.decreaseFunds(), seller.increaseFunds()

removeFromInventory(), addToInventory()

Model: BusinessEntity alt sınıflarının iş kurallarını uygular.

Controller: Başarı/hata durumuna göre GUI’yı günceller veya hata mesajı gösterir.

Tüm User_Interaction sınıfları bu MVC prensibini takip ederek View–Model etkileşimini yönetir ve uygulamanın iş akışını koordine eder.
///
///lambda kodu kısaltır(özellikle actionlistener komutlarını)
///equalsIgnoreCase büyük küçük harfi ignore etmemizi sağlar
///trim boşlukları temizlemek için kullanılır
///parseInt() veya parseDouble() String'ten int veya double'a dönüştürmeyi sağlar
///String.format() ondalık sayıları belirli bir biçimde ekrana basmayı sağlar
/// */


