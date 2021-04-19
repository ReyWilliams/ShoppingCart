package com.shoppingcart;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;

public class SellerFrame 
{


    public SellerFrame(String userName, ProductModel model)
    {
        //initialize products arraylist
        Products = model.getProducts();

        newProductPresent = false;
        //see if there is new product
        File tmpDir = new File("Serialized/newProduct.dat");
        boolean exists = tmpDir.exists();

        if(exists){

            try { //try to deserialize

                ObjectInputStream in = new ObjectInputStream(
                        new FileInputStream("Serialized/newProduct.dat"));

                newProduct = (Product) in.readObject();

                in.close();

                System.out.println("new product deserialized by seller frame");

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            newProductPresent = true;
        }


        //setting up the frame
        sellerFrame = new JFrame();
        sellerFrame.setSize(587,775);
        sellerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sellerFrame.setLayout(new BorderLayout(20,20));
        sellerFrame.setTitle("Welcome, seller" + userName);
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

        JPanel inventoryTopPanel = new JPanel(new FlowLayout());
        addProduct = new JButton("Add Product");
        addProduct.addActionListener(e->addProduct());
        inventoryTopPanel.add(addProduct);
        generalInventoryPanel.add(inventoryTopPanel, BorderLayout.NORTH);

        //set up inventory view panel (holds the squares that show inventory)
        InventoryViewPanel = new JPanel(new GridLayout(3,2,20,20));
        setUpInventoryView();
        JScrollPane scrollInventoryPane = new JScrollPane(InventoryViewPanel);
        scrollInventoryPane.getVerticalScrollBar().setUnitIncrement(10);
        generalInventoryPanel.add(scrollInventoryPane,BorderLayout.CENTER);


        generalPurchasePanel = new JPanel(new BorderLayout(50,25));

        //add spacers to the left and right and south
        generalPurchasePanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.EAST);
        generalPurchasePanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.WEST);
        generalPurchasePanel.add(Box.createRigidArea(new Dimension(0, 30)),BorderLayout.SOUTH);

        getPurchases();

        JScrollPane scrollPurchasePane = new JScrollPane(purchaseText);
        scrollPurchasePane.getVerticalScrollBar().setUnitIncrement(10);
        generalPurchasePanel.add(scrollPurchasePane,BorderLayout.CENTER);

        JPanel purchaseTopPanel = new JPanel();
        purchaseTopPanel.setLayout(new BoxLayout(purchaseTopPanel, BoxLayout.Y_AXIS));
        JButton viewProfit = new JButton("View Profit");
        viewProfit.setBackground(new Color(169,169,169));
        viewProfit.setFocusable(false);
        viewProfit.addActionListener(e->viewProfit());
        JLabel purchasesLabel = new JLabel("Purchases");
        purchasesLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
        purchaseTopPanel.add(viewProfit);
        purchaseTopPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        purchaseTopPanel.add(purchasesLabel);
        JPanel purchaseTopPanelFlow = new JPanel(new FlowLayout());
        purchaseTopPanelFlow.add(purchaseTopPanel);
        generalPurchasePanel.add(purchaseTopPanelFlow,BorderLayout.NORTH);

        profitPanel = new JPanel(new FlowLayout());
        profitLabel = new JLabel();
        profitPanel.add(profitLabel);
        generalPurchasePanel.add(profitPanel, BorderLayout.SOUTH);


        switchPanel.add("Inventory Page", generalInventoryPanel);
        switchPanel.add("Purchase Page", generalPurchasePanel);

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
        metricsSwitchButton.addActionListener(e->showPurchasePage());
        metricsSwitchButton.setBackground(new Color(169,169,169));
        metricsSwitchButton.setFocusable(false);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logOut());
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







        //set up new product panel panel
        if(newProductPresent) {
            NPPanel = new JPanel();

            NPPanel.setBackground(new Color(169, 169, 169));
            NPPanel.setLayout(new BoxLayout(NPPanel, BoxLayout.Y_AXIS));
            NPPanel.setSize(25, 25);

            //spacer
            NPPanel.add(Box.createRigidArea(new Dimension(0, 30)));

            //image setup
            BufferedImage NPIcon = null;
            try {
                NPIcon = ImageIO.read(new File("Icons/newProductIcon.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert NPIcon != null;
            JLabel NPIconLabel = new JLabel(new ImageIcon(NPIcon));
            NPIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            NPPanel.add(NPIconLabel);

            //set up name and quantity labels
            NPName = new JLabel(newProduct.getName());
            NPName.setFont(new Font("Century Gothic", Font.BOLD, 30));
            NPName.setForeground(new Color(0, 255, 127));
            NPName.setAlignmentX(Component.CENTER_ALIGNMENT);
            NPPanel.add(NPName);

            //spacer
            NPPanel.add(Box.createRigidArea(new Dimension(0, 30)));

            NPQuantity = new JLabel("On Hand: " + newProduct.getQuantity());
            NPQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
            NPQuantity.setFont(new Font("Century Gothic", Font.PLAIN, 26));
            NPQuantity.setForeground(Color.white);
            NPPanel.add(NPQuantity);

            //spacer
            NPPanel.add(Box.createRigidArea(new Dimension(0, 45)));

            //increment and decrement buttons
            JPanel NPIncandDec = new JPanel(new FlowLayout());
            NPIncandDec.setSize(10, 10);
            NPIncandDec.setBackground(new Color(169, 169, 169));

            incNPQuantity = new JButton("+");
            incNPQuantity.setBackground(new Color(147, 219, 170));
            incNPQuantity.setFocusable(false);
            incNPQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
            incNPQuantity.setFont(new Font("Century Gothic", Font.BOLD, 20));

            decNPQuantity = new JButton("-");
            decNPQuantity.setBackground(new Color(255, 114, 111));
            decNPQuantity.setFocusable(false);
            decNPQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
            decNPQuantity.setFont(new Font("Century Gothic", Font.BOLD, 20));

            NPIncandDec.add(incNPQuantity);
            NPIncandDec.add(decNPQuantity);
            incNPQuantity.addActionListener(this::updateInventory);
            decNPQuantity.addActionListener(this::updateInventory);
            NPPanel.add(NPIncandDec);

            InventoryViewPanel.add(NPPanel);
        }
    }

    void showInventoryPage(){
        sellerFrame.setSize(587,775);
        cardLayout.show(switchPanel, "Inventory Page");
    }

    void showPurchasePage(){
        if(purchaseBottomPanel != null && purchaseBottomPanel.getComponentCount() != 0){
            sellerFrame.setSize(587,855);
        }else{
            sellerFrame.setSize(587,775);
        }

        cardLayout.show(switchPanel, "Purchase Page");
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

        //new product buttons
        if(evt.getSource() == incNPQuantity){
            NPQuantity.setForeground(Color.WHITE);
            newProduct.setQuantity(newProduct.getQuantity() + 1); //increment quantity
            NPQuantity.setText("On Hand: " + newProduct.getQuantity());
            return;
        }
        if(evt.getSource() == decNPQuantity){
            if(newProduct.getQuantity() == 0){ //if the current quantity is 0
                NPQuantity.setForeground(new Color(255, 114,111)); //set label to red
                return; //exit, you cannot go lower
            }else if(newProduct.getQuantity() == 1){
                NPQuantity.setForeground(new Color(255, 114,111)); //set label to red
            }

            newProduct.setQuantity(newProduct.getQuantity() - 1); //decrement quantity
            NPQuantity.setText("On Hand: " + newProduct.getQuantity());
        }



    }

    void logOut(){
        sellerFrame.dispose();

        //serialize products
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("Serialized/products.dat"));

            out.writeObject(Products);

            out.close();

            System.out.println("products serialized by seller frame.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //try to serialize new product
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("Serialized/newProduct.dat"));

            out.writeObject(newProduct);

            out.close();

            System.out.println("new product serialized by seller frame.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        LoginData data = new LoginData();
        LoginFrame frame = new LoginFrame(data);
        LoginController controller = new LoginController(data, frame);

    }

    void getPurchases(){
        purchaseText = new JTextPane();
        purchaseText.setFont(new Font("Century Gothic", Font.BOLD, 12));
        PurchaseModel pmodel = new PurchaseModel();
        purchaseList = pmodel.getPurchases();

        StringBuilder text = new StringBuilder();
        for(Purchase p: purchaseList){
            text.append(p.toString()).append("\n");

        }

        purchaseText.setText(text.toString());
    }

    void viewProfit(){

        profitLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
        profitLabel.setForeground(new Color(147,219,170));
        profitLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        double profit = 0;

        for(Purchase p: purchaseList){
            for(Map.Entry<Product, Integer> n: p.items.entrySet()){
                profit += ((n.getKey().getSellPrice()-n.getKey().getBasePrice()) * n.getValue());
            }
        }

        profitLabel.setText("Profit: " + formatter.format(profit));

        if(purchaseBottomPanel != null && purchaseBottomPanel.getComponentCount() != 0){
            return;
        }

        purchaseBottomPanel = new JPanel(new FlowLayout());

        purchaseBottomPanel.add(profitLabel);
        generalPurchasePanel.add(purchaseBottomPanel, BorderLayout.SOUTH);

        sellerFrame.setSize(587,855);
        generalPurchasePanel.repaint();
    }

    void addProduct(){
        newProductPanel = new JPanel();
        newProductPanel.setLayout(null);
        newProductPanel.setVisible(true);
        switchPanel.add("Add Product Page", newProductPanel);

        JLabel productName = new JLabel("Product Name: ");
        productName.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        productName.setForeground(Color.BLACK);
        productName.setBounds(20,20,productName.getPreferredSize().width, productName.getPreferredSize().height);
        newProductPanel.add(productName);

        int tempHeight = productName.getPreferredSize().height;

        JLabel productID = new JLabel("Product ID: ");
        productID.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        productID.setForeground(Color.BLACK);
        productID.setBounds(20,40 + tempHeight,productID.getPreferredSize().width, productID.getPreferredSize().height);
        newProductPanel.add(productID);

        JLabel productType = new JLabel("Product Type: ");
        productType.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        productType.setForeground(Color.BLACK);
        productType.setBounds(20,60 + tempHeight*2,productType.getPreferredSize().width, productType.getPreferredSize().height);
        newProductPanel.add(productType);

        JLabel productBP = new JLabel("Product Base Price: ");
        productBP.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        productBP.setForeground(Color.BLACK);
        productBP.setBounds(20,80 + tempHeight*3,productBP.getPreferredSize().width, productBP.getPreferredSize().height);
        newProductPanel.add(productBP);

        JLabel productSP = new JLabel("Product Sell Price: ");
        productSP.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        productSP.setForeground(Color.BLACK);
        productSP.setBounds(20,100 + tempHeight*4,productSP.getPreferredSize().width, productSP.getPreferredSize().height);
        newProductPanel.add(productSP);

        JLabel productQuantity = new JLabel("Product Quantity: ");
        productQuantity.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        productQuantity.setForeground(Color.BLACK);
        productQuantity.setBounds(20,120 + tempHeight*5,productQuantity.getPreferredSize().width, productQuantity.getPreferredSize().height);
        newProductPanel.add(productQuantity);

        prodName = new JTextField(20);
        prodName.setBounds(200,20,prodName.getPreferredSize().width, prodName.getPreferredSize().height);
        prodName.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        prodName.setBackground(new Color(169,169,169));
        newProductPanel.add(prodName);

        prodID = new JTextField(20);
        prodID.setBounds(200,40 + tempHeight,prodID.getPreferredSize().width, prodID.getPreferredSize().height);
        prodID.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        prodID.setBackground(new Color(169,169,169));
        newProductPanel.add(prodID);

        prodTP = new JTextField(20);
        prodTP.setBounds(200,60 + tempHeight*2,prodTP.getPreferredSize().width, prodTP.getPreferredSize().height);
        prodTP.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        prodTP.setBackground(new Color(169,169,169));
        newProductPanel.add(prodTP);

        prodBP = new JTextField(20);
        prodBP.setBounds(200,80 + tempHeight*3,prodBP.getPreferredSize().width, prodBP.getPreferredSize().height);
        prodBP.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        prodBP.setBackground(new Color(169,169,169));
        newProductPanel.add(prodBP);

        prodSP = new JTextField(20);
        prodSP.setBounds(200,100 + tempHeight*4,prodSP.getPreferredSize().width, prodSP.getPreferredSize().height);
        prodSP.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        prodSP.setBackground(new Color(169,169,169));
        newProductPanel.add(prodSP);

        prodQuantity = new JTextField(20);
        prodQuantity.setBounds(200,120 + tempHeight*5,prodQuantity.getPreferredSize().width, prodQuantity.getPreferredSize().height);
        prodQuantity.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        prodQuantity.setBackground(new Color(169,169,169));
        newProductPanel.add(prodQuantity);

        submitAPButton = new JButton("Submit");
        submitAPButton.addActionListener(e->checkAddProduct());
        submitAPButton.setFocusable(false);
        submitAPButton.setBounds(200,180 + tempHeight*6,submitAPButton.getPreferredSize().width + 50 , submitAPButton.getPreferredSize().height);
        submitAPButton.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        submitAPButton.setBackground(new Color(169,169,169));
        newProductPanel.add(submitAPButton);

        addProductFields = new JTextField[]{prodName, prodID, prodTP, prodBP, prodSP, prodQuantity};


        cardLayout.show(switchPanel, "Add Product Page");
    }

    void checkAddProduct(){
        boolean valid = true;

        for(JTextField n: addProductFields) {

            if (n.getText().length() == 0) {
                n.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                valid = false; //not valid as something is empty
            } else {
                n.setBorder(new LineBorder(Color.BLACK, 0));
            }
        }
        File tmpDir = new File("Serialized/newProduct.dat");
        boolean exists = tmpDir.exists();

        if(!valid){ //if not valid exit function
            return;
        }else if(exists){
            submitAPButton.setBackground(new Color(255, 114,111));
            System.out.println("New Product already exists");
            return;
        }

        submitAPButton.setBackground(new Color(169,169,169));


        //if valid and not already new product go ahead and create product
        newProduct = new Product(prodName.getText(), prodID.getText(), prodTP.getText(),Double.parseDouble(prodBP.getText()),Double.parseDouble(prodSP.getText()),Integer.parseInt(prodQuantity.getText()),0);

        //try to serialize new product
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("Serialized/newProduct.dat"));

            out.writeObject(newProduct);

            out.close();

            System.out.println("new product serialized by seller frame.");
        } catch (IOException e) {
            e.printStackTrace();
        }


        logOut();
    }

    //variable declarations
    ArrayList<Product> Products;
    ArrayList<Purchase> purchaseList;
    JFrame sellerFrame;
    JPanel switchPanel;
    JPanel generalInventoryPanel;
    JPanel sellerTopPanel;
    JPanel InventoryViewPanel;
    JPanel generalPurchasePanel;
    JLabel profitLabel;
    JPanel profitPanel;
    JPanel purchaseBottomPanel;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    JButton addProduct;
    JPanel newProductPanel;
    JButton submitAPButton;
    Product newProduct;
    JTextField prodName, prodID, prodTP, prodBP, prodSP, prodQuantity;
    JTextField[] addProductFields;

    JTextPane purchaseText;
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
        JPanel NPPanel;
            JLabel NPName, NPQuantity;
            JButton incNPQuantity, decNPQuantity;
    JButton inventorySwitchButton;
    JButton metricsSwitchButton;
    JButton logoutButton;
    CardLayout cardLayout;
    boolean newProductPresent;

}