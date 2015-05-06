import java.io.IOException;


/**
 * A factor tail follows a factor and, if non-empty, modifies
 * the factor by either multiplying it or dividing it by a
 * subsequent factor. It then includes its own subsequent factor
 * tail.
 *
 * <hr/>
 * <pre>
 *         ...
 *     41. term : factor factor-tail
 *     42. factor-tail : * factor factor-tail
 *     43. factor-tail : / factor factor-tail
 *     44. factor-tail :
 *         ...
 * </pre>
 */
public class FactorTailNode {

    //=============//
    // Local Types //
    //=============//

    /**
     * The (higher precedence) arithmetic operators that may be
     * applied to two factors.
     */
    public enum Operator {
        MULTIPLY,
        DIVIDE
    }


    //==================//
    // Member Variables //
    //==================//

    private FactorNode m_factor;
    private FactorTailNode m_factorTail;

    public Operator m_oper;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new factor tail node that will apply 'oper'
     * to an external, left-associative factor and the specified
     * factor (with factor tail) value.
     *
     * @param oper The operator this factor tail will apply (or
     *             null)
     * @param factor The right-hand operand of 'oper' (or null)
     * @param factorTail The factor tail modifying 'factor'
     */
    public FactorTailNode(Operator oper,
                          FactorNode factor,
                          FactorTailNode factorTail)
    {
        m_oper = oper;
        m_factor = factor;
        m_factorTail = factorTail;
    }

    /**
     * Modifies the value of 'assoc' by this factor tail and
     * returns the result. If this factor tail is empty, the value
     * of 'assoc' is returned unmodified.
     *
     * @param assoc The left-associate operand of this factor
     *              tail's operator
     * @param progState The current program state
     *
     * @return The result of the operation implied by this
     *         factor-tail
     */
    public double getVal(double assoc, ProgState progState)
            throws DCRuntimeErrorException
    {
        // The "associate" is the value modified by this tail (if
        // the tail isn't empty).
        double factorVal = assoc;

        if (m_factor != null) {
            assert(m_factorTail != null);

            // Calculate the value of this factor tail.
            double tailVal = m_factor.getVal(progState);
            tailVal = m_factorTail.getVal(tailVal, progState);

            // Transform the factor value by the factor tail
            // value.
            if (m_oper == Operator.MULTIPLY) {
                factorVal *= tailVal;
            } else {
                if (tailVal == 0.0) {
                    throw new DCRuntimeErrorException("Division by zero.");
                }
                factorVal /= tailVal;
            }
        }


        return factorVal;
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, a factor tail node.
     *
     * A factor and factor-tail constitute a term.
     *
     * A factor tail begins with an asterisk (multiplication
     * operator), a slash (division operator) or is empty.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed FactorTailNode that was parsed from
     *         the source code
     */
    public static FactorTailNode parseFactorTail(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        // Eat any leading spaces and get next token.
        TokenDescriptor token;
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 42 / 43.
        //
        //      factor-tail : * term term-tail
        //      factor-tail : / term term-tail
        //

        // Look for a '*' or a '/'.
        Operator oper = null;
        if (token.getCode() == TokenCode.T_MULTIPLY) {
            oper = Operator.MULTIPLY;
        } else if (token.getCode() == TokenCode.T_DIVIDE) {
            oper = Operator.DIVIDE;
        }

        // Either construct a compound factor tail, with a
        // multiply or divide operator, or an empty factor tail.
        FactorTailNode factorTail;
        if (oper != null) {
            FactorNode factor = FactorNode.parseFactor(tokenReader);
            FactorTailNode nextFactorTail =
                    FactorTailNode.parseFactorTail(tokenReader);
            factorTail = new FactorTailNode(oper, factor, nextFactorTail);
        }

        //
        // GR 44.
        //
        //      factor-tail :
        //

        else {
            // This factor tail is empty. Since the token we read
            // isn't part of the factor tail, unread it.
            tokenReader.unread(token);
            factorTail = new FactorTailNode(null, null, null);
        }

        return factorTail;
    }

}
