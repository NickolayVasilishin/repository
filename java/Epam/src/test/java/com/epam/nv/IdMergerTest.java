package com.epam.nv;

import com.epam.nv.processor.IdMerger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Nikolay_Vasilishin on 5/25/2016.
 */
public class IdMergerTest {
    private static String PATH_PREFIX = "src/test/testResources/";
    @Test
    public void mergeResults() throws IOException {
        IdMerger merger = new IdMerger(FileSystem.getLocal(new Configuration()), new Path(PATH_PREFIX + "tmp_stcs"), new Path(PATH_PREFIX + "merged_result"));
        merger.addFile(new Path(PATH_PREFIX + "tmp_stcs/" + "input1.stcs"));
        merger.addFile(new Path(PATH_PREFIX + "tmp_stcs/" + "input2.stcs"));
        merger.addFile(new Path(PATH_PREFIX + "tmp_stcs/" + "input3.stcs"));
        merger.addFile(new Path(PATH_PREFIX + "tmp_stcs/" + "input4.stcs"));
        //Fails on writing to /tmp/
        merger.mergeFiles();
    }

}