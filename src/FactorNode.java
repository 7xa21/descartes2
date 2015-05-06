import java.io.IOException;


/**
 * A factor is a value with high precedence, such as a
 * parenthetical expression, an atom (variable or numeric literal)
 * or a negated factor.
 *
 * An arithmetic expression is a sum of terms, and a term is a
 * product of factors (i.e. when factors are combined with factor
 * tails).
 *
 * <hr/>
 * <pre>
 *         ...
 *     41. term : factor factor-tail
 *     42. factor-tail : * factor factor-tail
 *     43. factor-tail : / factor factor-tail
 *     44. factor : - factor
 *     45. factor : atom
 *     46. factor : ( expr )
 *         ...
 * </pre>
 */
public class FactorNode {

    //==================//
    // Member Variables //
    //==================//

    private AtomNode m_atom;
    private FactorNode m_negFactor;
    private ExprNode m_parenExpr;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a factor from an AtomNode.
     *
     * @param atom An AtomNode instance
     */
    public FactorNode(AtomNode atom) {
        m_atom = atom;
        m_negFactor = null;
        m_parenExpr = null;
    }

    /**
     * Constructs a factor that negates another factor.
     *
     * @param negFactor Another FactorNode instance that will be
     *                  negated by this FactorNode.
     */
    public FactorNode(FactorNode negFactor) {
        m_atom = null;
        m_negFactor = negFactor;
        m_parenExpr = null;
    }

    /**
     * Constructs a factor from a parenthetical expression.
     *
     * @param parenExpr An ExprNode instance that was enclosed in
     *                  parentheses when it was parsed.
     */
    public FactorNode(ExprNode parenExpr) {
        m_atom = null;
        m_negFactor = null;
        m_parenExpr = parenExpr;
    }

    /**
     * Evaluates this factor given the specified program state.
     *
     * @param progState The current program state
     *
     * @return The numeric value of this factor.
     */
    public double getVal(ProgState progState)
            throws DCRuntimeErrorException
    {
        double value;


        // Factor from atom
        if (m_atom != null) {
            assert (m_negFactor == null);
            assert (m_parenExpr == null);
            value = m_atom.getVal(progState);
        }

        // Factor from negated, other factor
        else if (m_negFactor != null) {
            assert (m_parenExpr == null);
            value = -m_negFactor.getVal(progState);
        }

        // Factor from parenthetical expression
        else {
            assert (m_parenExpr != null);
            value = m_parenExpr.getVal(progState);
        }


        return value;
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, a factor node.
     *
     * A factor and factor-tail constitute a term.
     *
     * Factors may either be a minus-sign followed by another
     * factor, an expression inside parentheses, or an atom. When
     * this method is called, a Factor is imminently expected to
     * appear in the source code.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed FactorNode that was parsed from the
     *         source code
     */
    public static FactorNode parseFactor(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;


        // Eat any leading spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        FactorNode factor;

        //
        // GR 45.
        //
        //      factor : - factor
        //

        // Look for a '-' unary operator.
        if (token.getCode() == TokenCode.T_SUBTRACT) {
            // Read the (negated) factor.
            FactorNode negFactor = FactorNode.parseFactor(tokenReader);

            factor = new FactorNode(negFactor);
        }

        //
        // GR 47.
        //
        //      factor : ( expr )
        //

        // Look for a '(' character.
        else if (token.getCode() == TokenCode.T_OPEN_PAREN) {
            // Read the expression.
            ExprNode expr = ExprNode.parseExpr(tokenReader);

            // Eat any spaces between the expression and the closing
            // parenthesis.
            do {
                token = tokenReader.getToken();
            } while (token.getCode() == TokenCode.T_SPACE);

            // The next token MUST be a closing parenthesis.
            if (token.getCode() != TokenCode.T_CLOSE_PAREN) {
                throw new DCSyntaxErrorException(
                        tokenReader, "Expected ')'.");
            }

            factor = new FactorNode(expr);
        }

        //
        // GR 46.
        //
        //      factor : atom
        //

        // The factor HAS to be an atom.
        else {
            // The token we read is part of the atom; put it back
            // so the atom parser can read it.
            tokenReader.unread(token);
            AtomNode atom = AtomNode.parseAtom(tokenReader);

            factor = new FactorNode(atom);
        }


        return factor;
    }

}
