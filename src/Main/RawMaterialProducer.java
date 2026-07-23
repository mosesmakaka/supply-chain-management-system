package Main;

//Producer interface'ini produce etmek için kullanıyoruz ve productname, quantity gibi attribute'ların özellikleri ve yetersiz olma durumundaki...
//ExceptionHandling'leri Producer interface'inden yapıyoruz.

//Aslında bu sınıf biraz da businessEntity'de rawmaterial nesnesini arraylist'te oluşturmak için var

public class RawMaterialProducer extends BusinessEntity implements Producer {
    private String materialName;
    private double generationCost;
    private double sellingPrice;

    //Hammaddenin özelliklerini giriyoruz, ilk kez RawMaterial oluşturacağız
    //super'a gönderdikleri entity'nin özellikleri
    //hammaddenin ismi, üretim maaliyetleri ve sellingprice'ını burada belirleriz
    //generationcost ve sellingprice aslında rawmaterial için de vardı ama burada RawMaterialProducer'da üretim sırasında maaliyeti almak için var
    //RawMaterial'daki aslında RawMaterial nesnesinin cost'unu en başta belirlemek için var, sonraki üretim için değil
    
    public RawMaterialProducer(String producerName, String materialName, double generationCost, double sellingPrice, int maxCapacity, double initialFunds) {
        super(producerName, maxCapacity, initialFunds);
        this.materialName = materialName;
        this.generationCost = generationCost;
        this.sellingPrice = sellingPrice;

    }
    //getter ve setter'lar
    public String getMaterialName() {
        return materialName;
    }

    public double getGenerationCost() {
        return generationCost;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }
    
    //3 Exception sınıfına gönderme yapıyoruz, 3 tane hata alma ihtimalimiz var
    public void generateMaterial(int amount) throws InsufficientFundsException, InsufficientStorageException, InvalidInputException {
        if (amount <= 0) {
            throw new InvalidInputException("Type positive number");
        }
        double totalCost = amount * generationCost;
        if (totalCost > funds) {
            throw new InsufficientFundsException("Not enough funds");
        }
        if (usedCapacity + amount > maxCapacity) {
            throw new InsufficientStorageException("Not enough storage");
        }
        //Harcama değişiyor
        funds -= totalCost;

        //FindItem çağrısı BusinessEntity'den geliyor, Item'da hem RawMaterial hem de Product saklıyor
        //Burada downcasting yapıyoruz, hammadde sınıfına özgü belirli metodları kullanabilmek için
        //Yoksa ve yeni material'sa devam ediyor eklemeye
        RawMaterial materialItem = (RawMaterial) findItem(materialName);
        if (materialItem != null) {
            materialItem.setQuantity(materialItem.getQuantity() + amount);
        } else {
            materialItem = new RawMaterial(materialName, amount, generationCost, sellingPrice);
            inventory.add(materialItem);
        }
        usedCapacity += amount;
    }

    //Producer'dan method override ediyor
    public void produce(String productName, int quantity) throws InsufficientFundsException, InsufficientStorageException, InvalidInputException {

        if (productName != null && !productName.equalsIgnoreCase(materialName)) {
            throw new InvalidInputException("This producer can only produce " + materialName + ".");
        }
        generateMaterial(quantity);
    }
}

