package com.fakero.shoppingassistant;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


/**
 * Created by fakero on 8.3.2018.
 */

public class CalculatorFragmentTest {
    @Test
    public void calculateTestWithDiscount(){
        double expected = 560.00;
        assertThat(CalculatorFragment.newInstance().calc(100, 30, 8), is(expected));
    }

    @Test
    public void calculateTestWithoutDiscount(){
        double expected = 100.00;
        assertThat(CalculatorFragment.newInstance().calc(50, 0, 2), is(expected));
    }

    @Test
    public void setAmountTest(){

    }
}
