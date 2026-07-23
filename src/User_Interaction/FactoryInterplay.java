package User_Interaction;

import Main.*;
import java.util.ArrayList;
import java.util.List;


public class FactoryInterplay {
	//GUI.Factory paketi ile Main.Factory (model) arasındaki tüm veri alış-verişini yönetmek.

    //“Tasarım ekle/Güncelle”, “Üret”, “Yan ürün bertaraf et” eylemlerini alıp Factory modeline iletip sonucu GUI’ya yansıtmak.
	
    //tüm factories nesnelerini tutar
    private static List<Factory> factories = new ArrayList<>();
    static {
        try {
            // load persisted factories from DB if present
            for (BusinessEntity e : DBHelper.loadEntitiesByType("Factory")) {
                factories.add((Factory) e);
            }
        } catch (Exception ex) {
            // ignore DB load errors for now
            ex.printStackTrace();
        }
    }

    // kullanıcı “hangi ürünü üreteceksin?” sorusuna, mevcut tanımlı ürünleri göstermek için productTypes’ı 
    private static List<String> productTypes = new ArrayList<>();

    public static List<Factory> getFactories() {
        return factories;
    }

    public static List<String> getProductTypes() {
        return productTypes;
    }

    //birden fazla factory girmemizi sağlar
    public static Factory registerFactory(String name, String capacityStr, String initialFundsStr) throws InvalidInputException {
        if (name == null) {
            throw new InvalidInputException("Write the name");
        }
        int capacity;
        double initialFunds;
        try {
            capacity = Integer.parseInt(capacityStr);
            initialFunds = Double.parseDouble(initialFundsStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Type Valid Numbers");
        }
        if (capacity < 0 || initialFunds < 0) {
            throw new InvalidInputException("Type positive Numbers");
        }
        Factory factory = new Factory(name.trim(), capacity, initialFunds);
        factories.add(factory);
        try {
            DBHelper.saveEntityWithType("Factory", factory);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return factory;
    }


    public static List<AvailableItem> listAvailableItemsForPurchase(Factory buyer) {
        List<AvailableItem> available = new ArrayList<>();

        for (RawMaterialProducer producer : RawMaterialInterplay.getProducers()) {

            for (Item item : producer.getInventory()) {
                if (item.getQuantity() > 0) {

                    if (item instanceof RawMaterial) {
                        RawMaterial raw = (RawMaterial) item;
                        available.add(new AvailableItem(producer, raw.getName(), raw.getSellingPrice(), raw.getQuantity()));
                    }
                }
            }
        }

        for (Factory sellerFactory : factories) {
            if (sellerFactory == buyer) continue;
            for (Item item : sellerFactory.getInventory()) {
                if (item.getQuantity() > 0) {

                    if (item instanceof Product && !(item instanceof Byproduct)) {
                        Product prod = (Product) item;
                        available.add(new AvailableItem(sellerFactory, prod.getName(), prod.getPrice(), prod.getQuantity()));
                    }
                }
            }
        }

        for (Market market : MarketInterplay.getMarkets()) {
            for (Item item : market.getInventory()) {
                if (item.getQuantity() > 0) {

                    if (item instanceof Product) {
                        Product prod = (Product) item;
                        available.add(new AvailableItem(market, prod.getName(), prod.getPrice(), prod.getQuantity()));
                    }
                }
            }
        }
        return available;
    }


    public static void buyItem(Factory buyer, AvailableItem item, String quantityStr) throws InvalidInputException, InsufficientFundsException, InsufficientStorageException, InsufficientResourcesException {
        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Type positive numbers");
        }
        if (quantity <= 0) {
            throw new InvalidInputException("Type positive numbers");
        }
        if (quantity > item.availableQuantity) {
            throw new InsufficientResourcesException("Big request");
        }
        double totalCost = item.price * quantity;
        if (totalCost > buyer.getFunds()) {
            throw new InsufficientFundsException("Not enough funds");
        }
        if (buyer.getUsedCapacity() + quantity > buyer.getMaxCapacity()) {
            throw new InsufficientStorageException("Not enough storage");
        }

        buyer.decreaseFunds(totalCost);
        if (item.seller instanceof BusinessEntity) {
            ((BusinessEntity) item.seller).increaseFunds(totalCost);
        }

        if (item.seller instanceof BusinessEntity) {
            BusinessEntity sellerEntity = (BusinessEntity) item.seller;
            sellerEntity.removeFromInventory(item.itemName, quantity);
        }

        Item newItem;
        if (item.seller instanceof RawMaterialProducer) {

            RawMaterialProducer producerSeller = (RawMaterialProducer) item.seller;
            newItem = new RawMaterial(item.itemName, quantity, producerSeller.getGenerationCost(), producerSeller.getSellingPrice());
        } else {

            newItem = new Product(item.itemName, quantity, item.price);
        }
        buyer.addToInventory(newItem);
    }


    public static void produceProducts(Factory factory, String productName, String quantityStr) throws InvalidInputException, InsufficientResourcesException, InsufficientStorageException {
        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Type positive quantity");
        }
        factory.produce(productName, quantity);
    }


    public static void createDesign(Factory factory, String productName, String priceStr, String byproductName, String byproductAmountStr, String byproductCostStr, List<ManufacturedProduct.InputRequirement> inputs) throws InvalidInputException {
        double price, bypCost;
        int bypAmount;
        try {
            price = Double.parseDouble(priceStr);
            bypAmount = (byproductAmountStr == null || byproductAmountStr.isEmpty()) ? 0 : Integer.parseInt(byproductAmountStr);
            bypCost = (byproductCostStr == null || byproductCostStr.isEmpty()) ? 0 : Double.parseDouble(byproductCostStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Type numeric numbers");
        }
        String bpName = (byproductName == null ? "" : byproductName.trim());
        factory.addDesign(productName.trim(), price, bpName, bypAmount, bypCost, inputs);

        if (!productTypes.contains(productName.trim())) {
            productTypes.add(productName.trim());
        }
    }


    public static void disposeByproduct(Factory factory, String byproductName, String quantityStr) throws InvalidInputException, InsufficientResourcesException, InsufficientFundsException {
        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Type positive numbers");
        }
        factory.disposeByproduct(byproductName, quantity);
    }


    public static class AvailableItem {
        public Object seller;
        public String itemName;
        public double price;
        public int availableQuantity;
        public AvailableItem(Object seller, String itemName, double price, int availableQuantity) {
            this.seller = seller;
            this.itemName = itemName;
            this.price = price;
            this.availableQuantity = availableQuantity;
        }
        @Override
        public String toString() {

            String sellerName = (seller instanceof BusinessEntity ? ((BusinessEntity) seller).getName() : seller.toString());
            return itemName + " (Seller: " + sellerName + ", Price: " + price + ", Available: " + availableQuantity + ")";
        }
    }
}
