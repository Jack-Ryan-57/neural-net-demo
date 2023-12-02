package com.nndemo;

import javax.swing.*;

import com.colortraining.ColorTrainer;
import com.neuralnetwork.NeuralNetwork;
import com.neuralnetwork.Matrix;
import com.image.ImageCreator;
import com.image.ImageOutliner;

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.awt.Font;

import com.image.ImageDoodler;


public class ColorDemo extends JPanel implements MouseListener, MouseMotionListener, ActionListener{

    boolean dragging = false;

    boolean temp = true, saveTemp = true, simpleTemp = true, traceTemp = true;

    private NeuralNetwork net;
    ColorTrainer ct;
    Color background = Color.CYAN;
    String colorDisplay = "Color: Blue";
    int[] shape;
    Font font;
    private boolean testing = true, training = true;
    
    public int offsetX, offsetY = 0;

    private int colorX = 10, colorY = 10, colorW = 500, colorH = 500;

    NetworkVisualizer nv;

    //Initializes JPanel and MouseListeners
    public ColorDemo(){
        font = new Font("Arial", Font.BOLD, 16);
        setFont(font);
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        if (training){
            this.setBackground(Color.WHITE);
            //Initial training of network
            g2.drawString("Training....", 50, 50);
            setVisible(true);
            System.out.println("Training....");
            ct = new ColorTrainer("colortrainingdata.txt");
            net = ct.network;
            //Get information needed for visual representation of network
            shape = ct.getNetworkInfo();
            nv = new NetworkVisualizer(net, this);
            training = false; 
            repaint();
            return;
        }
        if (testing){
            //Initial testing of network 
            this.setBackground(Color.WHITE);
            g2.drawString("Testing....", 50, 50);
            System.out.println("Testing....");
            ct.setDataName("colortestingdata.txt");
            ct.setTesting(true);
            ct.train();
            testing = false; 
            setVisible(true);
            repaint();
            return;
        }
        //Add buttons to screen 
        createDoodleButton();
        createSaveButton();
        createSimplifyButton();
        createTraceButton();
        setBackground(background);
        //Add color gradient to screen 
        BufferedImage img = new ImageCreator("nndemo/assets/img/colorgradient.png").createImage();
        g.drawImage(img, colorX, colorY, colorW, colorH, null);
        g2.drawString("Predicted accuracy: " + ct.getAccuracy()*100 + "%", 520, 30);
        g2.drawString(colorDisplay, 520, 50);
        nv.setBounds(520, 60, 450, 450);
        nv.drawNetwork(g);
        setVisible(true);
    }

    //Decide whether or not specified point is in area of gradient 
    public boolean pointIsInArea(Point p, int x, int y, int w, int h){
        return (p.x < (colorX + colorW) && p.x > colorX && p.y < (colorY + colorH) && p.y > (colorY));
    }

    //Create and display doodle button on screen
    //When this button is clicked, pixelates image and "doodles" by classifying pixel values
    public void createDoodleButton(){
        JButton b = new JButton("Create Doodle Image");
        if (temp){
            b.setLocation(70, 550);
            b.setSize(200, 50);
            b.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new ImageDoodler("painting.png", ct);
                    System.out.println("Finished Condensing Image");
                }
            });
            this.add(b);
            temp = false;
        }
    }

    //Create and display save button on screen 
    //Saves current configuration of Neural Network (weights, biases, values) to "colortraining/saved-net-data.txt"
    public void createSaveButton(){
        JButton b = new JButton("Save Neural Network Config");
        if (saveTemp){
            b.setLocation(290, 550);
            b.setSize(200, 50);
            b.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    net.saveNetwork("nndemo/src/main/java/com/colortraining");
                }
            });
            this.add(b);
            saveTemp = false;
        }
    }

    //Create and display save button on screen 
    //Button simplifies "painting-pixelated.png" by removing single stray pixels
    public void createSimplifyButton(){
        JButton b = new JButton("Simplify Image");
        if (simpleTemp){
            b.setLocation(510, 550);
            b.setSize(200, 50);
            b.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    File f = new File("painting-condensed.png");
                    if (f.exists()){
                        BufferedImage b = new ImageCreator("painting-condensed.png").createImage();
                        ImageDoodler.simplifyImage(b, "painting-condensed.png");
                    }else{
                        System.out.println("No Image Exists to Simplify");
                    }
                }
            });
            this.add(b);
            simpleTemp = false;
        }

    }

    public void createTraceButton(){
        JButton b = new JButton("Trace Image");
        if (traceTemp){
            b.setLocation(730, 550);
            b.setSize(200, 50);
            b.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    File f = new File("painting-condensed.png");
                    if (f.exists()){
                        BufferedImage b = new ImageCreator("painting-condensed.png").createImage();
                        new ImageOutliner(b);
                    }else{
                        System.out.println("No Image Exists to Trace");
                    }
                }
            });
            this.add(b);
            traceTemp = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        predictColor(e.getPoint());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragging = true;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e){
        Point p = e.getPoint();
        predictColor(p);
        if (dragging){
            repaint();
        }
    }


    @Override
    public void mouseMoved(MouseEvent e) {
    
    }

    //Picks up color user is hovering over and feeds forward through network to guess description
    public void predictColor(Point p){
        if (pointIsInArea(p, colorX, colorY, colorW, colorH)){
            try {
                Color c = new Robot().getPixelColor(p.x + offsetX, p.y + offsetY);
                Matrix m = net.feedForward(new float[]{(float)c.getRed()/255, (float)c.getGreen()/255, (float)c.getBlue()/255});
                String color = ct.getAnswer(m);
                colorDisplay = "Color: " + color;
                background = c;
                repaint();

            } catch (AWTException ex) {
                System.out.println("Robot error");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            
    }
}
