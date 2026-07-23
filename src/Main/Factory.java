package Main;

import java.util.ArrayList;
import java.util.List;


//BusinessEntity'den kapasite, fon, envanter yönetimini devralır.
//Producer Interface'inden produce override eder


public class Factory extends BusinessEntity implements Producer {
    private List<ManufacturedProduct> manufacturedProducts;
    //Fabrikanın tasarım reçetesini tutar. 

    public Factory(String factoryName, int maxCapacity, double initialFunds) {
        super(factoryName, maxCapacity, initialFunds);
        this.manufacturedProducts = new ArrayList<>();
    }
    
    //Fabrikanın hangi ürünleri üretebileceği bilgisi manufacturedProducts'ta tutulur, “Ben bu fabrikayım, şu ürünleri üretebilirim” demenin yolu.
    //Üretim reçetelerini tutar, aynı zamanda üretebileceği product'u tutar


    public List<ManufacturedProduct> getDesigns() {
        return manufacturedProducts;
    }

    //reçeteyi burada ekler
    public void addDesign(String productName, double productPrice, String byproductName, int byproductAmount, double byproductCost, List<ManufacturedProduct.InputRequirement> inputs) throws InvalidInputException {
        if (productName == null || productName.trim().isEmpty()) {
            throw new InvalidInputException("Type the name");
        }
        if (productPrice < 0) {
            throw new InvalidInputException("Type it positive");
        }
        if (byproductAmount < 0 || byproductCost < 0) {
            throw new InvalidInputException("Type it positive");
        }

        ManufacturedProduct manufacturedProduct = new ManufacturedProduct(productName, productPrice, byproductName, byproductAmount, byproductCost);
        if (inputs != null) {
            for (ManufacturedProduct.InputRequirement req : inputs) {
                manufacturedProduct.addInputRequirement(req.itemName, req.amount);
            }
        }
        manufacturedProducts.add(manufacturedProduct);
    }
    
    //Reçetesi girilen product'ları aslında burada produce edebiliriz
    public void produceProduct(ManufacturedProduct manufacturedProduct, int quantity) throws InsufficientResourcesException, InsufficientStorageException, InvalidInputException {
        if (quantity <= 0) {
            throw new InvalidInputException("Type it positive");
        }

        if (!manufacturedProducts.contains(manufacturedProduct)) {
            throw new InvalidInputException("There is no design ");
        }

        int totalOutputUnits = quantity;
        int totalByproductUnits = manufacturedProduct.getByproductAmount() * quantity;
        if (usedCapacity + totalOutputUnits + totalByproductUnits > maxCapacity) {
            throw new InsufficientStorageException("Not enough storage");
        }

        for (ManufacturedProduct.InputRequirement req : manufacturedProduct.getInputRequirements()) {
            Item inputItem = findItem(req.itemName);
            if (inputItem == null || inputItem.getQuantity() < req.amount * quantity) {
                throw new InsufficientResourcesException("Insufficient number");
            }
        }

        for (ManufacturedProduct.InputRequirement req : manufacturedProduct.getInputRequirements()) {
            removeFromInventory(req.itemName, req.amount * quantity);
        }

        Item existingOutput = findItem(manufacturedProduct.getProductName());
        if (existingOutput != null && existingOutput instanceof Product) {

            Product outputProduct = (Product) existingOutput;
            outputProduct.setQuantity(outputProduct.getQuantity() + quantity);
        } else {

            Product outputProduct = new Product(manufacturedProduct.getProductName(), quantity, manufacturedProduct.getProductPrice());
            inventory.add(outputProduct);
        }

        if (manufacturedProduct.getByproductName() != null && !manufacturedProduct.getByproductName().trim().isEmpty() && manufacturedProduct.getByproductAmount() > 0) {
            Item existingByp = findItem(manufacturedProduct.getByproductName());
            if (existingByp != null && existingByp instanceof Byproduct) {
                Byproduct byp = (Byproduct) existingByp;
                byp.setQuantity(byp.getQuantity() + totalByproductUnits);
            }

             else {
                Byproduct byp = new Byproduct(manufacturedProduct.getByproductName(), totalByproductUnits, manufacturedProduct.getByproductCost());
                inventory.add(byp);
            }
        }

        usedCapacity += totalOutputUnits + totalByproductUnits;
    }

    //byProduct'tan kurtulmak istiyorsak burada kurtulabiliriz
    public void disposeByproduct(String byproductName, int quantity) throws InsufficientResourcesException, InsufficientFundsException, InvalidInputException {
        if (quantity <= 0) {
            throw new InvalidInputException("Type it positive");
        }
        Item item = findItem(byproductName);
        if (item == null || !(item instanceof Byproduct)) {
            throw new InsufficientResourcesException("Not found");
        }
        Byproduct byp = (Byproduct) item;
        if (byp.getQuantity() < quantity) {
            throw new InsufficientResourcesException("Not enough quantity");
        }
        double cost = byp.getDisposalCost() * quantity;
        if (cost > funds) {
            throw new InsufficientFundsException("Not enough fund");
        }
        funds -= cost;
        removeFromInventory(byproductName, quantity);
    }
    
    
    //Producer interface’inin metodunu implement eder.
    //ProductName ile eşleşen tasarımı bulur ve produceProduct metodunu çağırır.

    @Override
    public void produce(String productName, int quantity) throws InsufficientResourcesException, InsufficientStorageException, InvalidInputException {
        ManufacturedProduct targetManufacturedProduct = null;
        for (ManufacturedProduct d : manufacturedProducts) {
            if (d.getProductName().equalsIgnoreCase(productName)) {
                targetManufacturedProduct = d;
                break;
            }
        }
        if (targetManufacturedProduct == null) {
            throw new InvalidInputException("There is no design");
        }
        produceProduct(targetManufacturedProduct, quantity);
    }
}

