package Main;


//Item sınfının normalde item ve quantity'si vardı, ekstra önemli attribute ekler (PRICE), bu sayede item price attribute'unu buradan çeker 

public class Product extends Item {
    private double price;

    public Product(String name, int quantity, double price) {
        super(name, quantity);
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    //toString metodu
    public String toString() {
        return name + " (x" + quantity + ", price: " + price + ")";
    }
}
