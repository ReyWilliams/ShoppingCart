package com.shoppingcart;

import javax.swing.*;

public class CartTester {

    public static void main(String[] args) {


        LoginData data = new LoginData();
        LoginFrame frame = new LoginFrame(data);
        LoginController controller = new LoginController(data, frame);

        //SellerFrame frame2 = new SellerFrame();
    }
}
