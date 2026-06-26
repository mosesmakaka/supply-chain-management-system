package User_Interaction;

import Main.*;
import java.util.ArrayList;
import java.util.List;
//User_Interaction'lar nesneleri yönetir.

//Interplay’ler, GUI ile Model arasındaki tüm veri akışını ve iş mantığı çağrılarını koordine eden “ara katman”lardır.
//Kullanıcı eylemlerini (View) alıp, uygulama içi iş kurallarını (Model) devreye sokar ve sonucu tekrar ekrana (View) yansıtır.
//Kısacası interplayler, kullanıcıdan gelen string ve nesneleri gerekli attribute'lara dönüştürür, geçersiz girişlerde invalidinput fırlatır,
//inputu main'e gönderir, anlamlı sonuç alıp tekrardan gui'ye gönderir



//DOWNCASTINGLER INTERPLAY'LERDE BULUNUR 

public class CustomerInterplay {

    private static List<Customer> customers = new ArrayList<>();

    public static List<Customer> getCustomers() {

        return customers;
    }
  
    public static Customer registerCustomer(String name, String initialMoneyStr) throws InvalidInputException {
        if (name == null ) {
            throw new InvalidInputException("Write the name");
        }
        double initialMoney;
        try {
            initialMoney = Double.parseDouble(initialMoneyStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Type a numeric value");
        }
        if (initialMoney < 0) {
            throw new InvalidInputException("Use positive numbers");
        }
        Customer customer = new Customer(name.trim(), initialMoney);
        customers.add(customer);
        return customer;
    }

  
    public static List<FactoryInterplay.AvailableItem> listAvailableProductsForShopping() {
        List<FactoryInterplay.AvailableItem> available = new ArrayList<>();
        for (Market market : MarketInterplay.getMarkets()) {
            for (Item item : market.getInventory()) {
                if (item.getQuantity() > 0 && item instanceof Product) {
                    Product prod = (Product) item;
                    available.add(new FactoryInterplay.AvailableItem(market, prod.getName(), prod.getPrice(), prod.getQuantity()));
                }
            }
        }
        return available;
    }


    public static void purchaseItem(Customer customer, FactoryInterplay.AvailableItem item, String quantityStr) throws InvalidInputException, InsufficientFundsException, InsufficientResourcesException {
        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Use positive numbers");
        }
        if (quantity <= 0) {
            throw new InvalidInputException("Use positive numbers");
        }
        if (quantity > item.availableQuantity) {
            throw new InsufficientResourcesException("Exceeding problem");
        }

        if (!(item.seller instanceof Market)) {
            throw new InvalidInputException("Wrong argument");
        }
        Market seller = (Market) item.seller;
        double totalCost = item.price * quantity;
        if (totalCost > customer.getMoney()) {
            throw new InsufficientFundsException("Not enough money");
        }



        customer.purchase(item.itemName, item.price, quantity);

        seller.increaseFunds(totalCost);

        seller.removeFromInventory(item.itemName, quantity);
    }
    public static void deleteCustomer(Customer c) {
        customers.remove(c);
    }

}
