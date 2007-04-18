package javax.speech.recognition;

public class SpeakerProfile {
    public static SpeakerProfile DEFAULT = new SpeakerProfile();

    private String id;

    private String name;

    private String variant;

    public SpeakerProfile() {

    }

    public SpeakerProfile(String id, String name, String variant) {
        this.id = id;
        this.name = name;
        this.variant = variant;
    }

    public String getIdentifier() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVariant() {
        return variant;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

    public boolean match(SpeakerProfile require) {
        return false;
    }
}
