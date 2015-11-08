package com.nv.hadoop;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TcpReducer extends Reducer<Text, Text, Text, Text> {

	private long numberOfPackets;
//	@Override
//	protected void setup(Context context) throws IOException,
//			InterruptedException {
////		context.write(new Text("<configuration>"), null);
//	}
//
//	@Override
//	protected void cleanup(Context context) throws IOException,
//			InterruptedException {
////		context.write(new Text("</configuration>"), null);
//	}
	

	@Override
	  protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		logDebug("Key: " + key.toString());
		//TODO Write in format xxx.xxx.xxx.xxx [NUMBER OF PACKETS] : {\n\t<packet/>}
		Text value = constructValue(values);
		logDebug("Values: " + value.toString());
		key = new Text(key.toString() + " [" + numberOfPackets + "]");	
		context.write(key, value);
	}

	private Text constructValue(Iterable<Text> values){
		StringBuilder packets = new StringBuilder();
		for (Text value : values){ 
			numberOfPackets++;
			packets.append("\n\t" + value);
		}
		return new Text(packets.toString());
	}
	
	private void logDebug(String message){
		Logger.getLogger("=====" + this.getClass().getName()).log(Level.INFO, "=====" + message);
	}
	
	private void logError(String message, Throwable e){
		Logger.getLogger(this.getClass().getName()).log(Level.WARNING, message, e);
	}
}