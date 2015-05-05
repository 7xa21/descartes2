import java.io.IOException;


/**
 * A bool factor is an arithmetic expression by itself, or an
 * arithmetic expression followed by a relational operator (e.g.
 * equality or inequality) and associated arithmetic expression.
 *
 * <hr/>
 * <pre>
 *     26. bool-term : bool-factor bool-factor tail
 *     27. bool-factor-tail : AND bool-factor bool-factor-tail
 *         ...
 *     29. bool-factor : arith-expr relation-option
 * </pre>
 */
public class BoolFactorNode {

    //==================//
    // Member Variables //
    //==================//

    private ArithExprNode m_arithExpr;
    private RelationOptionNode m_relationOption;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new bool factor node from an arithmetic
     * expression and a (possibly empty) relation option.
     *
     * @param arithExpr An arithmetic expression
     * @param relationOption A (possibly empty) relation option
     */
    public BoolFactorNode(ArithExprNode arithExpr,
                          RelationOptionNode relationOption) {
        m_arithExpr = arithExpr;
        m_relationOption = relationOption;
    }

    /**
     * Evaluates the bool factor given the current program state
     * and returns the result.
     *
     * If the bool factor is an arithmetic expression with an
     * empty relation option, the value of the bool factor is
     * simply that of the expression.
     *
     * If the relation option isn't empty, its operator is used
     * to compare this bool factor's arithmetic expression with
     * the relation option's associated arithmetic expression and
     * return either 1.0 or 0.0 depending on the result.
     *
     * @param progState The current program state
     *
     * @return The value of the bool factor
     */
    public double getVal(ProgState progState)
            throws DCRuntimeErrorException
    {
        // Return the value of the child arith-expr.
        double arithExprVal = m_arithExpr.getVal(progState);

        // The value of the arithmetic expression may be modified
        // by the subsequent relation option.
        arithExprVal = m_relationOption.getVal(arithExprVal, progState);


        return arithExprVal;
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, a bool factor.
     *
     * Bool factors are composed of an arithmetic expression and
     * a (possibly empty) relation option. When this method is
     * called, a bool factor is imminently expected to appear in
     * the input source code.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed BoolFactorNode that was parsed from
     *         the source code
     */
    public static BoolFactorNode parseBoolFactor(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 29.
        //
        //      bool-factor : arith-expr relation-option
        //

        // Read the arith-expr and relation-option
        ArithExprNode arithExpr = ArithExprNode.parseArithExpr(tokenReader);
        RelationOptionNode relationOption =
                RelationOptionNode.parseRelationOption(tokenReader);
        return new BoolFactorNode(arithExpr, relationOption);
    }
}
