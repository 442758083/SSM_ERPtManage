package com.ischoolbar.programmer.util;

import java.util.UUID;
import org.apache.log4j.Logger;

/**
 * Created by Lenovo on 2019/11/7.
 */
public class LoggerHelper {
  private static Logger logger=Logger.getLogger(LoggerHelper.class.getClass());
  private final static ThreadLocal<String> TRACEID = new ThreadLocal<>();

  public static void tracingLog( String message) {
    logger.info(generateTraceLog(message));
  }

  private static String generateTraceLog(String message) {
    return "traceId:" + getTraceId() + " >> " + message;
  }

  public static String getTraceId() {
    if (TRACEID.get() == null) {
      TRACEID.set(UUID.randomUUID().toString());
    }
    return TRACEID.get();
  }
}
