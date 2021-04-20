package com.shoppingcart;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Product class that handles the representation of products in program
 * Is serializable and cloneable
 * @author Reynaldo Williams, Katie Dao
 */
public class Product implements Serializable, Cloneable {

    /**
     * Default constructor for products which requires input of following parameters
     * @param name name of the product
     * @param ID id of product
     * @param type type of product
     * @param basePrice base price of product
     * @param sellPrice sell price of product
     * @param quantity quantity of product
     * @param numberSold number of products sold
     */
    public Product(String name, String ID, String type, double basePrice, double sellPrice, int quantity, int numberSold) {
        this.name = name;
        this.ID = ID;
        this.type = type;
        this.basePrice = basePrice;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
        this.numberSold = numberSold;
    }


    //setter functions

    /**
     * function to set name of product
     * @param name name to set to
     */
    void setName(String name){
        this.name = name;
    }

    /**
     * function to set ID of product
     * @param ID ID to set to
     */
    void setID(String ID){
        this.ID = ID;
    }

    /**
     * function to set type of product
     * @param type type to set to
     */
    void setType(String type){
        this.type = type;
    }

    /**
     * function to set base price
     * @param basePrice base price to set to
     */
    void setBasePrice(double basePrice){
        this.basePrice = basePrice;
    }

    /**
     * function to set sell price
     * @param sellPrice sell price to set to
     */
    void setSellPrice(double sellPrice){
        this.sellPrice = sellPrice;
    }

    /**
     * function to set quantity
     * @param quantity quantity to set to
     */
    void setQuantity(int quantity){
        this.quantity = quantity;
    }

    /**
     * function to set number sold
     * @param numberSold number sold to set to
     */
    void setNumberSold(int numberSold){
        this.numberSold = numberSold;
    }

    //getter functions

    /**
     * getter function for name of product
     * @return name of product
     */
    public String getName(){
        return name;
    }

    /**
     * function to get ID of product
     * @return ID of product
     */
    public String getID(){
        return ID;
    }

    /**
     * function to get type of product
     * @return type of product
     */
    public String getType(){
        return type;
    }

    /**
     * function to get base price of product
     * @return base price of product
     */
    public double getBasePrice(){
        return basePrice;
    }

    /**
     * function to get sell price
     * @return sell price of product
     */
    public double getSellPrice(){
        return sellPrice;
    }

    /**
     * function to get quantity of product
     * @return quantity of product
     */
    public int getQuantity(){
        return quantity;
    }

    /**
     * function to get number of products sold
     * @return number of products sold
     */
    public int getNumberSold(){
        return numberSold;
    }

    //toString method

    /**
     * function to return to string of product
     * @return string representation of product
     */
    public String toString(){
        return "Name: " + name +
                "\nID: " + ID +
                "\nType: " + type +
                "\nBase Price: " + basePrice +
                "\nSelling Price: " + sellPrice +
                "\nNumber Sold: " + numberSold;
    }

    /**
     * function to enable cloning of product
     * @return clone of product
     * @throws CloneNotSupportedException if class does not implement the cloneable interface
     */
    public Product clone() throws CloneNotSupportedException {
        Product clone = (Product) super.clone();

        clone.setQuantity(this.quantity);

        return clone;
    }

    /**
     * function to test for the equality of two products
     * @param o the other product to compare to
     * @return boolean representing if the products are equal (false = not equal/ true = equal)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.basePrice, basePrice) == 0 && Double.compare(product.sellPrice, sellPrice) == 0 && quantity == product.quantity && numberSold == product.numberSold && Objects.equals(name, product.name) && Objects.equals(ID, product.ID) && Objects.equals(type, product.type);
    }

    /**
     * function to generate hashcode of product
     * @return hashcode of product
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, ID, type, basePrice, sellPrice, quantity, numberSold);
    }

    //local variables
    String name, ID, type;
    double basePrice, sellPrice;
    int quantity, numberSold;

}
