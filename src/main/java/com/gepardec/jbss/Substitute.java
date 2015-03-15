package com.gepardec.jbss;

import java.io.File;
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
	
	private static void printUsage() {
		StringBuffer out = new StringBuffer();
		out.append("Usage:\n");
		out.append("    substitute properties template output\n");
		out.append("arguments:\n");
		out.append("    properties: comma saparates list of environment-files.\n");
		out.append("    template: freemarker template file\n");
		out.append("    output: filename for output\n");

		System.err.println(out.toString());
	}


	public static void main(String[] args) {
		if(args.length != 3){
			printUsage();
			System.exit(1);
		}
		
		Properties properties = new Properties();

		for (String propFileName : args[0].split(",")) {			
			try {
				properties.load(new FileInputStream(propFileName));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		Map<String, Object> data = prepareData(properties);
		
		processTemplate(args[1], args[2], data);
	}

	private static void processTemplate(String templateFilename, String outputFilename, Map<String, Object> data) {
		try {
			Configuration config = new Configuration();
			File templateFile = new File(templateFilename);
			File templateDir = templateFile.getParentFile();
			if ( null == templateDir ){
				templateDir = new File("./");
			}
			config.setDirectoryForTemplateLoading(templateDir);
			Template template = config.getTemplate(templateFile.getName());
			Writer out = new FileWriter(outputFilename);
			template.process(data, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (TemplateException e) {
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
