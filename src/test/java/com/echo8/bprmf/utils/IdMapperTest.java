package com.echo8.bprmf.utils;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class IdMapperTest {

    @Test
    public void testGetId() {
        IdMapper idMapper = new IdMapper();
        assertThat(idMapper.getId("testid"), is(0));
    }

    @Test
    public void testGetIdWithTrueAddFlag() {
        IdMapper idMapper = new IdMapper();
        assertThat(idMapper.getId("testid", true), is(0));
    }

    @Test
    public void testGetIdWithFalseAddFlag() {
        IdMapper idMapper = new IdMapper();
        assertThat(idMapper.getId("testid", false), is(nullValue()));
    }

    @Test
    public void testGetIdWithAlreadyAddedId() {
        IdMapper idMapper = new IdMapper();
        idMapper.getId("testid");
        assertThat(idMapper.getId("testid"), is(0));
    }

    @Test
    public void testGetIdWithTrueAddFlagAndAlreadyAddedId() {
        IdMapper idMapper = new IdMapper();
        idMapper.getId("testid");
        assertThat(idMapper.getId("testid", true), is(0));
    }

    @Test
    public void testGetIdWithFalseAddFlagAndAlreadyAddedId() {
        IdMapper idMapper = new IdMapper();
        idMapper.getId("testid");
        assertThat(idMapper.getId("testid", false), is(0));
    }

    @Test
    public void testGetMaxIdOnEmptyGenerator() {
        IdMapper idMapper = new IdMapper();
        assertThat(idMapper.getMaxId(), is(-1));
    }

    @Test
    public void testGetMaxId() {
        IdMapper idMapper = new IdMapper();
        idMapper.getId("testid1");
        idMapper.getId("testid2");
        assertThat(idMapper.getMaxId(), is(1));
    }

    @Test
    public void testGetRawId() {
        IdMapper idMapper = new IdMapper();
        Integer id = idMapper.getId("testid1");
        assertThat(idMapper.getRawId(id), is("testid1"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetRawIdNonExisting() {
        IdMapper idMapper = new IdMapper();
        idMapper.getRawId(5);
    }
}
