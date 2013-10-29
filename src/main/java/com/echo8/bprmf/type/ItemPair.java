package com.echo8.bprmf.type;

public class ItemPair {
    private final Integer posItemId;
    private final Integer negItemId;

    public ItemPair(Integer posItemId, Integer negItemId) {
        this.posItemId = posItemId;
        this.negItemId = negItemId;
    }

    public Integer getPosItemId() {
        return posItemId;
    }

    public Integer getNegItemId() {
        return negItemId;
    }
}
