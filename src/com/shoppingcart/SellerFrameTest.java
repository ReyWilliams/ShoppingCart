package com.shoppingcart;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SellerFrameTest {

    @org.junit.jupiter.api.Test
    void viewProfit() {
        Product HS = new Product("Hand Santizer","HS","Health", 5.00,7.00,10,0);

        ArrayList<Purchase> purchaseList = new ArrayList<>();

        HashMap<Product, Integer> items = new HashMap<>();

        //if we purchase five(5) hand sanitizers then we should have a profit of $10.00

        items.put(HS, 5);

        purchaseList.add(new Purchase("Test",35.0,items));


    }
}