package com.shoppingcart;

/**
 *Class used to run program
 * @author Reynaldo Williams
 */
public class CartMain {

    /**
     * Main method used to initiate start of program
     */
    public static void main(String[] args) {

        LoginData data = new LoginData(); //grabs login data
        LoginFrame frame = new LoginFrame(); //creates new login frame
        LoginController controller = new LoginController(data, frame); //creates a new login controller

    }
}
