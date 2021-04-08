package com.shoppingcart;

public class Product {

    public Product(String name, String ID, String type, double basePrice, double sellPrice, int quantity) {
        this.name = name;
        this.ID = ID;
        this.type = type;
        this.basePrice = basePrice;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
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

    //toString method
    public String toString(){
        return "Name: " + name +
                "\nID: " + ID +
                "\nType: " + type +
                "\nBase Price: " + basePrice +
                "\nSelling Price: " + sellPrice;
    }

    String name, ID, type;
    double basePrice, sellPrice;
    int quantity;
}
