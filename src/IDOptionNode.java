import java.io.IOException;


/**
 * An id-option is an optional modifier of a BREAK statement. It
 * provides the name of the loop that will be broken.
 *
 * <hr/>
 * <pre>
 *         ...
 *     15. break-stmt : BREAK id-option
 *     16. id-option : ID
 *     17. id-option :
 *         ...
 * </pre>
 */
public class IDOptionNode {

    //==================//
    // Member Variables //
    //==================//

    private String m_id;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new id-option using the specified loop ID.
     *
     * @param id A String that identifies an active loop statement
     */
    public IDOptionNode(String id) {
        m_id = id;
    }

    /**
     * Effectively executes a break statement by popping the loop
     * with this node's ID off the loop stack in the program
     * state and setting the current break name to that id.
     *
     * @param progState The current program state
     */
    public void popLoopID(ProgState progState) {
        String id;


        // If the ID is null, only pop the most recent loop id off
        // the stack.
        if (m_id == null) {
            assert(!progState.loopIDStack().isEmpty());
            id = progState.loopIDStack().pop();
        }

        // If the ID isn't null, keep popping until we pop the ID
        // off the stack.
        else {
            do {
                assert(!progState.loopIDStack().isEmpty());
                id = progState.loopIDStack().pop();
            } while (!id.equals(m_id));
        }

        // Set the break name to ensure stmt-tails don't continue
        // to execute. This causes any execute() methods to return
        // back up to the loop with the break name; it will clear
        // the break name and return to its parent stmt-tail,
        // which will proceed to execute statements following the
        // loop.
        progState.setBreakName(id);
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, an id option node.
     *
     * An id-option identifies a currently active loop. It
     * optionally appears after a BREAK keyword. If no identifier
     * token is found after a BREAK keyword, the id-option is
     * empty.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed IDOptionNode that was parsed from
     *         the source code
     */
    public static IDOptionNode parseIDOption(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;
        IDOptionNode idOption;

        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 16.
        //
        //      id-option : ID
        //

        if (token.getCode() == TokenCode.T_ID) {
            idOption = new IDOptionNode(token.getText());
        }

        //
        // GR 17.
        //
        //      id-option :
        //

        else {
            // The token wasn't an ID, so put it back.
            tokenReader.unread(token);

            // Create an empty id-option.
            idOption = new IDOptionNode(null);
        }


        return idOption;
    }

}
