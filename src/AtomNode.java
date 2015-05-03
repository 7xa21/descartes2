import java.io.IOException;


public class AtomNode {

    //==================//
    // Member Variables //
    //==================//

    private String m_id;
    private double m_constVal;


    //=========//
    // Methods //
    //=========//

    public AtomNode(String id) {
        m_id = id;
    }

    public AtomNode(double constVal) {
        m_constVal = constVal;
    }

    public double getVal(ProgState progState) {
        if (m_id == null)
            return m_constVal;
        else
            return progState.symTab().get(m_id);
    }


    //================//
    // Static Methods //
    //================//

    public static AtomNode parseAtom(TokenReader tokenReader)
        throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;

        // Eat up any leading spaces.
        do {
            token = tokenReader.getToken();
        } while(token.getCode() == TokenCode.T_SPACE);

        AtomNode atom;

        //
        // GR 48.
        //
        //      atom : ID
        //

        if (token.getCode() == TokenCode.T_ID) {
            atom = new AtomNode(token.getText());
        }

        //
        // GR 49.
        //
        //      atom : CONST
        //

        else if (token.getCode() == TokenCode.T_CONST) {
            atom = new AtomNode(Double.parseDouble(token.getText()));
        }

        // If we expected an atom but got neither a CONST nor an
        // ID token, there is a syntax error in the source code
        // we're reading.
        else {
            throw new DCSyntaxErrorException("Expected atom");
        }

        return atom;
    }

}