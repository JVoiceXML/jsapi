/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 68 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2010-2014 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.jvoicexml.jsapi2.sapi.recognition;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.StringTokenizer;

import javax.speech.recognition.Grammar;
import javax.speech.recognition.ResultToken;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.jvoicexml.jsapi2.recognition.BaseResult;
import org.jvoicexml.jsapi2.recognition.BaseResultToken;

/**
 * A recognition result from the SAPI engine.
 * 
 * @author Dirk Schnelle-Walka
 */
@SuppressWarnings("serial")
public final class SapiResult extends BaseResult {
    /** The extractor for the SML values. */
    private SmlInterpretationExtractor extractor;

    /** The received utterance. */
    private String sml;

    /**
     * Constructs a new object.
     */
    public SapiResult() {
    }

    /**
     * Constructs a new object.
     * 
     * @param grammar
     *            the grammar
     */
    public SapiResult(final Grammar grammar) {
        super(grammar);
    }

    /**
     * Retrieves the confidence of the result.
     * 
     * @return confidence
     */
    public float getConfidence() {
        if (extractor != null) {
            return extractor.getConfidence();
        }
        return 0.0f;
    }

    /**
     * Retrieves the utterance.
     * 
     * @return the utterance
     */
    public String getUtterance() {
        if (extractor != null) {
            return extractor.getUtterance();
        }
        return null;
    }

    /**
     * Sets the retrieved SML string.
     * 
     * @param value
     *            the SML string
     * @throws TransformerException
     *             error transforming the obtained result
     */
    public void setSml(final String value) throws TransformerException {
        sml = value;
        extractor = parseSml(sml);

        // iterate through tags and set resultTags
        final List<SmlInterpretation> interpretations = extractor
                .getInterpretations();
        final List<String> smltags = new java.util.ArrayList<String>();
        if (interpretations.isEmpty()) {
            final String utterance = extractor.getUtterance();
            final String tag = extractor.getUtteranceTag();
            // Hmpf this way we will not be able to process things like
            // <item>yes<tag>yes</tag></item>
            if (!utterance.equals(tag)) {
                smltags.add(tag);
            }
        } else {
            for (int k = 0; k < interpretations.size(); k++) {
                final SmlInterpretation interpretation = interpretations.get(k);
                final String tag = interpretation.getTag();
                final String val = interpretation.getValue();
                boolean addedObject = false;
                if (k < interpretations.size() - 1) {
                    final int currentLevel = interpretation
                            .getObjectHierachyLevel();
                    final SmlInterpretation next = interpretations.get(k + 1);
                    final int nextLevel = next.getObjectHierachyLevel();
                    if (currentLevel < nextLevel) {
                        if (currentLevel == 0) {
                            final String out = "out = new Object();";
                            smltags.add(out);
                        }
                        final String str = "out." + tag + " = new Object();";
                        smltags.add(str);
                        addedObject = true;
                    }
                }

                // for the time being, a help tag is of the form
                // "*.help = 'help'",
                // e.g. "out.help = 'help'"
                boolean specialTag = (tag.equalsIgnoreCase("help")
                        && val.equalsIgnoreCase("help"))
                        || (tag.equalsIgnoreCase("cancel")
                                && val.equalsIgnoreCase("cancel"));
                // SRGS-tags like <tag>FOO="bar"</tag>
                if (!specialTag && (val != null) && !val.isEmpty()) {
                    final String str = "out." + tag + "=" + val + ";";
                    smltags.add(str);
                } else {
                    if (!addedObject) {
                        // SRGS-tags like <tag>FOO</tag>
                        smltags.add(tag);
                    }
                }
            }
        }
        // Copy everything to tags.
        tags = new Object[smltags.size()];
        tags = smltags.toArray(tags);
        final String utt = getUtterance();
        final ResultToken[] tokens = resultToResultToken(utt);
        setTokens(tokens);
    }

    /**
     * Parses the given SML string.
     * 
     * @param sml
     *            the SML to parse
     * @return the parsed information
     * @throws TransformerException
     *             error parsing
     */
    private SmlInterpretationExtractor parseSml(final String sml)
            throws TransformerException {
        final TransformerFactory factory = TransformerFactory.newInstance();
        final Transformer transformer = factory.newTransformer();
        final Reader reader = new StringReader(sml);
        final Source source = new StreamSource(reader);
        final SmlInterpretationExtractor extractor = new SmlInterpretationExtractor();
        final javax.xml.transform.Result result = new SAXResult(extractor);
        transformer.transform(source, result);
        return extractor;
    }

    /**
     * Creates a vector of ResultToken (jsapi) from a sphinx result.
     * 
     * @param result
     *            The Sphinx4 result
     * @param current
     *            The current BaseResult (jsapi)
     * @return ResultToken[]
     */
    private ResultToken[] resultToResultToken(final String utterance) {
        final StringTokenizer st = new StringTokenizer(utterance);
        final int nTokens = st.countTokens();
        final ResultToken[] res = new ResultToken[nTokens];
        for (int i = 0; i < nTokens; ++i) {
            final String text = st.nextToken();
            final BaseResultToken brt = new BaseResultToken(this, text);
            if (getResultState() == BaseResult.ACCEPTED) {
                // @todo set confidenceLevel, startTime and end time,
                // of each token
            }
            res[i] = brt;
        }
        return res;
    }

    /**
     * Retrieves the SML string.
     * 
     * @return the SML string
     */
    public String getSml() {
        return sml;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append(SapiResult.class.getCanonicalName());
        str.append('[');
        str.append(getUtterance());
        str.append(',');
        str.append(getConfidence());
        str.append(',');
        str.append(sml);
        if (tags != null) {
            str.append(',');
            str.append('[');
            for (int i = 0; i < tags.length; i++) {
                final String tag = tags[i].toString();
                str.append(tag);
                if (i < tags.length - 1) {
                    str.append(',');
                }
            }
            str.append(']');
        }
        str.append(']');
        return str.toString();
    }
}
