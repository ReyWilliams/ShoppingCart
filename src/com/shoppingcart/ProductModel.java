package com.shoppingcart;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductModel {

    public ProductModel(){
        Products = new ArrayList<>();
        //create objects for all the possible products
        try {
            //scanner "in" will open file loginData.txt
            in = new Scanner(new FileReader("src/com/shoppingcart/productData.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (in.hasNextLine()){ //while there are more lines to read
            String[] values = in.nextLine().split("\\|",-1); //make an array of strings split by whitespace

            //code to ignore the header
            if(values[0].equals("Product Name")) continue;

            //create new products from values
            Products.add(new Product(values[0], values[1], values[2], Double.parseDouble(values[3]), Double.parseDouble(values[4]), Integer.parseInt(values[5]), Integer.parseInt(values[6]) ));
        }
    }

    ArrayList<Product> getProducts(){
        //returning the object itself because we want to allow changes
        return Products;
    }

    ArrayList<Product> getProductsClone(){
        ArrayList<Product> clone = new ArrayList<Product>();
        for(Product p : Products) {
            try {
                clone.add(p.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        //not returning the object itself because we want to allow changes
        return clone;
    }


    Scanner in;
    ArrayList<Product> Products;
}
