package com.echo8.bprmf.type;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import com.echo8.bprmf.conf.Defaults;

public class FactorMatrix {
    private float[] factors;

    private int numFactors;

    private final Random rand;

    public FactorMatrix() {
        this.rand = new Random();
    }

    public FactorMatrix(int numElements, int numFactors) {
        this(numElements, numFactors, Defaults.MEAN, Defaults.STD_DEV);
    }

    public FactorMatrix(int numElements, int numFactors, float mean, float stdDev) {
        this.factors = new float[numElements * numFactors];

        this.numFactors = numFactors;

        this.rand = new Random();

        initNormal(mean, stdDev);
    }

    private void initNormal(float mean, float stdDev) {
        for (int i = 0; i < factors.length; i++) {
            factors[i] = (float) (rand.nextGaussian() * stdDev + mean);
        }
    }

    public int getNumFactors() {
        return numFactors;
    }

    public void setValue(int element, int factor, float value) {
        factors[element * numFactors + factor] = value;
    }

    public float getValue(int element, int factor) {
        return factors[element * numFactors + factor];
    }

    public void save(ObjectOutputStream output) throws IOException {
        output.writeInt(factors.length);
        for (float factor : factors) {
            output.writeFloat(factor);
        }
        output.writeObject(numFactors);
    }

    public void load(ObjectInputStream input) throws ClassNotFoundException, IOException {
        factors = new float[input.readInt()];
        for (int i = 0; i < factors.length; i++) {
            factors[i] = input.readFloat();
        }
        numFactors = (Integer) input.readObject();
    }
}
