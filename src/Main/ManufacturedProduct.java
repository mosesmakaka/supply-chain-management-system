package Main;

import java.util.ArrayList;
import java.util.List;

//Fabrikanın üreteceği ürünlerin tasarım attribute'larını tutar. Fabrika, product'un ismini, fiyatını, byproduct ismini, byproduct miktarını..
//byproduct costunu tasarım attribute'unda bulundurur. 

//inputRequirement listesi tasarım için hangi RawMaterial'dan kaç tane alınması gerektiği bilgisini tutar. Y hammaddesinden X tane lazım şeklinde.
//bunları itemName ve amountRequired parametreleriyle inputRequirements arraylist'ine ekler

public class ManufacturedProduct {
    private String productName;
    private double productPrice;
    private String byproductName;
    private int byproductAmount;
    private double byproductCost;
    private List<InputRequirement> inputRequirements;

    public ManufacturedProduct(String productName, double productPrice, String byproductName, int byproductAmount, double byproductCost) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.byproductName = byproductName;
        this.byproductAmount = byproductAmount;
        this.byproductCost = byproductCost;
        this.inputRequirements = new ArrayList<>();
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public String getByproductName() {
        return byproductName;
    }

    public int getByproductAmount() {
        return byproductAmount;
    }

    public double getByproductCost() {
        return byproductCost;
    }

    public List<InputRequirement> getInputRequirements() {
        return inputRequirements;
    }

    public void addInputRequirement(String itemName, int amountRequired) {
        inputRequirements.add(new InputRequirement(itemName, amountRequired));
    }

    @Override
    public String toString() {
        return productName;
    }


    public static class InputRequirement {
        public String itemName;
        public int amount;

        public InputRequirement(String itemName, int amount) {
            this.itemName = itemName;
            this.amount = amount;
        }
    }
}
