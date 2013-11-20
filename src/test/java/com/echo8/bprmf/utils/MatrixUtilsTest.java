package com.echo8.bprmf.utils;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.echo8.bprmf.type.FactorMatrix;

public class MatrixUtilsTest {

    @Test
    public void testRowScalarProductWithRowDifference() {
        FactorMatrix userFactorMatrix = new FactorMatrix(1, 3);
        FactorMatrix itemFactorMatrix = new FactorMatrix(2, 3);

        userFactorMatrix.setValue(0, 0, 1.f);
        userFactorMatrix.setValue(0, 1, 2.f);
        userFactorMatrix.setValue(0, 2, 3.f);

        itemFactorMatrix.setValue(0, 0, 2.f);
        itemFactorMatrix.setValue(0, 1, 3.f);
        itemFactorMatrix.setValue(0, 2, 4.f);
        itemFactorMatrix.setValue(1, 0, 1.f);
        itemFactorMatrix.setValue(1, 1, 2.f);
        itemFactorMatrix.setValue(1, 2, 3.f);

        assertThat(MatrixUtils.rowScalarProductWithRowDifference(userFactorMatrix, 0,
                itemFactorMatrix, 0, 1), is(6.f));
    }

    @Test
    public void testRowScalarProduct() {
        FactorMatrix userFactorMatrix = new FactorMatrix(1, 3);
        FactorMatrix itemFactorMatrix = new FactorMatrix(1, 3);

        userFactorMatrix.setValue(0, 0, 1.f);
        userFactorMatrix.setValue(0, 1, 2.f);
        userFactorMatrix.setValue(0, 2, 3.f);

        itemFactorMatrix.setValue(0, 0, 2.f);
        itemFactorMatrix.setValue(0, 1, 3.f);
        itemFactorMatrix.setValue(0, 2, 4.f);

        assertThat(MatrixUtils.rowScalarProduct(userFactorMatrix, 0, itemFactorMatrix, 0), is(20.f));
    }
}
