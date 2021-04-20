package com.TestPackages;

import com.shoppingcart.Product;
import com.shoppingcart.Purchase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class TestPurchase {

    @Test
    public void testNewPurchase(){

        System.out.println("Testing creating new purchase...");
        HashMap<Product, Integer> items = new HashMap<>();
        items.put(new Product("Test","Test","Test",3,5,2,0),2);
        Purchase r = new Purchase("Rey", 10.0,items);

        Purchase k = new Purchase("Rey", 10.0,items);

        Assertions.assertEquals(r, k);

    }


    @Test
    public void testGetUserName(){
        System.out.println("Testing getUserName...");
        HashMap<Product, Integer> items = new HashMap<>();
        items.put(new Product("Test","Test","Test",3,5,2,0),2);
        Purchase r = new Purchase("Rey", 10.0,items);

        Purchase k = new Purchase("Rey", 10.0,items);

        Assertions.assertEquals(r.getUserName(),k.getUserName());
    }

    @Test
    public void testGetPurchaseTotal(){
        System.out.println("Testing getPurchaseTotal...");
        HashMap<Product, Integer> items = new HashMap<>();
        items.put(new Product("Test","Test","Test",3,5,2,0),2);
        Purchase r = new Purchase("Rey", 10.0,items);

        Purchase k = new Purchase("Rey", 10.0,items);

        Assertions.assertEquals(r.getPurchaseTotal(),k.getPurchaseTotal());
    }

    @Test
    public void testGetItems(){
        System.out.println("Testing getItems...");
        HashMap<Product, Integer> items = new HashMap<>();
        items.put(new Product("Test","Test","Test",3,5,2,0),2);
        Purchase r = new Purchase("Katie", 10.0,items);

        Purchase k = new Purchase("Katie", 10.0,items);

        Assertions.assertEquals(r.getItems(),k.getItems());
    }

    @Test
    public void testToString(){
        System.out.println("Testing toString...");
        HashMap<Product, Integer> items = new HashMap<>();
        items.put(new Product("Test","Test","Test",3,5,2,0),2);
        Purchase r = new Purchase("Katie", 10.0,items);

        Purchase k = new Purchase("Katie", 10.0,items);

        Assertions.assertEquals(r.toString(),k.toString());
    }





}
