package com.trangptt.POM;

import com.trangptt.library.WebUI;

import java.util.Map;

import static com.trangptt.library.TestObject.ObjectRepository.findTestObject;

public class TodaysDealsPage {
    public static void selectDiscountFilter(String value) throws Exception {
        WebUI.verifyElementVisible(findTestObject("Amazon.Today's Deals.Filter.Discount", Map.of("value", value)));
        WebUI.click(findTestObject("Amazon.Today's Deals.Filter.Discount", Map.of("value", value)));
    }

    public static void viewTheDeal(String index) throws Exception {
        WebUI.verifyElementVisible(findTestObject("Amazon.Today's Deals.Product Cart.Item", Map.of("index", index)));
        WebUI.click(findTestObject("Amazon.Today's Deals.Product Cart.Item", Map.of("index", index)));
    }
}
