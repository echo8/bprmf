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
    public void testSetAndGetValue() {
        FactorMatrix factorMatrix = new FactorMatrix(5, 5);

        float value = 0.f;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                factorMatrix.setValue(i, j, value);
                assertThat(factorMatrix.getValue(i, j), is(value));
                value++;
            }
        }
    }
}
