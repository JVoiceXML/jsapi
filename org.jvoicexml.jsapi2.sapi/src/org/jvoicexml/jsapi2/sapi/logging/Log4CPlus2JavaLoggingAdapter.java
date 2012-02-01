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
public final class Log4CPlus2JavaLoggingAdapter extends Thread {
    static {
        //Check the processor architecture
        if (System.getProperty("os.arch").equalsIgnoreCase("x86")) {
            System.loadLibrary("Jsapi2SapiBridge");
        } else {
            System.loadLibrary("Jsapi2SapiBridge_x64");
        }
            
         
        Log4CPlus2JavaLoggingAdapter adapter =
            new Log4CPlus2JavaLoggingAdapter();
        adapter.start();
        adapter.waitStarted();
    }

    /** Logger for this class. */
    private static final Logger LOGGER =
        Logger.getLogger(Log4CPlus2JavaLoggingAdapter.class.getName());

    /** Handle for the native logging adapter. */
    private long handle;

    /** Sempahore to notifgy about new logging events. */
    private final Object lock;

    /**
     * Constructs a new object.
     */
    public Log4CPlus2JavaLoggingAdapter() {
        setDaemon(true);
        lock = new Object();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        handle = initLogging();
        synchronized (lock) {
            lock.notifyAll();
        }
        while (!isInterrupted()) {
            final LogRecord record = getNextLogRecord(handle);
            if (record != null) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(record);
            }
        }
    }

    /**
     * Wait until the thread has started.
     */
    public void waitStarted() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                LOGGER.warning(e.getMessage());
            }
        }
    }

    public native long initLogging();

    /**
     * Retrieves the next log record.
     * @param handle the native handle
     * @return the next log record.
     */
    public native LogRecord getNextLogRecord(final long handle);
}
