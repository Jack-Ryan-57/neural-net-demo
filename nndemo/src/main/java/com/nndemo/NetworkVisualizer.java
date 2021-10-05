package com.nndemo;

import com.neuralnetwork.*;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;


public class NetworkVisualizer {

    //Amount of inputs and outpus, spacing between layers and neurons
    private int inputs, outputs, inputsSpacing, outputSpacing, layerSpacing, padding = 5;
    //Shape of hidden layers and space between hidden layer neurons
    private int[] hiddenLayers, hiddenSpacings;
    //Location storage for weight visualizations
    private int[][] inputNeurons, outputNeurons;
    private ArrayList<Integer[][]> hiddenNeurons = new ArrayList<Integer[][]>();
    private boolean border = true;

    ArrayList<Integer[]> weightLines;
    ArrayList<Matrix> info;
    NeuralNetwork net;

    boolean calculated = false;
    
    //Radius (in px) of displayed neurons
    int neuronRadius = 20;

    JPanel contentPane;

    private int x, y, w, h;
    
    //Args: network to be displayed, JPanel on which to display it
    //Gets info from network, displays it visually
    public NetworkVisualizer(NeuralNetwork net, JPanel p){
        this.net = net;
        int[] shape = net.getNetworkShape();
        info = net.getNetworkInfo();
        this.contentPane = p;
        this.hiddenLayers = new int[shape.length - 2];
        this.inputs = shape[0];
        this.inputNeurons = new int[inputs][2];
        for (int i = 1; i < shape.length - 1; i++){
            this.hiddenLayers[i - 1] = shape[i];
        }
        this.outputs = shape[shape.length - 1];
        this.outputNeurons = new int[outputs][2];
    }

    public NetworkVisualizer(NeuralNetwork net){
        this.net = net;
    }

    //Set bounds of visualized Network 
    public void setBounds(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    //Determine how much space there needs to be between layers and neurons
    public void calculateSpacing(){

        int totalLayers = hiddenLayers.length + 2;
        int width = w - 2*padding;
        int height = h - 2*padding;
        inputsSpacing = height/inputs;
        hiddenSpacings = new int[hiddenLayers.length];
        for (int i = 0; i < hiddenLayers.length; i++){
            hiddenSpacings[i] = height/hiddenLayers[i];
        }
        outputSpacing = height/outputs;
        layerSpacing = width/totalLayers + neuronRadius;

    }

    //Draws Network on JPanel using retrieved information from network passed in constructor 
    public void drawNetwork(Graphics g){
        calculateSpacing();
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.BLACK);
        if (border){
            g2.drawRect(x, y, w, h);
        }
        int startY = y + padding + inputsSpacing/2 - neuronRadius;
        int startX = x + padding;
        //Draw input neurons
        for (int i = 0; i < inputs; i++){
            g2.setColor(Color.BLACK);
            g2.drawOval(startX, startY, neuronRadius, neuronRadius);
            //Change to gradient color to display neuron activity
            g2.setColor(visualizeFiring(1, i));
            g2.fillOval(startX, startY, neuronRadius, neuronRadius);
            //Store information for neuron location
            inputNeurons[i][0] = startX;
            inputNeurons[i][1] = startY;
            startY += inputsSpacing;
        }
        //Draw hidden layer neurons
        for (int i = 0; i < hiddenLayers.length; i++){
            startY = y + hiddenSpacings[i] - neuronRadius;
            startX += layerSpacing;
            //Store information for hidden neuron locations
            Integer[][] addArr = new Integer[hiddenLayers[i]][2];
            for (int j = 0; j < hiddenLayers[i]; j++){
                g2.setColor(Color.BLACK);
                g2.drawOval(startX, startY, neuronRadius, neuronRadius);
                g2.setColor(visualizeFiring(i + 1, j));
                g2.fillOval(startX, startY, neuronRadius, neuronRadius);
                addArr[j][0] = startX;
                addArr[j][1] = startY;
                startY += hiddenSpacings[i];
            }
            hiddenNeurons.add(addArr);
        }
        //Draw output neurons
        startX += layerSpacing;
        startY = y + outputSpacing - neuronRadius;
        for (int i = 0; i < outputs; i++){
            g2.setColor(Color.BLACK);
            g2.drawOval(startX, startY, neuronRadius, neuronRadius);
            g2.setColor(visualizeFiring(info.size() - 1, i));
            g2.fillOval(startX, startY, neuronRadius, neuronRadius);
            //Store information for output neuron locations
            outputNeurons[i][0] = startX;
            outputNeurons[i][1] = startY;
            startY += outputSpacing;
        }
        //Draw weight lines between connected neurons
        if (!calculated){
            weightLines = calculateWeights(g2);
            calculated = true;
        }else{
            for (int i = 0; i < weightLines.size(); i++){
                Integer[] line = weightLines.get(i);
                g.setColor(Color.BLACK);
                g.drawLine(line[0], line[1], line[2], line[3]);
            }
        }
        info = net.getNetworkInfo();
    }

    public ArrayList<Integer[]> calculateWeights(Graphics2D g){

        ArrayList<Integer[]> ret = new ArrayList<Integer[]>();

        //Weight lines from each input to first hidden layer
        for (int i = 0; i < inputNeurons.length; i++){
            int startX = inputNeurons[i][0] + neuronRadius;
            int startY = inputNeurons[i][1] + neuronRadius/2;
            Integer[][] nextLayer = hiddenNeurons.get(0);
            for (int j = 0; j < nextLayer.length; j++){
                int endX = (int)(nextLayer[j][0]);
                int endY = (int)(nextLayer[j][1]) + neuronRadius/2;
                g.setColor(Color.BLACK);
                g.drawLine(startX, startY, endX, endY);
                ret.add(new Integer[]{startX, startY, endX, endY});
            }
        }
        //Weight lines between each hidden layer
        for (int i = 0; i < hiddenNeurons.size() - 1; i++){
            Integer[][] startLayer = hiddenNeurons.get(i);
            Integer[][] endLayer = hiddenNeurons.get(i + 1);
            for (int j = 0; j < startLayer.length; j++){
                int startX = (int)(startLayer[j][0]) + neuronRadius;
                int startY = (int)(startLayer[j][1]) + neuronRadius/2;
                for (int k = 0; k < endLayer.length; k++){
                    int endX = (int)(endLayer[k][0]);
                    int endY = (int)(endLayer[k][1]) + neuronRadius/2;
                    g.drawLine(startX, startY, endX, endY);
                    ret.add(new Integer[]{startX, startY, endX, endY});
                }
            }
        }
        //Weight lines from last hidden layer to outputs
        Integer[][] startLayer = hiddenNeurons.get(hiddenNeurons.size()-1);
        for (int i = 0; i < startLayer.length; i++){
            int startX = (int)(startLayer[i][0]) + neuronRadius;
            int startY = (int)(startLayer[i][1]) + neuronRadius/2;
            for (int j = 0; j < outputs; j++){
                int endX = (int)(outputNeurons[j][0]);
                int endY = (int)(outputNeurons[j][1]) + neuronRadius/2;
                g.drawLine(startX, startY, endX, endY);
                ret.add(new Integer[]{startX, startY, endX, endY});
            }
        }
        return ret;
    }

    public Color visualizeFiring(int layer, int neuron){
        float f = (info.get(layer).getMatrix()[neuron][0]);
        f = (normalize(f)*255);
        int num = (int)f;
        return new Color(num, num, num);
    }

    //Set values between 0 amd 1 (pretty lazy implementation if I'm being honest, could use some work)
    //Deals with network problem where values are larger than 1
    public float normalize(float num){
        float ret = num;
        if (ret > 1){
            ret = 1;
        }else if (ret < 0){
            ret = 0;
        }
        return ret;
    }

    //Toggle border around network 
    public void setBorder(boolean drawBorder){
        border = drawBorder;
    }


    //Decide how much padding around network to border
    public void setPadding(int padding){
        this.padding = padding;
    }
    
}
