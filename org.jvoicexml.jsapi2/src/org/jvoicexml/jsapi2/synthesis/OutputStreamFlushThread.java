package org.jvoicexml.jsapi2.synthesis;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamFlushThread extends Thread {
    private OutputStream out;
    private final Object lock;
    private final Object waitNextFlushLock;
    
    public OutputStreamFlushThread() {
        setDaemon(true);
        lock = new Object();
        waitNextFlushLock = new Object();
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("*** run");
            synchronized (waitNextFlushLock) {
                waitNextFlushLock.notifyAll();
            }
            System.out.println("*** wait for flush");
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void flush(final OutputStream stream) {
        System.out.println("*** waiting flush");
        synchronized (waitNextFlushLock) {
            try {
                waitNextFlushLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        System.out.println("*** done waiting flush");
        out = stream;
        synchronized (lock) {
            lock.notifyAll();
        }
        System.out.println("*** flushed");
    }
}
