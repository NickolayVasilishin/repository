package com.hadoop.example;

import java.io.IOException;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.LineRecordReader;
import org.apache.hadoop.mapred.RecordReader;

public class XmlRecordReader implements RecordReader<LongWritable, ArrayWritable> {
	
	private String startPacketTag = "<packet>";
	private String endPacketTag = "</packet>";
	private byte[] startTag;
	private byte[] endTag;
	private long start;
	private long end;
	private FSDataInputStream inputStream;
	private DataOutputBuffer buffer = new DataOutputBuffer();
	private LineRecordReader lineReader;
	private LongWritable lineKey;
	private Text lineValue;
	
	public XmlRecordReader(JobConf job, FileSplit split) throws IOException {
	        lineReader = new LineRecordReader(job, split);
	        lineKey = lineReader.createKey();
	        lineValue = lineReader.createValue();
	        startTag = startPacketTag.getBytes();
	        endTag = endPacketTag.getBytes();

	        // Open the file and seek to the start of the split
	        start = split.getStart();
	        end = start + split.getLength();
	        Path file = split.getPath();
	        FileSystem fs = file.getFileSystem(job);
	        inputStream = fs.open(split.getPath());
	        inputStream.seek(start);
	     }

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object createKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object createValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getPos() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getProgress() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean next(Object arg0, Object arg1) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}
}
	


