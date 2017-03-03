package org.jvoicexml.jsapi2.recognition;

import java.util.List;

import javax.speech.recognition.RuleComponent;

/**
 * Represents a node of a graph.
 *
 * @author David Jose Rodrigues
 * @author Dirk Schnelle-Walka
 */

public class GrammarNode {

    /** Represent an end alternative node. */
    public static final int END_ALTERNATIVE = 1;
    /** Represent an end count node. */
    public static final int END_COUNT = 2;
    /** Represent an end reference node. */
    public static final int END_REFERENCE = 3;
    /** Represent an end sequence node. */
    public static final int END_SEQUENCE = 4;
    /** Represent an start alternative node. */
    public static final int START_ALTERNATIVE = 5;
    /** Represent an start count node. */
    public static final int START_COUNT = 6;
    /** Represent an start reference node. */
    public static final int START_REFERENCE = 7;
    /** Represent an start sequence node. */
    public static final int START_SEQUENCE = 8;
    /** Represent an tag node. */
    public static final int TAG = 9;
    /** Represent an token node. */
    public static final int TOKEN = 10;
    
    /** Represent an token node. */
    public static final int SPECIAL = 11;

    /** <code>true</code> if this node is a final node of the graph. */
    private boolean isFinal;

    /** the arcs to the successors nodes. */
    private final List<GrammarArc> arcs;

    /** the type of this node. */
    private int type;

    /** the rule component associated with this node. */
    private RuleComponent component;

    /**
     * Creates a grammar node without a rule component associated.
     * @param isFinalNode boolean
     * @param nodeType the node type
     * @param rc RuleComponent the associated rule component
     */
    protected GrammarNode(final boolean isFinalNode, final int nodeType,
                          final RuleComponent rc) {
        this.isFinal = isFinalNode;
        this.type = nodeType;
        this.component = rc;
        arcs = new java.util.ArrayList<GrammarArc>();
    }

    /**
     * Create a grammar node, without a rule component associated.
     * @param isFinalNode boolean
     * @param nodeType int
     */
    protected GrammarNode(final boolean isFinalNode, final int nodeType) {
        this.isFinal = isFinalNode;
        this.type = nodeType;
        this.component = null;
        arcs = new java.util.ArrayList<GrammarArc>();
    }

    /**
     * Checks if this node is a final node.
     * @return <code>true</code> if this is a final node.
     */
    public final boolean isFinalNode() {
        return isFinal;
    }

    /**
     * Adds an arc, from this node to the destinationNode.
     * @param destinationNode the destination node
     */
    public final void addArc(final GrammarNode destinationNode) {
        arcs.add(new GrammarArc(destinationNode));
    }

    /**
     * Gets the node type.
     * @return int
     */
    public final int getNodeType() {
        return type;
    }

    /**
     * Gets the arc list.
     * @return List
     */
    public final List<GrammarArc> getArcs() {
        return arcs;
    }

    /**
     * Gets the rule component associated with this node.
     * @return RuleComponent
     */
    public final RuleComponent getRuleComponent() {
        return component;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append(getClass().getCanonicalName());
        str.append('[');
        switch (type) {
        case END_ALTERNATIVE:
            str.append("END_ALTERNATIVE");
            break;
        case END_COUNT:
            str.append("END_COUNT");
            break;
        case END_REFERENCE:
            str.append("END_REFERENCE");
            break;
        case END_SEQUENCE:
            str.append("END_SEQUENCE");
            break;
        case START_ALTERNATIVE:
            str.append("START_ALTERNATIVE");
            break;
        case START_COUNT:
            str.append("START_COUNT");
            break;
        case START_REFERENCE:
            str.append("START_REFERENCE");
            break;
        case START_SEQUENCE:
            str.append("START_SEQUENCE");
            break;
        case TAG:
            str.append("TAG");
            break;
        case TOKEN:
            str.append("TOKEN");
            break;
        default:
            str.append(type);
            break;
        }
        str.append(',');
        str.append(isFinal);
        str.append(',');
        str.append(arcs.size());
        str.append(',');
        str.append(component);
        str.append(']');
        return str.toString();
    }
}
