package com.neuralnetwork;

import java.util.ArrayList;
import com.colortraining.WriteToFile;

 
public class NeuralNetwork{
 
    private int inputNeurons;
    private int[] hiddenNeurons;
    private int outputNeurons;
    private float learningRate = 0.1f;
 
    private ArrayList<Matrix> errorMatrices = new ArrayList<Matrix>();
    public ArrayList<Layer> layers = new ArrayList<Layer>();

    Matrix inputsAsMatrix;
    
    //Arrays to update with new values
 
    //Constructor for neural network with one hidden layer
    //Params: list of input values, number hidden neurons, number output neurons
    public NeuralNetwork(float[] inputs, int hiddenNeurons, int outputNeurons){
        inputNeurons = inputs.length;
        Matrix m = new Layer(inputNeurons, hiddenNeurons).feedForward(inputs);
        //m.printMatrix();
 
        Layer l = new Layer(hiddenNeurons, outputNeurons, m);
        Matrix n = l.feedForward(Matrix.matrixToArray(l.inputs));
        n.printMatrix();
    }
 
 
    //Constructor for neural network with multiple hidden layers
    //Params: list of input values, list of number of neurons in each hidden layer, number of output neurons
    public NeuralNetwork(float[] inputs, int[] hiddenNeurons, int outputNeurons){
        
        this.hiddenNeurons = hiddenNeurons;
        //Populate inputs
        inputNeurons = inputs.length;
        this.outputNeurons = outputNeurons;
 
        //Create first layer from inputs 
        Layer inputToHiddenLayer = new Layer(inputNeurons, hiddenNeurons[0]);
        Matrix inputToHiddenMatrix = inputToHiddenLayer.feedForward(inputs);
        inputToHiddenLayer.populateNeurons(inputToHiddenMatrix);
        addLayer(inputToHiddenLayer, 0);


        //Populate inner hidden layers
        Matrix prev = inputToHiddenMatrix;
        for (int i = 1; i < hiddenNeurons.length; i++){
            
            Layer l  = new Layer(hiddenNeurons[i-1], hiddenNeurons[i]);
            float[] prevArray = Matrix.matrixToArray(prev);
            Matrix feed = l.feedForward(prevArray);
            l.populateNeurons(feed);
            prev = feed;
            addLayer(l, i);
        }
 
        //Create output Layer
        float[] prevArray = Matrix.matrixToArray(prev);
        Layer outputLayer = new Layer(hiddenNeurons[hiddenNeurons.length - 1], outputNeurons);
        Matrix outputMatrix = outputLayer.feedForward(prevArray);
        outputLayer.populateNeurons(outputMatrix);

        addLayer(outputLayer, layers.size() - 1);
        Layer.setRandomize(false);

    }

    
    public Matrix feedForward(float[] inputs){
        //Populate inputs
        inputNeurons = inputs.length;
        //Create first layer from inputs 
        Layer inputToHiddenLayer = layers.get(0);
        //Feed forward from inputs to first hidden layer
        Matrix inputToHiddenMatrix = inputToHiddenLayer.feedForward(inputs);
        inputsAsMatrix = new Matrix(inputNeurons, 1, Matrix.inputsToMatrix(inputs));
        inputToHiddenLayer.populateNeurons(inputToHiddenMatrix);
        //layers.add(inputToHiddenLayer);*/
        addLayer(inputToHiddenLayer, 0);
        
 
        //Populate inner hidden layers
        Matrix prev = inputToHiddenMatrix;
        for (int i = 1; i < hiddenNeurons.length; i++){
            
            Layer l  = layers.get(i);
            float[] prevArray = Matrix.matrixToArray(prev);
            Matrix feed = l.feedForward(prevArray);
            l.populateNeurons(feed);
 
            prev = feed;
            addLayer(l, i);
        }
 
        //Create output Layer
        float[] prevArray = Matrix.matrixToArray(prev);
        Layer outputLayer = layers.get(layers.size()-1);
        Matrix outputMatrix = outputLayer.feedForward(prevArray);
        outputLayer.populateNeurons(outputMatrix);
        addLayer(outputLayer, layers.size() - 1);

        return outputMatrix;
    }

    

    //Calculate error (TARGETS - OUTPUTS)
    public Matrix errorsMatrix(Matrix output, Matrix target){
        return Matrix.subtractMatrices(target, output);
    }


    public void addLayer(Layer l, int index){
        if (layers.size() != hiddenNeurons.length + 1){
            layers.add(l);
        }else{
            layers.set(index, l);
        }
    }
    

    public void train(float[] inputs, float[] targets){
 
        Matrix inputMatrix = feedForward(inputs);
        //Populate target matrix (Matrix of expected values)
        float[][] arr = new float[targets.length][1];
        for (int i = 0; i < targets.length; i++){
            arr[i][0] = targets[i];
        }
        Matrix targetMatrix = new Matrix(targets.length, 1, arr);
        //Error calculation
        Matrix errorMatrix = errorsMatrix(inputMatrix, targetMatrix);

        //E: errorMatrix, lr: learning rate, H: hidden neurons
 
        //Calculate hidden layer errors 
        Matrix prev = null;
        for (int i = layers.size() - 1; i >= 0; i--){
            Matrix transposed = Matrix.transpose(layers.get(i).weights);
            Layer currentLayer = layers.get(i);
            Matrix ret; 
            //From output layer to last hidden layer
            if (i == layers.size() - 1){
                ret = Matrix.matrixProduct(transposed, errorMatrix);
                //Calculate gradient
                //Formula: lr*E*(O*(1-O))
                Matrix gradient = currentLayer.dsigmoid();
                gradient = Matrix.hadamardProduct(gradient, errorMatrix);
                gradient.scaleMatrixBy(learningRate);
                //Calculate deltas
                //Formula: lr*E*(O*(1-O))*H(transposed)
                Matrix transposedHidden = Matrix.transpose(layers.get(i - 1).values);
                Matrix changeInWeights = Matrix.matrixProduct(gradient, transposedHidden);
                //Apply changes to weights based on deltas
                layers.get(i).weights.addMatrix(changeInWeights);
                //Apply changes to biases based on gradient
                layers.get(i).biases.addMatrix(gradient);
                prev = ret;

            }else if (i == 0){
                ret = Matrix.matrixProduct(transposed, prev);
                //Calculate gradient
                Matrix gradient = currentLayer.dsigmoid();
                Matrix tempError = errorMatrices.get(errorMatrices.size() - 1);
                gradient = Matrix.hadamardProduct(gradient, tempError);
                gradient.scaleMatrixBy(learningRate);
                //Calculate deltas 
                Matrix transposedInputs = Matrix.transpose(new Matrix(inputs.length, 1, Matrix.inputsToMatrix(inputs)));
                Matrix changeInWeights = Matrix.matrixProduct(gradient, transposedInputs);
                //Apply changes to weights based on deltas
                layers.get(i).weights.addMatrix(changeInWeights);
                //Apply changes to biases based on gradient
                layers.get(i).biases.addMatrix(gradient);

            //Every other layer in network
            }else{
                ret = Matrix.matrixProduct(transposed, prev);
                //Calculate gradient
                Matrix gradient = currentLayer.dsigmoid();
                Matrix tempError = errorMatrices.get(errorMatrices.size() - 1);
                gradient = Matrix.hadamardProduct(gradient, tempError);
                gradient.scaleMatrixBy(learningRate);
                //Calculate deltas
                Matrix transposedHidden = Matrix.transpose(layers.get(i - 1).values);
                Matrix changeInWeights = Matrix.matrixProduct(gradient, transposedHidden);
                //Apply changes to weights based on gradient
                layers.get(i).weights.addMatrix(changeInWeights);
                //Apply changes to biases based on gradient
                layers.get(i).biases.addMatrix(gradient);
                prev = ret;
            }
            errorMatrices.add(ret);
        }
        
        errorMatrices = new ArrayList<Matrix>();
        
    }

    public void setLearningRate(float rate){
        this.learningRate = rate;
    }

    public int[] getNetworkShape(){
        int[] ret = new int[hiddenNeurons.length + 2];
        ret[0] = inputNeurons;
        for (int i = 0; i < hiddenNeurons.length; i++){
            ret[i + 1] = hiddenNeurons[i];
        }
        ret[ret.length - 1] = outputNeurons;
        return ret;
    }

    public ArrayList<Matrix> getNetworkInfo(){
        ArrayList<Matrix> ret = new ArrayList<Matrix>();
        ret.add(inputsAsMatrix);
        for (int i = 0; i < layers.size(); i++){
            Layer l = layers.get(i);
            Matrix vals = l.values;
            ret.add(vals);
        }
        return ret;
    }

    public void clearNetwork(){
        for (int i = 0; i < layers.size(); i++){
            layers.remove(layers.get(i));
            Layer.first = true;
        }
    }

    public void saveNetwork(String path){
        WriteToFile wf = new WriteToFile(path+ "/" + "saved_net_data.txt");
        for (int i = 0; i < layers.size(); i++){
            if (i == 0){
                wf.write("Layer " + (i + 1) + "\n", false);
            }else{
                wf.write("Layer" + (i + 1) + "\n", true);
            }
            wf.write(layers.get(i).toConfigString(), true);
            wf.write("\n", true);
        }
    }

    public String toString(){
        String ret = "";
        int layerNum = 1;
        for (Layer l: layers){
            ret += "Layer: " + layerNum + "\n";
            ret += l.toString();
            layerNum++;
        }
        return ret;
    }
}

