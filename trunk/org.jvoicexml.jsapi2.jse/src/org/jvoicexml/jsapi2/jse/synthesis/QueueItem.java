/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision: $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: lyncher $
 *
 * JSAPI - An base implementation for JSR 113.
 *
 * Copyright (C) 2007-2009 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */

package org.jvoicexml.jsapi2.jse.synthesis;

import javax.speech.synthesis.Speakable;
import javax.speech.synthesis.SpeakableListener;
import javax.speech.AudioSegment;
import javax.speech.synthesis.PhoneInfo;

/**
 * An item of the {@link QueueManager}.
 * @author Renato Cassaca
 * @version $Revision: 1370 $
 */
public class QueueItem {

    private Object source;
    private final int id;

    /** The queued speakable. */
    private final Speakable speakable;

    private final SpeakableListener listener;
    private AudioSegment audioSegment;
    private String[] words;
    private float[] wordsStartTimes;
    private PhoneInfo[] phonesInfo;

    public QueueItem(final int id, final Speakable speakable,
            final SpeakableListener listener) {
        this.id = id;
        this.speakable = speakable;
        this.listener = listener;
        this.audioSegment = null;
        this.words = new String[0];
        this.wordsStartTimes = new float[0];
        this.phonesInfo = new PhoneInfo[0];
        this.source = speakable;
    }

    public QueueItem(int id, Speakable speakable, SpeakableListener listener,
            String text) {
        this(id, speakable, listener);
        this.source = text;
    }

    public QueueItem(int id, AudioSegment audioSegment,
            SpeakableListener listener) {
        this.id = id;
        this.speakable = null;
        this.listener = listener;
        this.audioSegment = audioSegment;
        this.words = new String[0];
        this.wordsStartTimes = new float[0];
        this.phonesInfo = new PhoneInfo[0];
        this.source = audioSegment.getMarkupText();
    }

    /**
     * Retrieves the speakable.
     * @return the speakable.
     */
    public Speakable getSpeakable() {
        return speakable;
    }

    public SpeakableListener getListener() {
        return listener;
    }

    public int getId() {
        return id;
    }

    public Object getSource() {
        return source;
    }

    public AudioSegment getAudioSegment() {
        return audioSegment;
    }

    public void setAudioSegment(AudioSegment audiosegment) {
        audioSegment = audiosegment;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] w) {
        words = w;
    }

    public float[] getWordsStartTime() {
        return wordsStartTimes;
    }

    public void setWordsStartTimes(float[] wordsstarttimes) {
        wordsStartTimes = wordsstarttimes;
    }

    public PhoneInfo[] getPhonesInfo() {
        return phonesInfo;
    }

    public void setPhonesInfo(PhoneInfo[] phonesinfo) {
        phonesInfo = phonesinfo;
    }
}
