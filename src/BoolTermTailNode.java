import java.io.IOException;


/**
 * A bool term tail is used to modify a bool term with an "OR"
 * boolean operator and subsequent bool term (and possibly another
 * bool term tail).
 *
 * <hr/>
 * <pre>
 *     23. expr : bool-term bool-term-tail
 *     24. bool-term-tail : OR bool-term bool-term-tail
 *     25. bool-term-tail :
 * </pre>
 */
public class BoolTermTailNode {

    //==================//
    // Member Variables //
    //==================//

    private BoolTermNode m_boolTerm;
    private BoolTermTailNode m_boolTermTail;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new bool-term-tail with the provided bool-term
     * and subsequent bool-term-tail.
     *
     * If the two parameters are null, an empty bool-term-tail is
     * constructed; otherwise both parameters must not be null.
     *
     * @param boolTerm A BoolTermNode (or null)
     * @param boolTermTail A BoolTermTailNode (or null)
     */
    public BoolTermTailNode(BoolTermNode boolTerm,
                            BoolTermTailNode boolTermTail)
    {
        m_boolTerm = boolTerm;
        m_boolTermTail = boolTermTail;
    }

    /**
     * Modifies the value of the 'assoc' parameter by logically
     * OR'ing it with a bool-term (and subsequent bool-term-tail).
     *
     * If this bool-term-tail was constructed with null members,
     * the value of 'assoc' is returned unmodified.
     *
     * @param assoc This is the value to use for the left-hand
     *              side of the OR operator denoted by this
     *              bool-term-tail
     * @param progState The current program state
     *
     * @return The value of the bool-term-tail
     */
    public double getVal(double assoc, ProgState progState)
            throws DCRuntimeErrorException
    {
        if (m_boolTerm != null) {
            assert(m_boolTermTail != null);

            // Get the value of the child bool-term.
            double tailVal = m_boolTerm.getVal(progState);

            // Modify the value of the child bool-term by its
            // subsequent bool-term-tail.
            tailVal = m_boolTermTail.getVal(tailVal, progState);

            // Boolean OR the 'assoc' parameter with the value of
            // the bool-term and bool-term-tail.
            if (assoc != 0.0 || tailVal != 0.0) {
                assoc = 1.0;
            } else {
                assoc = 0.0;
            }
        }


        return assoc;
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, a bool term tail.
     *
     * Bool term tails, if non-empty, consist of the keyword "OR"
     * followed by a bool-factor and a subsequent
     * bool-factor-tail.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed BoolTermTailNode that was parsed
     *         from the source code
     */
    public static BoolTermTailNode parseBoolTermTail(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;

        // Eat any leading spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 24.
        //
        //      bool-term-tail : OR bool-term bool-term-tail
        //

        // Look for "OR" keyword.
        BoolTermTailNode termTail;
        if (token.getCode() == TokenCode.T_OR) {
            // Now look for another bool-term and bool-term-tail.
            BoolTermNode term = BoolTermNode.parseBoolTerm(tokenReader);
            BoolTermTailNode nextTermTail =
                    BoolTermTailNode.parseBoolTermTail(tokenReader);

            termTail = new BoolTermTailNode(term, nextTermTail);
        }

        //
        // GR 25.
        //
        //      bool-term-tail :
        //

        // If the token was not "OR" then this bool-term-tail is
        // empty (and we need to unread() the token back to the
        // reader).
        else {
            tokenReader.unread(token);
            termTail = new BoolTermTailNode(null, null);
        }


        return termTail;
    }
}
