package com.colortraining;

import com.neuralnetwork.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;

public class ColorTrainer {
    
    String fileData;
    String fileName;
    ArrayList<String> data = new ArrayList<String>();
    int trainingIterations = 100000;
    boolean testing = false;
    private float accuracy;

    //Shape of network
    private int[] networkInfo = {3, 12, 12, 11};

    public NeuralNetwork network = new NeuralNetwork(new float[]{0f, 0f, 0f}, new int[]{16, 12}, 11);

    public HashMap<String, Integer> numbers = new HashMap<String, Integer>();

    public ColorTrainer(String dataName){
        this.fileName = dataName;
        network.setLearningRate(0.05f);
        populateHashMap();
        retrieveFileData(dataName);
        train();
    }

    public ColorTrainer(){}


    public void retrieveFileData(String file){
        //Populate String of raw data from file
        fileData = "";
        try{
            File dataFile = new File(file);
            Scanner scan = new Scanner(dataFile);
            while (scan.hasNextLine()){
                fileData += scan.nextLine() + "\n";
            }
            scan.close();
        }catch (FileNotFoundException e){
            System.out.println("Could not find specified file");
        }
        String ret = "";
        int startIndex = 0;
        //Populate ArrayList of data
        for (int i = 0; i < fileData.length(); i++){
            if (fileData.charAt(i) == '-'){
                ret = fileData.substring(startIndex, i - 1);
                startIndex = i + 8;
                i += 8;
                data.add(ret);
            }
        }
    }

    //Randomly train network based on training data 
    public void train(){

        int countCorrect = 0; 
        File f = new File("nndemo/src/main/java/com/colortraining/saved_net_data.txt");
        if (f.exists() && testing == false){
            System.out.println("Found configuration File. Initializing pre-trained network...");
            trainingIterations = 1000;
            new SavedNetReader("nndemo/src/main/java/com/colortraining/saved_net_data.txt", network);
        }
        for (int i = 0; i < trainingIterations; i++){
            //Get values from random data point in list
            int rand = (int)(Math.random()*data.size());
            String testData = data.get(rand), answer = "";
            int[] rgbs = new int[3];
            int line = 0, startIndex = 1;
            //Parse String of training data to get rgb values and associated color
            for (int j = 1; j < testData.length(); j++){
                if (line == 3){
                    answer = testData.substring(startIndex, testData.length()).replace("\n", "");
                }
                if (testData.charAt(j) == '\n'){
                    int parsed = Integer.parseInt(testData.substring(startIndex, j).replace(" ", ""));
                    rgbs[line] = parsed;
                    startIndex = j + 1;
                    line++;
                }
            }

            float[] inputs = new float[]{((float)rgbs[0])/255, ((float)rgbs[1])/255, ((float)rgbs[2])/255};
            float[] answers = getExpectedAnswer(answer);
            if (testing){
                Matrix output = network.feedForward(inputs);
                if (getAnswer(output).contains(answer)){
                    countCorrect++;
                }
            }else{
                network.train(inputs, answers);
            }
        }
        if(testing){
            accuracy = (float)countCorrect/trainingIterations;
            System.out.println("Accuracy: " + accuracy);
        }
    }

    //What is the length of the data file?
    public int dataLength(){
        int ret = 0;
        for (int i = 0; i < fileData.length(); i++){
            if (fileData.charAt(i) == '\n'){
                ret++;
            }
        }
        return ret;
    }

    //Match colors with their respective indeces in network output
    public void populateHashMap(){
        String[] colors = {"Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "Brown", "Gray", "Black", "White"};
        for (int i = 0; i < colors.length; i++){
            numbers.put(colors[i], Integer.valueOf(i));
        }
    }

    //Given expected color, change to float array to tell network what it should expect 
    public float[] getExpectedAnswer(String answer){
        float expectedAnswer = (float)numbers.get(answer);
        float[] ret = new float[numbers.size()];
        for (int i = 0; i < ret.length; i++){
            ret[i] = 0f; 
            if (i == expectedAnswer){
                ret[i] = 1f;
            }
        }
        return ret;
    }

    //Match each type of output to its respective color written as a String 
    public String getAnswer(Matrix outputArr){
        float[] outputs = new float[outputArr.getRows()];
        for (int i = 0; i < outputArr.getRows(); i++){
            outputs[i] = outputArr.getMatrix()[i][0];
        }
        float largest = Float.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < outputs.length; i++){
            if (outputs[i] > largest){
                largest = outputs[i];
                index = i;
            }
        }
        String ret = "";
        for (String s: numbers.keySet()){
            if (numbers.get(s) == Integer.valueOf(index)){
                ret = s;
            }
        }
        return ret;
    }

    //Feeds forward a color of specified rgb
    public String simplifyColor(int r, int g, int b){
        float[] in = {(float)r/255, (float)g/255, (float)b/255};
        String color = getAnswer(network.feedForward(in)).toLowerCase();
        return color;
    }

    //SETTER METHODS 

    //Change Name of Data File
    public void setDataName(String name){
        this.fileName = name;
    }

    //Toggle training/testing
    public void setTesting(boolean testing){
        this.testing = testing;
    }

    public void setTrainingIterations(int iterations){
        this.trainingIterations = iterations;
    }

    //GETTER METHODS 

    //Retrieve shape of network 
    public int[] getNetworkInfo(){
        return networkInfo;
    }
    
    public float getAccuracy(){
        return accuracy;
    }

}

