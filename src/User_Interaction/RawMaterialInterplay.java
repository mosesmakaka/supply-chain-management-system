package User_Interaction;

import Main.*;
import java.util.ArrayList;
import java.util.List;


public class RawMaterialInterplay {

    private static List<RawMaterialProducer> producers = new ArrayList<>();

    public static List<RawMaterialProducer> getProducers() {
        return producers;
    }


    public static RawMaterialProducer registerProducer(String name, String materialName, String generationCostStr, String sellingPriceStr, String capacityStr, String initialFundsStr) throws InvalidInputException {
        if (name == null || materialName == null) {
            throw new InvalidInputException("Write the name and material");
        }
        double genCost, sellPrice, initialFunds;
        int capacity;
        try {
            genCost = Double.parseDouble(generationCostStr);
            sellPrice = Double.parseDouble(sellingPriceStr);
            initialFunds = Double.parseDouble(initialFundsStr);
            capacity = Integer.parseInt(capacityStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Please enter valid values");
        }
        if (genCost < 0 || sellPrice < 0 || initialFunds < 0 || capacity < 0) {
            throw new InvalidInputException("Type positive numbers");
        }
        RawMaterialProducer producer = new RawMaterialProducer(name.trim(), materialName.trim(), genCost, sellPrice, capacity, initialFunds);
        producers.add(producer);
        return producer;
    }


    public static void generateMaterials(RawMaterialProducer producer, String amountStr) throws InvalidInputException, InsufficientFundsException, InsufficientStorageException {
        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Type a positive integer");
        }
        producer.generateMaterial(amount);
    }
}
