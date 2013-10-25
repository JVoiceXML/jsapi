package org.jvoicexml.jsapi2.synthesis;

/**
 * Indicator for {@link java.io.OutputStream}s that the playback can be
 * cancelled.
 * @author Dirk Schnelle-Walka
 *
 */
public interface CancellableOutputStream {
    /**
     * Cancels the current playback of the audio output.
     */
    void cancel();
}
