package com.shoppingcart;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;


public class ProductFrame {

    public ProductFrame(ProductModel model, String userName){

        //get username
        this.userName = userName;

        //initialize arraylist
        Products = model.getProducts();

        //deserialize objects
        File tmpDir = new File("User(" + userName + ").dat");
        boolean exists = tmpDir.exists();

        if(exists) { //user has file
            try { //try to deserialize

                ObjectInputStream in = new ObjectInputStream(
                        new FileInputStream("User(" + userName + ").dat"));

                ProductsClone = (ArrayList<Product>) in.readObject();
                userQuantity = (ArrayList<Integer>) in.readObject();
                in.close();

                System.out.println("User " + userName + " deserialized");

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{ //choose default values
            ProductsClone = model.getProductsClone();
            userQuantity = new ArrayList<>(Arrays.asList(0,0,0,0));
        }

        //setup product frame w/ parameters
        productFrame = new JFrame();

        productFrame.setSize(587,725);
        productFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        productFrame.setLayout(new BorderLayout(20,20));
        productFrame.setTitle("Welcome, " + userName);

        switchPanel = new JPanel();
//        switchPanel.setSize(875,687);
        cardLayout = new CardLayout();
        switchPanel.setLayout(cardLayout);

        generalProductsPanel = new JPanel(new BorderLayout(50,50));
//        generalProductsPanel.setSize(875,687);

        //setup top panel which will hold the buttons users will uses to switch between views and logout
        productTopPanel = new JPanel(new FlowLayout());
        checkoutButton = new JButton("Checkout Items");
        checkoutButton.addActionListener(e -> showCheckOutPage());
        checkoutButton.setFocusable(false);
        productsButton = new JButton("View Products");
        productsButton.setFocusable(false);
        productsButton.addActionListener(e -> showProductsPage());
        logoutButton = new JButton("Logout");
        logoutButton.setFocusable(false);
        logoutButton.addActionListener(e -> logOut());

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

        //add spacers to the left and right
        generalProductsPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.EAST);
        generalProductsPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.WEST);

        setUpProductsView(productsViewPanel);
        productsViewPanel.add(new JLabel("Testing"));

        //checkout Panel
        JPanel generalCheckOutPanel = new JPanel(new BorderLayout(50,50));

        //confirm button
        JPanel checkOutTopPanel = new JPanel(new FlowLayout());
        confirmCheckOutButton = new JButton("Confirm Checkout");
        confirmCheckOutButton.setFocusable(false);
        checkOutTopPanel.add(confirmCheckOutButton);
        generalCheckOutPanel.add(checkOutTopPanel, BorderLayout.NORTH);




        //checkout panel products
        checkOutProductsViewPanel = new JPanel(new GridLayout(2,2,20,20));


        //spacers
        //add spacers to the left and right
        generalCheckOutPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.EAST);
        generalCheckOutPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.WEST);


        generalCheckOutPanel.add(checkOutProductsViewPanel, BorderLayout.CENTER);

        //setup products
        setUpCheckOutProductsView(checkOutProductsViewPanel);

        checkOutProductsViewPanel.add(new JLabel("testing"));



        //set up switch panel
        switchPanel.add(productsPage, generalProductsPanel);
        switchPanel.add(checkOutPage, generalCheckOutPanel);
        showProductsPage();

        //finalize frame
        productFrame.add(switchPanel);
        productFrame.setVisible(true);
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
            handSanitizerIcon = ImageIO.read(new File("./handSanitizerIcon.png"));
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
            maskIcon = ImageIO.read(new File("./maskIcon.png"));
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
            toothPasteIcon = ImageIO.read(new File("./toothPasteIcon.png"));
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
        buytoothPaste.setAlignmentX(Component.CENTER_ALIGNMENT);
        buytoothPaste.setFont(new Font("Century Gothic", Font.BOLD, 12));
        buytoothPaste.addActionListener(this::showPurchase);
        toothPastePanel.add(buytoothPaste);

        productsViewPanel.add(toothPastePanel);

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
            handSanitizerIcon = ImageIO.read(new File("./handSanitizerIcon.png"));
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
        handSanitizerPanelCO.add(Box.createRigidArea(new Dimension(0, 30)));

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
        handSanitizerPanelCO.setVisible(userQuantity.get(0) > 0);
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
            maskIcon = ImageIO.read(new File("./maskIcon.png"));
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
        maskPanelCO.add(Box.createRigidArea(new Dimension(0, 30)));

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
        maskPanelCO.setVisible(userQuantity.get(1) > 0);

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
            toothPasteIcon = ImageIO.read(new File("./toothPasteIcon.png"));
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
        toothPastePanelCO.add(Box.createRigidArea(new Dimension(0, 30)));

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
        toothPastePanelCO.setVisible(userQuantity.get(2) > 0);

        checkOutProductsViewPanel.add(toothPastePanelCO);
    }



    void showPurchase(ActionEvent evt){

        //check validity for hand sanitizer
        if(evt.getSource() == buyHandSanitizer) {
            if (ProductsClone.get(0).getQuantity() == 0) { //if there is no more to add to cart
                handSanitizerName.setForeground(new Color(255, 11, 127));
                return;
            } else if (ProductsClone.get(0).getQuantity() == 1) {
                handSanitizerName.setForeground(new Color(255, 11, 127));
            }

            handSanitizerPanelCO.setVisible(true);
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

            maskPanelCO.setVisible(true);
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

            toothPastePanelCO.setVisible(true);
            userQuantity.set(2, userQuantity.get(2) + 1);
            toothPasteQuantityCO.setText("Cart Quantity: " + userQuantity.get(2));

            ProductsClone.get(2).setQuantity(ProductsClone.get(2).getQuantity() - 1);
            toothPasteQuantity.setText("Quantity: " + (ProductsClone.get(2).getQuantity()));

            toothPasteTotal.setText("Total: " + formatter.format(Products.get(2).getSellPrice() * userQuantity.get(2)));
        }
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
        }

        if(evt.getSource() == addHandSanitizerCO) {
            userQuantity.set(0,userQuantity.get(0) + 1);
            handSanitizerQuantityCO.setText("Cart Quantity: " + userQuantity.get(0));

            ProductsClone.get(0).setQuantity(ProductsClone.get(0).getQuantity() - 1);
            handSanitizerQuantity.setText("Quantity: " + (ProductsClone.get(0).getQuantity()));
        }else if(evt.getSource() == removeHandSanitizerCO){
            handSanitizerName.setForeground(new Color(0,255,127));
            userQuantity.set(0,userQuantity.get(0) - 1);
            handSanitizerQuantityCO.setText("Cart Quantity: " + userQuantity.get(0));
            ProductsClone.get(0).setQuantity(ProductsClone.get(0).getQuantity() + 1);
            handSanitizerQuantity.setText("Quantity: " + (ProductsClone.get(0).getQuantity()));
        }else if(evt.getSource() == addMaskCO) {
            userQuantity.set(1,userQuantity.get(1) + 1);
            maskQuantityCO.setText("Cart Quantity: " + userQuantity.get(1));

            ProductsClone.get(1).setQuantity(ProductsClone.get(1).getQuantity() - 1);
            maskQuantity.setText("Quantity: " + (ProductsClone.get(1).getQuantity()));
        }else if(evt.getSource() == removeMaskCO){
            maskName.setForeground(new Color(0,255,127));
            userQuantity.set(1,userQuantity.get(1) - 1);
            maskQuantityCO.setText("Cart Quantity: " + userQuantity.get(1));
            ProductsClone.get(1).setQuantity(ProductsClone.get(1).getQuantity() + 1);
            maskQuantity.setText("Quantity: " + (ProductsClone.get(1).getQuantity()));
        }else if(evt.getSource() == addtoothPasteCO) {
            userQuantity.set(2,userQuantity.get(2) + 1);
            toothPasteQuantityCO.setText("Cart Quantity: " + userQuantity.get(2));

            ProductsClone.get(2).setQuantity(ProductsClone.get(2).getQuantity() - 1);
            toothPasteQuantity.setText("Quantity: " + (ProductsClone.get(2).getQuantity()));
        }else if(evt.getSource() == removetoothPasteCO){
            toothPasteName.setForeground(new Color(0,255,127));
            userQuantity.set(2,userQuantity.get(2) - 1);
            toothPasteQuantityCO.setText("Cart Quantity: " + userQuantity.get(2));
            ProductsClone.get(2).setQuantity(ProductsClone.get(2).getQuantity() + 1);
            toothPasteQuantity.setText("Quantity: " + (ProductsClone.get(2).getQuantity()));
        }

        handSanitizerTotal.setText("Total: " + formatter.format(Products.get(0).getSellPrice() * userQuantity.get(0)));
        maskTotal.setText("Total: " + formatter.format(Products.get(1).getSellPrice() * userQuantity.get(1)));
        toothPasteTotal.setText("Total: " + formatter.format(Products.get(2).getSellPrice() * userQuantity.get(2)));
    }



    void showProductsPage(){
        cardLayout.show(switchPanel, productsPage);
        isOnProductsPage = true;
    }

    void showCheckOutPage(){
        cardLayout.show(switchPanel, checkOutPage);
        isOnProductsPage = false;
    }

    void logOut(){
        productFrame.dispose();

        try{
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("User(" + userName + ").dat"));

            out.writeObject(ProductsClone);
            out.writeObject(userQuantity);

            out.close();

            System.out.println("User " + userName + " serialized.\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        LoginData data = new LoginData();
        LoginFrame frame = new LoginFrame(data);
        LoginController controller = new LoginController(data, frame);
    }

    ArrayList<Product> Products, ProductsClone;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
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
    boolean isOnProductsPage;
    String userName;

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
    JPanel checkOutProductsViewPanel;
        JButton confirmCheckOutButton;
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









    ArrayList<Integer> userQuantity;






}
