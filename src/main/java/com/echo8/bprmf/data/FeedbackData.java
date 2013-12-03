package com.echo8.bprmf.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.echo8.bprmf.type.UserItemPair;
import com.echo8.bprmf.utils.IdMapper;

public class FeedbackData {
    private List<Set<Integer>> feedbackList;

    private IdMapper userIdMapper;
    private IdMapper itemIdMapper;

    public FeedbackData() {
        this.feedbackList = new ArrayList<Set<Integer>>();

        this.userIdMapper = new IdMapper();
        this.itemIdMapper = new IdMapper();
    }

    public void addFeedback(UserItemPair userItemPair) {
        Integer userId = userIdMapper.getId(userItemPair.getUser());
        Integer itemId = itemIdMapper.getId(userItemPair.getItem());

        for (int i = feedbackList.size(); i < userId + 1; i++) {
            feedbackList.add(new HashSet<Integer>());
        }

        feedbackList.get(userId).add(itemId);
    }

    public int getNumUsers() {
        return userIdMapper.getMaxId() + 1;
    }

    public int getNumItems() {
        return itemIdMapper.getMaxId() + 1;
    }

    public Set<Integer> getItemIdSetForUserId(Integer userId) {
        return feedbackList.get(userId);
    }

    public String getRawUserId(Integer userId) {
        return userIdMapper.getRawId(userId);
    }

    public Integer rawUserIdToUserId(String rawUserId) {
        return userIdMapper.getId(rawUserId, false);
    }

    public String getRawItemId(Integer itemId) {
        return itemIdMapper.getRawId(itemId);
    }

    public Integer rawItemIdToItemId(String rawItemId) {
        return itemIdMapper.getId(rawItemId, false);
    }

    public void save(ObjectOutputStream output) throws IOException {
        userIdMapper.save(output);
        itemIdMapper.save(output);
        output.writeObject(feedbackList);
    }

    @SuppressWarnings("unchecked")
    public void load(ObjectInputStream input) throws ClassNotFoundException, IOException {
        userIdMapper.load(input);
        itemIdMapper.load(input);
        feedbackList = (List<Set<Integer>>) input.readObject();
    }
}
