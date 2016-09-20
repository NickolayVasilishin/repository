package com.epam.hive.udf;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.junit.Test;

public class UDFextractFromUATest {

	@Test
	public void userAgentTest() throws IOException {
		UDFextractFromUA extractor = new UDFextractFromUA();
		String easyUA = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 SE 2.X MetaSr 1.0";
		System.out.println(extractor.evaluate(new Text(easyUA), new Text("browser")));
		System.out.println(extractor.evaluate(new Text(easyUA), new Text("os")));
		System.out.println(extractor.evaluate(new Text(easyUA), new Text("device")));
		System.out.println("");
		String hardUA = "Mozilla/5.0 (iPad; U;CPU OS 6_1 like Mac OS X; zh-CN; iPad3,3) AppleWebKit/534.46 (KHTML, like Gecko) UCBrowser/2.0.1.280 U3/0.8.0 Safari/7543.48.3";
		System.out.println(extractor.evaluate(new Text(hardUA), new Text("browser")));
		System.out.println(extractor.evaluate(new Text(hardUA), new Text("os")));
		System.out.println(extractor.evaluate(new Text(hardUA), new Text("device")));

		System.out.println("");
		hardUA = "Mozilla/5.0 (iPad; U; CPU OS 4_0 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B367 Safari/531.21.10";
		System.out.println(extractor.evaluate(new Text(hardUA), new Text("browser")));
		System.out.println(extractor.evaluate(new Text(hardUA), new Text("os")));
		System.out.println(extractor.evaluate(new Text(hardUA), new Text("device")));
	}
}
