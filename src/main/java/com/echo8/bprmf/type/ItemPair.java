package com.echo8.bprmf.type;

public class ItemPair {
    private final int posItemId;
    private final int negItemId;

    public ItemPair(int posItemId, int negItemId) {
        this.posItemId = posItemId;
        this.negItemId = negItemId;
    }

    public int getPosItemId() {
        return posItemId;
    }

    public int getNegItemId() {
        return negItemId;
    }
}
