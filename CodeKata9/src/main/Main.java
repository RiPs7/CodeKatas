package main;

import main.checkout.Checkout;

import java.util.Arrays;

public class Main {

    private static final boolean INTERACTIVE = false;

    private static final Checkout CHECKOUT = new Checkout();

    public static void main (String[] args) {
        if (INTERACTIVE) {
            MainUtils.readFromInputAndApplyFunction("Please scan a valid product.", Main::scanItem);
        } else {
            Arrays.stream(args[0].split("")).forEach(Main::scanItem);
        }
    }

    private static void scanItem (String sku) {
        if (CHECKOUT.isValidItem(sku)) {
            CHECKOUT.scan(sku);
            CHECKOUT.printTotal();
        } else {
            System.out.println("SKU is not valid.");
        }
    }

}
