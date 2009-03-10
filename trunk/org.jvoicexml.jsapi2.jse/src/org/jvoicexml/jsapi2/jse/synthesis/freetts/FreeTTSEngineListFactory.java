package org.jvoicexml.jsapi2.jse.synthesis.freetts;

import javax.speech.spi.EngineListFactory;
import javax.speech.synthesis.SynthesizerMode;
import javax.speech.EngineList;
import javax.speech.EngineMode;
import java.util.Vector;
import java.util.Locale;
import com.sun.speech.freetts.VoiceManager;

/**
 * Copyright 2003 Sun Microsystems, Inc.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */


/**
 * Supports the EngineCentral JSAPI 2.0 interface for the
 * FreeTTSSynthesizer.  To use a FreeTTSSynthesizer, you should place
 * a line into the speech.properties file as so:
 *
 * <pre>
 * FreeTTS=org.jvoicexml.implementation.jsapi20.jvxml.freetts.FreeTTSEngineListFactory
 * </pre>
 *
 */

public class FreeTTSEngineListFactory implements EngineListFactory {
    /**
     * Creates a FreeTTSEngineCentral.
     */
    public FreeTTSEngineListFactory() {
    }

    /**
     * Returns a list containing references to all matching
     * synthesizers.  The mapping of FreeTTS VoiceDirectories and
     * Voices to JSAPI Synthesizers and Voices is as follows:
     *
     * <p><ul>
     * <li>Each FreeTTS VoiceDirectory specifies the list of FreeTTS
     * Voices supported by that directory.  Each Voice in that
     * directory specifies its name (e.g., "kevin" "kevin16" "alan"),
     * domain (e.g., "general" or "time") and locale (e.g., Locale.US).
     * <li>For all FreeTTS Voices from all VoiceDirectories discovered
     * by the VoiceManager, this method will group the Voices
     * according to those that have both a common locale and domain
     * (e.g, all "general" domain voices for the US local will be
     * grouped together).
     * <li>For each group of voices that shares a common locale and
     * domain, this method generates a new JSAPI SynthesizerModeDesc
     * with the following attributes:
     *   <ul>
     *   <li>The engine name is of the form: "FreeTTS &lt;locale>
     *   &lt;domain> synthesizer"  For example, "FreeTTS en_us general
     *   synthesizer"
     *   <li>The locale is the locale shared by all the voices (e.g.,
     *   Locale.US)
     *   <li>The mode name is the domain shared by all the voices
     *   (e.g., "general").
     *   </ul>
     * <li>The JSAPI Voices for each resulting Synthesizer will have
     * the name of the FreeTTS Voice (e.g. "kevin" "kevin16").
     * </ul>
     *
     * @param require  an engine mode that describes the desired
     *          synthesizer
     *
     * @return an engineList containing matching engines, or null if
     *          no matching engines are found
     */
    public EngineList createEngineList(final EngineMode require) {
        // Must be a synthesizer.
        if (!(require instanceof SynthesizerMode)) {
            return null;
        }

        //Instatiate FreeTTS VoiceManager to get all voices available
        VoiceManager voiceManager = VoiceManager.getInstance();
        com.sun.speech.freetts.Voice[] voices = voiceManager.getVoices();

        // We want to get all combinations of domains and locales
        Vector domainLocaleVector = new Vector();
        for (int i = 0; i < voices.length; i++) {
            DomainLocale dl =
                new DomainLocale(voices[i].getDomain(), voices[i].getLocale());
            DomainLocale dlentry = (DomainLocale)
                getItem(domainLocaleVector, dl);
            if (dlentry == null) {
                domainLocaleVector.add(dl);
                dlentry = dl;
            }
            dlentry.addVoice(voices[i]);
        }

        // SynthesizerModes that will be create from combinig domain/locale with
        // voice names
        Vector<FreeTTSSynthesizerMode> synthesizerModes =
            new Vector<FreeTTSSynthesizerMode>();

        // build list of SynthesizerModeDesc's for each domain/locale
        // combination
        for (int i = 0; i < domainLocaleVector.size(); i++) {
            DomainLocale dl = (DomainLocale) domainLocaleVector.get(i);

            Vector<FreeTTSVoice> modeVoices = new Vector<FreeTTSVoice>();

            // iterate through the voices in a different order
            voices = dl.getVoices();
            for (int j = 0; j < voices.length; j++) {
                FreeTTSVoice jsapiVoice = new FreeTTSVoice(voices[j]);
                modeVoices.add(jsapiVoice);
            }

            FreeTTSSynthesizerMode desc = new FreeTTSSynthesizerMode("FreeTTS "
                    + dl.getLocale().toString() + " " + dl.getDomain()
                    + " synthesizer", dl.getDomain(), dl.getLocale(),
                    modeVoices.toArray(new FreeTTSVoice[] {}));

            if (require == null || desc.match(require)) {
                synthesizerModes.addElement(desc);
            }
        }

        final EngineList el;
        if (synthesizerModes.size() == 0) {
            el = null;
        } else {
            el = new EngineList(synthesizerModes.toArray(new EngineMode[]{}));
        }
        return el;
    }

    /**
     * Gets an item out of a vector.
     * Warning: linear search
     *
     * @param vector the vector to search
     * @param o the object to look for using vector.get(i).equals(o)
     *
     * @return the item if it exists in the vector, else null
     */
    private Object getItem(final Vector vector, final Object o) {
        for (int i = 0; i < vector.size(); i++) {
            if (vector.get(i).equals(o)) {
                return vector.get(i);
            }
        }
        return null;
    }
}

