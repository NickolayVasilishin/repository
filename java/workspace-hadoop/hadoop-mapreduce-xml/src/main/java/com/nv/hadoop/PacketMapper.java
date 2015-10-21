package com.nv.hadoop;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;


public class PacketMapper extends Mapper<LongWritable, Text, Text, Text> {

	@Override
	public void map(LongWritable key, Text inputValue, Context context)
			throws IOException, InterruptedException {

		String xmlString = inputValue.toString();
		SAXBuilder builder = new SAXBuilder();
		Reader in = new StringReader(xmlString);
		String value = "";
		String outputKey = "";
		try {

			Document doc = builder.build(in);
			Element root = doc.getRootElement();
			log(root.toString());
			String queryToTcp = "//proto[@name= 'tcp']";
			String queryToUdp = "//proto[@name= 'udp']";
			//TODO FIX
			String queryToIpSource = "//proto[@name= 'ip']/field[name= 'ip.src' show='192.168.100.55'";
			XPathExpression<Element> xpe = XPathFactory.instance().compile(queryToTcp, Filters.element());
			for (Element urle : xpe.evaluate(doc)) {
				value = getPacket(urle.getParentElement());
				outputKey = getIpSourceAddress(urle.getParentElement());
//				System.out.printf("This Element has name '%s' and text '%s'\n",
//						urle.getName(), urle.getValue());
			}
			context.write(new Text(outputKey), new Text(value));
		} catch (JDOMException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					null, ex);
		} catch (IOException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					null, ex);
		}

	}
	
	private String getPacket(Element el){
		XMLOutputter outp = new XMLOutputter();
		return outp.outputString(el);
	}
	
	private String getIpSourceAddress(Element el){
		return null;
	}
	
	private void log(String message){
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, message);
	}

}
