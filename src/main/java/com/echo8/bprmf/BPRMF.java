package com.echo8.bprmf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.echo8.bprmf.data.PosFeedbackData;
import com.echo8.bprmf.type.FactorMatrix;
import com.echo8.bprmf.type.ItemPair;
import com.echo8.bprmf.utils.MatrixUtils;

public class BPRMF {
    private static final float DEFAULT_LEARN_RATE = 0.05f;
    private static final float DEFAULT_REG_BIAS = 0.001f;
    private static final float DEFAULT_REG_U = 0.0025f;
    private static final float DEFAULT_REG_I = 0.0025f;
    private static final float DEFAULT_REG_J = 0.00025f;
    private static final Integer DEFAULT_NUM_ITERATIONS = 30;
    private static final Integer DEFAULT_NUM_FACTORS = 10;
    private static final Boolean DEFAULT_UPDATE_J = true;

    private final PosFeedbackData posFeedbackData;

    private FactorMatrix userFactorMatrix;
    private FactorMatrix itemFactorMatrix;

    private float[] itemBias;

    private float learnRate;
    private float regBias;
    private float regU;
    private float regI;
    private float regJ;

    private Integer numIterations;
    private Integer numFactors;

    private Boolean updateJ;

    private final Random rand;

    public BPRMF(PosFeedbackData posFeedbackData) {
        this.posFeedbackData = posFeedbackData;
        this.learnRate = DEFAULT_LEARN_RATE;
        this.regBias = DEFAULT_REG_BIAS;
        this.regU = DEFAULT_REG_U;
        this.regI = DEFAULT_REG_I;
        this.regJ = DEFAULT_REG_J;
        this.numIterations = DEFAULT_NUM_ITERATIONS;
        this.numFactors = DEFAULT_NUM_FACTORS;
        this.updateJ = DEFAULT_UPDATE_J;
        this.rand = new Random();
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

    public void setNumIterations(Integer numIterations) {
        this.numIterations = numIterations;
    }

    public void setNumFactors(Integer numFactors) {
        this.numFactors = numFactors;
    }

    public void setUpdateJ(Boolean updateJ) {
        this.updateJ = updateJ;
    }

    public void train() {
        itemBias = new float[posFeedbackData.getNumItems()];
        Arrays.fill(itemBias, 0.f);

        userFactorMatrix =
            new FactorMatrix(posFeedbackData.getNumUsers(), numFactors);
        itemFactorMatrix =
            new FactorMatrix(posFeedbackData.getNumItems(), numFactors);

        for (int i = 0; i < numIterations; i++) {
            for (Integer userId : getRandomUserIdList()) {
                ItemPair itemPair = sampleItemPair(userId);
                updateFactors(userId, itemPair);
            }
        }
    }

    private List<Integer> getRandomUserIdList() {
        List<Integer> userIdList = new ArrayList<Integer>();
        for (int i = 0; i < posFeedbackData.getNumUsers(); i++) {
            userIdList.add(i);
        }

        Collections.shuffle(userIdList);

        return userIdList;
    }

    private ItemPair sampleItemPair(Integer userId) {
        Set<Integer> itemIdSet = posFeedbackData.getFeedbackList().get(userId);
        List<Integer> itemIdList = new ArrayList<Integer>(itemIdSet);

        Integer posItemId = itemIdList.get(rand.nextInt(itemIdList.size()));
        Integer negItemId = rand.nextInt(posFeedbackData.getNumItems());
        while (itemIdSet.contains(negItemId)) {
            negItemId = rand.nextInt(posFeedbackData.getNumItems());
        }

        return new ItemPair(posItemId, negItemId);
    }

    private void updateFactors(Integer userId, ItemPair itemPair) {
        float x_uij =
            itemBias[itemPair.getPosItemId()]
                - itemBias[itemPair.getNegItemId()]
                + MatrixUtils.rowScalarProductWithRowDifference(
                    userFactorMatrix,
                    userId,
                    itemFactorMatrix,
                    itemPair.getPosItemId(),
                    itemPair.getNegItemId());

        float one_over_one_plus_ex = (float) (1.0 / (1.0 + Math.exp(x_uij)));

        itemBias[itemPair.getPosItemId()] +=
            learnRate
                * (one_over_one_plus_ex - regBias
                    * itemBias[itemPair.getPosItemId()]);

        if (updateJ) {
            itemBias[itemPair.getNegItemId()] +=
                learnRate
                    * (one_over_one_plus_ex - regBias
                        * itemBias[itemPair.getNegItemId()]);
        }

        for (int i = 0; i < numFactors; i++) {
            float w_uf = userFactorMatrix.getValue(userId, i);
            float h_if = itemFactorMatrix.getValue(itemPair.getPosItemId(), i);
            float h_jf = itemFactorMatrix.getValue(itemPair.getNegItemId(), i);

            userFactorMatrix.setValue(userId, i, w_uf
                + learnRate
                * ((h_if - h_jf) * one_over_one_plus_ex - regU * w_uf));
            itemFactorMatrix.setValue(itemPair.getPosItemId(), i, h_if
                + learnRate
                * (w_uf * one_over_one_plus_ex - regI * h_if));

            if (updateJ) {
                itemFactorMatrix.setValue(itemPair.getNegItemId(), i, h_jf
                    + learnRate
                    * (-w_uf * one_over_one_plus_ex - regJ * h_jf));
            }
        }
    }
}
