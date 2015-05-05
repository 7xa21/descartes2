import java.io.IOException;


/**
 * An expression consists of a bool term and a bool term tail.
 *
 * Expressions form the right-hand side of assignment statements,
 * and they form the conditional value for if statements.
 *
 * <hr/>
 * <pre>
 *         ...
 *     11. if-stmt : IF expr THEN stmt-list else-part
 *         ...
 *     18. assign-stmt : ID BECOMES expr
 *         ...
 *     23. expr : bool-term bool-term-tail
 *         ...
 * </pre>
 */
public class ExprNode {

    //==================//
    // Member Variables //
    //==================//

    private BoolTermNode m_boolTerm;
    private BoolTermTailNode m_boolTermTail;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new expression node.
     *
     * @param boolTerm A BoolTermNode instance
     * @param boolTermTail A BoolTermTailNode instance
     */
    public ExprNode(BoolTermNode boolTerm, BoolTermTailNode boolTermTail) {
        m_boolTerm = boolTerm;
        m_boolTermTail = boolTermTail;
    }

    /**
     * Evaluates the expression and returns the resulting value.
     *
     * @param progState The current program state
     *
     * @return The value of the evaluated expression
     */
    public double getVal(ProgState progState)
            throws DCRuntimeErrorException
    {
        // Get the value of the child bool-term.
        double termVal = m_boolTerm.getVal(progState);

        // The value of the expression may be modified by the bool
        // term tail.
        termVal = m_boolTermTail.getVal(termVal, progState);


        return termVal;
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into an expression node.
     *
     * When this method is called an expression is imminently
     * expected to appear in the input stream.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed ExprNode that was parsed from the
     *         source code
     */
    public static ExprNode parseExpr(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 23.
        //
        //      expr : bool-term bool-term-tail
        //

        // Read the bool-term and the bool-term-tail.
        BoolTermNode boolTerm = BoolTermNode.parseBoolTerm(tokenReader);
        BoolTermTailNode boolTermTail =
                BoolTermTailNode.parseBoolTermTail(tokenReader);


        return new ExprNode(boolTerm, boolTermTail);
    }
}
