/**
 * 
 */
package org.jvoicexml.jsapi2.mock;

import java.util.Vector;

import javax.speech.EngineEvent;
import javax.speech.EngineListener;
import javax.speech.recognition.RecognizerEvent;
import javax.speech.recognition.RecognizerListener;
import javax.speech.synthesis.SynthesizerEvent;
import javax.speech.synthesis.SynthesizerListener;

/**
 * A collector of engine events.
 * @author Dirk Schnelle-Walka
 */
public class EngineEventAccumulator
    implements RecognizerListener, SynthesizerListener {
    /** Collected events. */
    private final Vector events;

    /**
     * Constructs a new object.
     */
    public EngineEventAccumulator() {
        events = new Vector();
    }

    /**
     * {@inheritDoc}
     */
    public void synthesizerUpdate(final SynthesizerEvent e) {
        events.addElement(e);
    }

    /**
     * {@inheritDoc}
     */
    public void recognizerUpdate(RecognizerEvent e) {
        events.addElement(e);
    }

    /**
     * Retrieves the collected events.
     * @return the collected events
     */
    public EngineEvent[] getEvents() {
        final EngineEvent[] evs = new EngineEvent[events.size()];
        events.copyInto(evs);
        return evs;
    }
}
