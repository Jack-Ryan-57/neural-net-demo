package com.neuralnetwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class SavedNetReader {

    public static int currentIndex = 0;
    private NeuralNetwork net;


    //Constructor retrieves and parses file data from specified data path, sets specified network values to parsed data
    //It is assumed that the configuration file passed in matches the shape of the neural network passed in second arg
    public SavedNetReader(String dataPath, NeuralNetwork net){
        
        this.net = net;

        String data = SavedNetReader.parseFileData(dataPath);
        ArrayList<Float> vals = SavedNetReader.parseValues(data, currentIndex);
        ArrayList<Layer> layers = net.layers;
        //Retrieve all of the Layers from Network 
        int index = 0;
        for (Layer l: layers){
        
            Matrix biases = l.biases;
            Matrix weights = l.weights;
            Matrix values = l.values;
            //Add matrix values for each layer
            for (int i = 0; i < weights.getMatrix().length; i++){
                for (int j = 0; j < weights.getMatrix()[0].length; j++){
                    weights.getMatrix()[i][j] = vals.get(index);
                    index++;
                }
            }
            //Bias values for each layer
            for (int i = 0; i < biases.getMatrix().length; i++){
                biases.getMatrix()[i][0] = vals.get(index);
                index++;
            }
            //Neuron values for each layer
            for (int i = 0; i < values.getMatrix().length; i++){
                values.getMatrix()[i][0] = vals.get(index);
                index++;
            }     
        }
    }

    //Retrieve all data from specified file
    public static String parseFileData(String filePath){
        try{
            File f = new File(filePath);
            String ret = "";
            Scanner scan = new Scanner(f);
            while (scan.hasNextLine()){
                String read = scan.nextLine();
                ret += read + "\n";
            }
            scan.close();
            return ret;

        }catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    //Create an ArrayList of only the floats that were parsed from the file, remove everything else
    public static ArrayList<Float> parseValues(String input, int startIndex){
        ArrayList<Float> ret = new ArrayList<Float>();
        String line = "";
        for (int i = startIndex; i < input.length(); i++){
            char letter = input.charAt(i);
            if (letter == '\n'){
                try{
                    Float f = Float.parseFloat(line);
                    ret.add(f);
                }catch(NumberFormatException e){}
                line = "";
            }else{
                line += letter;
            }
        }
        return ret;
    }

    //Prints out entire neural network (weights, biases, values)
    public void printNeuralNet(){
        System.out.println(net.toString());
    }



}