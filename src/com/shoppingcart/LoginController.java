package com.shoppingcart;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    public LoginController(LoginData data, LoginFrame frame){
        this.frame = frame;
        this.data = data;

        this.frame.loginButton.addActionListener(new LoginListener());
        this.frame.userNameField.addActionListener(new LoginListener());
        this.frame.passWordField.addActionListener(new LoginListener());

    }

    class LoginListener implements ActionListener{

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
                Map.Entry<String,Boolean> entry = map.entrySet().iterator().next();
                String password = entry.getKey();
                boolean isSeller = entry.getValue();

                if(password.equals(passWord)){
                    frame.feedbackLabel.setForeground(Color.GREEN);
                    frame.feedbackLabel.setText("Login successful!");

                    if(frame.sellerCheckBox.isSelected()){

                        if(isSeller) {
                            frame.loginFrame.dispose();
                            ProductModel model = new ProductModel();
                            SellerFrame sellerFrame = new SellerFrame(userName, model);
                        }else{
                            frame.feedbackLabel.setForeground(Color.RED);
                            frame.feedbackLabel.setText("User not seller!");
                        }
                    }else{

                        frame.loginFrame.dispose();
                        ProductModel model = new ProductModel();
                        PurchaseModel pmodel = new PurchaseModel();
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

    String userName, passWord;
    LoginFrame frame;
    LoginData data;
}
