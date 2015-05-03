import java.io.IOException;


public class FactorTailNode {

    //=============//
    // Local Types //
    //=============//
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

    public FactorTailNode(Operator oper,
                          FactorNode factor,
                          FactorTailNode factorTail)
    {
        m_oper = oper;
        m_factor = factor;
        m_factorTail = factorTail;
    }

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

    public static FactorTailNode parseFactorTail(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        // Eat any leading spaces and get next token.
        TokenDescriptor token;
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 42/43:
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
        // GR 44:
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
