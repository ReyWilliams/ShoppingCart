package com.shoppingcart;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SellerFrame 
{
    //variable declaration 
    JFrame sellerFrame;
    JPanel sellerPanel;
    JButton inventoryButton;
    JButton profitButton;

    public SellerFrame()
    {
        sellerFrame = new JFrame();
        sellerPanel = new JPanel();

        //attributes for SellerFrame JFrame
        sellerFrame.setSize(875,687);
        sellerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sellerPanel.setLayout(null);

        //Label indicating its the sellers page
        JLabel sellerLabel = new JLabel("Sellers Page");
        sellerLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
        sellerLabel.setBounds(10, 10, 300, 35);
        sellerPanel.add(sellerLabel);

        //inventory button
        inventoryButton = new JButton("View Inventory");
        inventoryButton.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        inventoryButton.setBounds(10,50,150,30);
        sellerPanel.add(inventoryButton);

        //profit button
        profitButton = new JButton("View Profit");
        profitButton.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        profitButton.setBounds(10,100,150,30);
        sellerPanel.add(profitButton);

        //
        //adding frame for it to appear
        sellerFrame.add(sellerPanel);
        sellerFrame.setVisible(true);
    }
}