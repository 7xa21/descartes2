import java.io.IOException;


/**
 * An atom is either a literal numeric value or the ID of an
 * already-created variable that has some value assigned to it.
 *
 * <pre>
 *         ...
 *     46. factor : atom
 *         ...
 *     48. atom : ID
 *     49. atom : CONST
 * </pre>
 */
public class AtomNode {

    //==================//
    // Member Variables //
    //==================//

    private String m_id;
    private double m_constVal;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new atom node with the ID of an
     * already-created variable that has some value assigned to
     * it.
     *
     * @param id The ID of an already-created variable that is
     *           expected to appear in the symbol table
     */
    public AtomNode(String id) {
        m_id = id;
    }

    /**
     * Constructs a new atom node with the value of some numeric
     * literal that appeared in the source code.
     *
     * @param constVal The value of a numeric literal that
     *                 appeared in the source code
     */
    public AtomNode(double constVal) {
        m_constVal = constVal;
    }

    /**
     * Using the provided program state (which contains the symbol
     * table), return the value of this atom node.
     *
     * @param progState The current ProgramState
     */
    public double getVal(ProgState progState)
            throws DCRuntimeErrorException
    {
        if (m_id == null)
            return m_constVal;
        else {
            if (!progState.symTab().containsKey(m_id)) {
                throw new DCRuntimeErrorException(
                        "Unrecognized variable name: " + m_id
                );
            }
            return progState.symTab().get(m_id);
        }
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, an atom.
     *
     * Atoms either consist of an ID token or a numeric literal.
     * When this method is called, if one of these two tokens
     * don't appear, in constitutes a syntax error in the input
     * source code.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed AtomNode that was parsed from the
     *         source code
     */
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
            throw new DCSyntaxErrorException(
                    tokenReader,
                    "Expected atom");
        }

        return atom;
    }

}