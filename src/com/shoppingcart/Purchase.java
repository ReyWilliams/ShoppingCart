package com.shoppingcart;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

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

    public String toString(){

        int total = 0;
        for(Map.Entry<Product, Integer> n: items.entrySet()){
            total += n.getValue();
        }
        return "Name: " + userName + "\t" +
                "Purchase Total: " + formatter.format(purchaseTotal) + "\t" +
                "Number of Items: " + total;
    }

    String userName;
    double purchaseTotal;
    HashMap<Product, Integer> items;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private static final long serialVersionUID = -3616972512208131858L;
}
