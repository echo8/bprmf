package com.echo8.bprmf.data;

import com.echo8.bprmf.type.UserItemPair;

public class CdrDataFileFormat implements DataFileFormat {
	@Override
	public UserItemPair parseLine(String line) {
		try {
			String[] splitLine = line.split("\t");
			return new UserItemPair(splitLine[0], splitLine[1]);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalStateException(line, e);
		}
	}
}
