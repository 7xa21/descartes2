import java.io.IOException;


/**
 * A relation option follows an arithmetic expression in a
 * bool factor and is used to determine equality or inequality
 * between two arithmetic expressions.
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
 *     36. relation-option :
 *         ...
 * </pre>
 */
public class RelationOptionNode {

    //=============//
    // Local Types //
    //=============//

    /**
     * The comparison operators that can be applied to two
     * arithmetic expressions.
     */
    public enum Operator {
        LESS_THAN,
        LESS_EQUAL,
        EQUAL_TO,
        GREATER_EQUAL,
        GREATER_THAN,
        NOT_EQUAL
    }


    //==================//
    // Member Variables //
    //==================//

    private Operator m_oper;
    private ArithExprNode m_arithExpr;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new relation option with the specified
     * comparison operator and subsequent arithmetic expression.
     *
     * @param oper One of the operators from the
     *             RelationOptionNode.Operator enum
     * @param arithExpr An arithmetic expression node
     */
    public RelationOptionNode(Operator oper, ArithExprNode arithExpr) {
        m_oper = oper;
        m_arithExpr = arithExpr;
    }

    /**
     * Compares this node's arithmetic expression with the value
     * passed in 'assoc' using the specified comparison operator
     * and returns the result.
     *
     * If the relation-option node is empty, the value of 'assoc'
     * is returned unchanged.
     *
     * @param assoc The value that this relation option node's
     *              arithmetic expression will be compared with
     * @param progState The current program state
     *
     * @return The result of comparing the value of 'assoc' with
     *         the value of the child arithmetic expression
     */
    public double getVal(double assoc, ProgState progState)
            throws DCRuntimeErrorException
    {
        double relationOptionVal = assoc;

        if (m_oper != null) {
            assert(m_arithExpr != null);

            double arithExprVal = m_arithExpr.getVal(progState);

            if (m_oper == Operator.LESS_THAN) {
                relationOptionVal = relationOptionVal < arithExprVal ? 1 : 0;
            } else if (m_oper == Operator.LESS_EQUAL) {
                relationOptionVal = relationOptionVal <= arithExprVal ? 1 : 0;
            } else if (m_oper == Operator.EQUAL_TO) {
                relationOptionVal = relationOptionVal == arithExprVal ? 1 : 0;
            } else if (m_oper == Operator.GREATER_EQUAL) {
                relationOptionVal = relationOptionVal >= arithExprVal ? 1 : 0;
            } else if (m_oper == Operator.GREATER_THAN) {
                relationOptionVal = relationOptionVal > arithExprVal ? 1 : 0;
            } else {
                assert (m_oper == Operator.NOT_EQUAL);
                relationOptionVal = relationOptionVal != arithExprVal ? 1 : 0;
            }
        }


        return relationOptionVal;
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, a relation option node.
     *
     * Non-empty relation options consist of a comparison operator
     * and a subsequent arithmetic expression, or they are empty.
     *
     * An arithmetic expresion in a bool-factor that isn't
     * followed by a comparison operator is simply an empty
     * relation option.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed RelationOptionNode that was parsed
     *         from the source code
     */
    public static RelationOptionNode parseRelationOption(
            TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        RelationOptionNode relationOption;
        TokenDescriptor token;

        // Eat leading spaces
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 30 / 31 / 32 / 33 / 34 / 35.
        //
        //      relation-option : < arith-expr
        //      relation-option : <= arith-expr
        //      relation-option : = arith-expr
        //      relation-option : >= arith-expr
        //      relation-option : > arith-expr
        //      relation-option : <> arith-expr
        //
        Operator oper = null;
        switch (token.getCode()) {
            case T_LESS_THAN:			oper = Operator.LESS_THAN; break;
            case T_LESS_OR_EQUAL:		oper = Operator.LESS_EQUAL; break;
            case T_EQUAL:				oper = Operator.EQUAL_TO; break;
            case T_GREATER_OR_EQUAL:	oper = Operator.GREATER_EQUAL; break;
            case T_GREATER_THAN:		oper = Operator.GREATER_THAN; break;
            case T_NOT_EQUAL:			oper = Operator.NOT_EQUAL; break;
        }

        if (oper != null) {
            ArithExprNode arithExpr =
                    ArithExprNode.parseArithExpr(tokenReader);
            relationOption = new RelationOptionNode(oper, arithExpr);
        }

        //
        // GR 36.
        //
        //      relation-option :
        //
        else {
            tokenReader.unread(token);
            relationOption = new RelationOptionNode(null, null);
        }


        return relationOption;
    }

}
