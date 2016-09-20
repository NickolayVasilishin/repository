package com.epam.nv.io;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.UUID;

/**
 * Created by Nikolay_Vasilishin on 5/23/2016.
 */
public class IdWriter implements AutoCloseable {
    private long count;
    private FileSystem fileSystem;
    private Path tmpDir;
    private BufferedWriter writer;

    public IdWriter(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public Path openStream() throws IOException {
        Path path = Path.mergePaths(tmpDir, createTmpFile());
        writer = new BufferedWriter(new OutputStreamWriter(fileSystem.create(path), "UTF-8"));
        return path;
    }

    public void openStream(Path file) throws IOException {
        Path path = Path.mergePaths(tmpDir, file);
        writer = new BufferedWriter(new OutputStreamWriter(fileSystem.create(path), "UTF-8"));
    }

    public void write(String line) {
        try {
            writer.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeStream() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path write(List<Map.Entry<String, Long>> map) {
        Path path = Path.mergePaths(tmpDir, createTmpFile());
        Logger.getLogger(IdWriter.class.toString()).debug("Writing records to " + path);
        Logger.getLogger(IdWriter.class.toString()).debug(Runtime.getRuntime().totalMemory() + "b memory used.");

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileSystem.create(path), "UTF-8"))) {
            for (Map.Entry<String, Long> entry : map) {
                writer.write(entry.getKey() + "\t" + entry.getValue() + "\n");
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.getLogger(IdWriter.class.toString()).debug(count + " records flushed to " + path);
        count = 0L;
        return path;
    }

    private Path createTmpFile() {
        return new Path("/" + UUID.randomUUID().toString().split("-")[4] + ".stcs");
    }


    public void setTmpDir(Path tmpDir) {
        this.tmpDir = tmpDir;
    }

    @Override
    public void close() throws Exception {
        closeStream();
    }
}
