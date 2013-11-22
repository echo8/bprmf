package com.echo8.bprmf.data;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.echo8.bprmf.type.UserItemPair;

public class CsvDataFileFormatTest {

    @Test
    public void testParseLine() {
        DataFileFormat dataFileFormat = new CsvDataFileFormat();
        UserItemPair userItemPair = dataFileFormat.parseLine("user1,item1");
        assertThat(userItemPair.getUser(), is("user1"));
        assertThat(userItemPair.getItem(), is("item1"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseLineWithNonCsvLine() {
        DataFileFormat dataFileFormat = new CsvDataFileFormat();
        dataFileFormat.parseLine("user1:item1");
    }

    @Test
    public void testParseLineWithUnsupportedCsvFeature() {
        DataFileFormat dataFileFormat = new CsvDataFileFormat();
        UserItemPair userItemPair = dataFileFormat.parseLine("\"user id, with comma\",item1");
        assertThat(userItemPair.getUser(), is("\"user id"));
        assertThat(userItemPair.getItem(), is(" with comma\""));
    }
}
