package org.jvoicexml.jsapi2.jse.synthesis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.speech.Engine;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerMode;
import javax.speech.synthesis.SynthesizerProperties;
import javax.speech.synthesis.Voice;

import org.jvoicexml.jsapi2.BaseEngineProperties;

/**
 * <p>Title: JSAPI 2.0</p>
 *
 * <p>Description: An independent reference implementation of JSR 113</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: JVoiceXML group - http://jvoicexml.sourceforge.net</p>
 *
 * @author lyncher
 * @version 1.0
 */
public class BaseSynthesizerProperties extends BaseEngineProperties
    implements SynthesizerProperties {
    /** Logger for this class. */
    private static final Logger LOGGER =
            Logger.getLogger(BaseSynthesizerProperties.class.getName());

    private HashMap<String,Object> uncommitedProperties;
    private HashMap<String,Object> properties;
    private Semaphore propertiesSemaphore;
    private Semaphore uncommitedSemaphore;


    public BaseSynthesizerProperties(Synthesizer synthesizer) {
        super(synthesizer);
        properties = new HashMap<String,Object>();
        uncommitedProperties = new HashMap<String,Object>();
        propertiesSemaphore = new Semaphore(1);
        uncommitedSemaphore = new Semaphore(1);
        reset();
    }

    /**
     * getInterruptibility
     *
     * @return int
     */
    public int getInterruptibility() {
        try {
            propertiesSemaphore.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return ((Integer)(properties.get("interruptibility"))).intValue();
    }

    /**
     * getPitch
     *
     * @return int
     */
    public int getPitch() {
        try {
            propertiesSemaphore.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        int pitch = ((Integer)(properties.get("pitch"))).intValue();
        propertiesSemaphore.release();
        return pitch;
    }

    /**
     * getPitchRange
     *
     * @return int
     */
    public int getPitchRange() {
        try {
            propertiesSemaphore.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

       int pitchRange = ((Integer)(properties.get("pitchRange"))).intValue();
       propertiesSemaphore.release();
       return pitchRange;
    }

    /**
     * getSpeakingRate
     *
     * @return int
     */
    public int getSpeakingRate() {
        try {
            propertiesSemaphore.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        int speakingRate = ((Integer)(properties.get("speakingRate"))).intValue();
        propertiesSemaphore.release();
        return speakingRate;
    }

    /**
     * getVoice
     *
     * @return Voice
     */
    public Voice getVoice() {
        try {
            propertiesSemaphore.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        Voice voice =  (Voice)(properties.get("voice"));
        propertiesSemaphore.release();
        return voice;
    }

    /**
     * getVolume
     *
     * @return int
     */
    public int getVolume() {
        try {
            propertiesSemaphore.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        int volume = ((Integer)(properties.get("volume"))).intValue();
        propertiesSemaphore.release();
        return volume;
    }

    /**
     * reset
     *
     */
    public void reset() {
        try {
            propertiesSemaphore.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        properties.put("interruptibility", OBJECT_LEVEL);
        properties.put("picth", 160);
        properties.put("pitchRange", (int)(160 * 0.60));
        properties.put("speakingRate", DEFAULT_RATE);
        properties.put("volume", MEDIUM_VOLUME);
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

        propertiesSemaphore.release();
        super.reset();
    }

    /**
     * setInterruptibility
     *
     * @param level int
     *
     * @todo Default values are in System.getProperty:
     *   Synthesizer.defaultTrustedInterruptibility
     *   Synthesizer.defaultUntrustedInterruptibility
     *   Synthesizer.maximumUntrustedInterruptibility
     *
     */
    public void setInterruptibility(int level) {
        try {
            uncommitedSemaphore.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        uncommitedProperties.put("interruptibility", new Integer(level));
        uncommitedSemaphore.release();
    }

    /**
     * setPitch
     *
     * @param hertz int
     */
    public void setPitch(int hertz) {
        try {
            uncommitedSemaphore.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        uncommitedProperties.put("pitch", new Integer(hertz));
        uncommitedSemaphore.release();
    }

    /**
     * setPitchRange
     *
     * @param hertz int
     */
    public void setPitchRange(int hertz) {
        try {
            uncommitedSemaphore.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        uncommitedProperties.put("pitchRange", new Integer(hertz));
        uncommitedSemaphore.release();
    }

    /**
     * setSpeakingRate
     *
     * @param wpm int
     */
    public void setSpeakingRate(int wpm) {
        try {
            uncommitedSemaphore.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        uncommitedProperties.put("speakingRate", new Integer(wpm));
        uncommitedSemaphore.release();
    }

    /**
     * setVoice
     *
     * @param voice Voice
     */
    public void setVoice(Voice voice) {
        try {
            uncommitedSemaphore.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        uncommitedProperties.put("voice", voice);
        uncommitedSemaphore.release();
    }

    /**
     * setVolume
     *
     * @param volume int
     */
    public void setVolume(int volume) {
        try {
            uncommitedSemaphore.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        uncommitedProperties.put("volume", volume);
        uncommitedSemaphore.release();
    }


    /**
     * commitPropertiesChanges
     */
    public void commitPropertiesChanges() {
        boolean acquiredUncommitted = false;
        boolean acquiredProperties = false;

        try {
            uncommitedSemaphore.acquire();
            acquiredUncommitted = true;
            propertiesSemaphore.acquire();
            acquiredProperties = true;

            final Iterator it = uncommitedProperties.keySet().iterator();
            while (it.hasNext()) {
                final String name = (String) it.next();
                final Object oldValue = properties.get(name);
                final Object newValue = uncommitedProperties.get(name);
                properties.put(name, newValue);
                postPropertyChangeEvent(name, oldValue, newValue);
                uncommitedProperties.remove(name);
            }
        } catch (InterruptedException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(e.getMessage());
            }
        } finally {
            if (acquiredUncommitted) {
                uncommitedSemaphore.release();
            }
            if (acquiredProperties) {
                propertiesSemaphore.release();
            }
        }
    }

}
