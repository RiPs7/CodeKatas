package main;

import main.checkout.Checkout;

import static main.MainUtils.Print.ANSI_BRIGHT_BLUE;
import static main.MainUtils.Print.ANSI_BRIGHT_YELLOW;
import static main.MainUtils.Print.ANSI_RED;
import static main.MainUtils.Print.ANSI_RESET;
import static main.MainUtils.Print.ITALICS;

public class Main {

    private static final boolean SCAN_LOGGING = true;

    private static final Checkout CHECKOUT = new Checkout();

    private static final String PROMPT_MESSAGE =
        ANSI_BRIGHT_BLUE.getCode() + "Please scan a valid product." + ANSI_RESET.getCode();

    private static final String SCAN_MESSAGE =
        ANSI_BRIGHT_YELLOW.getCode() + ITALICS.getCode() + "Scanned item %s, unit price: %.2f, offer: %d for %.2f\n" +
            ANSI_RESET.getCode();

    private static final String INVALID_SKU_MESSAGE = ANSI_RED.getCode() + "SKU is not valid." + ANSI_RESET.getCode();


    public static void main (String[] args) {
        MainUtils.readFromInputAndApplyFunction(PROMPT_MESSAGE, Main::scanItem);
    }

    private static void scanItem (String sku) {
        if (CHECKOUT.isValidItem(sku)) {
            final Object[] itemDetails = CHECKOUT.scan(sku);
            final double unitPrice = (double) itemDetails[0];
            final int offerQuantity = (int) itemDetails[1];
            final double offerPrice = (double) itemDetails[2];
            if (SCAN_LOGGING) {
                System.out.printf(SCAN_MESSAGE, sku, unitPrice, offerQuantity, offerPrice);
            }
            CHECKOUT.printTotal();
            System.out.println(PROMPT_MESSAGE);
        } else {
            System.out.println(INVALID_SKU_MESSAGE);
        }
    }

}
