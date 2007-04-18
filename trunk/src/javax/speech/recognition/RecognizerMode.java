package javax.speech.recognition;

import java.util.Locale;

import javax.speech.EngineMode;

public class RecognizerMode extends EngineMode {
    public static RecognizerMode DEFAULT = new RecognizerMode();

    public static Integer LARGE_SIZE = new Integer(0);

    public static Integer MEDIUM_SIZE = new Integer(1);

    public static Integer SMALL_SIZE = new Integer(2);

    public static Integer VERY_LARGE_SIZE = new Integer(3);

    private Integer vocabSupport;

    private Locale[] locales;

    private SpeakerProfile[] profiles;

    public RecognizerMode() {
    }

    public RecognizerMode(Locale locale) {
        locales = new Locale[1];

        locales[0] = locale;
    }

    public RecognizerMode(String engineName, String modeName, Boolean running,
            Boolean supportsLetterToSound, Integer markupSupport,
            Integer vocabSupport, Locale[] locales, SpeakerProfile[] profiles) {
        super(engineName, modeName, running, supportsLetterToSound,
                markupSupport);
        this.vocabSupport = vocabSupport;
        this.locales = locales;
        this.profiles = profiles;
    }

    public boolean equals(Object mode) {
        return super.equals(mode);
    }

    public Locale[] getLocales() {
        return locales;
    }

    public Integer getMarkupSupport() {
        return super.getMarkupSupport();
    }

    public SpeakerProfile[] getSpeakerProfiles() {
        return profiles;
    }

    public Integer getVocabSupport() {
        return vocabSupport;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean match(EngineMode require) {
        return false;
    }

    public String toString() {
        return super.toString();
    }
}
