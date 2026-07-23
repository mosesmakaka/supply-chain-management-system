package Main;

//Fabrika üretiminde çıkan byproduct attribute'ları burada barınır
//sonra factory new Byproduct(...) şeklinde byproduct'ları kendi inventory'sine ekler, buradan çektiği bilgilerle nesneyi oluşturur


public class Byproduct extends Item {
    private double disposalCost;

    public Byproduct(String name, int quantity, double disposalCost) {
        super(name, quantity);
        this.disposalCost = disposalCost;
    }

    public double getDisposalCost() {
        return disposalCost;
    }
}

