/**
 * 
 */
package org.jvoicexml.jsapi2.sapi.logging;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A simple wrapper for log4cplus or similar native logging frameworks.
 * This thread retrieves the next {@link LogRecord} and forwards it to
 * the Java logging framework.
 * @author Dirk Schnelle-Walka
 *
 */
public class Log4CPlus2JavaLoggingAdapter extends Thread {
    static {
        System.loadLibrary("Jsapi2SapiBridge");
    }

    /** Handle for the native logging adapter. */
    private long handle;

    /**
     * Constructs a new object.
     */
    public Log4CPlus2JavaLoggingAdapter() {
        setDaemon(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        handle = initLogging();
        while (!isInterrupted()) {
            final LogRecord record = getNextLogRecord(handle);
            if (record != null) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(record);
            }
        }
    }

    public native long initLogging();

    /**
     * Retrieves the next log record.
     * @param handle the native handle
     * @return the next log record.
     */
    public native LogRecord getNextLogRecord(long handle);
}
