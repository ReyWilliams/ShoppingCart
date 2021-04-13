package com.shoppingcart;

import java.io.*;
import java.util.ArrayList;

public class PurchaseModel {
    public PurchaseModel(){

        File tmpDir = new File("Serialized/purchases.dat");
        boolean exists = tmpDir.exists();

        if(exists){ //if it exists
            try { //try to deserialize

                ObjectInputStream in = new ObjectInputStream(
                        new FileInputStream("Serialized/purchases.dat"));

                Purchases = (ArrayList<Purchase>) in.readObject();

                in.close();

                System.out.println("Purchases deserialized by purchase model.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{ //if it doesnt exist

            Purchases = new ArrayList<>(); //create a new one

            //try to serialize
            try {
                ObjectOutputStream out = new ObjectOutputStream(
                        new FileOutputStream("Serialized/purchases.dat"));

                out.writeObject(Purchases);

                out.close();

                System.out.println("Purchases serialized by purchase model.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    ArrayList<Purchase> getPurchases(){
        //returning the object itself because we want to allow changes
        return Purchases;
    }

    ArrayList<Purchase> getPurchasesClone(){
        ArrayList<Purchase> clone = new ArrayList<Purchase>();
        for(Purchase p : Purchases) {
            try {
                clone.add(p.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        //not returning the object itself because we want to allow changes
        return clone;
    }


    ArrayList<Purchase> Purchases;
}
