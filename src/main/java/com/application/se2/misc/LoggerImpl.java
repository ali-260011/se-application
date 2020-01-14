package com.application.se2.misc;

import static com.application.se2.AppConfigurator.LoggerConfig;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.SimpleLayout;

import com.application.se2.AppConfigurator.LoggerTopics;
import com.application.se2.model.Entity;

public class LoggerImpl implements Logger {
	private org.apache.log4j.Logger realLogger = null; // Java's built‐in logging

	private LoggerImpl(final Class<?> clazz) {
		realLogger = org.apache.log4j.Logger.getLogger(clazz);
		SimpleLayout layout = new SimpleLayout();
		ConsoleAppender consoleAppender = new ConsoleAppender(layout);
		this.realLogger.addAppender(consoleAppender);
		// ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF:
		this.realLogger.setLevel(Level.ALL);
	}

	public static Logger getInstance(final Class<?> clazz) {
		return new LoggerImpl(clazz);
	}

	@Override
	public void log(LoggerTopics topic, String msg, Object... args) {
		// Verwenden Sie die bisherige Implementierung von LoggerImpl.java.
		String id = "<none>";
		String indicator = " - shutdown";

		if (LoggerConfig.contains(topic)) {
			switch (topic) {
			case Always:
			case Info:
			case Warn:
				realLogger.info(msg);
				break;

			case Error:
				realLogger.info("ERROR: " + msg);
				break;

			case EntityCRUD:
				/*
				 * String cls = ""; if( args.length > 0 ) { Object arg = args[ 0 ]; arg = arg !=
				 * null && arg instanceof PrimaryObject? ((PrimaryObject)arg).getObject() : arg;
				 * id = arg instanceof Entity? ((Entity)arg).getId() : String.valueOf(
				 * arg.hashCode() ); cls= arg.getClass().getSimpleName(); } System.out.println(
				 * msg + " " + cls + "." + id );
				 */
				StringBuffer sb = new StringBuffer(msg);
				for (Object arg : args) {
					sb.append(arg.toString());
				}
				System.out.println(sb.toString());
				break;

			case Startup:
				indicator = " + startup";
			case Shutdown:
				realLogger.info(indicator + ": " + msg);
				break;

			case PropertiesAltered:
			case FieldAccessAltered:
				realLogger.info(msg);
				break;

			case RepositoryLoaded:
				if (args.length > 0) {
					Object arg = args[0];
					arg = arg != null && arg instanceof Traceable ? ((Traceable) arg).getRootObject() : arg;
					id = arg instanceof Entity ? ((Entity) arg).getId() : String.valueOf(arg.hashCode());
				}
				realLogger.info("Repository: --> " + id);
				break;

			case CSSLoaded:
				realLogger.info(msg);
				break;

			}
		}
	}

}
