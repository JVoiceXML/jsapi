/*
 * File:    $HeadURL$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2009 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */

package org.jvoicexml.jsapi2.synthesis;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.speech.Engine;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerMode;
import javax.speech.synthesis.SynthesizerProperties;
import javax.speech.synthesis.Voice;

import org.jvoicexml.jsapi2.BaseEngineProperties;

/**
 * Base implementation of {@link SynthesizerProperties}.
 *
 * <p>
 * Actual JSAPI2 implementations may want to override this class to
 * apply the settings to the synthesizer.
 * </p>
 *
 * @author Renato Cassaca
 * @author Dirk Schnelle-Walka
 * @version 1.0
 */
public class BaseSynthesizerProperties extends BaseEngineProperties
    implements SynthesizerProperties {
    /** Properties that have not been committed. */
    private Hashtable uncommitedProperties;

    /** Current properties. */
    private Hashtable properties;

    /**
     * Constructs a new Object.
     * @param synthesizer reference to the synthesizer.
     */
    public BaseSynthesizerProperties(final Synthesizer synthesizer) {
        super(synthesizer);
        properties = new Hashtable();
        uncommitedProperties = new Hashtable();
        reset();
    }

    /**
     * Convenience method to retrieve an integer property.
     * @param name name of the property.
     * @return value of the property.
     */
    private int getIntProperty(final String name) {
        synchronized (properties) {
            Integer value = (Integer) properties.get(name);
            if (value == null) {
                return 0;
            }
            return value.intValue();
        }
    }

    /**
     * Sets the value without committing them.
     * @param name name of the property.
     * @param value new value of the property.
     */
    private void setUncommittedIntProperty(final String name, final int value) {
        synchronized (uncommitedProperties) {
            uncommitedProperties.put(name, new Integer(value));
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getInterruptibility() {
        return getIntProperty("interruptibility");
    }

    /**
     * {@inheritDoc}
     */
    public void setInterruptibility(final int level) {
        if ((level != WORD_LEVEL) && (level != OBJECT_LEVEL)
                && (level != QUEUE_LEVEL)) {
            throw new IllegalArgumentException("Invalid interruptibiliy level :"
                    + level);
        }
        setUncommittedIntProperty("interruptibility", level);
    }

    /**
     * {@inheritDoc}
     */
    public int getPitch() {
        return getIntProperty("pitch");
    }

    /**
     * {@inheritDoc}
     */
    public void setPitch(final int hertz) {
        if (hertz <= 0) {
            throw new IllegalArgumentException(
                    "Pitch is not a positive integer:"  + hertz);
        }
        setUncommittedIntProperty("pitch", hertz);
    }

    /**
     * {@inheritDoc}
     */
    public int getPitchRange() {
        return getIntProperty("pitchRange");
    }

    /**
     * {@inheritDoc}
     */
    public void setPitchRange(final int hertz) {
        if (hertz < 0) {
            throw new IllegalArgumentException(
                    "Pitch is a negative integer:"  + hertz);
        }
        setUncommittedIntProperty("pitchRange", hertz);
    }

    /**
     * {@inheritDoc}
     */
    public int getSpeakingRate() {
        return getIntProperty("speakingRate");
    }

    /**
     * {@inheritDoc}
     */
    public void setSpeakingRate(final int wpm) {
        if (wpm < 0) {
            throw new IllegalArgumentException(
                    "Speaking rate is not a postivie integer:"  + wpm);
        }
        setUncommittedIntProperty("speakingRate", wpm);
    }

    /**
     * {@inheritDoc}
     */
    public Voice getVoice() {
        synchronized (properties) {
            return (Voice) properties.get("voice");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setVoice(final Voice voice) {
        final Engine synthesizer = getEngine();
        final SynthesizerMode mode =
            (SynthesizerMode) synthesizer.getEngineMode();
        final Voice[] voices = mode.getVoices();
        for (int i = 0; i < voices.length; i++) {
            final Voice current = voices[i];
            if (current.match(voice)) {
                synchronized (uncommitedProperties) {
                    uncommitedProperties.put("voice", voice);
                }
                return;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getVolume() {
        return getIntProperty("volume");
    }


    /**
     * {@inheritDoc}
     */
    public void setVolume(final int volume) {
        if ((volume < MIN_VOLUME) || (volume > MAX_VOLUME)) {
            throw new IllegalArgumentException("Volume is out of range: "
                    + volume);
        }
        setUncommittedIntProperty("volume", volume);
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        synchronized (properties) {
            properties.put("interruptibility", new Integer(OBJECT_LEVEL));
            properties.put("pitch", new Integer(160));
            properties.put("pitchRange", new Integer((int)(160 * 0.60)));
            properties.put("speakingRate", new Integer(DEFAULT_RATE));
            properties.put("volume", new Integer(MEDIUM_VOLUME));

            //Set default voice
            final Engine engine = getEngine();
            SynthesizerMode mode = (SynthesizerMode) engine.getEngineMode();
            if (mode == null) {
                properties.put("voice", null);
            } else {
                Voice[] voices = mode.getVoices();
                if ((voices != null) && (voices.length > 0)) {
                    properties.put("voice", voices[0]);
                } else {
                    properties.put("voice", null);
                }
            }
        }

        super.reset();
    }

    /**
     * Applies all uncommitted properties.
     */
    public void commitPropertiesChanges() {
        synchronized (uncommitedProperties) {
            synchronized (properties) {
                final Enumeration enumeration = uncommitedProperties.keys();
                while (enumeration.hasMoreElements()) {
                    final String name = (String) enumeration.nextElement();
                    final Object oldValue = properties.get(name);
                    final Object newValue = uncommitedProperties.get(name);
                    properties.put(name, newValue);
                    postPropertyChangeEvent(name, oldValue, newValue);
                    uncommitedProperties.remove(name);
                }
            }
        }
    }

    protected boolean setProperty(String propName, Object value) {
        // TODO Auto-generated method stub
        return false;
    }

}
