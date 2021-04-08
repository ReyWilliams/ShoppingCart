package com.shoppingcart;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EventListener;


public class ProductFrame {

    public ProductFrame(ProductModel model, String userName){

        //initialize arraylist
        Products = model.getProducts();
        ProductsClone = model.getProductsClone();

        //setup product frame w/ parameters
        productFrame = new JFrame();

        productFrame.setSize(587,675);
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



        productFrame.add(productTopPanel, BorderLayout.NORTH);

        //add the top panel to the north of the general panel
//        generalProductsPanel.add(productTopPanel, BorderLayout.NORTH);

        //setup the products view panel which will be a grid
        productsViewPanel = new JPanel(new GridLayout(2,2,20,20));

        //add the products to the general products panel at the south
        generalProductsPanel.add(productsViewPanel,BorderLayout.CENTER);

        //add spacers to the left and right
        generalProductsPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.EAST);
        generalProductsPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.WEST);

        setUpProductsView(productsViewPanel);
        productsViewPanel.add(new JLabel("Testing"));
        productsViewPanel.add(new JLabel("Testing"));
        productsViewPanel.add(new JLabel("Testing"));

        //checkout Panel
        JPanel checkOutPanel = new JPanel(new BorderLayout(20,20));

        //top checkout panel setup
        JPanel checkOutTopPanel = new JPanel(new FlowLayout());
        //confirm button
        JButton confirmCheckOutButton = new JButton("Confirm Checkout");
        confirmCheckOutButton.setFocusable(false);
        checkOutTopPanel.add(confirmCheckOutButton);
        checkOutPanel.add(checkOutTopPanel, BorderLayout.NORTH);

        //checkout panel products
        checkOutProductsViewPanel = new JPanel(new GridLayout(2,2,20,20));
        checkOutPanel.add(checkOutProductsViewPanel, BorderLayout.CENTER);


        //spacers
        //add spacers to the left and right
        checkOutPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.EAST);
        checkOutPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.WEST);
        setUpCheckOutProductsView(checkOutProductsViewPanel);

        //set up switch panel
        switchPanel.add(productsPage, generalProductsPanel);
        switchPanel.add(checkOutPage, checkOutPanel);
        showProductsPage();

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
        buyHandSanitizer.addActionListener(e -> showHandSanitizerPurchase());
        handSanitizerPanel.add(buyHandSanitizer);

        productsViewPanel.add(handSanitizerPanel);

    }

    void setUpCheckOutProductsView(JPanel checkOutProductsViewPanel){

        //set up the hand sanitizer panel
        handSanitizerPanelCO = new JPanel(){
            protected void paint(){
                handSanitizerQuantityCO.setText("Cart Quantity: " + userQuantityHS);
                setBackground(new Color(169,169,169));
            }
        };


        handSanitizerPanelCO.setBackground(new Color(169,169,169));
        handSanitizerPanelCO.setLayout( new BoxLayout(handSanitizerPanelCO,BoxLayout.Y_AXIS));
        handSanitizerPanelCO.setSize(25,25);

        //spacer
        handSanitizerPanelCO.add(Box.createRigidArea(new Dimension(0, 30)));

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

        handSanitizerQuantityCO = new JLabel("Cart Quantity: " + userQuantityHS);
        handSanitizerQuantityCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        handSanitizerQuantityCO.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        handSanitizerQuantityCO.setForeground(Color.white);
        handSanitizerPanelCO.add(handSanitizerQuantityCO);

        //spacer
        handSanitizerPanelCO.add(Box.createRigidArea(new Dimension(0, 30)));

        //increment and decrement buttons
        JPanel handSanitizerIncandDecCO = new JPanel(new FlowLayout());
        handSanitizerIncandDecCO.setBackground(new Color(169,169,169));
        addHandSanitizerCO = new JButton("+");
        addHandSanitizerCO.setBackground(new Color(147,219,170));
        addHandSanitizerCO.setFocusable(false);
        addHandSanitizerCO.addActionListener(e->updateHSCart(e));
        addHandSanitizerCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        addHandSanitizerCO.setFont(new Font("Century Gothic", Font.BOLD, 20));
        handSanitizerIncandDecCO.add(addHandSanitizerCO);

        removeHandSanitizerCO = new JButton("-");
        removeHandSanitizerCO.setBackground(new Color(255, 114,111));
        removeHandSanitizerCO.setFocusable(false);
        removeHandSanitizerCO.addActionListener(e->updateHSCart(e));
        removeHandSanitizerCO.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeHandSanitizerCO.setFont(new Font("Century Gothic", Font.BOLD, 20));
        handSanitizerIncandDecCO.add(removeHandSanitizerCO);

        handSanitizerPanelCO.add(handSanitizerIncandDecCO);


        handSanitizerPanelCO.setVisible(false);
        checkOutProductsViewPanel.add(handSanitizerPanelCO);


    }



    void showHandSanitizerPurchase(){

        if(ProductsClone.get(0).getQuantity() == 0){ //if there is no more to add to cart
            handSanitizerName.setForeground(new Color(255,11,127));
            return;
        }else if(ProductsClone.get(0).getQuantity() == 1){
            handSanitizerName.setForeground(new Color(255,11,127));
        }


        handSanitizerPanelCO.setVisible(true);
        handSanitizerQuantityCO.setText("Cart Quantity: " + ++userQuantityHS);

        ProductsClone.get(0).setQuantity(ProductsClone.get(0).getQuantity() - 1);
        handSanitizerQuantity.setText("Quantity: " + (ProductsClone.get(0).getQuantity()));
    }

    void updateHSCart(ActionEvent evt){
        if(userQuantityHS == 0 && evt.getSource() == removeHandSanitizerCO){
            return;
        }else if(userQuantityHS >= Products.get(0).getQuantity() && evt.getSource() == addHandSanitizerCO){
            return;
        }

        if(evt.getSource() == addHandSanitizerCO) {
            handSanitizerQuantityCO.setText("Cart Quantity: " + ++userQuantityHS);

            ProductsClone.get(0).setQuantity(ProductsClone.get(0).getQuantity() - 1);
            handSanitizerQuantity.setText("Quantity: " + (ProductsClone.get(0).getQuantity()));
        }else{
            handSanitizerName.setForeground(new Color(0,255,127));
            handSanitizerQuantityCO.setText("Cart Quantity: " + --userQuantityHS);
            ProductsClone.get(0).setQuantity(ProductsClone.get(0).getQuantity() + 1);
            handSanitizerQuantity.setText("Quantity: " + (ProductsClone.get(0).getQuantity()));

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

    void logOut(){
        productFrame.dispose();
        LoginData data = new LoginData();
        LoginFrame frame = new LoginFrame(data);
        LoginController controller = new LoginController(data, frame);
    }

    ArrayList<Product> Products, ProductsClone;
    JFrame productFrame;
    JPanel productTopPanel;
    JPanel productsViewPanel;
        JPanel handSanitizerPanel;
            JLabel handSanitizerName;
            JLabel handSanitizerPrice;
            JLabel handSanitizerQuantity;
            JButton buyHandSanitizer;
        JPanel toiletPaperPanel;
        JPanel Television;
        JPanel Water;
    JPanel generalProductsPanel;
    JButton checkoutButton;
    JButton productsButton;
    JButton logoutButton;
    private final CardLayout cardLayout;
    private final JPanel switchPanel;
    public final String productsPage = "Products Page";
    public final String checkOutPage = "Checkout Page";
    boolean isOnProductsPage;
    JPanel checkOutProductsViewPanel;
    JPanel handSanitizerPanelCO;
    JLabel handSanitizerQuantityCO;
    int userQuantityHS = 0;
    JButton addHandSanitizerCO, removeHandSanitizerCO;

    NumberFormat formatter = NumberFormat.getCurrencyInstance();

}
