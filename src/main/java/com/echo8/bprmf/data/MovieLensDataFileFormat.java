package com.echo8.bprmf.data;

import com.echo8.bprmf.conf.Defaults;
import com.echo8.bprmf.type.UserItemPair;

public class MovieLensDataFileFormat implements DataFileFormat {
    private final int positiveThreshold;

    public MovieLensDataFileFormat() {
        this.positiveThreshold = Defaults.DEFAULT_MIN_POS_VALUE;
    }

    public MovieLensDataFileFormat(int positiveThreshold) {
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
