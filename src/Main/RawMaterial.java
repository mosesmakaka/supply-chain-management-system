package Main;

//hammaddenin özelliklerini temsil ediyor
// özellikle Factory ile etkileşime girmek için çünkü zaten biz RawMaterial'ın bazı özelliklerini Item'in constructor'una gönderiyoruz
// kendi generationCost'u ve, factory için sellingPrice'ı var ekstradan
//RawmaterialProducer sonradan envantere new Rawmaterial(...) şeklinde ürün ekleyecek BusinessEntity arralist'ine

public class RawMaterial extends Item {
    private double generationCost;
    private double sellingPrice;

    public RawMaterial(String name, int quantity, double generationCost, double sellingPrice) {
        super(name, quantity);
        this.generationCost = generationCost;
        this.sellingPrice = sellingPrice;
    }
    //getter ve setter'lar var çünkü private
    public double getGenerationCost() {
        return generationCost;
    }

    public double getSellingPrice()  {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
