package com.epam.nv.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.SnappyCodec;

import java.io.*;
import java.util.Iterator;

/**
 * Created by Nikolay_Vasilishin on 5/19/2016.
 */
public class IdReader implements Iterator<String>, AutoCloseable, Iterable<String> {
    private FileSystem fileSystem;
    private BufferedReader input;
    private String currentLine;
    private CompressionCodec codec;
    private boolean compressed = true;

    public IdReader init(FileSystem fileSystem, boolean compressed, Path... inputFiles) throws IOException {
        this.compressed = compressed;
        init(fileSystem, inputFiles);
        return this;
    }

    public IdReader init(FileSystem fileSystem, Path... inputFiles) throws IOException {
        this.fileSystem = fileSystem;
        CompressionCodecFactory factory = new CompressionCodecFactory(new Configuration());
        codec = factory.getCodecByClassName("org.apache.hadoop.io.compress.BZip2Codec");
        input = buildInputStream(inputFiles);
        return this;
    }

    public IdReader init(FileSystem fileSystem, String... inputFiles) throws IOException {
        this.fileSystem = fileSystem;
        Path[] inputPaths = new Path[inputFiles.length];
        int i = 0;
        for (String inputFile : inputFiles) {
            inputPaths[i++] = new Path(inputFile);
        }
        CompressionCodecFactory factory = new CompressionCodecFactory(new Configuration());
        codec = factory.getCodecByClassName("org.apache.hadoop.io.compress.BZip2Codec");
        input = buildInputStream(inputPaths);
        return this;
    }

    private InputStream compressedStream(InputStream inputStream) throws IOException {
        if(compressed) {
            return codec.createInputStream(inputStream);
        } else {
            return inputStream;
        }
    }

    private BufferedReader buildInputStream(Path[] inputFiles) throws IOException {
        //If there is only one source
        if (inputFiles.length == 1)
            return new BufferedReader(new InputStreamReader(compressedStream(fileSystem.open(inputFiles[0]))));
        //only two sources
        SequenceInputStream singleInput = new SequenceInputStream(compressedStream(fileSystem.open(inputFiles[0])), compressedStream(new SequenceInputStream(
                new ByteArrayInputStream("\n".getBytes()), // gives an endline between the provided files
                fileSystem.open(inputFiles[1]))));
        //more than two sources
        for (int i = 2; i < inputFiles.length; i++) {
            singleInput = new SequenceInputStream(compressedStream(singleInput), compressedStream(new SequenceInputStream(
                    new ByteArrayInputStream("\n".getBytes()), // gives an endline between the provided files
                    fileSystem.open(inputFiles[i]))));
        }
        return new BufferedReader(new InputStreamReader(singleInput));
    }

    private String extractId(String line) {

        String[] fields = line.split("\\s+", 4);
        if (fields.length > 2) {
            return fields[2];
        } else if (fields.length == 2) {
            return line;
        }
        return "";
    }

    @Override
    public boolean hasNext() {
        try {
            currentLine = input.readLine();
            while (currentLine != null && (currentLine.length() == 0 || currentLine.trim().equals("") || extractId(currentLine).trim().equals(""))) {
                currentLine = input.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentLine != null && !currentLine.equals("");
    }

    @Override
    public String next() {
        return extractId(currentLine);
    }

    @Override
    public void close() throws Exception {
        input.close();
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }
}
