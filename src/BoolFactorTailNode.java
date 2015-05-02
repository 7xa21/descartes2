import java.io.IOException;
import java.util.HashMap;

public class BoolFactorTailNode {

    //==================//
    // Member Variables //
    //==================//

    private BoolFactorNode m_boolFactor;
    private BoolFactorTailNode m_boolFactorTail;


    //=========//
    // Methods //
    //=========//

    public BoolFactorTailNode(BoolFactorNode boolFactor,
                              BoolFactorTailNode boolFactorTail)
    {
        m_boolFactor = boolFactor;
        m_boolFactorTail = boolFactorTail;
    }

    public double getVal(double assoc, ProgState progState)
            throws DCRuntimeErrorException
    {
        // Get the value of the child bool-factor.
        double factorVal = assoc;

        if (m_boolFactor != null) {
            assert(m_boolFactorTail != null);

            double tailVal = m_boolFactor.getVal(progState);
            tailVal = m_boolFactorTail.getVal(tailVal, progState);

            if (factorVal != 0.0 && tailVal != 0.0) {
                factorVal = 1.0;
            } else {
                factorVal = 0.0;
            }
        }


        return factorVal;
    }


    //================//
    // Static Methods //
    //================//

    public static BoolFactorTailNode parseBoolFactorTail(
            TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;

        // Eat any leading spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 27:
        //
        //		bool-factor-tail : AND bool-factor bool-factor-tail
        //

        // Look for "AND" keyword.
        BoolFactorTailNode factorTail;
        if (token.getCode() == TokenCode.T_AND) {
            // Now look for the bool-factor and bool-factor-tail.
            BoolFactorNode factor =
                    BoolFactorNode.parseBoolFactor(tokenReader);
            BoolFactorTailNode nextFactorTail =
                    BoolFactorTailNode.parseBoolFactorTail(tokenReader);

            factorTail = new BoolFactorTailNode(factor, nextFactorTail);
        }

        //
        // GR 28:
        //
        //		bool-factor-tail :
        //

        // If the token was not "AND" then this bool-factor-tail
        // is empty (and we need to unread() the token back to the
        // reader).
        else {
            tokenReader.unread(token);
            factorTail = new BoolFactorTailNode(null, null);
        }


        return factorTail;
    }
}
