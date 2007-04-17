package javax.speech;

public class Word {
    public static final long ABBREVIATION = 0;
    
    public static final long ACOUSTIC = 1;
    
    public static final long ADJECTIVE = 2;
    
    public static final long ADVERB  = 3;
    
    public static final long AUXILIARY = 4;
    
    public static final long CARDINAL  = 5;
    
    public static final long CONJUNCTION = 6;
    
    public static final long CONTRACTION  = 7;
    
    public static final long DETERMINER = 7;

    public static final long DONT_CARE = 8;

    public static final long NOUN = 9;

    public static final long OTHER = 10;
    
    public static final long PREPOSITION = 11;
    
    public static final long PRONOUN = 12;

    public static final long PROPER_ADJECTIVE = 13;

    public static final long PROPER_NOUN = 14;

    public static final long UNKNOWN = 15;

    public static final long VERB = 16;
    
    private String text;
    
    private String[] pronunciations;
    
    private String spokenForm;
    
    private AudioSegment audioSegment;
    
    private long categories;
    
    public Word(String text, String[] pronunciations, String spokenForm, 
            AudioSegment audioSegment, long categories) {
        this.text = text;
        this.pronunciations = pronunciations;
        this.spokenForm = spokenForm;
        this.audioSegment = audioSegment;
        this.categories = categories;
    }

    public AudioSegment getAudioSegment() {
        return audioSegment;
    }

    public long getCategories() {
        return categories;
    }

    public String[] getPronunciations() {
        return pronunciations;
    }

    public String getSpokenForm() {
        return spokenForm;
    }

    public String getText() {
        return text;
    }
}
