package com.echo8.bprmf.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.echo8.bprmf.type.UserItemPair;
import com.echo8.bprmf.utils.IdGenerator;

public class PosFeedbackData {
    private final List<Set<Integer>> feedbackList;

    private final IdGenerator userIdGenerator;
    private final IdGenerator itemIdGenerator;

    public PosFeedbackData() {
        this.feedbackList = new ArrayList<Set<Integer>>();

        this.userIdGenerator = new IdGenerator();
        this.itemIdGenerator = new IdGenerator();
    }

    public void addFeedback(UserItemPair userItemPair) {
        Integer userId = userIdGenerator.getId(userItemPair.getUser());
        Integer itemId = itemIdGenerator.getId(userItemPair.getItem());

        for (int i = feedbackList.size(); i < userId + 1; i++) {
            feedbackList.add(new HashSet<Integer>());
        }

        feedbackList.get(userId).add(itemId);
    }

    public Integer getNumUsers() {
        return userIdGenerator.getMaxId() + 1;
    }

    public Integer getNumItems() {
        return itemIdGenerator.getMaxId() + 1;
    }

    public List<Set<Integer>> getFeedbackList() {
        return feedbackList;
    }
}
