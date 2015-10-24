package com.nv.hadoop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class Main {
	public static final String START_TAG = "<packet>";
	public static final String END_TAG = "</packet>";
	public static void main(String[] args) throws IOException {
		
		SAXBuilder builder = new SAXBuilder();
		String value = "";
		String outputKey = "";
		
		BufferedReader fileReader = new BufferedReader(new FileReader("pcap.xml"));
		StringBuilder xmlPacket = new StringBuilder();
		String line;
		while(!(line = fileReader.readLine()).equals(START_TAG));
		do {
			xmlPacket.append(line + "\n");
		} while(!(line = fileReader.readLine()).equals(END_TAG));
		xmlPacket.append(END_TAG);
		System.out.println(xmlPacket.toString());
		
		Reader in = new StringReader(xmlPacket.toString());
		
		try {

			Document doc = builder.build(in);
			Element root = doc.getRootElement();
			log(root.toString());
			log("" + isTcpPacket(root));
			String queryToTcp = "//proto[@name= 'tcp']";
			XPathExpression<Element> xpe = XPathFactory.instance().compile(queryToTcp, Filters.element());
			for (Element packet : xpe.evaluate(doc)) {
				value = getPacket(packet.getParentElement());
				outputKey = getIpSourceAddress(packet.getParentElement());
//				System.out.printf("This Element has name '%s' and text '%s'\n",
//						urle.getName(), urle.getValue());
			}
			System.out.println(outputKey);
//			context.write(new Text(outputKey), new Text(value));
		} catch (JDOMException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
	
	private static String getPacket(Element el){
		XMLOutputter outp = new XMLOutputter();
		return outp.outputString(el);
	}
	
	private static String getIpSourceAddress(Element el){
		String queryToIpSource = "/packet/proto[@name= 'ip']/field[@name='ip.src']";
		XPathExpression<Element> xpe = XPathFactory.instance().compile(queryToIpSource, Filters.element());
		Element element = xpe.evaluate(el).get(0);
		return element.getAttributeValue("show");
	}
	
	private static boolean isTcpPacket(Element element){
		return !XPathFactory.instance().compile("//proto[@name= 'tcp']", Filters.element()).evaluate(element).isEmpty();
	}
	
	private static void log(String message){
		Logger.getLogger(Main.class.getName()).log(Level.INFO, message);
	}

}
