package com.shoppingcart;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Purchase class that handles the representation of a purchase
 * @author Katie Dao
 */
public class Purchase implements Serializable, Cloneable {

    /**
     * default constructor which creates a new purchase
     * @param userName the name of person making purchase
     * @param purchaseTotal the total of their purchase
     * @param items the items they purchased
     */
    public Purchase(String userName, double purchaseTotal, HashMap<Product, Integer> items){
        this.userName = userName;
        this.purchaseTotal = purchaseTotal;
        this.items = items;
    }

    /**
     * clones a purchase
     * @return the cloned purchase
     * @throws CloneNotSupportedException if class does not implement cloneable interface
     */
    public Purchase clone() throws CloneNotSupportedException {
        Purchase clone = (Purchase) super.clone();

        clone.userName = (this.userName);
        clone.purchaseTotal = (this.purchaseTotal);
        clone.items = (this.items);

        return clone;
    }

    /**
     * function to set the name of user making purchase
     * @param userName username to set to
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * function to set purchase total
     * @param purchaseTotal total to set to
     */
    public void setPurchaseTotal(double purchaseTotal){
        this.purchaseTotal = purchaseTotal;
    }

    /**
     * function to assign purchased items
     * @param items purchased items
     */
    public void setItems(HashMap<Product, Integer> items){
        this.items = items;
    }

    /**
     * getter function for name of person making purchase
     * @return name of person making purchase
     */
    public String getUserName(){
        return userName;
    }

    /**
     * getter function for purchase total
     * @return the purchase total
     */
    public double getPurchaseTotal() {
        return purchaseTotal;
    }

    /**
     * getter function for items bought
     * @return items bought
     */
    public HashMap<Product, Integer> getItems() {
        return items;
    }

    /**
     * function to check for equality of function
     * @param o purchase to compare to
     * @return boolean indicating equality
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return Double.compare(purchase.purchaseTotal, purchaseTotal) == 0 && Objects.equals(userName, purchase.userName) && Objects.equals(items, purchase.items) && Objects.equals(formatter, purchase.formatter);
    }

    /**
     * function to generate hashcode
     * @return hashcode of purchase object
     */
    @Override
    public int hashCode() {
        return Objects.hash(userName, purchaseTotal, items, formatter);
    }

    /**
     * function to generate to string of purchase
     * @return string representation of purchase
     */
    public String toString(){

        int total = 0;
        for(Map.Entry<Product, Integer> n: items.entrySet()){
            total += n.getValue();
        }

        return "Name: " + userName + "\t" +
                "Purchase Total: " + formatter.format(purchaseTotal) + "\t" +
                "Number of Items: " + total;
    }

    //local variables
    String userName;
    double purchaseTotal;
    HashMap<Product, Integer> items;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
}
