package Main;

import java.util.ArrayList;
import java.util.List;

//Burada tamamen Müşteri bilgileri ve sınıfların sahip olduğu mallar saklanıyor, birbirleriyle etkileşimleri sonucu değişen envanterleri saklanıyor
//RawMaterialProducer, Factory, Market sınıfları burada inventory'e yeni nesne ekliyor
//Entity’nin adı (ör. fabrika ismi veya müşteri ismi, mevcut parası, max depo miktarı, şu anki depolaması, sahip olduğu item nesneleri(arraylistte)
//Ürünü olanlar datalarını buradan alıyor	
//Entity dediklerim businessman'ler işte



//burada businessman'ler aslında factory, market, rawmaterialproducer gibi sınıflarda var, onlar buradan ortak 
//attributeları alıyor ve bu sınıfı extend ediyor
public abstract class BusinessEntity {
    protected String name;
    protected double funds;
    protected int maxCapacity;
    protected int usedCapacity;
    protected List<Item> inventory;

    //Buradaki Inventory ArrayList'in amacı çok fazla: ilk olarak türü Item, yani Item attribute'larını barındırıyor
    //RawMaterialProducer ürettiği hammaddeleri new Rawmaterial(...) nesnesiyle bu listeye ekler
    //Factory üretim sonucunda elde ettiği ürünlerini ve Market, Factory'den aldığı ürünleri new Product(...) nesnesiyle bu listeye ekler
    //Aynı zamanda Factory elde ettiği ByProduct ürünlerini new ByProduct(...) nesnesiyle bu listeye ekler
    //Ancak customer sınıfı aldığı ürünleri buraya koymaz, kendi sınıfı içerisinde list'e sahiptir
    //İşlevi, hem producer'lar kapasite kontrolü yapsın hem de elindeki malları görsün diye
    //Aynı zamanda işlevi, sınıflar birbirleriyle etkileşirken biri birinden bir şey aldıysa birini artırmak diğerini azaltmak için kullanıyoruz
    
    
    //constructor, entity'ler için bilgileri burada "this." ile eşleştiririz
    public BusinessEntity(String name, int maxCapacity, double initialFunds) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.funds = initialFunds;
        this.usedCapacity = 0;
        this.inventory = new ArrayList<>();
    }
    
    
    //getterlar ve setterlar
    public String getName() {
        return name;
    }

    public double getFunds() {
        return funds;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getUsedCapacity() {
        return usedCapacity;
    }

    //Entity'lerin funds'larını değiştirme methodları, satış veya alıştan sonra
    public void increaseFunds(double amount) {
        this.funds += amount;
    }

    public void decreaseFunds(double amount) {
        this.funds -= amount;
    }

  //Burada sınıfların birbiriyle etkileşmesi için ve aradığı ürünleri list'te göstermesi için yapılmış metod
  //Burada item dediğimiz şey aslında birden fazla nesne tutar hem Rawmaterial nesnesi (Rawmaterial nesnesini RawMaterialProducer ekler), 
  //hem Product nesnesi (Product nesnesini hem Market hem de Factory ekler) hem de Byproduct nesnesi (Byproduct nesnesini Factory ekler) tutar
    
    protected Item findItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }
    //yukarıda bahsettiğim product'lar (yani sınıfların oluşturduğu nesneler) entity'lerin inventory'sine burada ekleniyor.
    //Burada addToInventory ve removeFromInventory tamamen sınıfların birbirlerinden ürün alması sonucu stoklarının değişmesiyle alakalı
    //kapasite kontrolü yapar
    public void addToInventory(Item newItem) throws InsufficientStorageException {
        if (newItem.getQuantity() <= 0) {
            return;
        }

        if (usedCapacity + newItem.getQuantity() > maxCapacity) {
            throw new InsufficientStorageException("Not enough storage capacity");
        }

        Item existing = findItem(newItem.getName());
        if (existing != null) {

            existing.setQuantity(existing.getQuantity() + newItem.getQuantity());

        } else {

            inventory.add(newItem);
        }

        usedCapacity += newItem.getQuantity();
    }

    //üstteki yazdığım açıklama geçerli
    //ürün miktarını artırıp azaltır, stokları denetler, bazı throwsException'ları var kapasite için
    public void removeFromInventory(String itemName, int quantity) throws InsufficientResourcesException {
        if (quantity <= 0) {
            throw new InsufficientResourcesException("Wrong quantity");
        }
        Item existing = findItem(itemName);
        if (existing == null || existing.getQuantity() < quantity) {
            throw new InsufficientResourcesException("Not enough available");
        }

        existing.setQuantity(existing.getQuantity() - quantity);
        usedCapacity -= quantity;

        if (existing.getQuantity() == 0) {
            inventory.remove(existing);
        }
    }
    

    //getterlar, entity'lerin ellerindeki mallar burada getleniyor
    public List<Item> getInventory() {
        return inventory;
    }


    public String toString() {
        return name;
    }
}
