package com.TestPackages;

import com.shoppingcart.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestProduct {

    @Test
    public void testNewProduct(){
        Product k = new Product("Test","Test","Test",3,5,2,0);
        Product r = new Product("Test","Test","Test",3,5,2,0);

        Assertions.assertEquals(r, k);
    }

    @Test
    public void testGetName(){

        Product k = new Product("Test","Test","Test",3,5,2,0);
        Product r = new Product("Test","Test","Test",3,5,2,0);

        Assertions.assertEquals(r.getName(), k.getName());
    }

    @Test
    public void testGetID(){

        Product k = new Product("Test","Test","Test",3,5,2,0);
        Product r = new Product("Test","Test","Test",3,5,2,0);

        Assertions.assertEquals(r.getID(), k.getID());
    }


    @Test
    public void testGetType(){

        Product k = new Product("Test","Test","Test",3,5,2,0);
        Product r = new Product("Test","Test","Test",3,5,2,0);

        Assertions.assertEquals(r.getType(), k.getType());
    }

    @Test
    public void testGetBasePrice(){

        Product k = new Product("Test","Test","Test",3,5,2,0);
        Product r = new Product("Test","Test","Test",3,5,2,0);

        Assertions.assertEquals(r.getBasePrice(), k.getBasePrice());

    }

    @Test
    public void testGetSellPrice(){

        Product k = new Product("Test","Test","Test",3,5,2,0);
        Product r = new Product("Test","Test","Test",3,5,2,0);

        Assertions.assertEquals(r.getSellPrice(), k.getSellPrice());

    }

    @Test
    public void testGeQuantity(){

        Product k = new Product("Test","Test","Test",3,5,2,0);
        Product r = new Product("Test","Test","Test",3,5,2,0);

        Assertions.assertEquals(r.getQuantity(), k.getQuantity());

    }

    @Test
    public void testNumberSold(){

        Product k = new Product("Test","Test","Test",3,5,2,0);
        Product r = new Product("Test","Test","Test",3,5,2,0);

        Assertions.assertEquals(r.getNumberSold(), k.getNumberSold());

    }

    @Test
    public void testToString(){
        Product k = new Product("Test","Test","Test",3,5,2,0);
        Product r = new Product("Test","Test","Test",3,5,2,0);

        Assertions.assertEquals(r.toString(), k.toString());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Product k = new Product("Test","Test","Test",3,5,2,0);
        Product r = k.clone();

        Assertions.assertEquals(k,r);
    }

    @Test
    public void testHashCode() throws CloneNotSupportedException {
        Product k = new Product("Test","Test","Test",3,5,2,0);
        Product r = k.clone();

        Assertions.assertEquals(k.hashCode(),r.hashCode());
    }


}
