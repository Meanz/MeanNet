/**
 * 
 */
package com.meanworks.server;

import java.io.PrintStream;

/**
 * @author Meanz
 * 
 */
public class ServerLogger {

	/**
	 * The different logging levels
	 */
	public static int LOGGING_LEVEL_ALL = 0; // Same as 3
	public static int LOGGING_LEVEL_INFO = 1; // Error + Info
	public static int LOGGING_LEVEL_DEBUG = 2; // Debug + Error + Info
	public static int LOGGING_LEVEL_ERROR = 3; // Error only

	/**
	 * The default server logger, used for debugging
	 */
	private final static ServerLogger defaultLogger = new ServerLogger(
			"DebugLogger");

	/**
	 * Get the default server logger
	 * 
	 * @return
	 */
	public static ServerLogger getDefault() {
		return defaultLogger;
	}

	/**
	 * The name to use for this logger
	 */
	private String name;

	/**
	 * The info stream, default System.out
	 */
	private PrintStream infoStream = System.out;

	/**
	 * The debug stream, default System.out
	 */
	private PrintStream debugStream = System.out;

	/**
	 * The error stream, default System.err
	 */
	private PrintStream errorStream = System.err;

	/**
	 * The logging level for this logger, default to Info + Error
	 */
	private int loggingLevel = LOGGING_LEVEL_ALL; // ALL

	/**
	 * Construct a new ServerLogger
	 * 
	 * @param name
	 */
	public ServerLogger(String name) {
		this.name = name;
	}

	/**
	 * Set the logging level of this logger
	 * 
	 * @param loggingLevel
	 */
	public void setLoggingLevel(int loggingLevel) {
		this.loggingLevel = loggingLevel;
	}

	/**
	 * Set the output of this logger
	 * 
	 * @param output
	 */
	public void setInfoStream(PrintStream output) {
		this.infoStream = output;
	}

	/**
	 * Set the debug stream for this logger
	 * 
	 * @param output
	 */
	public void setDebugStream(PrintStream output) {
		this.debugStream = output;
	}

	/**
	 * Set the error stream for this logger
	 * 
	 * @param output
	 */
	public void setErrorStream(PrintStream output) {
		this.errorStream = output;
	}

	/**
	 * Write the given object to this logger
	 * 
	 * @param msg
	 */
	public void write(PrintStream out, Object msg) {
		if (out == null) {
			System.err.println("\t\tServerLogger [" + name + "]"
					+ " has NULL output, writing to STDERR");
			System.err.println("" + msg);
		} else {
			out.println("" + msg);
		}
	}

	/**
	 * Send a info message to the logger
	 * 
	 * @param msg
	 */
	public void info(Object msg) {
		if (loggingLevel == LOGGING_LEVEL_ALL
				|| loggingLevel == LOGGING_LEVEL_INFO
				|| loggingLevel == LOGGING_LEVEL_DEBUG) {
			write(infoStream, "[" + name + "][INFO] " + msg);
		} else {
			// Meh
		}
	}

	/**
	 * Send a debug message to the logger
	 * 
	 * @param msg
	 */
	public void debug(Object msg) {
		if (loggingLevel == LOGGING_LEVEL_ALL
				|| loggingLevel == LOGGING_LEVEL_DEBUG) {
			write(debugStream, "[" + name + "][DEBUG] " + msg);
		}
	}

	/**
	 * Send a error message to the logger
	 * 
	 * @param msg
	 */
	public void error(Object msg) {
		write(errorStream, "[" + name + "][ERROR] " + msg);
	}
}
