package main.checkout;

import java.util.HashMap;
import java.util.Map;

public class Checkout {

    private Map<String, Integer> itemsSkuAndQuantity;

    private double total;

    public Checkout () {
        this.total = 0;
        this.itemsSkuAndQuantity = new HashMap<>();
    }

    public boolean isValidItem (String sku) {
        return ItemDatabase.getItem(sku) != null;
    }

    public Object[] scan (String sku) {
        itemsSkuAndQuantity.merge(sku, 1, Integer::sum);
        refreshTotal();

        return new Object[] {
            ItemDatabase.getItemPriceForSingleUnit(sku),
            ItemDatabase.getItemQuantityForOffer(sku),
            ItemDatabase.getItemPriceForOffer(sku)
        };
    }

    private void refreshTotal () {
        total = itemsSkuAndQuantity.entrySet()
            .stream()
            .mapToDouble(entry -> {
                final String sku = entry.getKey();
                final int quantity = entry.getValue();
                final double unitPrice = ItemDatabase.getItemPriceForSingleUnit(sku);
                final int offerQuantity = ItemDatabase.getItemQuantityForOffer(sku);
                final double offerPrice = ItemDatabase.getItemPriceForOffer(sku);

                final int offers = offerQuantity == 0 ? 0 : quantity / offerQuantity;
                final int units = quantity - (offers * offerQuantity);
                return offers * offerPrice + units * unitPrice;
            })
            .reduce(0, Double::sum);
    }

    public double getTotal () {
        return total;
    }

    public void printTotal () {
        System.out.printf("Total in checkout: %.2f\n\n", total);
    }

}
