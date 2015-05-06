import java.io.IOException;


/**
 * An arithmetic expression is a sum of terms.
 *
 * <hr/>
 * <pre>
 *         ...
 *     29. bool-factor : arith-expr relation-option
 *     30. relation-option : < arith-expr
 *     31. relation-option : <= arith-expr
 *     32. relation-option : = arith-expr
 *     33. relation-option : >= arith-expr
 *     34. relation-option : > arith-expr
 *     35. relation-option : <> arith-expr
 *         ...
 *     37. arith-expr : term term-tail
 *         ...
 * </pre>
 */
public class ArithExprNode {

    //==================//
    // Member Variables //
    //==================//

    private TermNode m_term;
    private TermTailNode m_termTail;


    //=========//
    // Methods //
    //=========//

    /**
     * Construct a new arithmetic expression node with the given
     * term and term-tail.
     *
     * @param term The first term in the expression
     * @param termTail The term tail, to which subsequent terms in
     *                 the expression are attached.
     */
    public ArithExprNode(TermNode term, TermTailNode termTail) {
        m_term = term;
        m_termTail = termTail;
    }

    /**
     * Evaluates the arithmetic expression using the current
     * symbol table and returns its value.
     *
     * @param progState The current program state
     *
     * @return The value of the evaluated arithmetic expression.
     */
    public double getVal(ProgState progState)
            throws DCRuntimeErrorException
    {
        // Get the value of the child term.
        double termVal = m_term.getVal(progState);

        // The value may be modified by the term tail.
        termVal = m_termTail.getVal(termVal, progState);


        return termVal;
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into an arithmetic expression.
     *
     * When this method is called a term is imminently expected to
     * appear in the input stream.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed ArithExprNode that was parsed from
     *         the source code
     */
    public static ArithExprNode parseArithExpr(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 37.
        //
        //      arith-expr : term term-tail
        //

        // Read the term and the term-tail.
        TermNode term = TermNode.parseTerm(tokenReader);
        TermTailNode termTail = TermTailNode.parseTermTail(tokenReader);
        return new ArithExprNode(term, termTail);
    }

}
