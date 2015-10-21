package com.hadoop.example;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.Mapper;






/**
 * Turns lines from a text file into (word, 1) tuples.
 */
public class TokenizingMapper extends Mapper<LongWritable, Text, Text, Text> {
    private static final IntWritable one = new IntWritable(1);
    
    public void map(LongWritable offset, Text value, OutputCollector<Text, Text> output,
            Reporter reporter) throws IOException{
        
        StringTokenizer tok = new StringTokenizer(value.toString());
        while (tok.hasMoreTokens()) {
            Text word = new Text(tok.nextToken());
            output.collect(word, word);
        }
    }
}
