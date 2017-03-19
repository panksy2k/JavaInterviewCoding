package com.pankaj.operators;

import java.util.Collections;
import java.util.List;

/**
 * Created by pankajpardasani on 17/03/2017.
 */
public class OperatorsTest {
    private final List<Object> aList;

    public OperatorsTest() {
        aList = Collections.emptyList();
    }

    public static void main(String... args) {
        OperatorsTest op = new OperatorsTest();

        if (op.aList != null | op.aList.size() > 0) {
            System.out.println("Size is " + op.aList.size());
        }
    }
}
