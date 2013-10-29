package com.echo8.bprmf.data;

import com.echo8.bprmf.type.UserItemPair;

public class MovieLensDataFileFormat implements DataFileFormat {
    private final static Integer DEFAULT_POSITIVE_THRESHOLD = 4;

    private final Integer positiveThreshold;

    public MovieLensDataFileFormat() {
        this.positiveThreshold = DEFAULT_POSITIVE_THRESHOLD;
    }

    public MovieLensDataFileFormat(Integer positiveThreshold) {
        this.positiveThreshold = positiveThreshold;
    }

    @Override
    public UserItemPair parseLine(String line) {
        String[] splitLine = line.split("::");
        if (Integer.parseInt(splitLine[2]) >= positiveThreshold) {
            return new UserItemPair(splitLine[0], splitLine[1]);
        }
        return null;
    }
}
