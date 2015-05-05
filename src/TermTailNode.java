import java.io.IOException;


/**
 * A non-empty term tail is used to add or subtract a term from
 * another term.
 *
 * <hr/>
 * <pre>
 *         ...
 *     37. arith-expr : term term-tail
 *     38. term-tail : + term term-tail
 *     39. term-tail : - term term-tail
 *     40. term-tail :
 *         ...
 * </pre>
 */
public class TermTailNode {

    //=============//
    // Local Types //
    //=============//

    /**
     * The operators that a term-tail may apply to two terms.
     */
    public enum Operator {
        ADD,
        SUBTRACT
    }


    //==================//
    // Member Variables //
    //==================//

    private TermNode m_term;
    private TermTailNode m_termTail;

    public Operator m_oper;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new term-tail node with the specified
     * arithmetic operator, term and subsequent term tail.
     *
     * If all three of the parameters are null, this term-tail is
     * empty; otherwise all three parameters must be non-null.
     *
     * @param oper One of the operators in the
     *             TermTailNode.Operator enum (or null)
     * @param term The right-hand term that this term-tail adds to
     *             (or subtracts from) another, previous term
     * @param termTail A subsequent term tail that's applied to
     *                 the 'term' parameter
     */
    public TermTailNode(Operator oper, TermNode term, TermTailNode termTail) {
        m_oper = oper;
        m_term = term;
        m_termTail = termTail;
    }

    /**
     * Evaluates the child term and term-tail and either adds or
     * subtracts it to the value passed in the 'assoc' parameter
     * and returns the result.
     *
     * If this term-tail is empty, it simply returns the value of
     * the 'assoc' parameter.
     *
     * @param assoc The value that this term-tail modifies
     * @param progState The current program state
     *
     * @return The value of the 'assoc' parameter, modified by
     *         this term tail if it's non-empty
     */
    public double getVal(double assoc, ProgState progState)
            throws DCRuntimeErrorException
    {
        // Get the value of the child term.
        double termVal = assoc;

        if (m_term != null) {
            assert(m_termTail != null);

            // Calculate the value of this term tail.
            double tailVal = m_term.getVal(progState);
            tailVal = m_termTail.getVal(tailVal, progState);

            // Translate the term value by the term tail value.
            if (m_oper == Operator.ADD) {
                termVal += tailVal;
            } else {
                termVal -= tailVal;
            }
        }


        return termVal;
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, a term tail.
     *
     * Statement lists appear after terms. They may be empty, or
     * they may consist of either a "+" or "-" terminal followed
     * by a term and a subsequent term tail.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed TermTailNode that was parsed from
     *         the source code
     */
    public static TermTailNode parseTermTail(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;

        // Eat any leading spaces and get next token.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 38 / 39.
        //
        //      term-tail : + term term-tail
        //      term-tail : - term term-tail
        //

        // Look for a '+' or a '-' operator.
        Operator oper = null;
        if (token.getCode() == TokenCode.T_ADD) {
            oper = Operator.ADD;
        } else if (token.getCode() == TokenCode.T_SUBTRACT) {
            oper = Operator.SUBTRACT;
        }

        // If there was a '+' or '-' operator, build a non-empty
        // term with the subsequent term and term-tail.
        TermTailNode termTail;
        if (oper != null) {
            // Read the term and term-tail.
            TermNode term = TermNode.parseTerm(tokenReader);
            TermTailNode nextTermTail =
                    TermTailNode.parseTermTail(tokenReader);

            termTail = new TermTailNode(oper, term, nextTermTail);
        }

        //
        // GR 40.
        //
        //      term-tail :
        //

        // If there was no '+' or '-' operator found, then this
        // term-tail is empty (and we need to unread() the token
        // back to the TokenReader).
        else {
            tokenReader.unread(token);
            termTail = new TermTailNode(null, null, null);
        }


        return termTail;
    }
}
