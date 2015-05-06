import java.io.IOException;


/**
 * A term is a product of factors.
 *
 * <hr/>
 * <pre>
 *         ...
 *     37. arith-expr : term term-tail
 *     38. term-tail : + term term-tail
 *     39. term-tail : - term term-tail
 *     40. term-tail :
 *     41. term : factor factor-tail
 *         ...
 * </pre>
 */
public class TermNode {

    //==================//
    // Member Variables //
    //==================//

    private FactorNode m_factor;
    private FactorTailNode m_factorTail;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new term node with the specified factor and
     * factor tail.
     *
     * @param factor A FactorNode
     * @param factorTail A FactorTailNode (which may be empty)
     */
    public TermNode(FactorNode factor, FactorTailNode factorTail) {
        m_factor = factor;
        m_factorTail = factorTail;
    }

    /**
     * Evaluates the term and returns the result.
     *
     * @param progState The current program state.
     *
     * @return The value of this term
     */
    public double getVal(ProgState progState)
            throws DCRuntimeErrorException
    {
        // Get the value of the child factor.
        double factorVal = m_factor.getVal(progState);

        // The value may be modified by the factor tail.
        factorVal = m_factorTail.getVal(factorVal, progState);


        return factorVal;
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, a term node.
     *
     * A term is composed of a factor and a factor tail. When this
     * method is called a factor and a factor tail are imminently
     * expected to occur in the input source code.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed TermNode that was parsed from the
     *         source code
     */
    public static TermNode parseTerm(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 41.
        //
        //      term : factor factor-tail
        //

        // Read the factor and the factor-tail.
        FactorNode factor = FactorNode.parseFactor(tokenReader);
        FactorTailNode factorTail =
                FactorTailNode.parseFactorTail(tokenReader);


        return new TermNode(factor, factorTail);
    }

}
