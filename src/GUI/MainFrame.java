package GUI;

import GUI.Customer.CList;
import GUI.Factory.FList;
import GUI.Market.MList;
import GUI.RawMaterial.RawMProducerList;
import GUI.Utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

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
    }

    public static void main(String[] args) {
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


