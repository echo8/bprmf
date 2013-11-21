package com.echo8.bprmf.utils;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.echo8.bprmf.type.FactorMatrix;

@RunWith(MockitoJUnitRunner.class)
public class MatrixUtilsTest {
    @Mock
    private FactorMatrix userFactorMatrix;

    @Mock
    private FactorMatrix itemFactorMatrix;

    @Test
    public void testRowScalarProductWithRowDifference() {
        when(userFactorMatrix.getNumFactors()).thenReturn(3);
        when(userFactorMatrix.getValue(0, 0)).thenReturn(1.f);
        when(userFactorMatrix.getValue(0, 1)).thenReturn(2.f);
        when(userFactorMatrix.getValue(0, 2)).thenReturn(3.f);

        when(itemFactorMatrix.getValue(0, 0)).thenReturn(2.f);
        when(itemFactorMatrix.getValue(0, 1)).thenReturn(3.f);
        when(itemFactorMatrix.getValue(0, 2)).thenReturn(4.f);
        when(itemFactorMatrix.getValue(1, 0)).thenReturn(1.f);
        when(itemFactorMatrix.getValue(1, 1)).thenReturn(2.f);
        when(itemFactorMatrix.getValue(1, 2)).thenReturn(3.f);

        assertThat(MatrixUtils.rowScalarProductWithRowDifference(userFactorMatrix, 0,
                itemFactorMatrix, 0, 1), is(6.f));
    }

    @Test
    public void testRowScalarProduct() {
        when(userFactorMatrix.getNumFactors()).thenReturn(3);
        when(userFactorMatrix.getValue(0, 0)).thenReturn(1.f);
        when(userFactorMatrix.getValue(0, 1)).thenReturn(2.f);
        when(userFactorMatrix.getValue(0, 2)).thenReturn(3.f);

        when(itemFactorMatrix.getValue(0, 0)).thenReturn(1.f);
        when(itemFactorMatrix.getValue(0, 1)).thenReturn(2.f);
        when(itemFactorMatrix.getValue(0, 2)).thenReturn(3.f);

        assertThat(MatrixUtils.rowScalarProduct(userFactorMatrix, 0, itemFactorMatrix, 0), is(14.f));
    }
}
