package javax.speech;

public class AudioEvent extends SpeechEvent {

    private int audioLevel;

    public AudioEvent(Engine source, int id) {
        super(source, id);
    }

    public AudioEvent(Engine source, int id, int audioLevel) {
        super(source, id);

        this.audioLevel = audioLevel;
    }

    public int getAudioLevel() {
        return audioLevel;
    }

    public String paramString() {
        // TODO: implement AudioEvent.paramString
        return super.paramString();
    }
}
