package com.echo8.bprmf.type;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class FactorMatrixTest {

    @Test
    public void testGetNumFactors() {
        FactorMatrix factorMatrix = new FactorMatrix(5, 5);
        assertThat(factorMatrix.getNumFactors(), is(5));
    }

    @Test
    public void testGetValue() {
        FactorMatrix factorMatrix = new FactorMatrix(5, 5);

        factorMatrix.setValue(3, 2, 4.f);
        assertThat(factorMatrix.getValue(3, 2), is(4.f));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidElement() {
        FactorMatrix factorMatrix = new FactorMatrix(5, 5);
        factorMatrix.getValue(6, 2);
    }
}
