package com.shoppingcart;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    /**
     * Controll that handles the events on login frame
     * @param data login data that will be tested against input data
     * @param frame the frame that holds everything that is happening
     * @author Reynaldo Williams
     */
    public LoginController(LoginData data, LoginFrame frame){
        this.frame = frame; //grab frame and data
        this.data = data;

        //attach listeners to buttons on login frame
        this.frame.loginButton.addActionListener(new LoginListener());
        this.frame.userNameField.addActionListener(new LoginListener());
        this.frame.passWordField.addActionListener(new LoginListener());

    }

    //Action listeners that handles events
    class LoginListener implements ActionListener{
        /**
         * Everytime an action is performed function is called
         * @param e the action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            //clear the feedback label
            frame.feedbackLabel.setText("");
            userName = frame.userNameField.getText();
            passWord = String.valueOf(frame.passWordField.getPassword());

            if(data.loginMap.containsKey(userName)){
                frame.feedbackLabel.setText("");

                //if that is the correct password
                HashMap<String,Boolean> map = data.loginMap.get(userName);

                //grab the accompanying hashmap
                Map.Entry<String,Boolean> entry = map.entrySet().iterator().next();
                String password = entry.getKey(); //get password
                boolean isSeller = entry.getValue(); //get seller value (T/F)

                //if the password matches
                if(password.equals(passWord)){

                    //set feedback label to green (success)
                    frame.feedbackLabel.setForeground(Color.GREEN);
                    frame.feedbackLabel.setText("Login successful!");

                    //if the seller checkbox was selected
                    if(frame.sellerCheckBox.isSelected()){

                        //check is they are seller
                        if(isSeller) {
                            frame.loginFrame.dispose(); //get rid of login frame and create new seller frame
                            ProductModel model = new ProductModel();
                            SellerFrame sellerFrame = new SellerFrame(userName, model);
                        }else{ //if they are not seller, show feedback
                            frame.feedbackLabel.setForeground(Color.RED);
                            frame.feedbackLabel.setText("User not seller!");
                        }
                    }else{ //if seller checkbox was not clicked

                        //dispose login frame and login them in to product frame
                        frame.loginFrame.dispose();
                        ProductModel model = new ProductModel(); //generate product model
                        PurchaseModel pmodel = new PurchaseModel(); //generate new purchase model
                        ProductFrame productFrame = new ProductFrame(model, userName, pmodel);
                    }
                }else{ //if that is not the correct password
                    frame.feedbackLabel.setForeground(Color.RED);
                    frame.feedbackLabel.setText("Incorrect password!");
                }
            }else{ //if user not found
                frame.feedbackLabel.setForeground(Color.RED);
                frame.feedbackLabel.setText("User not found!");

            }
        }
    }

    //local viariables
    String userName, passWord;
    LoginFrame frame;
    LoginData data;
}
