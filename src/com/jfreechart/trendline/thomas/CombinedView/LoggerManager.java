/**
 * 
 */
package com.jfreechart.trendline.thomas.CombinedView;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * @author Ghanshyam Vaghasiya
 *
 */
public class LoggerManager {
	public static void initializeLoggingContext(){
		initializeLoggingContext("conf\\log4j2.xml");
	}
	
	public static void initializeLoggingContext(String logFilePath){
		LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
		File file = new File(logFilePath);
		loggerContext.setConfigLocation(file.toURI());
	}
}
