package com.echo8.bprmf.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.echo8.bprmf.type.UserItemPair;
import com.echo8.bprmf.utils.IdGenerator;

public class FeedbackData {
    private List<Set<Integer>> feedbackList;

    private IdGenerator userIdGenerator;
    private IdGenerator itemIdGenerator;

    public FeedbackData() {
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

    public int getNumUsers() {
        return userIdGenerator.getMaxId() + 1;
    }

    public int getNumItems() {
        return itemIdGenerator.getMaxId() + 1;
    }

    public Set<Integer> getItemIdSetForUserId(Integer userId) {
        return feedbackList.get(userId);
    }

    public String getRawUserId(Integer userId) {
        return userIdGenerator.getRawId(userId);
    }

    public Integer rawUserIdToUserId(String rawUserId) {
        return userIdGenerator.getId(rawUserId, false);
    }

    public String getRawItemId(Integer itemId) {
        return itemIdGenerator.getRawId(itemId);
    }

    public Integer rawItemIdToItemId(String rawItemId) {
        return itemIdGenerator.getId(rawItemId, false);
    }

    public void save(ObjectOutputStream output) throws IOException {
        userIdGenerator.save(output);
        itemIdGenerator.save(output);
    }

    public void load(ObjectInputStream input) throws ClassNotFoundException, IOException {
        userIdGenerator.load(input);
        itemIdGenerator.load(input);
    }
}
