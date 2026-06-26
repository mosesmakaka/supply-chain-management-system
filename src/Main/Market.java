package Main;


//marketname capacity funds gibi attribute'ları businessentity'den alır.
//ekstra attribute'ları kendisi veriyor, productprice'ını kendisi belirliyor.

public class Market extends BusinessEntity {
    public Market(String marketName, int maxCapacity, double initialFunds) {
        super(marketName, maxCapacity, initialFunds);
    }

    public void setProductPrice(String productName, double newPrice) throws InvalidInputException {
        if (newPrice < 0) {
            throw new InvalidInputException("Type positive value");
        }
        Item item = findItem(productName);
        if (item == null || !(item instanceof Product)) {
            throw new InvalidInputException("Product not founded");
        }
        //findItem metodu genel Item referansı döndürdü, oysa fiyatı güncellemek için Product sınıfının setPrice() sınıfına erişmemiz
        //gerekiyordu, bu nedenle downcasting yaptık
        Product prod = (Product) item;
        prod.setPrice(newPrice);
    }
}
