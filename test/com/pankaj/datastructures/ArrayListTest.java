package com.pankaj.datastructures;

import com.pankaj.datastructures.interfaces.iList;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Created by pankajpardasani on 25/01/2017.
 */

public class ArrayListTest {
    private Supplier<String[]> sampleData = () -> new String[] {"Pankaj", "Deetya", "Nikki", "Rivaan"};

    @Test
    public void testArrayListAdd() {
        iList<String> list = new ArrayList<>();
        for(String names : sampleData.get()) {
            list.add(names);
        }

        Assert.assertEquals(sampleData.get().length, list.size());
    }

    @Test
    public void testArrayListInsert() {
        Supplier<String> newName = () -> "Pardasani";

        iList<String> list = new ArrayList<>();
        for(String names : sampleData.get()) {
            list.add(names);
        }

        list.add(0, newName.get());
        Assert.assertEquals(list.get(0), newName.get());

        int arr[] = new int[5];
        Arrays.binarySearch(arr, 5);
    }
}
