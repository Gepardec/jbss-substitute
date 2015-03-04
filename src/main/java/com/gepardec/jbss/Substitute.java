package com.gepardec.jbss;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Substitute {
	
	private static final Pattern MAP_VARIABLES_PATTER = Pattern.compile("(\\w+)_(\\d+)");

	public static void main(String[] args) {
		if(args.length != 3){
			System.err.println("Invalid arguments. Expected: 1st: properties file, 2nd: template file, 3rd: output name");
			System.exit(1);
		}
		
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(args[0]));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		Map<String, Object> data = prepareData(properties);
		
		processTemplate(args[1], args[2], data);
		System.exit(0);
	}

	private static void processTemplate(String templateFilename, String outputFilename, Map<String, Object> data) {
		try {
			Configuration config = new Configuration();
			Template template = config.getTemplate(templateFilename);
			Writer out = new FileWriter(outputFilename);
			template.process(data, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 * Prepares data for freemarker. All properties will be copied one to one.
	 * Additionally bunches of variables like var11, var12 etc. will be converter to map var[11]=..
	 */
	@SuppressWarnings("unchecked")
	static Map<String, Object> prepareData(Properties properties) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		for(String propertyName : properties.stringPropertyNames()){
			result.put(propertyName, properties.getProperty(propertyName));
			Matcher matcher = MAP_VARIABLES_PATTER.matcher(propertyName);
			if(matcher.find()){
				String mapName = matcher.group(1);
				String mapEntryName = matcher.group(2);
				String mapEntryValue = properties.getProperty(propertyName);
				if(result.get(mapName) == null){
					result.put(mapName, new HashMap<String, String>());
				}
				((HashMap<String, String>)result.get(mapName)).put(mapEntryName, mapEntryValue);
			}
		}
		
		return result;

	}

}
