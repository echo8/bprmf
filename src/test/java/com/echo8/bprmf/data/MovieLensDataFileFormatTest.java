package com.echo8.bprmf.data;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.echo8.bprmf.type.UserItemPair;

public class MovieLensDataFileFormatTest {

    @Test
    public void testParseLine() {
        DataFileFormat dataFileFormat = new MovieLensDataFileFormat();
        UserItemPair userItemPair = dataFileFormat.parseLine("user1::item1::5");
        assertThat(userItemPair.getUser(), is("user1"));
        assertThat(userItemPair.getItem(), is("item1"));
    }

    @Test
    public void testParseLineWithNegativeFeedback() {
        DataFileFormat dataFileFormat = new MovieLensDataFileFormat();
        UserItemPair userItemPair = dataFileFormat.parseLine("user1::item1::1");
        assertThat(userItemPair, is(nullValue()));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseLineWithNonMovieLensLine() {
        DataFileFormat dataFileFormat = new MovieLensDataFileFormat();
        dataFileFormat.parseLine("user1,item1");
    }
}
