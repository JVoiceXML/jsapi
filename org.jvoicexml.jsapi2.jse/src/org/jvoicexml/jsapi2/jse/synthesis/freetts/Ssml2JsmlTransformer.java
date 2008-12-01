package org.jvoicexml.jsapi2.jse.synthesis.freetts;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Ssml2JsmlTransformer {

    private TransformerFactory tfactory;
    private DOMSource xmlDomSource;
    private DocumentBuilder domBuilder;
    private DOMResult xmlDomResult;
    private DocumentBuilderFactory domFactory;
    private Transformer serializer = null;

    private Templates template;

    public Ssml2JsmlTransformer() {
        tfactory = TransformerFactory.newInstance();
        xmlDomSource = null;
        domBuilder = null;
        domFactory = DocumentBuilderFactory.newInstance();
        try {
            domBuilder = domFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }

        /** create a template from a xsl file */
        try {
        	InputStream in = Ssml2JsmlTransformer.class.getResourceAsStream(
        			"ssml2jsml.xsl");
            template = tfactory.newTemplates(new StreamSource(in));
        } catch (TransformerConfigurationException ex2) {
            ex2.printStackTrace();
        }

        try {
            serializer = template.newTransformer();
        } catch (TransformerConfigurationException ex3) {
            ex3.printStackTrace();
        }
    }

    public Document transform(String ssml) {
        try {
            final ByteArrayInputStream bais = new ByteArrayInputStream(ssml.
                    getBytes());
            xmlDomSource = new DOMSource(domBuilder.parse(bais));

        } catch (IOException ex1) {
            ex1.printStackTrace();
        } catch (SAXException ex1) {
            ex1.printStackTrace();
        }

        Document resDocument = domBuilder.newDocument();
        xmlDomResult = new DOMResult(resDocument);

        /** transform to a Document */
        try {
            serializer.transform(xmlDomSource, xmlDomResult);
        } catch (TransformerException ex4) {
            ex4.printStackTrace();
        }

        return resDocument;
    }
}
