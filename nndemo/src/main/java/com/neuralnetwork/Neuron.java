package com.neuralnetwork;

//Definitely unnecessary Neuron class that I probably won't use
public class Neuron {
    
    private float value;

    public Neuron(float val){
        value = val;
    }

    public float getValue(){
        return value;
    }

    public void setValue(float value){
        this.value = value;
    }
}
