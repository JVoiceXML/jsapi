package org.jvoicexml.jsapi2.j2se.recognition.sphinx4;

import javax.speech.spi.EngineListFactory;
import javax.speech.EngineMode;
import javax.speech.EngineList;


/**
 * <p>Title: JSAPI 2.0</p>
 *
 * <p>Description: An independent reference implementation of JSR 113</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: JVoiceXML group - http://jvoicexml.sourceforge.net</p>
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class SphinxEngineListFactory implements EngineListFactory {

    private final EngineMode[] engineModes;

    public SphinxEngineListFactory() {
        engineModes = new EngineMode[] {
                      new SphinxRecognizerMode()
        };
    }

    /**
     * createEngineList
     *
     * @param require EngineMode
     * @return EngineList
     * @todo Implement this javax.speech.spi.EngineListFactory method
     */
    public EngineList createEngineList(EngineMode require) {
        EngineList engineList = new EngineList(engineModes);

        if (require != null) {
            //Removes unwanted modes
            engineList.requireMatch(require);
        }

        return (engineList.size() > 0 ? engineList : null);
    }
}
