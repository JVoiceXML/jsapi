package org.jvoicexml.jsapi2.jse.recognition;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.speech.EngineException;
import javax.speech.EngineMode;
import javax.speech.EngineStateException;
import javax.speech.recognition.Grammar;
import javax.speech.recognition.GrammarEvent;
import javax.speech.recognition.GrammarException;
import javax.speech.recognition.GrammarListener;
import javax.speech.recognition.GrammarManager;
import javax.speech.recognition.Rule;
import javax.speech.recognition.RuleGrammar;

/**
 * A base implementation of a {@link GrammarManager}.
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class BaseGrammarManager implements GrammarManager {

    /** The listeners of grammar events. */
    protected final List<GrammarListener> grammarListeners;

    /** Storage of created grammars. */
    protected final HashMap<String, Grammar> grammars;

    /** Mask that filter events. */
    private int grammarMask;

    /** Recognizer which the GrammarManager belongs. */
    private final BaseRecognizer recognizer;

    /**
     * Constructor that associates a Recognizer.
     * with a GrammarManager
     *
     * @param recognizer BaseRecognizer
     */
    public BaseGrammarManager(final BaseRecognizer recognizer) {
        grammarListeners = new ArrayList<GrammarListener>();
        grammars = new HashMap<String, Grammar>();
        grammarMask = GrammarEvent.DEFAULT_MASK;
        this.recognizer = recognizer;
    }

    /**
     * Constructor that allows to use a GrammarManager in
     * standalone mode.
     */
    public BaseGrammarManager() {
        this(null);
    }

    /**
     * {@inheritDoc}
     */
    public void addGrammarListener(final GrammarListener listener) {
        grammarListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void removeGrammarListener(final GrammarListener listener) {
        grammarListeners.remove(listener);
    }

    /**
     *
     * @param grammarReference String
     * @param rootName String
     * @return RuleGrammar
     * @throws IllegalArgumentException
     * @throws EngineStateException
     * @throws EngineException
     */
    public RuleGrammar createRuleGrammar(String grammarReference,
                                         String rootName) throws
            IllegalArgumentException, EngineStateException, EngineException {
        return createRuleGrammar(grammarReference, rootName, Locale.getDefault());
    }

    /**
     *
     * @param grammarReference String
     * @param rootName String
     * @return RuleGrammar
     * @throws IllegalArgumentException
     * @throws EngineStateException
     * @throws EngineException
     */
    public RuleGrammar createRuleGrammar(String grammarReference,
                                         String rootName,
                                         Locale locale) throws
            IllegalArgumentException, EngineStateException, EngineException {

        //Validate current state
        insureValidEngineState();

        if (grammars.containsValue(grammarReference)) {
            throw new IllegalArgumentException("Duplicate grammar name: " +
                                               grammarReference);
        }

        //Create grammar
        BaseRuleGrammar brg = new BaseRuleGrammar(recognizer, grammarReference);
        brg.setAttribute("xml:lang", locale.toString());
        brg.setRoot(rootName);

        //Register it
        grammars.put(grammarReference, brg);

        return brg;
    }

    /**
     * Deletes a Grammar
     *
     * @param grammar Grammar
     * @throws IllegalArgumentException
     * @throws EngineStateException
     */
    public void deleteGrammar(Grammar grammar) throws IllegalArgumentException,
            EngineStateException {

        //Validate current state
        insureValidEngineState();

        if (!grammars.containsKey(grammar.getReference()))
            throw new IllegalArgumentException("The Grammar is unknown");

        //Remove the grammar
        grammars.remove(grammar.getReference());
    }

    /**
     * Lists the Grammars known to this Recognizer
     *
     * @return Grammar[]
     * @throws EngineStateException
     */
    public Grammar[] listGrammars() throws EngineStateException {

        //Validate current state
        insureValidEngineState();

        //List of all grammars
        ArrayList<Grammar> allGrammars = new ArrayList<Grammar> ();

        //Get engine built-in grammrs
        if (recognizer != null) {
            List<Grammar> builInGrammars = recognizer.getBuiltInGrammars();
            allGrammars.addAll(builInGrammars);
        }

        //Add local managed grammars
        allGrammars.addAll(grammars.values());

        //Return an array with al know grammars
        return (Grammar[]) allGrammars.toArray(new Grammar[allGrammars.size()]);
    }

    /**
     * Gets the RuleGrammar with the specified grammarReference.
     *
     * @param grammarReference String
     * @return RuleGrammar
     * @throws EngineStateException
     */
    public Grammar getGrammar(String grammarReference) throws
            EngineStateException {

        //Validate current state
        insureValidEngineState();

        return grammars.get(grammarReference);
    }

    /**
     * Loads a RuleGrammar from a URI or named resource.
     *
     * @param grammarReference String
     * @return RuleGrammar
     * @throws GrammarException
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws EngineStateException
     * @throws EngineException
     */
    public Grammar loadGrammar(String grammarReference, String mediaType) throws
            GrammarException, IllegalArgumentException, IOException,
            EngineStateException, EngineException {
        return loadGrammar(grammarReference, mediaType, true, false, null);
    }

    /**
     * Loads a RuleGrammar from a URI or named resource
     * and optionally loads any referenced Grammars.
     *
     * @param grammarReference String
     * @param loadReferences boolean
     * @param reloadGrammars boolean
     * @param loadedGrammars Vector
     * @return RuleGrammar
     * @throws GrammarException
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws EngineStateException
     * @throws EngineException
     */
    public Grammar loadGrammar(String grammarReference,
                               String mediaType,
                               boolean loadReferences,
                               boolean reloadGrammars,
                               Vector loadedGrammars) throws
            GrammarException, IllegalArgumentException,
            IOException, EngineStateException, EngineException {

        //Validate current state
        insureValidEngineState();

        //Make sure that recognizer supports markup
        if (recognizer != null) {
            final EngineMode mode = recognizer.getEngineMode();
            if (!mode.getSupportsMarkup()) {
                throw new EngineException("Engine doesn't support markup");
            }
        }

        //Proccess grammar
        URL url = new URL(grammarReference);
        InputStream grammarStream = url.openStream();
        SrgsRuleGrammarParser srgsParser = new SrgsRuleGrammarParser();
        Rule[] rules = srgsParser.load(grammarStream);
        if (rules != null) {
            //Initialize rule grammar
            BaseRuleGrammar brg = new BaseRuleGrammar(recognizer,
                    grammarReference);
            brg.addRules(rules);
            brg.setAttributes(srgsParser.getAttributes());

            //Register grammar
            grammars.put(grammarReference, brg);

            return brg;
        }

        return null;
    }

    /**
     * Creates a RuleGrammar from grammar text provided by a Reader.
     *
     * @param grammarReference String
     * @param reader Reader
     * @return RuleGrammar
     * @throws GrammarException
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws EngineStateException
     * @throws EngineException
     */
    public Grammar loadGrammar(String grammarReference, String mediaType,
                               Reader reader) throws
            GrammarException, IllegalArgumentException, IOException,
            EngineStateException, EngineException {

        //Validate current state
        insureValidEngineState();

        //Make sure that recognizer supports markup
        if (recognizer != null) {
            if (!recognizer.getEngineMode().getSupportsMarkup()) {
                throw new EngineException("Engine doesn't support markup");
            }
        }

        //Proccess grammar
        SrgsRuleGrammarParser srgsParser = new SrgsRuleGrammarParser();
        Rule[] rules = srgsParser.load(reader);
        if (rules != null) {
            //Initialize rule grammar
            BaseRuleGrammar brg = new BaseRuleGrammar(recognizer, grammarReference);
            brg.addRules(rules);
            brg.setAttributes(srgsParser.getAttributes());

            //Register grammar
            grammars.put(grammarReference, brg);

            return brg;
        }

        return null;
    }

    /**
     * Creates a RuleGrammar from grammar text provided as a String.
     *
     * @param grammarReference String
     * @param grammarText String
     * @return RuleGrammar
     * @throws GrammarException
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws EngineStateException
     * @throws EngineException
     */
    public Grammar loadGrammar(String grammarReference,
                               String mediaType,
                               String grammarText) throws
            GrammarException, IllegalArgumentException, IOException,
            EngineStateException, EngineException {
        return loadGrammar(grammarReference, mediaType,
                           new StringReader(grammarText));
    }

    /**
     *
     * @param grammarReference String
     * @param mediaType String
     * @param byteStream InputStream
     * @param encoding String
     * @return Grammar
     * @throws GrammarException
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws EngineStateException
     * @throws EngineException
     */
    public Grammar loadGrammar(String grammarReference,
                               String mediaType,
                               InputStream byteStream,
                               String encoding) throws GrammarException,
            IllegalArgumentException,
            IOException,
            EngineStateException,
            EngineException {

        return loadGrammar(grammarReference, mediaType,
                           new InputStreamReader(byteStream, encoding));
    }


    public void setGrammarMask(int mask) {
        grammarMask = mask;
    }

    public int getGrammarMask() {
        return grammarMask;
    }

    private void insureValidEngineState() throws EngineStateException {
        if (recognizer != null) {
            //Validate current state
            if (recognizer.testEngineState(recognizer.DEALLOCATED | recognizer.DEALLOCATING_RESOURCES) == true) {
                throw new EngineStateException("Cannot execute GrammarManager operation: invalid engine state: " + recognizer.stateToString(recognizer.getEngineState()));
            }

            //Wait until end of allocating (if it's currently allocating)
            while (recognizer.testEngineState(recognizer.ALLOCATING_RESOURCES)) {
                try {
                    recognizer.waitEngineState(recognizer.ALLOCATED);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

}
