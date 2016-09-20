package com.epam.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;

public class UDFextractFromUA extends UDF {
	
	public static String extractDevice(String agentPart) {
		agentPart = agentPart.toLowerCase();
		if(agentPart.contains("android")) {
			if(agentPart.contains("tv")) {
				return "Android TV";
			} else if (agentPart.contains("tablet")) {
				return "Android Tablet";
			} else {
				return "Android";
			}
		} else if(agentPart.contains("bada")) {
			return "Bada";
		} else if(agentPart.contains("blackberry")) {
			if (agentPart.contains("tablet")) {
				return "Blackberry Tablet";
			} else {
				return "Blackberry";
			} 
		} else if (agentPart.contains("chrome") || agentPart.contains("ubuntu") || agentPart.contains("linux") || (agentPart.contains("windows") && !agentPart.contains("phone"))) {
			return "PC";
		} else if(agentPart.contains("windows") && agentPart.contains("phone")) {
			return "Windows Phone";
		} else if(agentPart.contains("symbian")) {
			return "Symbian";
		} else if(agentPart.contains("playstation") || agentPart.contains("nintendo")) {
			return "Game Console";
		} else if(agentPart.contains("ipad")) {
			return "iPad";
		} else if(agentPart.contains("iphone")) {
			return "iPhone";
		} else if(agentPart.contains("mac os")) {
			return "Mac PC";
		} else if(agentPart.contains("maemo")) {
			return "Maemo";
		} else if(agentPart.contains("meego")) {
			return "MeeGo";
		} else if(agentPart.contains("webos")) {
			return "WebOS";
		} else {
			return "Unknown";
		}	
	}
	
	public Text evaluate(Text userAgent, Text typeOfField) {

		UserAgent agent = UserAgent.parseUserAgentString(userAgent.toString());
		String type = typeOfField.toString().toLowerCase();
		switch (type) {
			case "device":
				OperatingSystem operatingSystem = agent.getOperatingSystem();
				return new Text(operatingSystem != null ? extractDevice(operatingSystem.getName()) : "NULL");
			case "browser":
				Browser browser = agent.getBrowser();
				Version browserVersion = agent.getBrowserVersion();
				
				String browserName = browser != null ? browser.getGroup().getName() : "UNBROWS";
				browserName += " ";
				browserName += browserVersion != null ? browserVersion.getMajorVersion() : "UNVER";
				return new Text(browserName);
			case "os":
				operatingSystem = agent.getOperatingSystem();
				return new Text(operatingSystem != null ? operatingSystem.getName() : "NULL");
			default:
				throw new RuntimeException("Unexpected type. Use 'browser', 'os', 'device'.");
		}
	}
}
