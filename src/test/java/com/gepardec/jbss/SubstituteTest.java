package com.gepardec.jbss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class SubstituteTest {

	@SuppressWarnings("unchecked")
	@Test
	public void prepareDateTest() throws IOException {
		Properties properties = new Properties();
		properties.load(this.getClass().getResourceAsStream("/test.env"));
		Map<String, Object> data = Substitute.prepareData(properties);
		assertEquals("password@123!", data.get("SOME_PASSWORD"));
		assertEquals(2, ((Map<String, String>) data.get("SERVER")).size());
		assertTrue(((Map<String, String>) data.get("SERVER"))
				.containsKey("1"));
		assertTrue(((Map<String, String>) data.get("SERVER"))
				.containsKey("2"));

	}
	
	@Test
	public void mainTest() throws IOException{
		File result = File.createTempFile("template", "result");
		Substitute.main(new String[]{"src/test/resources/test.env","src/test/resources/template.ftl",result.getAbsoluteFile().toString()});
		Properties properties = new Properties();
		properties.load(new FileInputStream(result));
		assertEquals("unknown", properties.getProperty("USER"));
		assertEquals("password@123!", properties.getProperty("PASSWORD"));
		assertEquals("http://server1.com/", properties.getProperty("HOST_1"));
		assertEquals("http://server2.com/", properties.getProperty("HOST_2"));
		assertEquals(null, properties.getProperty("HOST_3"));
	}
}
