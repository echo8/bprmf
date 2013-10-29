package com.echo8.bprmf.data;

import com.echo8.bprmf.type.UserItemPair;

public interface DataFileFormat {
    UserItemPair parseLine(String line);
}
