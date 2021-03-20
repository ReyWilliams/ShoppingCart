package com.shoppingcart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class ProductFrame {

    public ProductFrame(ProductModel model){

        //initialize arraylist
        Products = model.getProducts();

        //setup product frame w/ parameters
        productFrame = new JFrame();

        productFrame.setSize(875,687);
        productFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        productFrame.setLayout(new BorderLayout());

        switchPanel = new JPanel();
//        switchPanel.setSize(875,687);
        cardLayout = new CardLayout();
        switchPanel.setLayout(cardLayout);

        generalProductsPanel = new JPanel(new BorderLayout());
//        generalProductsPanel.setSize(875,687);

        //setup top panel which will hold the buttons users will uses to switch between views
        productTopPanel = new JPanel(new FlowLayout());
        checkoutButton = new JButton("Checkout Items");
        checkoutButton.setFocusable(false);
        productsButton = new JButton("View Products");
        productsButton.setFocusable(false);

        //add the bottoms to the top panel
        productTopPanel.add(checkoutButton);
        productTopPanel.add(productsButton);

        //add the top panel to the north of the general panel
        generalProductsPanel.add(productTopPanel, BorderLayout.NORTH);

        //setup the products view panel which will be a grid
        productsViewPanel = new JPanel(new GridLayout(2,2,10,10));
        setUpProductsView(productsViewPanel);

        //add the products to the general products panel at the south
        generalProductsPanel.add(productsViewPanel,BorderLayout.CENTER);

        //small testing
        JPanel testPanel = new JPanel();
        JLabel Testing = new JLabel("testing");
        testPanel.add(Testing);

        switchPanel.add(productsPage, generalProductsPanel);
        switchPanel.add(checkOutPage, testPanel);
        showProductsPage();

        productFrame.add(switchPanel);

        productFrame.setVisible(true);

    }

    void setUpProductsView(JPanel productsViewPanel){
        //set up the hand sanitizer panel
        handSantizerPanel = new JPanel();
        handSantizerPanel.setLayout( new BoxLayout(handSantizerPanel,BoxLayout.Y_AXIS));
        handSanitizerName = new JLabel(Products.get(0).getName());
        handSantizerPanel.add(handSanitizerName);
        handSantizerPrice = new JLabel(String.valueOf(Products.get(0).getSellPrice()));
        handSantizerPanel.add(handSantizerPrice);
        handSantizierQuantity = new JLabel(String.valueOf(Products.get(0).getQuantity()));
        buyHandSanitizer = new JButton("ADD TO CART");

        productsViewPanel.add(handSantizerPanel);

    }

    void swtichPanels(){
        if(isOnProductsPage){
            showCheckOutPage();
        }else{
            showProductsPage();
        }
    }

    void showProductsPage(){
        cardLayout.show(switchPanel, productsPage);
        isOnProductsPage = true;
    }

    void showCheckOutPage(){
        cardLayout.show(switchPanel, checkOutPage);
        isOnProductsPage = false;
    }

    ArrayList<Product> Products;
    JFrame productFrame;
    JPanel productTopPanel;
    JPanel productsViewPanel;
        JPanel handSantizerPanel;
            JLabel handSanitizerName;
            JLabel handSantizerPrice;
            JLabel handSantizierQuantity;
            JButton buyHandSanitizer;
        JPanel Banana;
        JPanel Television;
        JPanel Water;
    JPanel generalProductsPanel;
    JButton checkoutButton;
    JButton productsButton;
    private final CardLayout cardLayout;
    private final JPanel switchPanel;
    public final String productsPage = "Products Page";
    public final String checkOutPage = "Checkout Page";
    boolean isOnProductsPage;



}
