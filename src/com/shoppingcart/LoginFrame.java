package com.shoppingcart;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class LoginFrame {

    public LoginFrame(LoginData data){

        //copy data object
        this.data = data;

        //get login data
        loginMap = data.getLoginMap();

        loginFrame = new JFrame();
        loginPanel = new JPanel();
        loginFields = new JTextField[2];

        //setup attributes for loginFrame JFrame
        loginFrame.setSize(875,687);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //set panel layout to null to allow for absolute positioning
        loginPanel.setLayout(null);


        //Welcome text
        JLabel welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setFont(new Font("Century Gothic", Font.PLAIN, 50));
        welcomeLabel.setBounds(250, 75,300,70);
        loginPanel.add(welcomeLabel);

        //image setup
        BufferedImage cartIcon = null;
        try {
            cartIcon = ImageIO.read(new File("./cartIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert cartIcon != null;
        JLabel cartIconLabel = new JLabel(new ImageIcon(cartIcon));
        cartIconLabel.setBounds(500,75,90,70);
        loginPanel.add(cartIconLabel);

        //creating and positioning our username, password, and seller labels
        JLabel userNameLabel = new JLabel("Username:");
        userNameLabel.setFont(new Font("Century Gothic", Font.BOLD, 24));
        userNameLabel.setBounds(150,200,200,50);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Century Gothic", Font.BOLD, 24));
        passwordLabel.setBounds(150,300,200,50);
        JLabel sellerLabel = new JLabel("Seller: ");
        sellerLabel.setFont(new Font("Century Gothic", Font.BOLD, 24));
        sellerLabel.setBounds(195,400,200,50);

        loginPanel.add(userNameLabel);
        loginPanel.add(passwordLabel);
        loginPanel.add(sellerLabel);

        //creating and positioning our username and password textfields
        userNameField = new JTextField(15);
        userNameField.setBounds(300, 210,170,40);
        userNameField.setFont(new Font("Century Gothic", Font.ITALIC, 20));
        loginFields[0] = userNameField;

        passWordField = new JPasswordField(15);
        passWordField.setBounds(300, 310,170,40);
        passWordField.setFont(new Font("Century Gothic", Font.ITALIC, 20));
        loginFields[1] = userNameField;

        loginPanel.add(userNameField);
        loginPanel.add(passWordField);

        //create and position our seller checkbox
        sellerCheckBox = new JCheckBox();
        sellerCheckBox.setBounds(300,400,50,50);

        loginPanel.add(sellerCheckBox);



        //create and position our login button
        loginButton   = new JButton("Login");
        loginButton.setFont(new Font("Century Gothic", Font.BOLD, 20));
        loginButton.setBounds(400,500,100,50);
        loginPanel.add(loginButton);


        //Login feedback label
        feedbackLabel = new JLabel("LOGIN FAILED, try again");
        feedbackLabel.setFont(new Font("Century Gothic", Font.BOLD, 18));
        feedbackLabel.setBounds(315,575,200,50);

        loginPanel.add(feedbackLabel);


        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);


    }

    JTextField[] loginFields;
    JTextField userNameField;
    JPasswordField passWordField;
    JButton loginButton;
    JFrame loginFrame;
    JPanel loginPanel;
    JCheckBox sellerCheckBox;
    JLabel feedbackLabel;
    HashMap<String, String> loginMap;
    LoginData data;

}
