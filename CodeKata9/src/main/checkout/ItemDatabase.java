package main.checkout;

import main.MainUtils;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

class ItemDatabase {

    private static final Map<String, Map<String, Map<Integer, Double>>> itemDetails = instantiateDatabase();

    private static final String UNIT_PRICE = "unit_price";
    private static final String SPECIAL_OFFER = "special_offer";

    private static Map<String, Map<String, Map<Integer, Double>>> instantiateDatabase () {
        try {
            final Map<String, Map<String, Map<Integer, Double>>> itemDetails = new HashMap<>();
            final JSONObject jsonObject = MainUtils.readJsonObjectFromResources("cost-rules.json");
            for (final Object entry : jsonObject.entrySet()) {
                if (entry instanceof Map.Entry<?, ?>) {
                    final Map.Entry<?, ?> mapEntry = (Map.Entry<?, ?>) entry;
                    final Object key = mapEntry.getKey();
                    final Object value = mapEntry.getValue();
                    if (key instanceof String && value instanceof JSONObject) {
                        final String stringKey = (String) key;
                        final Map<String, Map<Integer, Double>> itemDetail = new HashMap<>();
                        final JSONObject jsonObjectValue = (JSONObject) value;
                        for (final Object innerEntry : jsonObjectValue.entrySet()) {
                            if (innerEntry instanceof Map.Entry<?, ?>) {
                                final Map.Entry<?, ?> innerMapEntry = (Map.Entry<?, ?>) innerEntry;
                                final Object innerKey = innerMapEntry.getKey();
                                final Object innerValue = innerMapEntry.getValue();
                                final Map<Integer, Double> itemQuantityPriceMap = new HashMap<>();
                                if (innerKey instanceof String) {
                                    final String innerStringKey = (String) innerKey;
                                    if (innerValue instanceof Long) {
                                        itemQuantityPriceMap.put(1, Double.parseDouble(((Long) innerValue).toString()));
                                    }
                                    if (innerValue instanceof Double) {
                                        itemQuantityPriceMap.put(1, (Double) innerValue);
                                    }
                                    if (innerValue instanceof String) {
                                        final String description = (String) innerValue;
                                        if ("".equals(description)) {
                                            continue;
                                        }
                                        final String[] quantityPrice = description.split(" for ");
                                        itemQuantityPriceMap.put(
                                            Integer.parseInt(quantityPrice[0]),
                                            Double.parseDouble(quantityPrice[1]));
                                    }
                                    itemDetail.put(innerStringKey, itemQuantityPriceMap);
                                }
                            }
                        }
                        itemDetails.put(stringKey, itemDetail);
                    }
                }
            }
            return itemDetails;
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    static Map<String, Map<Integer, Double>> getItem (String sku) {
        return itemDetails.get(sku);
    }

    static Integer getItemQuantityForOffer (String sku) {
        final Map<Integer, Double> specialOffer = getItem(sku).get(SPECIAL_OFFER);
        if (specialOffer == null) {
            return 0;
        }
        return specialOffer.keySet().toArray(new Integer[0])[0];
    }

    static Double getItemPriceForOffer (String sku) {
        final Map<Integer, Double> specialOffer = getItem(sku).get(SPECIAL_OFFER);
        if (specialOffer == null) {
            return 0.0;
        }
        return specialOffer.values().toArray(new Double[0])[0];
    }

    static Double getItemPriceForSingleUnit (String sku) {
        return getItem(sku).get(UNIT_PRICE).values().toArray(new Double[0])[0];
    }

}
