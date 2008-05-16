package org.jvoicexml.jsapi2.j2se.synthesis.freetts;

/**
 * Copyright 2003 Sun Microsystems, Inc.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */

/**
 * Extends the BaseVoice class to encapsulate FreeTTSSynthesizer specific data.
 */
public class FreeTTSVoice extends javax.speech.synthesis.Voice {
    /** The encapsulated voice. */
    private com.sun.speech.freetts.Voice freettsVoice;

    /**
     * Constructs a FreeTTSVoice
     * 
     * @param freettsVoice
     *                the freetts voice
     * @param validatorName
     *                the classname of the validator to use
     */
    public FreeTTSVoice(com.sun.speech.freetts.Voice voice) {
        super(voice.getLocale(), voice.getName(),
                genderToInt(voice.getGender()), ageToInt(voice
                        .getAge()), VARIANT_DONT_CARE);

        freettsVoice = voice;

        // voiceId = freettsVoice.getName()+Math.random();
    }

    /**
     * Convert a freetts gender to jsapi gender
     * 
     * @param gender
     *                the freetts gender
     * 
     * @return the jsapi gender
     */
    private static int genderToInt(com.sun.speech.freetts.Gender gender) {
        if (gender == com.sun.speech.freetts.Gender.MALE) {
            return javax.speech.synthesis.Voice.GENDER_MALE;
        } else if (gender == com.sun.speech.freetts.Gender.FEMALE) {
            return javax.speech.synthesis.Voice.GENDER_FEMALE;
        } else if (gender == com.sun.speech.freetts.Gender.NEUTRAL) {
            return javax.speech.synthesis.Voice.GENDER_NEUTRAL;
        } else if (gender == com.sun.speech.freetts.Gender.DONT_CARE) {
            return javax.speech.synthesis.Voice.GENDER_DONT_CARE;
        } else {
            throw new Error("jaspi does not have an equivalent to gender "
                    + gender.toString());
        }
    }

    /**
     * Convert a freetts age to jsapi age
     * 
     * @param age
     *                the freetts age
     * 
     * @return the jsapi age
     */
    private static int ageToInt(com.sun.speech.freetts.Age age) {
        if (age == com.sun.speech.freetts.Age.CHILD) {
            return javax.speech.synthesis.Voice.AGE_CHILD;
        } else if (age == com.sun.speech.freetts.Age.TEENAGER) {
            return javax.speech.synthesis.Voice.AGE_TEENAGER;
        } else if (age == com.sun.speech.freetts.Age.YOUNGER_ADULT) {
            return javax.speech.synthesis.Voice.AGE_YOUNGER_ADULT;
        } else if (age == com.sun.speech.freetts.Age.MIDDLE_ADULT) {
            return javax.speech.synthesis.Voice.AGE_MIDDLE_ADULT;
        } else if (age == com.sun.speech.freetts.Age.OLDER_ADULT) {
            return javax.speech.synthesis.Voice.AGE_OLDER_ADULT;
        } else if (age == com.sun.speech.freetts.Age.NEUTRAL) {
            return javax.speech.synthesis.Voice.AGE_DONT_CARE;
        } else if (age == com.sun.speech.freetts.Age.DONT_CARE) {
            return javax.speech.synthesis.Voice.AGE_DONT_CARE;
        } else {
            throw new Error("jaspi does not have an equivalent to age "
                    + age.toString());
        }
    }

    /**
     * Gets a string representation of the object
     * 
     * @return the name of this voice
     */
    public String toString() {
        return freettsVoice.getName();
    }

    /**
     * Gets a FreeTTS com.sun.speech.freetts.Voice from this JSAPI voice
     * 
     * @return a FreeTTS Voice or null, if the voice cannot be found
     */
    public synchronized com.sun.speech.freetts.Voice getVoice() {
        return freettsVoice;
    }
}
