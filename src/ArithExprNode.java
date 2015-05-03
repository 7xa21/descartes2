import java.io.IOException;


/**
 * An arithmetic expression is a sum of terms.
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
     * Construct a new arithmetic expression node.
     *
     * Arithmetic expressions consist of a term and a term-tail.
     * Arithmetic expressions are a sum of terms.
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
     *
     * @throws DCRuntimeErrorException Thrown in the event of a
     *         runtime error (for instance, if a division by zero
     *         occurs)
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
     * @param tokenReader
     * @return
     * @throws IOException
     * @throws DCSyntaxErrorException
     */
    public static ArithExprNode parseArithExpr(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 37:
        //
        //		arith-expr : term term-tail
        //

        // Read the term and the term-tail.
        TermNode term = TermNode.parseTerm(tokenReader);
        TermTailNode termTail = TermTailNode.parseTermTail(tokenReader);
        return new ArithExprNode(term, termTail);
    }

}
