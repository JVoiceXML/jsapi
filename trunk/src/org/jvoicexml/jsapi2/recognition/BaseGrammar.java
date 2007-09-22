package org.jvoicexml.jsapi2.recognition;

import javax.speech.recognition.Grammar;
import javax.speech.recognition.Recognizer;
import java.util.Vector;
import javax.speech.recognition.GrammarListener;
import javax.speech.recognition.ResultListener;
import javax.speech.recognition.GrammarEvent;
import java.util.Enumeration;
import javax.speech.recognition.ResultEvent;

/**
 * <p>Title: JSAPI 2.0</p>
 *
 * <p>Description: An independent reference implementation of JSR 113</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: JVoiceXML group - http://jvoicexml.sourceforge.net</p>
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class BaseGrammar implements Grammar {

    private final Recognizer recognizer;

    private final Vector grammarListeners;
    private final Vector resultListeners;
    private boolean active;
    private boolean enabled;
    private final String reference;
    private int activationMode;


    public BaseGrammar(final Recognizer recognizer, String reference) {
        this.recognizer = recognizer;
        this.reference = reference;
        active = false;
        enabled = false;
        activationMode = Grammar.ACTIVATION_FOCUS;
        grammarListeners = new Vector();
        resultListeners = new Vector();
    }

    /**
     * addGrammarListener
     *
     * @param listener GrammarListener
     */
    public void addGrammarListener(GrammarListener listener) {
        if (!grammarListeners.contains(listener))
            grammarListeners.addElement(listener);
    }

    /**
     * addResultListener
     *
     * @param listener ResultListener
     */
    public void addResultListener(ResultListener listener) {
        if (resultListeners.contains(listener))
            resultListeners.addElement(listener);
    }

    /**
     * getActivationMode
     *
     * @return int
     */
    public int getActivationMode() {
        return activationMode;
    }

    /**
     * getRecognizer
     *
     * @return Recognizer
     */
    public Recognizer getRecognizer() {
        return recognizer;
    }

    /**
     * getReference
     *
     * @return String
     */
    public String getReference() {
        return reference;
    }

    /**
     * isActive
     *
     * @return boolean
     */
    public boolean isActive() {
        return active;
    }

    /**
     * isEnabled
     *
     * @return boolean
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * removeGrammarListener
     *
     * @param listener GrammarListener
     */
    public void removeGrammarListener(GrammarListener listener) {
        grammarListeners.removeElement(listener);
    }

    /**
     * removeResultListener
     *
     * @param listener ResultListener
     */
    public void removeResultListener(ResultListener listener) {
        resultListeners.removeElement(listener);
    }

    /**
     * setActivationMode
     *
     * @param mode int
     */
    public void setActivationMode(int mode) throws IllegalArgumentException {
        if ((mode != Grammar.ACTIVATION_FOCUS) &&
            (mode != Grammar.ACTIVATION_GLOBAL) &&
            (mode != Grammar.ACTIVATION_MODAL)) {
            throw new IllegalArgumentException("Unkown activation mode");
        } else {
            activationMode = mode;
        }
    }

    /**
     * setEnabled
     *
     * @param flag boolean
     */
    public void setEnabled(boolean flag) {
        enabled = false;
    }

    protected void setActive(boolean flag) {
        if (flag == active) {
            return;
        }
        active = flag;
        postGrammarEvent(active ?
                         GrammarEvent.GRAMMAR_ACTIVATED :
                         GrammarEvent.GRAMMAR_DEACTIVATED);
    }


    protected void postGrammarEvent(int eventId) {
        final GrammarEvent event = new GrammarEvent(this, eventId);
        postGrammarEvent(event);
    }

    protected void postGrammarEvent(final GrammarEvent event) {
        final Runnable r = new Runnable() {
            public void run() {
                fireEvent(event);
            }
        };
        postSpeechEvent(r);
    }

    protected void postResultEvent(final ResultEvent event) {
        final Runnable r = new Runnable() {
            public void run() {
                fireEvent(event);
            }
        };
        postSpeechEvent(r);
    }

    private void postSpeechEvent(final Runnable task) {
        try {
            getRecognizer().getSpeechEventExecutor().execute(task);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void fireEvent(GrammarEvent event) {
        Enumeration listeners = grammarListeners.elements();
        while (listeners.hasMoreElements()) {
            GrammarListener gl = (GrammarListener) listeners.nextElement();
            gl.grammarUpdate(event);
        }
    }

    public void fireEvent(ResultEvent event) {
        Enumeration listeners = resultListeners.elements();
        while (listeners.hasMoreElements()) {
            ResultListener rl = (ResultListener) listeners.nextElement();
            rl.resultUpdate(event);
        }
    }


}
