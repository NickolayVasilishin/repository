package com.nv.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class TcpPacketAccumulator extends Configured implements Tool {

	 public static void main(String[] args) throws Exception {
	        int res = ToolRunner.run(new Configuration(), new TcpPacketAccumulator(), args);
	        System.exit(res);
	    }

	    @Override
	    public int run(String[] args) throws Exception {
	        
	        if (args.length != 2) {
	            System.err.println("Usage: hadoop jar hadoop-example-1.0-job.jar"
	                                       + " [generic options] <in> <out>");
	            System.out.println();
	            ToolRunner.printGenericCommandUsage(System.err);
	            return 1;
	        }
	        Configuration conf = getConf();
	        conf.set("xmlinput.start", "<packet>");
	        conf.set("xmlinput.end", "</packet>");
	        
	        Job job = new Job(getConf(), "PacketAccumulator");
	        job.setJarByClass(getClass());
	        
	        job.setInputFormatClass(TextInputFormat.class);
	        
	        job.setMapperClass(PacketMapper.class);
	        job.setCombinerClass(TcpReducer.class);
	        job.setReducerClass(TcpReducer.class);
	        
	        job.setOutputKeyClass(Text.class);
	        job.setOutputValueClass(IntWritable.class);
	        
	        FileInputFormat.addInputPath(job, new Path(args[0]));
	        FileOutputFormat.setOutputPath(job, new Path(args[1]));
	        
	        boolean success = job.waitForCompletion(true);
	        
	        return success ? 0 : 1;
	    }

}
