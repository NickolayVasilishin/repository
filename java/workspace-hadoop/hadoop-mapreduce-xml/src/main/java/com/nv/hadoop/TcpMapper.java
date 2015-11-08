package com.nv.hadoop;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.jdom2.JDOMException;

public class TcpMapper extends Mapper<LongWritable, Text, Text, Text> {

	@Override
	public void map(LongWritable key, Text inputValue, Context context)
			throws IOException, InterruptedException {
		logDebug(inputValue.toString());
		PacketExtractor packet = null;
		try {
			packet = new PacketExtractor(inputValue.toString());
			logDebug(packet.toString());
		} catch (JDOMException e) {
			logError("JDOM Exception, ", e);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			logError("Parsing packet failed, ", e);
			e.printStackTrace();
		}
		// TODO Check available methods of exit
		if (packet == null || !packet.isTcpPacket())
			return;

		context.write(new Text(packet.getIpSourceAddress()),
				new Text(packet.getWholePacket()));
	}

	private void logDebug(String message) {
		Logger.getLogger("=====" + this.getClass().getName()).log(Level.INFO,
				message);
	}

	private void logError(String message, Throwable e) {
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, message, e);
	}

}
