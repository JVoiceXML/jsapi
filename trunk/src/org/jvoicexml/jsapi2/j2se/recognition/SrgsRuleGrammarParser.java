package org.jvoicexml.jsapi2.j2se.recognition;


import java.util.ArrayList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.speech.recognition.Rule;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.xpath.XPathConstants;
import javax.speech.recognition.RuleComponent;
import javax.speech.recognition.RuleSequence;
import javax.xml.xpath.XPathExpressionException;
import javax.speech.recognition.RuleToken;
import javax.speech.recognition.RuleAlternatives;
import javax.speech.recognition.RuleSpecial;
import javax.speech.recognition.RuleReference;
import javax.speech.recognition.RuleTag;
import javax.speech.recognition.RuleCount;
import org.xml.sax.InputSource;
import java.io.Reader;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import org.xml.sax.SAXException;
import org.w3c.dom.NamedNodeMap;
import java.util.HashMap;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: INESC-ID L2F</p>
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class SrgsRuleGrammarParser {

    private XPath xpath;

    private HashMap<String, String> attributes;


    public SrgsRuleGrammarParser() {
        // Create a new XPath
        XPathFactory factory = XPathFactory.newInstance();
        xpath = factory.newXPath();
        attributes = new HashMap<String,String>();
    }

    public Rule[] load(Reader reader) {
        return load(new InputSource(reader));
    }

    public Rule[] load(InputStream stream) {
        return load(new InputSource(stream));
    }

    public Rule[] loadRule(Reader reader) {
        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().
                                      newDocumentBuilder();
            return parseGrammar(builder.parse(new InputSource(reader)));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Rule[] loadRule(InputStream stream){
        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().
                                      newDocumentBuilder();
            return parseGrammar(builder.parse(new InputSource(stream)));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    private Rule[] load(InputSource inputSource) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Node grammarNode = (Node) xpath.evaluate("/grammar", builder.parse(inputSource), XPathConstants.NODE);

            Rule[] rules = parseGrammar(grammarNode);

            //Extract header from grammar
            NamedNodeMap docAttributes = grammarNode.getAttributes();
            for (int i = 0; i < docAttributes.getLength(); i++) {
                attributes.put(docAttributes.item(i).getNodeName(),
                               docAttributes.item(i).getNodeValue());
            }

            return rules;

        } catch (XPathExpressionException ex2) {
            ex2.printStackTrace();
            return null;
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (SAXException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Rule[] parseGrammar(Node grammarNode) {
        ArrayList<Rule> rules = new ArrayList<Rule>();

        try {
            NodeList ruleNodes = (NodeList) xpath.evaluate("rule", grammarNode,
                    XPathConstants.NODESET);
            for (int i = 0; i < ruleNodes.getLength(); i++) {
                Node ruleNode = ruleNodes.item(i);
                String ruleId = xpath.evaluate("@id", ruleNode);
                int scope = Rule.PRIVATE_SCOPE;
                String scopeStr = xpath.evaluate("@scope", ruleNode);
                if (scopeStr != "") {
                    if (scopeStr.equalsIgnoreCase("public"))
                        scope = Rule.PUBLIC_SCOPE;
                }

                ArrayList<RuleComponent> rcs = evalChildNodes(ruleNode);
//                NodeList nodes = (NodeList)xpath.evaluate("child::node()", ruleNode, XPathConstants.NODESET);
//                for (int k = 0; k < nodes.getLength(); k++) {
                //ArrayList<RuleComponent> rcs = evalNode(nodes.item(k));
                if (rcs.size() == 1) {
                    Rule rule = new Rule(ruleId, rcs.get(0), scope);
                    rules.add(rule);
                } else if (rcs.size() > 1) {
                    RuleSequence rs = new RuleSequence(rcs.toArray(new
                            RuleComponent[] {}));
                    Rule rule = new Rule(ruleId, rs, scope);
                    rules.add(rule);
                }
                //   }
            }
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        }
        return rules.toArray(new Rule[] {});
    }


    private ArrayList<RuleComponent> evalNode(Node node) throws
            XPathExpressionException {
        ArrayList<RuleComponent> ruleComponents = new ArrayList<RuleComponent>();
        String nodeName = node.getNodeName();
        if (nodeName.equalsIgnoreCase("#text")) {
            String text = node.getNodeValue().trim();
            if (text.length() > 0) {
                RuleToken ruleToken = new RuleToken(text);
                ruleComponents.add(ruleToken);
            }
        } else if (nodeName.equalsIgnoreCase("one-of")) {
            ArrayList<RuleComponent> rcs = evalChildNodes(node);
            RuleAlternatives ra = new RuleAlternatives(rcs.toArray(new
                    RuleComponent[] {}));
            ruleComponents.add(ra);
        } else if (nodeName.equalsIgnoreCase("item")) {
            int repeatMin = -1;
            int repeatMax = -1;
            int repeatProb = -1;
            try {
                String repeatStr = xpath.evaluate("@repeat", node);
                String repeatProbStr = xpath.evaluate("@repeat-prob", node);

                if (repeatStr != "") {
                    String minStr = repeatStr.substring(0,
                            repeatStr.indexOf('-'));
                    String maxStr = repeatStr.substring(repeatStr.indexOf('-') +
                            1);
                    if (minStr.trim().length() > 0)
                        repeatMin = Integer.parseInt(minStr);
                    if (maxStr.trim().length() > 0)
                        repeatMax = Integer.parseInt(maxStr);
                }

                if (repeatProbStr != "") {
                    repeatProb = Integer.parseInt(repeatProbStr);
                }

            } catch (XPathExpressionException ex) {
                ex.printStackTrace();
            }

            ArrayList<RuleComponent> rcs = evalChildNodes(node);
            RuleSequence rs = new RuleSequence(rcs.toArray(new RuleComponent[] {}));

            if ((repeatMin != -1) && (repeatMax != -1) && (repeatProb != -1)) {
                RuleCount rc = new RuleCount(rs, repeatMin, repeatMax, repeatProb);
                ruleComponents.add(rc);
            }
            else if ((repeatMin != -1) && (repeatMax != -1)) {
                RuleCount rc = new RuleCount(rs, repeatMin, repeatMax);
                ruleComponents.add(rc);
            }
            else if (repeatMin != -1) {
                RuleCount rc = new RuleCount(rs, repeatMin);
                ruleComponents.add(rc);
            }
            else {
                ruleComponents.add(rs);
            }

        } else if (nodeName.equalsIgnoreCase("ruleref")) {
            String specialStr = (String) xpath.evaluate("@special", node);
            if (specialStr != "") {
                if (specialStr.equalsIgnoreCase("NULL")) {
                    ruleComponents.add(RuleSpecial.NULL);
                } else if (specialStr.equalsIgnoreCase("VOID")) {
                    ruleComponents.add(RuleSpecial.VOID);
                } else if (specialStr.equalsIgnoreCase("GARBAGE")) {
                    ruleComponents.add(RuleSpecial.GARBAGE);
                }
            } else {
                String uriStr = (String) xpath.evaluate("@uri", node);
                String ruleName = uriStr.substring(uriStr.indexOf("#") + 1);
                String grammarName = uriStr.substring(0, uriStr.indexOf("#"));
                if ((uriStr.indexOf("#") == -1) || (grammarName.length() < 1)) {
                    ruleComponents.add(new RuleReference(ruleName));
                } else {
                    String typeStr = (String) xpath.evaluate("@type", node);
                    if (typeStr == "") {
                        ruleComponents.add(new RuleReference(grammarName,
                                ruleName));
                    } else {
                        ruleComponents.add(new RuleReference(grammarName,
                                ruleName, typeStr));
                    }
                }
            }
        } else if (nodeName.equalsIgnoreCase("token")) {
            String tokenText = (String) xpath.evaluate("*", node,
                    XPathConstants.STRING);
            RuleToken ruleToken = new RuleToken(tokenText);
            ruleComponents.add(ruleToken);
        } else if (nodeName.equalsIgnoreCase("tag")) {
            Object tagObject = xpath.evaluate("*", node, XPathConstants.STRING);
            RuleTag ruleTag = new RuleTag(tagObject);
            ruleComponents.add(ruleTag);
        } else if (nodeName.equalsIgnoreCase("example")) {
            //Ignore
        }

        return ruleComponents;
    }

    private ArrayList<RuleComponent> evalChildNodes(Node nodes) throws
            XPathExpressionException {
        ArrayList<RuleComponent> ruleComponents = new ArrayList<RuleComponent>();
        NodeList childs = (NodeList) xpath.evaluate("child::node()", nodes,
                XPathConstants.NODESET);
        for (int i = 0; i < childs.getLength(); i++) {
            ruleComponents.addAll(evalNode(childs.item(i)));
        }

        return ruleComponents;
    }

    public HashMap getAttributes() {
        return attributes;
    }
}
