package javax.speech;

public interface AudioManager {
    void addAudioListener(AudioListener listener);
    
    void removeAudioListener(AudioListener listener);
    
    void audioStart();
    
    void audioStop();
    
    void setMediaLocator(String locator);
    
    String getMediaLocator();
    
    String[] getSupportedMediaLocators(String mediaLocator);
    
    boolean isSameChannel(AudioManager audioManager);
    
    boolean isSupportedMediaLocator(String mediaLocator);
    
}
