import java.io.IOException;
import java.util.HashMap;

public class BoolTermNode {


    //==================//
    // Member Variables //
    //==================//

    private BoolFactorNode m_boolFactor;
    private BoolFactorTailNode m_boolFactorTail;


    //=========//
    // Methods //
    //=========//

    public BoolTermNode(BoolFactorNode boolFactor,
                        BoolFactorTailNode boolFactorTail)
    {
        m_boolFactor = boolFactor;
        m_boolFactorTail = boolFactorTail;
    }

    public double getVal(ProgState progState)
            throws DCRuntimeErrorException
    {
        // Get the value of the child bool-factor.
        double factorVal = m_boolFactor.getVal(progState);

        // The value of this bool term may be modified by the bool
        // factor tail.
        factorVal = m_boolFactorTail.getVal(factorVal, progState);


        return factorVal;
    }


    //================//
    // Static Methods //
    //================//

    public static BoolTermNode parseBoolTerm(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 26:
        //
        //		bool-term : bool-factor bool-factor-tail
        //

        // Read the bool-factor and the bool-factor tail.
        BoolFactorNode factor = BoolFactorNode.parseBoolFactor(tokenReader);
        BoolFactorTailNode factorTail =
                BoolFactorTailNode.parseBoolFactorTail(tokenReader);


        return new BoolTermNode(factor, factorTail);
    }

}
