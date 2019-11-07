package main.checkout;

import java.util.HashMap;
import java.util.Map;

class CheckoutCart {

    private Map<String, Integer> itemsSkuAndQuantity;

    private double total;

    CheckoutCart () {
        itemsSkuAndQuantity = new HashMap<>();
    }

    void addItem (String sku) {
        itemsSkuAndQuantity.merge(sku, 1, Integer::sum);
        refreshTotal();
    }

    double getTotal () {
        return total;
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

                final int offers = quantity / offerQuantity;
                final int units = quantity - (offers * offerQuantity);
                return offers * offerPrice + units * unitPrice;
            })
            .reduce(0, Double::sum);

    }
}
