import java.io.IOException;


/**
 * A bool term is a bool factor by itself, or a bool factor
 * followed by a bool factor tail.
 *
 * <hr/>
 * <pre>
 *     23. expr : bool-term bool-term-tail
 *     24. bool-term-tail : OR bool-term bool-term-tail
 *         ...
 *     26. bool-term : bool-factor bool-factor-tail
 * </pre>
 */
public class BoolTermNode {

    //==================//
    // Member Variables //
    //==================//

    private BoolFactorNode m_boolFactor;
    private BoolFactorTailNode m_boolFactorTail;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new bool term node from a bool factor and a
     * (possibly empty) bool factor tail.
     *
     * @param boolFactor A BoolFactorNode
     * @param boolFactorTail A BoolFactorTailNode
     */
    public BoolTermNode(BoolFactorNode boolFactor,
                        BoolFactorTailNode boolFactorTail)
    {
        m_boolFactor = boolFactor;
        m_boolFactorTail = boolFactorTail;
    }

    /**
     * Evaluates the bool term and returns the value.
     *
     * If the bool term was constructed with an empty bool factor
     * tail, then just the value of the child bool factor is
     * returned. Otherwise the bool factor is logically AND'd by
     * the bool factor tail.
     *
     * @param progState The current program state
     *
     * @return The value of the bool term
     */
    public double getVal(ProgState progState)
            throws DCRuntimeErrorException
    {
        // Get the value of the child bool-factor.
        double factorVal = m_boolFactor.getVal(progState);

        // The value of the child bool-factor may be modified by
        // the child bool-factor-tail.
        factorVal = m_boolFactorTail.getVal(factorVal, progState);


        return factorVal;
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, a bool term.
     *
     * Bool terms are composed of a bool-factor and a (possibly
     * empty) bool factor tail. When this method is called, a bool
     * term is imminently expected to appear in the input source
     * code.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed BoolTermNode that was parsed from
     *         the source code
     */
    public static BoolTermNode parseBoolTerm(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 26.
        //
        //      bool-term : bool-factor bool-factor-tail
        //

        // Read the bool-factor and the bool-factor tail.
        BoolFactorNode factor = BoolFactorNode.parseBoolFactor(tokenReader);
        BoolFactorTailNode factorTail =
                BoolFactorTailNode.parseBoolFactorTail(tokenReader);


        return new BoolTermNode(factor, factorTail);
    }

}
