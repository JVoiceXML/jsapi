package javax.speech.synthesis;

public class PhoneInfo {
    public static int UNKNOWN_DURATION = Integer.MAX_VALUE;
    
    private String phoneme;
    
    private int duration;
    
    public PhoneInfo(String phoneme, int duration) {
        this.phoneme = phoneme;
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public String getPhoneme() {
        return phoneme;
    }
    
    
}
