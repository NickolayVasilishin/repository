package com.nv.hadoop;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TcpReducer extends Reducer<Text, Text, Text, Text> {

	private long numberOfPackets;

	@Override
	  protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		logDebug("Key: " + key.toString());
		numberOfPackets = 0;
		//TODO Write in format xxx.xxx.xxx.xxx [NUMBER OF PACKETS] : {\n\t<packet/>}
		count(values);
		logDebug("Values: " + numberOfPackets);
		key = new Text(key.toString() + " [" + numberOfPackets + "]");	
		for(Text value: values)
			context.write(key, value);
	}

	private void count(Iterable<Text> values){
		for (Text value : values)
			numberOfPackets++;
	}
	
	private void logDebug(String message){
		Logger.getLogger("=====" + this.getClass().getName()).log(Level.INFO, "=====" + message);
	}
	
	private void logError(String message, Throwable e){
		Logger.getLogger(this.getClass().getName()).log(Level.WARNING, message, e);
	}
}