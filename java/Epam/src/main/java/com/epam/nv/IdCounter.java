package com.epam.nv;

import com.epam.nv.io.IdReader;
import com.epam.nv.io.IdWriter;
import com.epam.nv.processor.IdMerger;
import com.epam.nv.processor.IdSorter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Nikolay_Vasilishin on 5/17/2016.
 */
public class IdCounter implements AutoCloseable {
    private static long DEFAULT_COLLECTOR_LIMIT = 500000L;
    private static long DEFAULT_BUFFER_SIZE = 10L;

    private IdReader reader;
    private IdWriter writer;
    private IdSorter sorter;
    private IdMerger merger;
    private ExecutorService asyncWriter;
    private static final Path tmpDir = new Path("/tmp/" + UUID.randomUUID().toString().split("-")[0]);

    private FileSystem fileSystem;



    public IdCounter init(String outputFile, String... inputFile) throws IOException {
        Logger.getLogger(IdCounter.class.toString()).info("IdCounter instantiation.");

        fileSystem = FileSystem.get(new Configuration());
        fileSystem.mkdirs(tmpDir);
        fileSystem.deleteOnExit(tmpDir);

        reader = new IdReader().init(fileSystem, inputFile);

        Long limit = Long.valueOf(Optional.ofNullable(System.getProperty("id.collector.limit")).orElse("" + DEFAULT_COLLECTOR_LIMIT));
        Logger.getLogger(IdCounter.class.toString()).info("Collector limit set to " + limit);
        sorter = new IdSorter(limit);

        asyncWriter = Executors.newSingleThreadExecutor();

        writer = new IdWriter(fileSystem);
        writer.setTmpDir(tmpDir);

        merger = new IdMerger(fileSystem, tmpDir, new Path(outputFile));

        Logger.getLogger(IdCounter.class.toString()).info("IdCounter instantiated.");
        return this;
    }

    /**
     * read a file from hdfs
     *
     * @throws IOException
     */
    public IdCounter processFile() throws IOException {
        Logger.getLogger(IdCounter.class.toString()).info("Processing files.");
        for(String id:reader){
            //if we have reached the limit - write current results in tmp file
            if(sorter.isFull()) {
                Logger.getLogger(IdCounter.class.toString()).debug("Collector is full. Flushing results.");
                System.out.print(".");
                Map<String, Long> idMap = sorter.getIdMap();
                asyncWriter.submit(() ->  {
                    List<Map.Entry<String, Long>> entries = IdSorter.sortByValue(idMap);
                    merger.addFile(writer.write(entries));
                });
            }
            sorter.put(id);
        }
        asyncWriter.shutdown();
        //Add last file
        Path tmp = writer.write(IdSorter.sortByValue(sorter.getIdMap()));
        try {
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.getLogger(IdCounter.class.toString()).info("Last piece was written. Starting to merge.");
        merger.addFile(tmp);
        merger.mergeFiles();
        Logger.getLogger(IdCounter.class.toString()).info("Done.");
        return this;
    }





    @Override
    public void close() throws Exception {
        if (fileSystem != null)
            fileSystem.close();
    }

    public static void main(String[] args) throws Exception {
        Logger.getLogger(IdCounter.class).info("Using " + Runtime.getRuntime().totalMemory() + " bytes at start");
        Logger.getLogger(IdCounter.class).info("Using " + Runtime.getRuntime().maxMemory() + " bytes max");

        //TODO do smth with input
        if(args.length < 2) {
            System.out.println("Usage: idcounter <inputfile[, ...]> <outputfile>");
        } else {
            String[] inputFiles = Arrays.copyOfRange(args, 0, args.length-1);
            String outputFile = args[args.length-1];
            new IdCounter().init(outputFile, inputFiles).processFile().close();
        }


    }
}
