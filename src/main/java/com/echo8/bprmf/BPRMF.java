package com.echo8.bprmf;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.echo8.bprmf.conf.Defaults;
import com.echo8.bprmf.data.FeedbackData;
import com.echo8.bprmf.type.FactorMatrix;
import com.echo8.bprmf.type.ItemPair;
import com.echo8.bprmf.utils.MatrixUtils;
import com.echo8.bprmf.utils.RandomUtils;

public class BPRMF {
    private FeedbackData feedbackData;

    private FactorMatrix userFactorMatrix;
    private FactorMatrix itemFactorMatrix;

    private float[] itemBias;

    private float learnRate;
    private float regBias;
    private float regU;
    private float regI;
    private float regJ;

    private int numIterations;
    private int numFactors;

    private boolean updateJ;

    public BPRMF() {
        this.learnRate = Defaults.LEARN_RATE;
        this.regBias = Defaults.REG_BIAS;
        this.regU = Defaults.REG_U;
        this.regI = Defaults.REG_I;
        this.regJ = Defaults.REG_J;
        this.numIterations = Defaults.NUM_ITERATIONS;
        this.numFactors = Defaults.NUM_FACTORS;
        this.updateJ = Defaults.UPDATE_J;
    }

    public void setFeedbackData(FeedbackData feedbackData) {
        this.feedbackData = feedbackData;
    }

    public FeedbackData getFeedbackData() {
        return feedbackData;
    }

    public void setLearnRate(float learnRate) {
        this.learnRate = learnRate;
    }

    public void setRegBias(float regBias) {
        this.regBias = regBias;
    }

    public void setRegU(float regU) {
        this.regU = regU;
    }

    public void setRegI(float regI) {
        this.regI = regI;
    }

    public void setRegJ(float regJ) {
        this.regJ = regJ;
    }

    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    public void setNumFactors(int numFactors) {
        this.numFactors = numFactors;
    }

    public void setUpdateJ(boolean updateJ) {
        this.updateJ = updateJ;
    }

    public void train() {
        itemBias = new float[feedbackData.getNumItems()];
        Arrays.fill(itemBias, 0.f);

        userFactorMatrix = new FactorMatrix(feedbackData.getNumUsers(), numFactors);
        itemFactorMatrix = new FactorMatrix(feedbackData.getNumItems(), numFactors);

        for (int i = 0; i < numIterations; i++) {
            for (Integer userId : getRandomUserIdList()) {
                List<Integer> posItemIdList =
                        new ArrayList<Integer>(feedbackData.getItemIdSetForUserId(userId));
                Collections.shuffle(posItemIdList, RandomUtils.getInstance());
                for (Integer posItemId : posItemIdList) {
                    Integer negItemId = sampleNegativeItemId(userId);
                    ItemPair itemPair = new ItemPair(posItemId, negItemId);
                    updateFactors(userId, itemPair);
                }
            }
        }
    }

    private List<Integer> getRandomUserIdList() {
        List<Integer> userIdList = new ArrayList<Integer>();
        for (int i = 0; i < feedbackData.getNumUsers(); i++) {
            userIdList.add(i);
        }

        Collections.shuffle(userIdList, RandomUtils.getInstance());

        return userIdList;
    }

    private Integer sampleNegativeItemId(Integer userId) {
        Random rand = RandomUtils.getInstance();
        Set<Integer> itemIdSet = feedbackData.getItemIdSetForUserId(userId);
        Integer negItemId = rand.nextInt(feedbackData.getNumItems());
        while (itemIdSet.contains(negItemId)) {
            negItemId = rand.nextInt(feedbackData.getNumItems());
        }

        return negItemId;
    }

    private void updateFactors(Integer userId, ItemPair itemPair) {
        float xUIJ =
                itemBias[itemPair.getPosItemId()]
                        - itemBias[itemPair.getNegItemId()]
                        + MatrixUtils.rowScalarProductWithRowDifference(userFactorMatrix, userId,
                                itemFactorMatrix, itemPair.getPosItemId(), itemPair.getNegItemId());

        float sigmoidAtXUIJ = (float) (1.0 / (1.0 + Math.exp(xUIJ)));

        itemBias[itemPair.getPosItemId()] +=
                learnRate * (sigmoidAtXUIJ - regBias * itemBias[itemPair.getPosItemId()]);

        if (updateJ) {
            itemBias[itemPair.getNegItemId()] +=
                    learnRate * (-sigmoidAtXUIJ - regBias * itemBias[itemPair.getNegItemId()]);
        }

        for (int i = 0; i < numFactors; i++) {
            float wUF = userFactorMatrix.getValue(userId, i);
            float hIF = itemFactorMatrix.getValue(itemPair.getPosItemId(), i);
            float hJF = itemFactorMatrix.getValue(itemPair.getNegItemId(), i);

            userFactorMatrix.setValue(userId, i, wUF + learnRate
                    * ((hIF - hJF) * sigmoidAtXUIJ - regU * wUF));
            itemFactorMatrix.setValue(itemPair.getPosItemId(), i, hIF + learnRate
                    * (wUF * sigmoidAtXUIJ - regI * hIF));

            if (updateJ) {
                itemFactorMatrix.setValue(itemPair.getNegItemId(), i, hJF + learnRate
                        * (-wUF * sigmoidAtXUIJ - regJ * hJF));
            }
        }
    }

    public float predict(Integer userId, Integer itemId) {
        return itemBias[itemId]
                + MatrixUtils.rowScalarProduct(userFactorMatrix, userId, itemFactorMatrix, itemId);
    }

    public void save(ObjectOutputStream output) throws IOException {
        feedbackData.save(output);
        userFactorMatrix.save(output);
        itemFactorMatrix.save(output);

        output.writeInt(itemBias.length);
        for (float bias : itemBias) {
            output.writeFloat(bias);
        }

        output.writeFloat(learnRate);
        output.writeFloat(regBias);
        output.writeFloat(regU);
        output.writeFloat(regI);
        output.writeFloat(regJ);

        output.writeObject(numIterations);
        output.writeObject(numFactors);
        output.writeObject(updateJ);
    }

    public void load(ObjectInputStream input) throws ClassNotFoundException, IOException {
        feedbackData = new FeedbackData();
        feedbackData.load(input);
        userFactorMatrix = new FactorMatrix();
        userFactorMatrix.load(input);
        itemFactorMatrix = new FactorMatrix();
        itemFactorMatrix.load(input);

        itemBias = new float[input.readInt()];
        for (int i = 0; i < itemBias.length; i++) {
            itemBias[i] = input.readFloat();
        }

        learnRate = input.readFloat();
        regBias = input.readFloat();
        regU = input.readFloat();
        regI = input.readFloat();
        regJ = input.readFloat();

        numIterations = (Integer) input.readObject();
        numFactors = (Integer) input.readObject();
        updateJ = (Boolean) input.readObject();
    }
}
