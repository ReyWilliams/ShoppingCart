package com.shoppingcart;



import jdk.swing.interop.SwingInterOpUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class ProductFrame {

    public ProductFrame(ProductModel model, String userName, PurchaseModel pmodel){

        //get username
        this.userName = userName;

        //get model
        this.model = model;

        //initialize arraylist
        Products = model.getProducts();


        //initialize checked out to false (user has not checked out)
        checkedOut = false;


        //set up sounds
        URL soundbyte = null;
        try {
            soundbyte = new File("Sounds/ka-ching.wav").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        clip = java.applet.Applet.newAudioClip(soundbyte);



        //deserialize objects
        File tmpDir = new File("Serialized/User(" + userName + ").dat");
        boolean exists = tmpDir.exists();

        if(exists) { //user has file
            try { //try to deserialize

                ObjectInputStream in = new ObjectInputStream(
                        new FileInputStream("Serialized/User(" + userName + ").dat"));

                ProductsClone = (ArrayList<Product>) in.readObject();
                userQuantity = (ArrayList<Integer>) in.readObject();
                cartTotalVal = in.readDouble();

                in.close();

                System.out.println("User " + userName + " deserialized");

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{ //choose default values
            ProductsClone = model.getProductsClone();
            userQuantity = new ArrayList<>(Arrays.asList(0,0,0,0));
            cartTotalVal = 0;
        }

        if(exists){ //if there was previously a serialized user file, perform an update to the quantities to make sure everything is synched
            quantityCheck();
        }

        tmpDir = new File("Serialized/purchases.dat");
        exists = tmpDir.exists();

        if(exists) { //purchases has file
            try { //try to deserialize

                ObjectInputStream in = new ObjectInputStream(
                        new FileInputStream("Serialized/purchases.dat"));

                purchaseList = (ArrayList<Purchase>) in.readObject();

                in.close();

                System.out.println("Purchases deserialized by product frame.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{ //choose default values
            purchaseList = pmodel.getPurchases();
        }



        //setup product frame w/ parameters
        productFrame = new JFrame();

        productFrame.setSize(587,775);
        productFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        productFrame.setLayout(new BorderLayout(20,20));
        productFrame.setTitle("Welcome, " + userName);

        switchPanel = new JPanel();
        cardLayout = new CardLayout();
        switchPanel.setLayout(cardLayout);

        generalProductsPanel = new JPanel(new BorderLayout(50,50));

        //setup top panel which will hold the buttons users will uses to switch between views and logout
        productTopPanel = new JPanel(new FlowLayout());
        checkoutButton = new JButton("Checkout Items");
        checkoutButton.setBackground(new Color(169,169,169));
        checkoutButton.addActionListener(e -> showCheckOutPage());
        checkoutButton.setFocusable(false);
        productsButton = new JButton("View Products");
        productsButton.setFocusable(false);
        productsButton.setBackground(new Color(169,169,169));
        productsButton.addActionListener(e -> showProductsPage());
        logoutButton = new JButton("Logout");
        logoutButton.setFocusable(false);
        logoutButton.setBackground(new Color(169,169,169));
        logoutButton.addActionListener(e -> logOut(e));

        //add the bottoms to the top panel
        productTopPanel.add(productsButton);
        productTopPanel.add(checkoutButton);
        productTopPanel.add(logoutButton);

        //add the top panel to the north of the general panel
        productFrame.add(productTopPanel, BorderLayout.NORTH);


        //setup the products view panel which will be a grid
        productsViewPanel = new JPanel(new GridLayout(2,2,20,20));

        //add the products to the general products panel at the center
        generalProductsPanel.add(productsViewPanel,BorderLayout.CENTER);


        //add text saying user's name as top
        JPanel productTopPanel = new JPanel(new FlowLayout());
        JLabel welcomeUser = new JLabel("Welcome " + userName +"!");
        welcomeUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeUser.setFont(new Font("Century Gothic", Font.BOLD, 26));
        welcomeUser.setForeground(Color.black);
        productTopPanel.add(welcomeUser);
        generalProductsPanel.add(productTopPanel,BorderLayout.NORTH);

        //add spacers to the left and right and south
        generalProductsPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.EAST);
        generalProductsPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.WEST);
        generalProductsPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.SOUTH);

        setUpProductsView(productsViewPanel);

        //checkout Panel
        JPanel generalCheckOutPanel = new JPanel(new BorderLayout(50,50));

        //confirm button and cart total label
        JPanel checkOutTopPanel = new JPanel(new FlowLayout());

        cartTotalLabel = new JLabel();
        cartTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cartTotalLabel.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        cartTotalLabel.setForeground(Color.BLACK);
        cartTotalLabel.setText("Total: " + formatter.format(cartTotalVal));

        confirmCheckOutButton = new JButton("Confirm Checkout");
        confirmCheckOutButton.setBackground(new Color(169,169,169));
        confirmCheckOutButton.setFocusable(false);
        confirmCheckOutButton.addActionListener(e->showPurchasePage());

        checkOutTopPanel.add(confirmCheckOutButton);
        checkOutTopPanel.add(cartTotalLabel);
        generalCheckOutPanel.add(checkOutTopPanel, BorderLayout.NORTH);




        //checkout panel products
        checkOutProductsViewPanel = new JPanel(new GridLayout(2,2,20,20));


        //spacers
        //add spacers to the left and right and south
        generalCheckOutPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.EAST);
        generalCheckOutPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.WEST);
        generalCheckOutPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.SOUTH);

        generalCheckOutPanel.add(checkOutProductsViewPanel, BorderLayout.CENTER);


        //setup products
        setUpCheckOutProductsView(checkOutProductsViewPanel);

        //setup purchase panel
        setUpPurchasePanel();

        //set up switch panel
        switchPanel.add(productsPage, generalProductsPanel);
        switchPanel.add(checkOutPage, generalCheckOutPanel);

        switchPanel.add(purchasePage, generalPurchasePanel);
        showProductsPage();

        //finalize frame
        productFrame.add(switchPanel);
        productFrame.setVisible(true);
    }

    void showPurchasePage(){

        //update purchase total text.
        purchaseTotal.setText("Purchase Total: " + formatter.format(cartTotalVal));
        purchaseTotal.setBackground(new Color(169,169,169));
        purchaseTotal.setOpaque(true);
        purchaseTotal.setBounds(170,140 + tempHeight*6,purchaseTotal.getPreferredSize().width , purchaseTotal.getPreferredSize().height);

        //if cart total is 0, then user cannot check out so turn total to red
        if(cartTotalVal == 0){
            cartTotalLabel.setForeground(new Color(255, 114,111));
            cartTotalLabel.setFont(new Font("Century Gothic", Font.BOLD, 18));
            return;
        }

        //switch Panel
        productFrame.setSize(587,525);
        cardLayout.show(switchPanel, purchasePage);
        isOnProductsPage = false;

    }

    void setUpProductsView(JPanel productsViewPanel){

        //set up the hand sanitizer panel
        handSanitizerPanel = new JPanel();

        handSanitizerPanel.setBackground(new Color(169,169,169));
        handSanitizerPanel.setLayout( new BoxLayout(handSanitizerPanel,BoxLayout.Y_AXIS));
        handSanitizerPanel.setSize(25,25);

        //spacer
        handSanitizerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        //image setup
        BufferedImage handSanitizerIcon = null;
        try {
            handSanitizerIcon = ImageIO.read(new File("Icons/handSanitizerIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert handSanitizerIcon != null;
        JLabel handSanitizerIconLabel = new JLabel(new ImageIcon(handSanitizerIcon));
        handSanitizerIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        handSanitizerPanel.add(handSanitizerIconLabel);

        //set up name, price and quantity labels
        handSanitizerName = new JLabel(Products.get(0).getName());
        handSanitizerName.setFont(new Font("Century Gothic", Font.BOLD, 20));
        handSanitizerName.setForeground(new Color(0,255,127));
        handSanitizerName.setAlignmentX(Component.CENTER_ALIGNMENT);
        handSanitizerPanel.add(handSanitizerName);

        handSanitizerPrice = new JLabel("Price: " + formatter.format(Products.get(0).getSellPrice()));
        handSanitizerPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        handSanitizerPrice.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        handSanitizerPrice.setForeground(Color.WHITE);
        handSanitizerPanel.add(handSanitizerPrice);

        handSanitizerQuantity = new JLabel("Quantity: " + ProductsClone.get(0).getQuantity());
        handSanitizerQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        handSanitizerQuantity.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        handSanitizerQuantity.setForeground(Color.white);
        handSanitizerPanel.add(handSanitizerQuantity);

        //spacer
        handSanitizerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        buyHandSanitizer = new JButton("ADD TO CART");
        buyHandSanitizer.setBackground(new Color(169,169,200));
        buyHandSanitizer.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyHandSanitizer.setFont(new Font("Century Gothic", Font.BOLD, 12));
        buyHandSanitizer.addActionListener(this::showPurchase);
        handSanitizerPanel.add(buyHandSanitizer);

        productsViewPanel.add(handSanitizerPanel);

        //set up the mask panel
        maskPanel = new JPanel();

        maskPanel.setBackground(new Color(169,169,169));
        maskPanel.setLayout( new BoxLayout(maskPanel,BoxLayout.Y_AXIS));
        maskPanel.setSize(25,25);

        //spacer
        maskPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        //image setup
        BufferedImage maskIcon = null;
        try {
            maskIcon = ImageIO.read(new File("Icons/maskIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert maskIcon != null;
        JLabel maskIconLabel = new JLabel(new ImageIcon(maskIcon));
        maskIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        maskPanel.add(maskIconLabel);

        //set up name, price and quantity labels
        maskName = new JLabel(Products.get(1).getName());
        maskName.setFont(new Font("Century Gothic", Font.BOLD, 20));
        maskName.setForeground(new Color(0,255,127));
        maskName.setAlignmentX(Component.CENTER_ALIGNMENT);
        maskPanel.add(maskName);

        maskPrice = new JLabel("Price: " + formatter.format(Products.get(1).getSellPrice()));
        maskPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        maskPrice.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        maskPrice.setForeground(Color.WHITE);
        maskPanel.add(maskPrice);

        maskQuantity = new JLabel("Quantity: " + ProductsClone.get(1).getQuantity());
        maskQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        maskQuantity.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        maskQuantity.setForeground(Color.white);
        maskPanel.add(maskQuantity);

        //spacer
        maskPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        buyMask = new JButton("ADD TO CART");
        buyMask.setBackground(new Color(169,169,200));
        buyMask.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyMask.setFont(new Font("Century Gothic", Font.BOLD, 12));
        buyMask.addActionListener(this::showPurchase);
        maskPanel.add(buyMask);

        productsViewPanel.add(maskPanel);

        //set up the toothPaste panel
        toothPastePanel = new JPanel();

        toothPastePanel.setBackground(new Color(169,169,169));
        toothPastePanel.setLayout( new BoxLayout(toothPastePanel,BoxLayout.Y_AXIS));
        toothPastePanel.setSize(25,25);

        //spacer
        toothPastePanel.add(Box.createRigidArea(new Dimension(0, 30)));

        //image setup
        BufferedImage toothPasteIcon = null;
        try {
            toothPasteIcon = ImageIO.read(new File("Icons/toothPasteIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert toothPasteIcon != null;
        JLabel toothPasteIconLabel = new JLabel(new ImageIcon(toothPasteIcon));
        toothPasteIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        toothPastePanel.add(toothPasteIconLabel);

        //set up name, price and quantity labels
        toothPasteName = new JLabel(Products.get(2).getName());
        toothPasteName.setFont(new Font("Century Gothic", Font.BOLD, 20));
        toothPasteName.setForeground(new Color(0,255,127));
        toothPasteName.setAlignmentX(Component.CENTER_ALIGNMENT);
        toothPastePanel.add(toothPasteName);

        toothPastePrice = new JLabel("Price: " + formatter.format(Products.get(2).getSellPrice()));
        toothPastePrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        toothPastePrice.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        toothPastePrice.setForeground(Color.WHITE);
        toothPastePanel.add(toothPastePrice);

        toothPasteQuantity = new JLabel("Quantity: " + ProductsClone.get(2).getQuantity());
        toothPasteQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        toothPasteQuantity.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        toothPasteQuantity.setForeground(Color.white);
        toothPastePanel.add(toothPasteQuantity);

        //spacer
        toothPastePanel.add(Box.createRigidArea(new Dimension(0, 30)));

        buytoothPaste = new JButton("ADD TO CART");
        buytoothPaste.setBackground(new Color(169,169,200));
        buytoothPaste.setAlignmentX(Component.CENTER_ALIGNMENT);
        buytoothPaste.setFont(new Font("Century Gothic", Font.BOLD, 12));
        buytoothPaste.addActionListener(this::showPurchase);
        toothPastePanel.add(buytoothPaste);

        productsViewPanel.add(toothPastePanel);

        //set up the medication panel
        medicationPanel = new JPanel();

        medicationPanel.setBackground(new Color(169,169,169));
        medicationPanel.setLayout( new BoxLayout(medicationPanel,BoxLayout.Y_AXIS));
        medicationPanel.setSize(25,25);

        //spacer
        medicationPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        //image setup
        BufferedImage medicationIcon = null;
        try {
            medicationIcon = ImageIO.read(new File("Icons/medicationIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert medicationIcon != null;
        JLabel medicationIconLabel = new JLabel(new ImageIcon(medicationIcon));
        medicationIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        medicationPanel.add(medicationIconLabel);

        //set up name, price and quantity labels
        medicationName = new JLabel(Products.get(3).getName());
        medicationName.setFont(new Font("Century Gothic", Font.BOLD, 20));
        medicationName.setForeground(new Color(0,255,127));
        medicationName.setAlignmentX(Component.CENTER_ALIGNMENT);
        medicationPanel.add(medicationName);

        medicationPrice = new JLabel("Price: " + formatter.format(Products.get(3).getSellPrice()));
        medicationPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        medicationPrice.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        medicationPrice.setForeground(Color.WHITE);
        medicationPanel.add(medicationPrice);

        medicationQuantity = new JLabel("Quantity: " + ProductsClone.get(3).getQuantity());
        medicationQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        medicationQuantity.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        medicationQuantity.setForeground(Color.white);
        medicationPanel.add(medicationQuantity);

        //spacer
        medicationPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        buyMedication = new JButton("ADD TO CART");
        buyMedication.setBackground(new Color(169,169,200));
        buyMedication.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyMedication.setFont(new Font("Century Gothic", Font.BOLD, 12));
        buyMedication.addActionListener(this::showPurchase);
        medicationPanel.add(buyMedication);

        productsViewPanel.add(medicationPanel);
    }

    void setUpCheckOutProductsView(JPanel checkOutProductsViewPanel){

        //set up the hand sanitizer CO panel
        handSanitizerPanelCO = new JPanel();

        handSanitizerPanelCO.setBackground(new Color(169,169,169));
        handSanitizerPanelCO.setLayout( new BoxLayout(handSanitizerPanelCO,BoxLayout.Y_AXIS));
        handSanitizerPanelCO.setSize(25,25);

        //spacer
        handSanitizerPanelCO.add(Box.createRigidArea(new Dimension(0, 15)));

        //image setup
        BufferedImage handSanitizerIcon = null;
        try {
            handSanitizerIcon = ImageIO.read(new File("Icons/handSanitizerIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert handSanitizerIcon != null;
        JLabel handSanitizerIconLabelCO = new JLabel(new ImageIcon(handSanitizerIcon));
        handSanitizerIconLabelCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        handSanitizerPanelCO.add(handSanitizerIconLabelCO);

        //set up name, price and quantity labels
        JLabel handSanitizerNameCO = new JLabel(Products.get(0).getName());
        handSanitizerNameCO.setFont(new Font("Century Gothic", Font.BOLD, 20));
        handSanitizerNameCO.setForeground(Color.black);
        handSanitizerNameCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        handSanitizerPanelCO.add(handSanitizerNameCO);

        JLabel handSanitizerPriceCO = new JLabel("Price: " + formatter.format(Products.get(0).getSellPrice()));
        handSanitizerPriceCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        handSanitizerPriceCO.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        handSanitizerPriceCO.setForeground(Color.WHITE);
        handSanitizerPanelCO.add(handSanitizerPriceCO);

        handSanitizerQuantityCO = new JLabel("Cart Quantity: " + userQuantity.get(0));
        handSanitizerQuantityCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        handSanitizerQuantityCO.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        handSanitizerQuantityCO.setForeground(Color.white);
        handSanitizerPanelCO.add(handSanitizerQuantityCO);

        handSanitizerTotal = new JLabel(" Item Total: " + formatter.format(Products.get(0).getSellPrice() * userQuantity.get(0)));
        handSanitizerTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        handSanitizerTotal.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        handSanitizerTotal.setForeground(Color.WHITE);
        handSanitizerPanelCO.add(handSanitizerTotal);

        //spacer
        handSanitizerPanelCO.add(Box.createRigidArea(new Dimension(0, 15)));

        //increment and decrement buttons
        JPanel handSanitizerIncAndDecCO = new JPanel(new FlowLayout());
        handSanitizerIncAndDecCO.setSize(10,10);
        handSanitizerIncAndDecCO.setBackground(new Color(169,169,169));
        addHandSanitizerCO = new JButton("+");
        addHandSanitizerCO.setBackground(new Color(147,219,170));
        addHandSanitizerCO.setFocusable(false);
        addHandSanitizerCO.addActionListener(this::updateCart);
        addHandSanitizerCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        addHandSanitizerCO.setFont(new Font("Century Gothic", Font.BOLD, 20));
        handSanitizerIncAndDecCO.add(addHandSanitizerCO);

        removeHandSanitizerCO = new JButton("-");
        removeHandSanitizerCO.setBackground(new Color(255, 114,111));
        removeHandSanitizerCO.setFocusable(false);
        removeHandSanitizerCO.addActionListener(this::updateCart);
        removeHandSanitizerCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeHandSanitizerCO.setFont(new Font("Century Gothic", Font.BOLD, 20));
        handSanitizerIncAndDecCO.add(removeHandSanitizerCO);

        handSanitizerPanelCO.add(handSanitizerIncAndDecCO);

        //if user has item in their cart make it visible when they login
//        handSanitizerPanelCO.setVisible(userQuantity.get(0) > 0);

        if(userQuantity.get(0) > 0)
        checkOutProductsViewPanel.add(handSanitizerPanelCO);

        //set up the mask CO panel
        maskPanelCO = new JPanel();

        maskPanelCO.setBackground(new Color(169,169,169));
        maskPanelCO.setLayout( new BoxLayout(maskPanelCO,BoxLayout.Y_AXIS));
        maskPanelCO.setSize(25,25);

        //spacer
        maskPanelCO.add(Box.createRigidArea(new Dimension(0, 15)));

        //image setup
        BufferedImage maskIcon = null;
        try {
            maskIcon = ImageIO.read(new File("Icons/maskIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert maskIcon != null;
        JLabel maskIconLabelCO = new JLabel(new ImageIcon(maskIcon));
        maskIconLabelCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        maskPanelCO.add(maskIconLabelCO);

        //set up name, price and quantity labels
        JLabel maskNameCO = new JLabel(Products.get(1).getName());
        maskNameCO.setFont(new Font("Century Gothic", Font.BOLD, 20));
        maskNameCO.setForeground(Color.black);
        maskNameCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        maskPanelCO.add(maskNameCO);

        JLabel maskPriceCO = new JLabel("Price: " + formatter.format(Products.get(1).getSellPrice()));
        maskPriceCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        maskPriceCO.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        maskPriceCO.setForeground(Color.WHITE);
        maskPanelCO.add(maskPriceCO);

        maskQuantityCO = new JLabel("Cart Quantity: " + userQuantity.get(1));
        maskQuantityCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        maskQuantityCO.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        maskQuantityCO.setForeground(Color.white);
        maskPanelCO.add(maskQuantityCO);

        maskTotal = new JLabel("Item Total: " + formatter.format(Products.get(1).getSellPrice() * userQuantity.get(1)));
        maskTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        maskTotal.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        maskTotal.setForeground(Color.WHITE);
        maskPanelCO.add(maskTotal);

        //spacer
        maskPanelCO.add(Box.createRigidArea(new Dimension(0, 15)));

        //increment and decrement buttons
        JPanel maskIncAndDecCO = new JPanel(new FlowLayout());
        maskIncAndDecCO.setBackground(new Color(169,169,169));
        maskIncAndDecCO.setSize(10,10);
        addMaskCO = new JButton("+");
        addMaskCO.setBackground(new Color(147,219,170));
        addMaskCO.setFocusable(false);
        addMaskCO.addActionListener(this::updateCart);
        addMaskCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        addMaskCO.setFont(new Font("Century Gothic", Font.BOLD, 20));
        maskIncAndDecCO.add(addMaskCO);

        removeMaskCO = new JButton("-");
        removeMaskCO.setBackground(new Color(255, 114,111));
        removeMaskCO.setFocusable(false);
        removeMaskCO.addActionListener(this::updateCart);
        removeMaskCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeMaskCO.setFont(new Font("Century Gothic", Font.BOLD, 20));
        maskIncAndDecCO.add(removeMaskCO);

        maskPanelCO.add(maskIncAndDecCO);

        //if user has item in their cart make it visible when they login
//        maskPanelCO.setVisible(userQuantity.get(1) > 0);

        if(userQuantity.get(1) > 0)
        checkOutProductsViewPanel.add(maskPanelCO);

        //set up the toothPaste CO panel
        toothPastePanelCO = new JPanel();

        toothPastePanelCO.setBackground(new Color(169,169,169));
        toothPastePanelCO.setLayout( new BoxLayout(toothPastePanelCO,BoxLayout.Y_AXIS));
        toothPastePanelCO.setSize(25,25);

        //spacer
        toothPastePanelCO.add(Box.createRigidArea(new Dimension(0, 15)));

        //image setup
        BufferedImage toothPasteIcon = null;
        try {
            toothPasteIcon = ImageIO.read(new File("Icons/toothPasteIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert toothPasteIcon != null;
        JLabel toothPasteIconLabelCO = new JLabel(new ImageIcon(toothPasteIcon));
        toothPasteIconLabelCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        toothPastePanelCO.add(toothPasteIconLabelCO);

        //set up name, price and quantity labels
        JLabel toothPasteNameCO = new JLabel(Products.get(2).getName());
        toothPasteNameCO.setFont(new Font("Century Gothic", Font.BOLD, 20));
        toothPasteNameCO.setForeground(Color.black);
        toothPasteNameCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        toothPastePanelCO.add(toothPasteNameCO);

        JLabel toothPastePriceCO = new JLabel("Price: " + formatter.format(Products.get(2).getSellPrice()));
        toothPastePriceCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        toothPastePriceCO.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        toothPastePriceCO.setForeground(Color.WHITE);
        toothPastePanelCO.add(toothPastePriceCO);

        toothPasteQuantityCO = new JLabel("Cart Quantity: " + userQuantity.get(2));
        toothPasteQuantityCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        toothPasteQuantityCO.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        toothPasteQuantityCO.setForeground(Color.white);
        toothPastePanelCO.add(toothPasteQuantityCO);

        toothPasteTotal = new JLabel("Item Total: " + formatter.format(Products.get(2).getSellPrice() * userQuantity.get(2)));
        toothPasteTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        toothPasteTotal.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        toothPasteTotal.setForeground(Color.WHITE);
        toothPastePanelCO.add(toothPasteTotal);

        //spacer
        toothPastePanelCO.add(Box.createRigidArea(new Dimension(0, 15)));

        //increment and decrement buttons
        JPanel toothPasteIncAndDecCO = new JPanel(new FlowLayout());
        toothPasteIncAndDecCO.setBackground(new Color(169,169,169));
        toothPasteIncAndDecCO.setSize(10,10);
        addtoothPasteCO = new JButton("+");
        addtoothPasteCO.setBackground(new Color(147,219,170));
        addtoothPasteCO.setFocusable(false);
        addtoothPasteCO.addActionListener(this::updateCart);
        addtoothPasteCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        addtoothPasteCO.setFont(new Font("Century Gothic", Font.BOLD, 20));
        toothPasteIncAndDecCO.add(addtoothPasteCO);

        removetoothPasteCO = new JButton("-");
        removetoothPasteCO.setBackground(new Color(255, 114,111));
        removetoothPasteCO.setFocusable(false);
        removetoothPasteCO.addActionListener(this::updateCart);
        removetoothPasteCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        removetoothPasteCO.setFont(new Font("Century Gothic", Font.BOLD, 20));
        toothPasteIncAndDecCO.add(removetoothPasteCO);

        toothPastePanelCO.add(toothPasteIncAndDecCO);

        //if user has item in their cart make it visible when they login
        //toothPastePanelCO.setVisible(userQuantity.get(2) > 0);

        if(userQuantity.get(2) > 0)
        checkOutProductsViewPanel.add(toothPastePanelCO);

        //set up the medication CO panel
        medicationPanelCO = new JPanel();

        medicationPanelCO.setBackground(new Color(169,169,169));
        medicationPanelCO.setLayout( new BoxLayout(medicationPanelCO,BoxLayout.Y_AXIS));
        medicationPanelCO.setSize(25,25);

        //spacer
        medicationPanelCO.add(Box.createRigidArea(new Dimension(0, 15)));

        //image setup
        BufferedImage medicationIcon = null;
        try {
            medicationIcon = ImageIO.read(new File("Icons/medicationIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert medicationIcon != null;
        JLabel medicationIconLabelCO = new JLabel(new ImageIcon(medicationIcon));
        medicationIconLabelCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        medicationPanelCO.add(medicationIconLabelCO);

        //set up name, price and quantity labels
        JLabel medicationNameCO = new JLabel(Products.get(3).getName());
        medicationNameCO.setFont(new Font("Century Gothic", Font.BOLD, 20));
        medicationNameCO.setForeground(Color.black);
        medicationNameCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        medicationPanelCO.add(medicationNameCO);

        JLabel medicationPriceCO = new JLabel("Price: " + formatter.format(Products.get(3).getSellPrice()));
        medicationPriceCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        medicationPriceCO.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        medicationPriceCO.setForeground(Color.WHITE);
        medicationPanelCO.add(medicationPriceCO);

        medicationQuantityCO = new JLabel("Cart Quantity: " + userQuantity.get(3));
        medicationQuantityCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        medicationQuantityCO.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        medicationQuantityCO.setForeground(Color.white);
        medicationPanelCO.add(medicationQuantityCO);

        medicationTotal = new JLabel("Item Total: " + formatter.format(Products.get(3).getSellPrice() * userQuantity.get(3)));
        medicationTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        medicationTotal.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        medicationTotal.setForeground(Color.WHITE);
        medicationPanelCO.add(medicationTotal);

        //spacer
        medicationPanelCO.add(Box.createRigidArea(new Dimension(0, 15)));

        //increment and decrement buttons
        JPanel medicationIncAndDecCO = new JPanel(new FlowLayout());
        medicationIncAndDecCO.setBackground(new Color(169,169,169));
        medicationIncAndDecCO.setSize(10,10);
        addMedicationCO = new JButton("+");
        addMedicationCO.setBackground(new Color(147,219,170));
        addMedicationCO.setFocusable(false);
        addMedicationCO.addActionListener(this::updateCart);
        addMedicationCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        addMedicationCO.setFont(new Font("Century Gothic", Font.BOLD, 20));
        medicationIncAndDecCO.add(addMedicationCO);

        removeMedicationCO = new JButton("-");
        removeMedicationCO.setBackground(new Color(255, 114,111));
        removeMedicationCO.setFocusable(false);
        removeMedicationCO.addActionListener(this::updateCart);
        removeMedicationCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeMedicationCO.setFont(new Font("Century Gothic", Font.BOLD, 20));
        medicationIncAndDecCO.add(removeMedicationCO);

        medicationPanelCO.add(medicationIncAndDecCO);

        //if user has item in their cart make it visible when they login
//        medicationPanelCO.setVisible(userQuantity.get(3) > 0);

        if(userQuantity.get(3) > 0)
        checkOutProductsViewPanel.add(medicationPanelCO);
    }

    void setUpPurchasePanel(){
        generalPurchasePanel = new JPanel();
        generalPurchasePanel.setLayout(null);
        generalPurchasePanel.setVisible(true);

        JLabel userNameLabel = new JLabel("Full Name: ");
        userNameLabel.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        userNameLabel.setForeground(Color.BLACK);
        userNameLabel.setBounds(20,20,userNameLabel.getPreferredSize().width, userNameLabel.getPreferredSize().height);
        generalPurchasePanel.add(userNameLabel);


        tempHeight = userNameLabel.getPreferredSize().height;

        JLabel userNumberLabel = new JLabel("Number: ");
        userNumberLabel.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        userNumberLabel.setForeground(Color.BLACK);
        userNumberLabel.setBounds(20,40 + tempHeight*1,userNameLabel.getPreferredSize().width, userNameLabel.getPreferredSize().height);
        generalPurchasePanel.add(userNumberLabel);


        JLabel addressLabel = new JLabel("Address: ");
        addressLabel.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        addressLabel.setForeground(Color.BLACK);
        addressLabel.setBounds(20,60 + tempHeight*2,userNameLabel.getPreferredSize().width, userNameLabel.getPreferredSize().height);
        generalPurchasePanel.add(addressLabel);

        JLabel CCLabel = new JLabel("CC Number: ");
        CCLabel.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        CCLabel.setForeground(Color.BLACK);
        CCLabel.setBounds(20,80 + tempHeight*3,CCLabel.getPreferredSize().width, userNameLabel.getPreferredSize().height);
        generalPurchasePanel.add(CCLabel);

        JLabel CCExp = new JLabel("CC Exp (MM/YY): ");
        CCExp.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        CCExp.setForeground(Color.BLACK);
        CCExp.setBounds(20,100 + tempHeight*4,CCExp.getPreferredSize().width, userNameLabel.getPreferredSize().height);
        generalPurchasePanel.add(CCExp);

        JLabel CCCSV = new JLabel("CC CCV Number: ");
        CCCSV.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        CCCSV.setForeground(Color.BLACK);
        CCCSV.setBounds(20,120 + tempHeight*5,CCCSV.getPreferredSize().width, CCCSV.getPreferredSize().height);
        generalPurchasePanel.add(CCCSV);

        purchaseTotal = new JLabel("Purchase Total: " + cartTotalVal);
        purchaseTotal.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        purchaseTotal.setForeground(Color.BLACK);
        purchaseTotal.setBounds(170,140 + tempHeight*6,purchaseTotal.getPreferredSize().width , purchaseTotal.getPreferredSize().height);
        generalPurchasePanel.add(purchaseTotal);

        //set up text entry fields
        userNameEntry = new JTextField(20);
        userNameEntry.setBounds(200,20,userNameEntry.getPreferredSize().width, userNameEntry.getPreferredSize().height);
        userNameEntry.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        userNameEntry.setBackground(new Color(169,169,169));
        generalPurchasePanel.add(userNameEntry);

        userNumber = new JTextField(20);
        userNumber.setBounds(200,40 + tempHeight,userNumber.getPreferredSize().width, userNumber.getPreferredSize().height);
        userNumber.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        userNumber.setBackground(new Color(169,169,169));
        generalPurchasePanel.add(userNumber);

        userAddress = new JTextField(20);
        userAddress.setBounds(200,60 + 2*tempHeight,userAddress.getPreferredSize().width, userAddress.getPreferredSize().height);
        userAddress.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        userAddress.setBackground(new Color(169,169,169));
        generalPurchasePanel.add(userAddress);

        CCNumber = new JTextField(20);
        CCNumber.setBounds(200,80 + 3*tempHeight,CCNumber.getPreferredSize().width, CCNumber.getPreferredSize().height);
        CCNumber.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        CCNumber.setBackground(new Color(169,169,169));
        generalPurchasePanel.add(CCNumber);

        CCExpiration = new JTextField(20);
        CCExpiration.setBounds(200,100 + 4*tempHeight,CCExpiration.getPreferredSize().width, CCExpiration.getPreferredSize().height);
        CCExpiration.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        CCExpiration.setBackground(new Color(169,169,169));
        generalPurchasePanel.add(CCExpiration);

        CCV = new JTextField(20);
        CCV.setBounds(200,120 + 5*tempHeight,CCV.getPreferredSize().width, CCV.getPreferredSize().height);
        CCV.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        CCV.setBackground(new Color(169,169,169));
        generalPurchasePanel.add(CCV);

        checkOutFields = new JTextField[]{userNameEntry, userAddress,CCNumber,CCV,CCExpiration,userNumber};

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e->checkPurchaseSubmission());
        submitButton.setFocusable(false);
        submitButton.setBounds(200,180 + tempHeight*7,purchaseTotal.getPreferredSize().width , purchaseTotal.getPreferredSize().height);
        submitButton.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        submitButton.setBackground(new Color(169,169,169));
        generalPurchasePanel.add(submitButton);
    }

    void showPurchase(ActionEvent evt){


        //set total label to black
        cartTotalLabel.setForeground(Color.black);
        cartTotalLabel.setFont(new Font("Century Gothic", Font.PLAIN, 18));

        if(evt.getSource() == buyHandSanitizer) {
            if (ProductsClone.get(0).getQuantity() == 0) { //if there is no more to add to cart
                handSanitizerName.setForeground(new Color(255, 11, 127));
                return;
            } else if (ProductsClone.get(0).getQuantity() == 1) {
                handSanitizerName.setForeground(new Color(255, 11, 127));
            }

//            handSanitizerPanelCO.setVisible(true);

            if(userQuantity.get(0) == 0){
                checkOutProductsViewPanel.add(handSanitizerPanelCO);
            }

            userQuantity.set(0, userQuantity.get(0) + 1);
            handSanitizerQuantityCO.setText("Cart Quantity: " + userQuantity.get(0));

            ProductsClone.get(0).setQuantity(ProductsClone.get(0).getQuantity() - 1);
            handSanitizerQuantity.setText("Quantity: " + (ProductsClone.get(0).getQuantity()));

            handSanitizerTotal.setText("Total: " + formatter.format(Products.get(0).getSellPrice() * userQuantity.get(0)));
        }else if(evt.getSource() == buyMask){
            if (ProductsClone.get(1).getQuantity() == 0) { //if there is no more to add to cart
                maskName.setForeground(new Color(255, 11, 127));
                return;
            } else if (ProductsClone.get(1).getQuantity() == 1) {
                maskName.setForeground(new Color(255, 11, 127));
            }

//            maskPanelCO.setVisible(true);
            if(userQuantity.get(1) == 0){
                checkOutProductsViewPanel.add(maskPanelCO);
            }

            userQuantity.set(1, userQuantity.get(1) + 1);
            maskQuantityCO.setText("Cart Quantity: " + userQuantity.get(1));

            ProductsClone.get(1).setQuantity(ProductsClone.get(1).getQuantity() - 1);
            maskQuantity.setText("Quantity: " + (ProductsClone.get(1).getQuantity()));

            maskTotal.setText("Total: " + formatter.format(Products.get(1).getSellPrice() * userQuantity.get(1)));
        }else if(evt.getSource() == buytoothPaste){
            if (ProductsClone.get(2).getQuantity() == 0) { //if there is no more to add to cart
                toothPasteName.setForeground(new Color(255, 11, 127));
                return;
            } else if (ProductsClone.get(2).getQuantity() == 1) {
                toothPasteName.setForeground(new Color(255, 11, 127));
            }

//            toothPastePanelCO.setVisible(true);
            if(userQuantity.get(2) == 0){
                checkOutProductsViewPanel.add(toothPastePanelCO);
            }

            userQuantity.set(2, userQuantity.get(2) + 1);
            toothPasteQuantityCO.setText("Cart Quantity: " + userQuantity.get(2));

            ProductsClone.get(2).setQuantity(ProductsClone.get(2).getQuantity() - 1);
            toothPasteQuantity.setText("Quantity: " + (ProductsClone.get(2).getQuantity()));

            toothPasteTotal.setText("Total: " + formatter.format(Products.get(2).getSellPrice() * userQuantity.get(2)));
        }else if(evt.getSource() == buyMedication){
            if (ProductsClone.get(3).getQuantity() == 0) { //if there is no more to add to cart
                medicationName.setForeground(new Color(255, 11, 127));
                return;
            } else if (ProductsClone.get(3).getQuantity() == 1) {
                medicationName.setForeground(new Color(255, 11, 127));
            }

//            medicationPanelCO.setVisible(true);

            if(userQuantity.get(3) == 0){
                checkOutProductsViewPanel.add(medicationPanelCO);
            }

            userQuantity.set(3, userQuantity.get(3) + 1);
            medicationQuantityCO.setText("Cart Quantity: " + userQuantity.get(3));

            ProductsClone.get(3).setQuantity(ProductsClone.get(3).getQuantity() - 1);
            medicationQuantity.setText("Quantity: " + (ProductsClone.get(3).getQuantity()));

            medicationTotal.setText("Total: " + formatter.format(Products.get(3).getSellPrice() * userQuantity.get(3)));
        }

        updateTotal();
    }

    void updateCart(ActionEvent evt){

        if(userQuantity.get(0) == 0 && evt.getSource() == removeHandSanitizerCO){
            return;
        }else if(userQuantity.get(0) >= Products.get(0).getQuantity() && evt.getSource() == addHandSanitizerCO){
            return;
        }else if(userQuantity.get(1) == 0 && evt.getSource() == removeMaskCO){
            return;
        }else if(userQuantity.get(1) >= Products.get(1).getQuantity() && evt.getSource() == addMaskCO){
            return;
        }else if(userQuantity.get(2) == 0 && evt.getSource() == removetoothPasteCO){
            return;
        }else if(userQuantity.get(2) >= Products.get(2).getQuantity() && evt.getSource() == addtoothPasteCO){
            return;
        }else if(userQuantity.get(3) == 0 && evt.getSource() == removeMedicationCO){
            return;
        }else if(userQuantity.get(3) >= Products.get(3).getQuantity() && evt.getSource() == addMedicationCO){
            return;
        }

        if(evt.getSource() == addHandSanitizerCO) {

            if(ProductsClone.get(0).getQuantity() == 1){
                handSanitizerName.setForeground(new Color(255, 11, 127));
            }
            userQuantity.set(0,userQuantity.get(0) + 1);
            handSanitizerQuantityCO.setText("Cart Quantity: " + userQuantity.get(0));

            ProductsClone.get(0).setQuantity(ProductsClone.get(0).getQuantity() - 1);
            handSanitizerQuantity.setText("Quantity: " + (ProductsClone.get(0).getQuantity()));
        }else if(evt.getSource() == removeHandSanitizerCO){

            if(userQuantity.get(0) == 1){
                checkOutProductsViewPanel.remove(handSanitizerPanelCO);
                checkOutProductsViewPanel.repaint();

                if(checkOutProductsViewPanel.getComponentCount() == 0){
                    showProductsPage();
                }
            }

            handSanitizerName.setForeground(new Color(0,255,127));
            userQuantity.set(0,userQuantity.get(0) - 1);
            handSanitizerQuantityCO.setText("Cart Quantity: " + userQuantity.get(0));
            ProductsClone.get(0).setQuantity(ProductsClone.get(0).getQuantity() + 1);
            handSanitizerQuantity.setText("Quantity: " + (ProductsClone.get(0).getQuantity()));
        }else if(evt.getSource() == addMaskCO) {

            if(ProductsClone.get(1).getQuantity() == 1){
                maskName.setForeground(new Color(255, 11, 127));
            }

            userQuantity.set(1,userQuantity.get(1) + 1);
            maskQuantityCO.setText("Cart Quantity: " + userQuantity.get(1));

            ProductsClone.get(1).setQuantity(ProductsClone.get(1).getQuantity() - 1);
            maskQuantity.setText("Quantity: " + (ProductsClone.get(1).getQuantity()));
        }else if(evt.getSource() == removeMaskCO){

            if(userQuantity.get(1) == 1){
                checkOutProductsViewPanel.remove(maskPanelCO);
                checkOutProductsViewPanel.repaint();

                if(checkOutProductsViewPanel.getComponentCount() == 0){
                    showProductsPage();
                }
            }

            maskName.setForeground(new Color(0,255,127));
            userQuantity.set(1,userQuantity.get(1) - 1);
            maskQuantityCO.setText("Cart Quantity: " + userQuantity.get(1));
            ProductsClone.get(1).setQuantity(ProductsClone.get(1).getQuantity() + 1);
            maskQuantity.setText("Quantity: " + (ProductsClone.get(1).getQuantity()));
        }else if(evt.getSource() == addtoothPasteCO) {

            if(ProductsClone.get(2).getQuantity() == 1){
                toothPasteName.setForeground(new Color(255, 11, 127));
            }

            userQuantity.set(2,userQuantity.get(2) + 1);
            toothPasteQuantityCO.setText("Cart Quantity: " + userQuantity.get(2));

            ProductsClone.get(2).setQuantity(ProductsClone.get(2).getQuantity() - 1);
            toothPasteQuantity.setText("Quantity: " + (ProductsClone.get(2).getQuantity()));
        }else if(evt.getSource() == removetoothPasteCO){

            if(userQuantity.get(2) == 1){
                checkOutProductsViewPanel.remove(toothPastePanelCO);
                checkOutProductsViewPanel.repaint();

                if(checkOutProductsViewPanel.getComponentCount() == 0){
                    showProductsPage();
                }
            }

            toothPasteName.setForeground(new Color(0,255,127));
            userQuantity.set(2,userQuantity.get(2) - 1);
            toothPasteQuantityCO.setText("Cart Quantity: " + userQuantity.get(2));
            ProductsClone.get(2).setQuantity(ProductsClone.get(2).getQuantity() + 1);
            toothPasteQuantity.setText("Quantity: " + (ProductsClone.get(2).getQuantity()));
        }else if(evt.getSource() == addMedicationCO) {

            if(ProductsClone.get(3).getQuantity() == 1){
                medicationName.setForeground(new Color(255, 11, 127));
            }

            userQuantity.set(3,userQuantity.get(3) + 1);
            medicationQuantityCO.setText("Cart Quantity: " + userQuantity.get(3));

            ProductsClone.get(3).setQuantity(ProductsClone.get(3).getQuantity() - 1);
            medicationQuantity.setText("Quantity: " + (ProductsClone.get(3).getQuantity()));
        }else if(evt.getSource() == removeMedicationCO){

            if(userQuantity.get(3) == 1){
                checkOutProductsViewPanel.remove(medicationPanelCO);
                checkOutProductsViewPanel.repaint();

                if(checkOutProductsViewPanel.getComponentCount() == 0){
                    showProductsPage();
                }
            }

            medicationName.setForeground(new Color(0,255,127));
            userQuantity.set(3,userQuantity.get(3) - 1);
            medicationQuantityCO.setText("Cart Quantity: " + userQuantity.get(3));
            ProductsClone.get(3).setQuantity(ProductsClone.get(3).getQuantity() + 1);
            medicationQuantity.setText("Quantity: " + (ProductsClone.get(3).getQuantity()));
        }


        handSanitizerTotal.setText("Total: " + formatter.format(Products.get(0).getSellPrice() * userQuantity.get(0)));
        maskTotal.setText("Total: " + formatter.format(Products.get(1).getSellPrice() * userQuantity.get(1)));
        toothPasteTotal.setText("Total: " + formatter.format(Products.get(2).getSellPrice() * userQuantity.get(2)));
        medicationTotal.setText("Total: " + formatter.format(Products.get(3).getSellPrice() * userQuantity.get(3)));

        updateTotal();
    }

    void updateTotal(){

        cartTotalLabel.setForeground(Color.black);
        cartTotalLabel.setFont(new Font("Century Gothic", Font.PLAIN, 18));

        int i = 0;
        cartTotalVal = 0;
        for(int val: userQuantity){
            cartTotalVal += (val*Products.get(i++).getSellPrice());
        }


        cartTotalLabel.setText("Total: " + formatter.format(cartTotalVal));
    }

    void showProductsPage(){
        productFrame.setSize(587,775);
        cardLayout.show(switchPanel, productsPage);
        isOnProductsPage = true;
    }

    void showCheckOutPage(){
        productFrame.setSize(587,775);
        cardLayout.show(switchPanel, checkOutPage);
        isOnProductsPage = false;
    }

    void checkPurchaseSubmission(){
        boolean valid = true;
        //check for empty fields
        for(JTextField n: checkOutFields) {

            if (n.getText().length() == 0) {
                n.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                valid = false; //not valid as something is empty
            } else {
                n.setBorder(new LineBorder(Color.BLACK, 0));
            }
        }
            if(!valid){ //if not valid exit function
                return;
            }

            //if valid go ahead
            checkedOut = true;
            HashMap<Product, Integer> userPurchases = new HashMap<>();
            int i = 0;
            for(int val: userQuantity){ //for every value in the list of user quantities
                if(val != 0){ //if value is not 0 (they bought the product)
                    userPurchases.put(Products.get(i), val); //add product and amount to purchases.
                }

                i++; //increment the counter so it matches the right product
            }
            purchaseList.add(new Purchase(userNameEntry.getText(),cartTotalVal, userPurchases));

            clip.play();

            purchaseTotal.setText("Purchase Successful");
            purchaseTotal.setFont(new Font("Century Gothic", Font.PLAIN, 24));
            purchaseTotal.setBounds(170,140 + tempHeight*6,purchaseTotal.getPreferredSize().width , purchaseTotal.getPreferredSize().height);
            purchaseTotal.setForeground(new Color(147,219,170));



            Timer timer = new Timer(TIMER_DELAY,this::finalizeTransaction);
            timer.setRepeats(false);
            timer.start();

            timer = new Timer(3000,this::logOut);
            timer.setRepeats(false);
            timer.start();
        }


    void logOut(ActionEvent actionEvent){
        productFrame.dispose();

        if(!checkedOut) { //if user did not check out
            //serialize their session
            try {
                ObjectOutputStream out = new ObjectOutputStream(
                        new FileOutputStream("Serialized/User(" + userName + ").dat"));

                out.writeObject(ProductsClone);
                out.writeObject(userQuantity);
                out.writeDouble(cartTotalVal);

                out.close();

                System.out.println("User " + userName + " serialized.\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("Serialized/purchases.dat"));

            out.writeObject(purchaseList);

            out.close();

            System.out.println("Purchases serialized by product frame.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("Serialized/products.dat"));

            out.writeObject(Products);

            out.close();

            System.out.println("products serialized by product frame.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        LoginData data = new LoginData();
        LoginFrame frame = new LoginFrame(data);
        LoginController controller = new LoginController(data, frame);
    }

    //function to make sure everything syncs after purchase
    void finalizeTransaction(ActionEvent actionEvent){

        int i = 0;
        for(int val: userQuantity){
            if(val != 0){ //if user bought item
                //update the quantity by how much they bought
                System.out.println("\nBefore: " + Products.get(i).getQuantity());
                Products.get(i).setQuantity(Products.get(i).getQuantity() - val);
                System.out.println("After: " + Products.get(i).getQuantity());
                System.out.println("Updated " + Products.get(i).getName() + " Count");
            }
            i++;
        }

        //delete the serialized file as we do not need to continue session
//        try {
//
//            Files.delete(Path.of("User(" + userName + ").dat"));
//        }catch(Exception e){
//            e.printStackTrace();
//        }

    }

    void quantityCheck(){
        int i = 0;
        for(int val: userQuantity){
            if(val + ProductsClone.get(i).getQuantity() != Products.get(i).getQuantity()){
                //if there is a difference in quantities

                ProductsClone = model.getProductsClone();
                userQuantity = new ArrayList<>(Arrays.asList(0,0,0,0));
                cartTotalVal = 0;
                return;

            }
            i++;
        }
    }
    ArrayList<Product> Products, ProductsClone;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    java.applet.AudioClip clip;
    JFrame productFrame;
    JPanel productTopPanel;
    JPanel generalProductsPanel;
    JButton checkoutButton;
    JButton productsButton;
    JButton logoutButton;
    private final CardLayout cardLayout;
    private final JPanel switchPanel;
    public final String productsPage = "Products Page";
    public final String checkOutPage = "Checkout Page";
    public final String purchasePage = "Purchase Page";
    boolean isOnProductsPage;
    String userName;
    int tempHeight;
    ProductModel model;

    JPanel productsViewPanel;
        JPanel handSanitizerPanel;
            JLabel handSanitizerName;
            JLabel handSanitizerPrice;
            JLabel handSanitizerQuantity;
            JButton buyHandSanitizer;
        JPanel maskPanel;
            JLabel maskName;
            JLabel maskPrice;
            JLabel maskQuantity;
            JButton buyMask;
        JPanel toothPastePanel;
            JLabel toothPasteName;
            JLabel toothPastePrice;
            JLabel toothPasteQuantity;
            JButton buytoothPaste;
        JPanel medicationPanel;
            JLabel medicationName;
            JLabel medicationPrice;
            JLabel medicationQuantity;
            JButton buyMedication;
    JPanel checkOutProductsViewPanel;
        JButton confirmCheckOutButton;
        JLabel cartTotalLabel;
        double cartTotalVal;
        JPanel  handSanitizerPanelCO;
            JLabel handSanitizerQuantityCO;
            JButton addHandSanitizerCO, removeHandSanitizerCO;
            JLabel handSanitizerTotal;
        JPanel maskPanelCO;
            JLabel maskQuantityCO;
            JButton addMaskCO, removeMaskCO;
            JLabel maskTotal;
        JPanel toothPastePanelCO;
            JLabel toothPasteQuantityCO;
            JButton addtoothPasteCO, removetoothPasteCO;
            JLabel toothPasteTotal;
        JPanel medicationPanelCO;
            JLabel medicationQuantityCO;
            JButton addMedicationCO, removeMedicationCO;
            JLabel medicationTotal;
        JPanel generalPurchasePanel;
            JTextField userNameEntry;
            JTextField userAddress;
            JTextField CCNumber;
            JTextField CCV;
            JTextField CCExpiration;
            JTextField userNumber;
            JTextField[] checkOutFields;
            JButton submitButton;
    JLabel purchaseTotal;
    private static final int TIMER_DELAY = 3000 ;


    ArrayList<Integer> userQuantity;
    ArrayList<Purchase> purchaseList;
    boolean checkedOut;

}
