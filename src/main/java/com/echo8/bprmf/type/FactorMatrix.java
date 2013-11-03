package com.echo8.bprmf.type;

import java.util.Random;

import com.echo8.bprmf.conf.Defaults;

public class FactorMatrix {
    private final float[] factors;

    private final Integer numFactors;

    private final Random rand;

    public FactorMatrix(int numElements, int numFactors) {
        this(
            numElements,
            numFactors,
            Defaults.DEFAULT_MEAN,
            Defaults.DEFAULT_STD_DEV);
    }

    public FactorMatrix(int numElements, int numFactors, float mean,
            float stdDev) {
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

    public Integer getNumFactors() {
        return numFactors;
    }

    public void setValue(int element, int factor, float value) {
        factors[element * numFactors + factor] = value;
    }

    public float getValue(int element, int factor) {
        return factors[element * numFactors + factor];
    }
}
