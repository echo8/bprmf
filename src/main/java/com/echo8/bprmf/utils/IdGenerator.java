package com.echo8.bprmf.utils;

import java.util.HashMap;
import java.util.Map;

public class IdGenerator {
    private final Map<String, Integer> strToIdMap;

    public IdGenerator() {
        this.strToIdMap = new HashMap<String, Integer>();
    }

    public Integer getId(String str) {
        Integer id = strToIdMap.get(str);
        if (id == null) {
            id = strToIdMap.size();
            strToIdMap.put(str, id);
        }

        return id;
    }

    public Integer getMaxId() {
        return strToIdMap.size() - 1;
    }
}
