package User_Interaction;

import Main.*;
import java.util.ArrayList;
import java.util.List;

public class MarketInterplay {

    private static List<Market> markets = new ArrayList<>();

    public static List<Market> getMarkets() {
        return markets;
    }

    public static Market registerMarket(String name, String capacityStr, String initialFundsStr) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Write the name");
        }
        int capacity;
        double initialFunds;
        try {
            capacity = Integer.parseInt(capacityStr);
            initialFunds = Double.parseDouble(initialFundsStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Type valid numbers");
        }
        if (capacity < 0 || initialFunds < 0) {
            throw new InvalidInputException("Type positive numbers");
        }
        Market market = new Market(name.trim(), capacity, initialFunds);
        markets.add(market);
        return market;
    }

    public static List<FactoryInterplay.AvailableItem> listAvailableItemsForPurchase(Market buyer) {
        List<FactoryInterplay.AvailableItem> available = new ArrayList<>();

        for (Factory sellerFactory : FactoryInterplay.getFactories()) {
            for (Item item : sellerFactory.getInventory()) {
                if (item.getQuantity() <= 0) continue;

                if (item instanceof Product && !(item instanceof Byproduct)) {
                    Product prod = (Product) item;
                    available.add(new FactoryInterplay.AvailableItem(
                            sellerFactory, prod.getName(), prod.getPrice(), prod.getQuantity()));
                }
                else if (item instanceof RawMaterial) {
                    RawMaterial rm = (RawMaterial) item;
                    available.add(new FactoryInterplay.AvailableItem(
                            sellerFactory, rm.getName(), rm.getSellingPrice(), rm.getQuantity()));
                }
            }
        }

        for (Market sellerMarket : markets) {
            if (sellerMarket == buyer) continue;
            for (Item item : sellerMarket.getInventory()) {
                if (item.getQuantity() > 0 && item instanceof Product) {
                    Product prod = (Product) item;
                    available.add(new FactoryInterplay.AvailableItem(
                            sellerMarket, prod.getName(), prod.getPrice(), prod.getQuantity()));
                }
            }
        }

        return available;
    }

    public static void buyItem(
            Market buyer,
            FactoryInterplay.AvailableItem item,
            String quantityStr
    ) throws InvalidInputException, InsufficientFundsException,
            InsufficientStorageException, InsufficientResourcesException {

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Write positive integer");
        }
        if (quantity <= 0) {
            throw new InvalidInputException("Write positive integer");
        }
        if (quantity > item.availableQuantity) {
            throw new InsufficientResourcesException("Exceeding");
        }
        double totalCost = item.price * quantity;
        if (totalCost > buyer.getFunds()) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        if (buyer.getUsedCapacity() + quantity > buyer.getMaxCapacity()) {
            throw new InsufficientStorageException("Not enough storage");
        }

        buyer.decreaseFunds(totalCost);
        BusinessEntity sellerEntity = (BusinessEntity) item.seller;
        sellerEntity.increaseFunds(totalCost);


        boolean isRaw = false;
        RawMaterial sourceRm = null;
        if (sellerEntity instanceof Factory) {
            for (Item it : ((Factory) sellerEntity).getInventory()) {
                if (it instanceof RawMaterial && it.getName().equals(item.itemName)) {
                    isRaw = true;
                    sourceRm = (RawMaterial) it;
                    break;
                }
            }
        }

        sellerEntity.removeFromInventory(item.itemName, quantity);

        Item newItem;
        if (isRaw && sourceRm != null) {
            newItem = new RawMaterial(
                    item.itemName,
                    quantity,
                    sourceRm.getGenerationCost(),
                    sourceRm.getSellingPrice()
            );
        } else {
            newItem = new Product(item.itemName, quantity, item.price);
        }
        buyer.addToInventory(newItem);
    }

    public static void setPrice(Market market, String productName, String priceStr) throws InvalidInputException {
        double newPrice;
        try {
            newPrice = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Type a number");
        }
        market.setProductPrice(productName, newPrice);
    }
}
