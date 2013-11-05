package com.echo8.bprmf.data;

import com.echo8.bprmf.type.UserItemPair;

public class CsvDataFileFormat implements DataFileFormat {

    public CsvDataFileFormat() {
    }

    @Override
    public UserItemPair parseLine(String line) {
        String[] splitLine = line.split(",");
        return new UserItemPair(splitLine[0], splitLine[1]);
    }
}
