package javax.speech.synthesis;

import java.util.Locale;

import javax.speech.EngineMode;

public class SynthesizerMode extends EngineMode {
    public static SynthesizerMode DEFAULT = null;

    private Voice[] voices;

    public SynthesizerMode() {
    }

    public SynthesizerMode(Locale locale) {

    }

    public SynthesizerMode(String engineName, String modeName, Boolean running,
            Boolean supportsLetterToSound, Integer markupSupport, Voice[] voices) {
        super(engineName, modeName, running, supportsLetterToSound,
                markupSupport);
        this.voices = voices;
    }

    public boolean equals(Object mode) {
        return false;
    }

    public Integer getMarkupSupport() {
        return super.getMarkupSupport();
    }

    public Voice[] getVoices() {
        return voices;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean match(EngineMode require) {
        return super.match(require);
    }

    public String toString() {
        return super.toString();
    }
}
