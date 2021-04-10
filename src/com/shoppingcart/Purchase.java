package com.shoppingcart;

import java.io.Serializable;
import java.util.HashMap;

public class Purchase implements Serializable, Cloneable {

    public Purchase(String userName, double purchaseTotal, HashMap<Product, Integer> items){
        this.userName = userName;
        this.purchaseTotal = purchaseTotal;
        this.items = items;
    }

    public Purchase clone() throws CloneNotSupportedException {
        Purchase clone = (Purchase) super.clone();

        clone.userName = (this.userName);
        clone.purchaseTotal = (this.purchaseTotal);
        clone.items = (this.items);

        return clone;
    }



    String userName;
    double purchaseTotal;
    HashMap<Product, Integer> items;
}
