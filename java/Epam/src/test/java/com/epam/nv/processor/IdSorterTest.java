package com.epam.nv.processor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Nikolay_Vasilishin on 6/7/2016.
 */
public class IdSorterTest {
    IdSorter sorter;

    @Before
    public void setup() {
        sorter = new IdSorter(100);
    }

    @Test
    public void put() throws Exception {
        sorter.put("aaa");
        sorter.put("aaa");
        sorter.put("aab");
        sorter.put("aab");
        sorter.put("baa");
        sorter.put("bac");
        sorter.put("bsc");
        sorter.put("bsc");
        sorter.put("bsc");
        sorter.put("bsc");
        sorter.put("asc");
        sorter.put("aaa");
        sorter.getIdMap().forEach((s, l) -> System.out.println(s + "\t" + l));
    }

}