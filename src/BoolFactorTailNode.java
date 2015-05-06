import java.io.IOException;


/**
 * A bool factor tail is used to modify a bool factor with an
 * "AND" boolean operator and subsequent bool factor (and
 * possibly another bool factor tail).
 *
 * <hr/>
 * <pre>
 *         ...
 *     26. bool-term : bool-factor bool-factor-tail
 *     27. bool-factor-tail : AND bool-factor bool-factor-tail
 *     28. bool-factor-tail :
 *         ...
 * </pre>
 */
public class BoolFactorTailNode {

    //==================//
    // Member Variables //
    //==================//

    private BoolFactorNode m_boolFactor;
    private BoolFactorTailNode m_boolFactorTail;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new bool-factor-tail with the provided
     * bool-factor and subsequent bool-factor-tail.
     *
     * If the two parameters are null, an empty bool-factor-tail
     * is constructed; otherwise both parameters must not be null.
     *
     * @param boolFactor A BoolFactorNode (or null)
     * @param boolFactorTail A BoolFactorTailNode (or null)
     */
    public BoolFactorTailNode(BoolFactorNode boolFactor,
                              BoolFactorTailNode boolFactorTail)
    {
        m_boolFactor = boolFactor;
        m_boolFactorTail = boolFactorTail;
    }

    /**
     * Modifies the value of the 'assoc' parameter by logically
     * AND'ing it with a bool-factor (and subsequent
     * bool-factor-tail).
     *
     * If this bool-factor-tail was constructed with null members,
     * the value of 'assoc' is returned unmodified.
     *
     * @param assoc This is the value to use for the left-hand
     *              side of the AND operator denoted by this
     *              bool-factor-tail
     * @param progState The current program state
     *
     * @return The value of the bool-factor-tail
     */
    public double getVal(double assoc, ProgState progState)
            throws DCRuntimeErrorException
    {
        if (m_boolFactor != null) {
            assert(m_boolFactorTail != null);

            // Get the value of the child bool-factor.
            double tailVal = m_boolFactor.getVal(progState);

            // Modify the value of the child bool-factor by its
            // subsequent bool-factor-tail.
            tailVal = m_boolFactorTail.getVal(tailVal, progState);

            // Boolean AND the 'assoc' parameter with the value
            // of the bool-factor and bool-factor-tail.
            if (assoc != 0.0 && tailVal != 0.0) {
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
     * into, and returns, a bool factor tail.
     *
     * Bool factor tails, if non-empty, consist of the keyword
     * "AND" followed by a bool-factor and a subsequent
     * bool-factor-tail.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed BoolFactorTailNode that was parsed
     *         from the source code
     */
    public static BoolFactorTailNode parseBoolFactorTail(
            TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;

        // Eat any leading spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 27.
        //
        //      bool-factor-tail : AND bool-factor bool-factor-tail
        //

        // Look for "AND" keyword.
        BoolFactorTailNode factorTail;
        if (token.getCode() == TokenCode.T_AND) {
            // Now look for the bool-factor and bool-factor-tail.
            BoolFactorNode factor =
                    BoolFactorNode.parseBoolFactor(tokenReader);
            BoolFactorTailNode nextFactorTail =
                    BoolFactorTailNode.parseBoolFactorTail(tokenReader);

            factorTail = new BoolFactorTailNode(factor, nextFactorTail);
        }

        //
        // GR 28.
        //
        //      bool-factor-tail :
        //

        // If the token was not "AND" then this bool-factor-tail
        // is empty (and we need to unread() the token back to the
        // reader).
        else {
            tokenReader.unread(token);
            factorTail = new BoolFactorTailNode(null, null);
        }


        return factorTail;
    }

}
