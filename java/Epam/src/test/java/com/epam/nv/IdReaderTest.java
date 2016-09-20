package com.epam.nv;

import com.epam.nv.io.IdReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Nikolay_Vasilishin on 5/19/2016.
 */
public class IdReaderTest {
    private static String PATH_PREFIX = "src/test/testResources";



    @Test
    public void readIds() throws IOException {
        int i = 0;
        String[] ids = {"Vh16OwT6OQNUXbj", "Vhkr1vpROHuhQWB", "VhKdLnuY3tlhXMa", "VhTVORqG36N6qMj", "VhL01pk8OTkW3Mc", "ZY5h13qZD4kIjU", "Vh16OwT6OQNUXbj", "Vhkr1vpROHuhQWB", "VhKdLnuY3tlhXMa", "VhTVORqG36N6qMj", "VhL01pk8OTkW3Mc", "ZY5h13qZD4kIjU"};
        for (String id : new IdReader().init(FileSystem.get(new Configuration(true)), PATH_PREFIX + "/input.bid", PATH_PREFIX + "/input2.bid")) {
            assertEquals(ids[i++], id);
        }
    }
    
    @Test
    public void readDifferentFiles() throws IOException {
        String[] outputs = {"VhTVORqG36N6qMj 139193",
                "VhL01pk8OTkW3Mc 154158",
                "ZY5h13qZD4kIjU  114149",
                "Vh16OwT6OQNUXbj 14141",
                "Vhkr1vpROHuhQWB 141155",
                "VhKdLnuY3tlhXMa 684884",
                "VhTVORqG36N6qMj 156842",
                "VhL01pk8OTkW3Mc 55225",
                "ZY5h13qZD4kIjU  58384",
                "Vh16OwT6OQNUXbj",
                "Vhkr1vpROHuhQWB",
                "VhKdLnuY3tlhXMa",
                "VhTVORqG36N6qMj",
                "VhL01pk8OTkW3Mc",
                "ZY5h13qZD4kIjU",
                "Vh16OwT6OQNUXbj",
                "Vhkr1vpROHuhQWB",
                "VhKdLnuY3tlhXMa",
                "VhTVORqG36N6qMj",
                "VhL01pk8OTkW3Mc",
                "ZY5h13qZD4kIjU"};
        int i = 0;
        for (String id : new IdReader().init(FileSystem.get(new Configuration(true)), PATH_PREFIX + "/input.stcs", PATH_PREFIX + "/input.bid", PATH_PREFIX + "/input2.bid")) {
//            System.out.println(id);
            assertEquals(outputs[i++], id);
        }
    }

}