/**
 * 
 */
package org.jvoicexml.jsapi2;

import java.security.Permission;

import javax.speech.EngineStateException;
import javax.speech.SpeechLocale;
import javax.speech.SpeechPermission;
import javax.speech.VocabularyManager;
import javax.speech.Word;

/**
 * Base implementation of a {@link VocabularyManager}.
 * @author Dirk Schnelle-Walka
 *
 */
public class BaseVocabularyManager implements VocabularyManager {
    /**
     * {@inheritDoc}
     */
    public void addWord(final Word word) throws EngineStateException,
            SecurityException {
        final SecurityManager security = System.getSecurityManager();
        if (security != null) {
            final Permission permission = new SpeechPermission(
                    "javax.speech.VocabularyManager.update");
            security.checkPermission(permission);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addWords(final Word[] words) throws EngineStateException,
            SecurityException {
        final SecurityManager security = System.getSecurityManager();
        if (security != null) {
            final Permission permission = new SpeechPermission(
                    "javax.speech.VocabularyManager.update");
            security.checkPermission(permission);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String[] getPronounciations(final String text,
            final SpeechLocale locale)
            throws EngineStateException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Word[] getWords(final String text, final SpeechLocale locale)
            throws EngineStateException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void removeWord(final Word word) throws EngineStateException,
            IllegalArgumentException, SecurityException {
        final SecurityManager security = System.getSecurityManager();
        if (security != null) {
            final Permission permission = new SpeechPermission(
                    "javax.speech.VocabularyManager.update");
            security.checkPermission(permission);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeWords(final Word[] words) throws EngineStateException,
            IllegalArgumentException, SecurityException {
        final SecurityManager security = System.getSecurityManager();
        if (security != null) {
            final Permission permission = new SpeechPermission(
                    "javax.speech.VocabularyManager.update");
            security.checkPermission(permission);
        }
    }
}
