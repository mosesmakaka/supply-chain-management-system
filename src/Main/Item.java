package Main;

    //Item ve onun alt sınıfları RawMaterial, Product ve ByProduct tamamen RawMaterial'ın oluşturulmasında, 
    //fabrikaya verilip yeni ürün ortaya çıkarılmasında  ve byproduct'un ortaya çıkarılmasında sınıflara yardımcı olur attribute constructor'u ile

public abstract class Item {
    protected String name;
    protected int quantity;

    //Burada hem RawMaterial hem de Product sınıfı bu constructor'u çağırıyor ve name, quantity özelliklerini buradan alıyor
    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
    //setter ve getterlar var çünkü değişkenler protected
    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
    
    //miktarın girilmesinden sorumlu metoddur
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //toString metodu var
    public String toString() {
        return name + " (x" + quantity + ")";
    }
}
