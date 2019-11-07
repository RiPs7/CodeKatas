package main.checkout;

public class Checkout {

    private CheckoutCart checkoutCart;

    public Checkout () {
        checkoutCart = new CheckoutCart();
    }

    public boolean isValidItem (String sku) {
        return ItemDatabase.getItem(sku) != null;
    }

    public void scan (String sku) {
        final double unitPrice = ItemDatabase.getItemPriceForSingleUnit(sku);
        final int offerQuantity = ItemDatabase.getItemQuantityForOffer(sku);
        final double offerPrice = ItemDatabase.getItemPriceForOffer(sku);
        System.out.printf("Scanned item %s, unit price: %.2f, offer: %d for %.2f\n", sku, unitPrice, offerQuantity,
            offerPrice);
        checkoutCart.addItem(sku);
    }

    public void printTotal () {
        System.out.printf("Total in checkout cart: %.2f\n", checkoutCart.getTotal());
    }

}
