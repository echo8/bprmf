package com.echo8.bprmf.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdGenerator {
    private final Map<String, Integer> rawIdToIdMap;
    private final List<String> rawIdList;

    public IdGenerator() {
        this.rawIdToIdMap = new HashMap<String, Integer>();
        this.rawIdList = new ArrayList<String>();
    }

    public Integer getId(String str) {
        return getId(str, true);
    }

    public Integer getId(String rawId, Boolean addNewId) {
        Integer id = rawIdToIdMap.get(rawId);
        if (id == null && addNewId) {
            id = rawIdToIdMap.size();
            rawIdToIdMap.put(rawId, id);
            rawIdList.add(rawId);
        }

        return id;
    }

    public Integer getMaxId() {
        return rawIdToIdMap.size() - 1;
    }

    public String getRawId(Integer id) {
        return rawIdList.get(id);
    }
}
