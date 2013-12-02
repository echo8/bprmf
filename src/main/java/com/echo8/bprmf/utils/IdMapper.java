package com.echo8.bprmf.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdMapper {
    private Map<String, Integer> rawIdToIdMap;
    private List<String> rawIdList;

    public IdMapper() {
        this.rawIdToIdMap = new HashMap<String, Integer>();
        this.rawIdList = new ArrayList<String>();
    }

    public int getId(String str) {
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

    public int getMaxId() {
        return rawIdToIdMap.size() - 1;
    }

    public String getRawId(Integer id) {
        return rawIdList.get(id);
    }

    public void save(ObjectOutputStream output) throws IOException {
        output.writeObject(rawIdToIdMap);
        output.writeObject(rawIdList);
    }

    @SuppressWarnings("unchecked")
    public void load(ObjectInputStream input) throws ClassNotFoundException, IOException {
        rawIdToIdMap = (Map<String, Integer>) input.readObject();
        rawIdList = (List<String>) input.readObject();
    }
}
