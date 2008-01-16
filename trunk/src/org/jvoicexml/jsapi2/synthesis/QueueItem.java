package org.jvoicexml.jsapi2.synthesis;

import javax.speech.synthesis.Speakable;
import javax.speech.synthesis.SpeakableListener;
import javax.speech.AudioSegment;

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
public class QueueItem {

    private int id;
    private Speakable speakable;
    private SpeakableListener listener;
    private AudioSegment audioSegment;

    public QueueItem(int id, Speakable speakable, SpeakableListener listener) {
        this.id = id;
        this.speakable = speakable;
        this.listener = listener;
        this.audioSegment = null;
    }

    public QueueItem(int id, AudioSegment audioSegment, SpeakableListener listener) {
       this.id = id;
       this.speakable = speakable;
       this.listener = null;
       this.audioSegment = audioSegment;
   }

    public Speakable getSpeakable() {
        return speakable;
    }

    public SpeakableListener getListener() {
        return listener;
    }

    public int getId() {
        return id;
    }

    public AudioSegment getAudioSegment() {
        return audioSegment;
    }

    public void setAudioSegment(AudioSegment audiosegment) {
        audioSegment = audiosegment;
    }

}
