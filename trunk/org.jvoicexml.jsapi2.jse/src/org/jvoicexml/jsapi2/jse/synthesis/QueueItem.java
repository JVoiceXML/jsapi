package org.jvoicexml.jsapi2.jse.synthesis;

import javax.speech.synthesis.Speakable;
import javax.speech.synthesis.SpeakableListener;
import javax.speech.AudioSegment;
import javax.speech.synthesis.PhoneInfo;

/**
 * <p>
 * Title: JSAPI 2.0
 * </p>
 * 
 * <p>
 * Description: An independent reference implementation of JSR 113
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: JVoiceXML group - http://jvoicexml.sourceforge.net
 * </p>
 * 
 * @author Renato Cassaca
 * @version 1.0
 */
public class QueueItem {

    private Object source;
    private int id;
    private Speakable speakable;
    private SpeakableListener listener;
    private AudioSegment audioSegment;
    private String[] words;
    private float[] wordsStartTimes;
    private PhoneInfo[] phonesInfo;

    public QueueItem(int id, Speakable speakable, SpeakableListener listener) {
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
        this.listener = listener;
        this.audioSegment = audioSegment;
        this.words = new String[0];
        this.wordsStartTimes = new float[0];
        this.phonesInfo = new PhoneInfo[0];
        this.source = audioSegment;
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
