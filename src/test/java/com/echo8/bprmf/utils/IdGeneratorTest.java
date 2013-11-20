package com.echo8.bprmf.utils;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class IdGeneratorTest {

    @Test
    public void testGetId() {
        IdGenerator idGenerator = new IdGenerator();
        assertThat(idGenerator.getId("testid"), is(0));
    }

    @Test
    public void testGetIdWithTrueAddFlag() {
        IdGenerator idGenerator = new IdGenerator();
        assertThat(idGenerator.getId("testid", true), is(0));
    }

    @Test
    public void testGetIdWithFalseAddFlag() {
        IdGenerator idGenerator = new IdGenerator();
        assertThat(idGenerator.getId("testid", false), is(nullValue()));
    }

    @Test
    public void testGetIdWithAlreadyAddedId() {
        IdGenerator idGenerator = new IdGenerator();
        idGenerator.getId("testid");
        assertThat(idGenerator.getId("testid"), is(0));
    }

    @Test
    public void testGetIdWithTrueAddFlagAndAlreadyAddedId() {
        IdGenerator idGenerator = new IdGenerator();
        idGenerator.getId("testid");
        assertThat(idGenerator.getId("testid", true), is(0));
    }

    @Test
    public void testGetIdWithFalseAddFlagAndAlreadyAddedId() {
        IdGenerator idGenerator = new IdGenerator();
        idGenerator.getId("testid");
        assertThat(idGenerator.getId("testid", false), is(0));
    }

    @Test
    public void testGetMaxIdOnEmptyGenerator() {
        IdGenerator idGenerator = new IdGenerator();
        assertThat(idGenerator.getMaxId(), is(-1));
    }

    @Test
    public void testGetMaxId() {
        IdGenerator idGenerator = new IdGenerator();
        idGenerator.getId("testid1");
        idGenerator.getId("testid2");
        assertThat(idGenerator.getMaxId(), is(1));
    }

    @Test
    public void testGetRawId() {
        IdGenerator idGenerator = new IdGenerator();
        Integer id = idGenerator.getId("testid1");
        assertThat(idGenerator.getRawId(id), is("testid1"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetRawIdNonExisting() {
        IdGenerator idGenerator = new IdGenerator();
        idGenerator.getRawId(5);
    }
}
