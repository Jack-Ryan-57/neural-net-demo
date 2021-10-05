package com.digitrecognition;

import com.neuralnetwork.*;

public class DigitRecognition {
    
    public int[][][] trainImgVals, testImgVals;
    public int[] trainLabelVals, testLabelVals;
    private int trainIterations = 10000;
    private boolean training = true;

    private NeuralNetwork net;

    public DigitRecognition(){

        System.out.println("Training....");
        //Retrieve training data
        String trainingImgs = "nndemo/src/main/java/com/digitrecognition/mnistdataset/train-images.idx3-ubyte";
        String trainingLabels = "nndemo/src/main/java/com/digitrecognition/mnistdataset/train-labels.idx1-ubyte";
        DataReader dri = new DataReader(trainingImgs);
        DataReader drl = new DataReader(trainingLabels);
        trainImgVals = dri.imgVals;
        trainLabelVals = drl.dataVals;

        // Train neural network  

        float[] init = imgToFloats(trainImgVals[0]);
        net = new NeuralNetwork(init, new int[]{16, 16, 12},  10);
        net.setLearningRate(0.1f);
        train();

        System.out.println("Testing....");
        //Retrieve testing data
        String testImgs = "nndemo/src/main/java/com/digitrecognition/mnistdataset/t10k-images.idx3-ubyte";
        String testLabels = "nndemo/src/main/java/com/digitrecognition/mnistdataset/t10k-labels.idx1-ubyte";
        DataReader drti = new DataReader(testImgs);
        DataReader drtl = new DataReader(testLabels);
        testImgVals = drti.imgVals;
        testLabelVals = drtl.dataVals;

        //Test how well the neural network was trained
        this.setTraining(false);
        train();
    }

    public void train(){


        //Pick random data points
        int countCorrect = 0; 
        for (int i = 0; i < trainIterations; i++){
            int rand = (int)(Matrix.random(0, 10000));
            //Get image values in float array (to use as inputs)
            float[] picture = imgToFloats(trainImgVals[rand]);
            if (training){

                //Get expected output and train network using respective labels 
                int expectedAnswer = trainLabelVals[rand];
                net.train(picture, answerToFloatArr(expectedAnswer));

            }else{

                int expectedAnswer = testLabelVals[rand];
                Matrix output = net.feedForward(picture);              
                int got = getNetworkOutput(matrixToOutput(output));
                if (i < 5){
                    System.out.println(expectedAnswer);
                    System.out.println(got);
                    System.out.println(output);
                }  
                if ((int)got == (int)expectedAnswer){
                    countCorrect++;
                }
            }
        }

        if (!training){
            float accuracy = (float)((countCorrect/trainIterations)*100);
            System.out.println("Predicted accuracy: " + accuracy);
        }
    }

    //Convert 2d array of images to 1d float array 
    //Awful idea, redundant, definitely needs changing
    public float[] imgToFloats(int[][] arr){
        float[] ret = new float[arr.length*arr[0].length];
        for (int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr[0].length; j++){
                //Set values to floats normalized between 0 and 1
                ret[i*arr[0].length + j] = (float)((arr[i][j]))/255;
            }
        }
        return ret;
    }

    //Change integer digit to float array for neural network output
    public float[] answerToFloatArr(int num){
        float[] ret = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
        ret[num] = 1f;
        return ret;
    }

    //Get specific answer from network array of floats
    public int fromFloatArr(float[] arr){
        for (int i = 0; i < arr.length; i++){
            if (arr[i] == 1f){
                return (int)i;
            }
        }
        return -1;
    }


    public String toString(float[] arr){
        String ret = "[";
        for (int i = 0; i < arr.length; i++){
            ret += arr[i];
            if (i < arr.length - 1){
                ret += ", ";
            }
        }
        ret += "]\n";
        return ret;
    }

    //Figure out what the network decided the answer was by picking the one with the highest value
    public int getNetworkOutput(float[] outputs){
        float biggest = Float.MIN_VALUE;
        int ret = -1;
        for (int i = 0; i < outputs.length; i++){
            if (outputs[i] > biggest){
                biggest = outputs[i];
                ret = i;
            }
        }
        if (biggest >= 0){
            return ret;
        }else{
            return -1;
        }
    }

    //Turn output Matrix into a 1d float array of outputs
    public float[] matrixToOutput(Matrix m){
        float[][] arr = m.getMatrix();
        float[] ret = new float[arr.length];
        for (int i = 0; i < arr.length; i++){
            ret[i] = arr[i][0];
        }
        return ret;
    }

    //Setter method for training boolean 
    public void setTraining(boolean training){
        this.training = training;
    }



}
