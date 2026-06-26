package Main;

import java.util.ArrayList;
import java.util.List;

//müşterinin adını, sahip olduğu parayı ve satın aldığı ürünleri saklar, aynı zamanda purchasedItems kendi aldığı ürünleri tutar
//product nesnelerini purchasedItems'a ekler, 

//biraz daha bağımsız bir sınıf, businessEntity'i extend etmiyor
public class Customer {
    private String name;
    private double money;
    private List<Product> purchasedItems;

    public Customer(String name, double initialMoney) {
        this.name = name;
        this.money = initialMoney;
        this.purchasedItems = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }


    public void purchase(String itemName, double price, int quantity) throws InsufficientFundsException, InvalidInputException {
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity must be positive.");
        }
        double totalCost = price * quantity;
        if (totalCost > money) {
            throw new InsufficientFundsException("Insufficient funds to purchase.");
        }

        money -= totalCost;

        Product existing = null;
        for (Product p : purchasedItems) {
            if (p.getName().equalsIgnoreCase(itemName)) {
                existing = p;
                break;
            }
        }
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            // Store price as the price paid (for reference)
            Product bought = new Product(itemName, quantity, price);
            purchasedItems.add(bought);
        }
    }


    public List<Product> getPurchasedItems() {
        return purchasedItems;
    }

    @Override
    public String toString() {
        return name;
    }
}
