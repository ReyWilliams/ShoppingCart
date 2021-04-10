package com.shoppingcart;

import java.io.Serial;
import java.io.Serializable;

public class Product implements Serializable, Cloneable {

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
    void setName(String name){
        this.name = name;
    }
    void setID(String ID){
        this.ID = ID;
    }
    void setType(String type){
        this.type = type;
    }
    void setBasePrice(double basePrice){
        this.basePrice = basePrice;
    }
    void setSellPrice(double sellPrice){
        this.sellPrice = sellPrice;
    }
    void setQuantity(int quantity){
        this.quantity = quantity;
    }
    void setNumberSold(int numberSold){
        this.numberSold = numberSold;
    }

    //getter functions
    String getName(){
        return name;
    }
    String getID(){
        return ID;
    }
    String getType(){
        return type;
    }
    double getBasePrice(){
        return basePrice;
    }
    double getSellPrice(){
        return sellPrice;
    }
    int getQuantity(){
        return quantity;
    }
    int getNumberSold(){
        return numberSold;
    }

    //toString method
    public String toString(){
        return "Name: " + name +
                "\nID: " + ID +
                "\nType: " + type +
                "\nBase Price: " + basePrice +
                "\nSelling Price: " + sellPrice +
                "\nNumber Sold: " + numberSold;
    }

    public Product clone() throws CloneNotSupportedException {
        Product clone = (Product) super.clone();

        clone.setQuantity(this.quantity);

        return clone;
    }

    String name, ID, type;
    double basePrice, sellPrice;
    int quantity, numberSold;

    @Serial
    private static final long serialVersionUID = 8624235003495733176L;
}
