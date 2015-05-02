import java.io.IOException;
import java.util.HashMap;

public class RelationOptionNode {

    //=============//
    // Local Types //
    //=============//

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

    public RelationOptionNode(Operator oper, ArithExprNode arithExpr) {
        m_oper = oper;
        m_arithExpr = arithExpr;
    }

    public RelationOptionNode() {
        m_oper = null;
        m_arithExpr = null;
    }

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
        // GR 30/31/32/33/34/35:
        //
        //		relation-option : < arith-expr
        //		relation-option : <= arith-expr
        //		relation-option : = arith-expr
        //		relation-option : >= arith-expr
        //		relation-option : > arith-expr
        //		relation-option : <> arith-expr
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
        // GR 36:
        //
        //		relation-option :
        //
        else {
            relationOption = new RelationOptionNode();
            tokenReader.unread(token);
        }


        return relationOption;
    }
}
