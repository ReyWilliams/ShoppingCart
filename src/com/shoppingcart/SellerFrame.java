package com.shoppingcart;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SellerFrame 
{


    public SellerFrame(String userName, ProductModel model)
    {
        //initialize products arraylist
        Products = model.getProducts();

        //setting up the frame
        sellerFrame = new JFrame();
        sellerFrame.setSize(587,775);
        sellerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sellerFrame.setLayout(new BorderLayout(20,20));
        sellerFrame.setTitle("Welcome, " + userName);
        sellerFrame.setVisible(true);

        //setting up the panel that will allow us to switch
        switchPanel = new JPanel();
        cardLayout = new CardLayout();
        switchPanel.setLayout(cardLayout);

        setUpTopPanel();

        //setting up the general inventory panel
        generalInventoryPanel = new JPanel(new BorderLayout(50,50));

        //add spacers to the left and right and south
        generalInventoryPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.EAST);
        generalInventoryPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.WEST);
        generalInventoryPanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.SOUTH);

        //set up inventory view panel (holds the squares that show inventory)
        InventoryViewPanel = new JPanel(new GridLayout(2,2,20,20));
        setUpInventoryView();
        generalInventoryPanel.add(InventoryViewPanel,BorderLayout.CENTER);




        switchPanel.add("Inventory Page", generalInventoryPanel);

        sellerFrame.add(switchPanel);
        cardLayout.show(switchPanel, "Inventory Page");

    }

    void setUpTopPanel(){
        sellerTopPanel = new JPanel(new FlowLayout());

        inventorySwitchButton = new JButton("View Inventory");
        inventorySwitchButton.addActionListener(e->showInventoryPage());
        inventorySwitchButton.setBackground(new Color(169,169,169));
        inventorySwitchButton.setFocusable(false);

        metricsSwitchButton = new JButton("View Metrics");
        metricsSwitchButton.setBackground(new Color(169,169,169));
        metricsSwitchButton.setFocusable(false);

        logoutButton = new JButton("Logout");
        logoutButton.setFocusable(false);
        logoutButton.setBackground(new Color(169,169,169));

        //add the bottoms to the top panel
        sellerTopPanel.add(inventorySwitchButton);
        sellerTopPanel.add(metricsSwitchButton);
        sellerTopPanel.add(logoutButton);

        sellerFrame.add(sellerTopPanel, BorderLayout.NORTH);


    }

    void setUpInventoryView(){
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

        //set up name and quantity labels
        handSanitizerName = new JLabel(Products.get(0).getName());
        handSanitizerName.setFont(new Font("Century Gothic", Font.BOLD, 30));
        handSanitizerName.setForeground(new Color(0,255,127));
        handSanitizerName.setAlignmentX(Component.CENTER_ALIGNMENT);
        handSanitizerPanel.add(handSanitizerName);

        //spacer
        handSanitizerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        handSanitizerQuantity = new JLabel("On Hand: " + Products.get(0).getQuantity());
        handSanitizerQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        handSanitizerQuantity.setFont(new Font("Century Gothic", Font.PLAIN, 26));
        handSanitizerQuantity.setForeground(Color.white);
        handSanitizerPanel.add(handSanitizerQuantity);

        //spacer
        handSanitizerPanel.add(Box.createRigidArea(new Dimension(0, 45)));

        //increment and decrement buttons
        JPanel HSIncandDec = new JPanel(new FlowLayout());
        HSIncandDec.setSize(10,10);
        HSIncandDec.setBackground(new Color(169,169,169));

        incHSQuantity = new JButton("+");
        incHSQuantity.setBackground(new Color(147,219,170));
        incHSQuantity.setFocusable(false);
        incHSQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        incHSQuantity.setFont(new Font("Century Gothic", Font.BOLD, 20));

        decHSQuantity = new JButton("-");
        decHSQuantity.setBackground(new Color(255, 114,111));
        decHSQuantity.setFocusable(false);
        decHSQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        decHSQuantity.setFont(new Font("Century Gothic", Font.BOLD, 20));

        HSIncandDec.add(incHSQuantity);
        HSIncandDec.add(decHSQuantity);
        incHSQuantity.addActionListener(this::updateInventory);
        decHSQuantity.addActionListener(this::updateInventory);
        handSanitizerPanel.add(HSIncandDec);

        InventoryViewPanel.add(handSanitizerPanel);

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

        //set up name and quantity labels
        maskPanelName = new JLabel(Products.get(1).getName());
        maskPanelName.setFont(new Font("Century Gothic", Font.BOLD, 30));
        maskPanelName.setForeground(new Color(0,255,127));
        maskPanelName.setAlignmentX(Component.CENTER_ALIGNMENT);
        maskPanel.add(maskPanelName);

        //spacer
        maskPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        maskPanelQuantity = new JLabel("On Hand: " + Products.get(1).getQuantity());
        maskPanelQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        maskPanelQuantity.setFont(new Font("Century Gothic", Font.PLAIN, 26));
        maskPanelQuantity.setForeground(Color.white);
        maskPanel.add(maskPanelQuantity);

        //spacer
        maskPanel.add(Box.createRigidArea(new Dimension(0, 45)));

        //increment and decrement buttons
        JPanel MaskIncandDec = new JPanel(new FlowLayout());
        MaskIncandDec.setSize(10,10);
        MaskIncandDec.setBackground(new Color(169,169,169));

        incMaskQuantity = new JButton("+");
        incMaskQuantity.setBackground(new Color(147,219,170));
        incMaskQuantity.setFocusable(false);
        incMaskQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        incMaskQuantity.setFont(new Font("Century Gothic", Font.BOLD, 20));

        decMaskQuantity = new JButton("-");
        decMaskQuantity.setBackground(new Color(255, 114,111));
        decMaskQuantity.setFocusable(false);
        decMaskQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        decMaskQuantity.setFont(new Font("Century Gothic", Font.BOLD, 20));

        MaskIncandDec.add(incMaskQuantity);
        MaskIncandDec.add(decMaskQuantity);
        incMaskQuantity.addActionListener(this::updateInventory);
        decMaskQuantity.addActionListener(this::updateInventory);
        maskPanel.add(MaskIncandDec);

        InventoryViewPanel.add(maskPanel);

        //set up the toothpaste panel
        toothPastePanel = new JPanel();

        toothPastePanel.setBackground(new Color(169,169,169));
        toothPastePanel.setLayout( new BoxLayout(toothPastePanel,BoxLayout.Y_AXIS));
        toothPastePanel.setSize(25,25);

        //spacer
        toothPastePanel.add(Box.createRigidArea(new Dimension(0, 30)));

        //image setup
        BufferedImage TPIcon = null;
        try {
            TPIcon = ImageIO.read(new File("Icons/toothPasteIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert TPIcon != null;
        JLabel TPIconLabel = new JLabel(new ImageIcon(TPIcon));
        TPIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        toothPastePanel.add(TPIconLabel);

        //set up name and quantity labels
        toothPasteName = new JLabel(Products.get(2).getName());
        toothPasteName.setFont(new Font("Century Gothic", Font.BOLD, 30));
        toothPasteName.setForeground(new Color(0,255,127));
        toothPasteName.setAlignmentX(Component.CENTER_ALIGNMENT);
        toothPastePanel.add(toothPasteName);

        //spacer
        toothPastePanel.add(Box.createRigidArea(new Dimension(0, 30)));

        toothPasteQuantity = new JLabel("On Hand: " + Products.get(2).getQuantity());
        toothPasteQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        toothPasteQuantity.setFont(new Font("Century Gothic", Font.PLAIN, 26));
        toothPasteQuantity.setForeground(Color.white);
        toothPastePanel.add(toothPasteQuantity);

        //spacer
        toothPastePanel.add(Box.createRigidArea(new Dimension(0, 45)));

        //increment and decrement buttons
        JPanel TPIncandDec = new JPanel(new FlowLayout());
        TPIncandDec.setSize(10,10);
        TPIncandDec.setBackground(new Color(169,169,169));

        incTPQuantity = new JButton("+");
        incTPQuantity.setBackground(new Color(147,219,170));
        incTPQuantity.setFocusable(false);
        incTPQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        incTPQuantity.setFont(new Font("Century Gothic", Font.BOLD, 20));

        decTPQuantity = new JButton("-");
        decTPQuantity.setBackground(new Color(255, 114,111));
        decTPQuantity.setFocusable(false);
        decTPQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        decTPQuantity.setFont(new Font("Century Gothic", Font.BOLD, 20));

        TPIncandDec.add(incTPQuantity);
        TPIncandDec.add(decTPQuantity);
        incTPQuantity.addActionListener(this::updateInventory);
        decTPQuantity.addActionListener(this::updateInventory);
        toothPastePanel.add(TPIncandDec);

        InventoryViewPanel.add(toothPastePanel);

        //set up medication panel
        mediPanel = new JPanel();

        mediPanel.setBackground(new Color(169,169,169));
        mediPanel.setLayout( new BoxLayout(mediPanel,BoxLayout.Y_AXIS));
        mediPanel.setSize(25,25);

        //spacer
        mediPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        //image setup
        BufferedImage MDIcon = null;
        try {
            MDIcon = ImageIO.read(new File("Icons/medicationIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert MDIcon != null;
        JLabel MDIconLabel = new JLabel(new ImageIcon(MDIcon));
        MDIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mediPanel.add(MDIconLabel);

        //set up name and quantity labels
        mediName = new JLabel(Products.get(3).getName());
        mediName.setFont(new Font("Century Gothic", Font.BOLD, 30));
        mediName.setForeground(new Color(0,255,127));
        mediName.setAlignmentX(Component.CENTER_ALIGNMENT);
        mediPanel.add(mediName);

        //spacer
        mediPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        mediQuantity = new JLabel("On Hand: " + Products.get(3).getQuantity());
        mediQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        mediQuantity.setFont(new Font("Century Gothic", Font.PLAIN, 26));
        mediQuantity.setForeground(Color.white);
        mediPanel.add(mediQuantity);

        //spacer
        mediPanel.add(Box.createRigidArea(new Dimension(0, 45)));

        //increment and decrement buttons
        JPanel MediIncandDec = new JPanel(new FlowLayout());
        MediIncandDec.setSize(10,10);
        MediIncandDec.setBackground(new Color(169,169,169));

        incMediQuantity = new JButton("+");
        incMediQuantity.setBackground(new Color(147,219,170));
        incMediQuantity.setFocusable(false);
        incMediQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        incMediQuantity.setFont(new Font("Century Gothic", Font.BOLD, 20));

        decMediQuantity = new JButton("-");
        decMediQuantity.setBackground(new Color(255, 114,111));
        decMediQuantity.setFocusable(false);
        decMediQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        decMediQuantity.setFont(new Font("Century Gothic", Font.BOLD, 20));

        MediIncandDec.add(incMediQuantity);
        MediIncandDec.add(decMediQuantity);
        incMediQuantity.addActionListener(this::updateInventory);
        decMediQuantity.addActionListener(this::updateInventory);
        mediPanel.add(MediIncandDec);

        InventoryViewPanel.add(mediPanel);

    }

    void showInventoryPage(){
        sellerFrame.setSize(587,775);
        cardLayout.show(switchPanel, "Inventory Page");
    }

    void updateInventory(ActionEvent evt){

        //hand sanitizer buttons
        if(evt.getSource() == incHSQuantity){
            handSanitizerQuantity.setForeground(Color.WHITE);
            Products.get(0).setQuantity(Products.get(0).getQuantity() + 1); //increment quantity
            handSanitizerQuantity.setText("On Hand: " + Products.get(0).getQuantity());
            return;
        }
        if(evt.getSource() == decHSQuantity){
            if(Products.get(0).getQuantity() == 0){ //if the current quantity is 0
                handSanitizerQuantity.setForeground(new Color(255, 114,111)); //set label to red
                return; //exit, you cannot go lower
            }else if(Products.get(0).getQuantity() == 1){
                handSanitizerQuantity.setForeground(new Color(255, 114,111)); //set label to red
            }

            Products.get(0).setQuantity(Products.get(0).getQuantity() - 1); //decrement quantity
            handSanitizerQuantity.setText("On Hand: " + Products.get(0).getQuantity());
            return;
        }

        //mask buttons
        if(evt.getSource() == incMaskQuantity){
            maskPanelQuantity.setForeground(Color.WHITE);
            Products.get(1).setQuantity(Products.get(1).getQuantity() + 1); //increment quantity
            maskPanelQuantity.setText("On Hand: " + Products.get(1).getQuantity());
            return;
        }
        if(evt.getSource() == decMaskQuantity){
            if(Products.get(1).getQuantity() == 0){ //if the current quantity is 0
                maskPanelQuantity.setForeground(new Color(255, 114,111)); //set label to red
                return; //exit, you cannot go lower
            }else if(Products.get(1).getQuantity() == 1){
                maskPanelQuantity.setForeground(new Color(255, 114,111)); //set label to red
            }

            Products.get(1).setQuantity(Products.get(1).getQuantity() - 1); //decrement quantity
            maskPanelQuantity.setText("On Hand: " + Products.get(1).getQuantity());
            return;
        }

        //toothpaste buttons
        if(evt.getSource() == incTPQuantity){
            toothPasteQuantity.setForeground(Color.WHITE);
            Products.get(2).setQuantity(Products.get(2).getQuantity() + 1); //increment quantity
            toothPasteQuantity.setText("On Hand: " + Products.get(2).getQuantity());
            return;
        }
        if(evt.getSource() == decTPQuantity){
            if(Products.get(2).getQuantity() == 0){ //if the current quantity is 0
                toothPasteQuantity.setForeground(new Color(255, 114,111)); //set label to red
                return; //exit, you cannot go lower
            }else if(Products.get(2).getQuantity() == 1){
                toothPasteQuantity.setForeground(new Color(255, 114,111)); //set label to red
            }

            Products.get(2).setQuantity(Products.get(2).getQuantity() - 1); //decrement quantity
            toothPasteQuantity.setText("On Hand: " + Products.get(2).getQuantity());
            return;
        }

        //medication buttons
        if(evt.getSource() == incMediQuantity){
            mediQuantity.setForeground(Color.WHITE);
            Products.get(3).setQuantity(Products.get(3).getQuantity() + 1); //increment quantity
            mediQuantity.setText("On Hand: " + Products.get(3).getQuantity());
            return;
        }
        if(evt.getSource() == decMediQuantity){
            if(Products.get(3).getQuantity() == 0){ //if the current quantity is 0
                mediQuantity.setForeground(new Color(255, 114,111)); //set label to red
                return; //exit, you cannot go lower
            }else if(Products.get(3).getQuantity() == 1){
                mediQuantity.setForeground(new Color(255, 114,111)); //set label to red
            }

            Products.get(3).setQuantity(Products.get(3).getQuantity() - 1); //decrement quantity
            mediQuantity.setText("On Hand: " + Products.get(3).getQuantity());
            return;
        }




    }


    //variable declarations
    ArrayList<Product> Products;

    JFrame sellerFrame;
    JPanel switchPanel;
    JPanel generalInventoryPanel;
    JPanel sellerTopPanel;
    JPanel InventoryViewPanel;
        JPanel handSanitizerPanel;
            JLabel handSanitizerName, handSanitizerQuantity;
            JButton incHSQuantity, decHSQuantity;
        JPanel maskPanel;
            JLabel maskPanelName, maskPanelQuantity;
            JButton incMaskQuantity, decMaskQuantity;
        JPanel toothPastePanel;
            JLabel toothPasteName, toothPasteQuantity;
            JButton incTPQuantity, decTPQuantity;
        JPanel mediPanel;
            JLabel mediName, mediQuantity;
            JButton incMediQuantity, decMediQuantity;
    JButton inventorySwitchButton;
    JButton metricsSwitchButton;
    JButton logoutButton;
    CardLayout cardLayout;

}