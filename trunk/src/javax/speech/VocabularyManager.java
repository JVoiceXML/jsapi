package javax.speech;

public interface VocabularyManager {
    void addWord(Word word);

    void addWords(Word[] words);

    Word[] getWords(String text);

    Word[] listProblemWords();

    void removeWord(Word word);

    void removeWords(Word[] words);
}
