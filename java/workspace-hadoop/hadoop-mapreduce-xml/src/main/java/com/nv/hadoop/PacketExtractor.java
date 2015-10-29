package com.nv.hadoop;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class PacketExtractor {
	public static final String START_TAG = "<packet>";
	public static final String END_TAG = "</packet>";
	public static final String queryToTcp = "//proto[@name= 'tcp']";
	public static final String queryToUdp = "//proto[@name= 'udp']";
	public static final String queryToTcpOrUdp = "//proto[@name= 'udp' or @name= 'tcp']";
	public static final String queryToIpSource = "/packet/proto[@name= 'ip']/field[@name='ip.src']";
	
	private Element root;

	public PacketExtractor(String xmlPacket) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Reader in = new StringReader(xmlPacket);
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		if(!root.getName().equals(START_TAG))
			throw new IllegalArgumentException("Passed xml element" + root.getName() + " is not a <packet>");
	}
	
	public String getWholePacket(){
		XMLOutputter outp = new XMLOutputter();
		return outp.outputString(root);
	}
	
	public String getIpSourceAddress(){
		return XPathFactory.instance().compile(queryToIpSource, Filters.element()).evaluate(root).get(0).getAttributeValue("show");
	}
	
	public boolean isTcpPacket(){
		return !XPathFactory.instance().compile("//proto[@name= 'tcp']", Filters.element()).evaluate(root).isEmpty();
	}
}
