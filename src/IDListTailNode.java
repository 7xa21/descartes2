import java.io.IOException;
import java.util.Scanner;


/**
 * An id-list-tail is used with PRINT and READ statements to
 * include additional identifiers to be printed or read.
 *
 * <hr/>
 * <pre>
 *         ...
 *     19. print-stmt : PRINT ID id-list-tail
 *     20. read-stmt : READ ID id-list-tail
 *     21. id-list-tail : , ID id-list-tail
 *     22. id-list-tail :
 *         ...
 * </pre>
 */
public class IDListTailNode {

    //==================//
    // Member Variables //
    //==================//

    private String m_id;
    private IDListTailNode m_idListTail;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new id-list-tail with the provided id and
     * subsequent id-list-tail.
     *
     * If both parameters are null, an empty id-list-tail is
     * constructed, terminating a list of IDs.
     *
     * @param id A String identifier
     * @param idListTail A subsequent id-list-tail
     */
    public IDListTailNode(String id, IDListTailNode idListTail) {
        m_id = id;
        m_idListTail = idListTail;
    }

    /**
     * Used to read a value from the terminal and store it in the
     * variable identified by this id-list-tail. The program
     * state's symbol table is updated so the identified variable
     * contains the value read from the user.
     *
     * If this id-list-tail is empty, the method does nothing.
     *
     * @param progState The current program state
     */
    public void read(ProgState progState) {
        // If m_id is null, it means this IDListTailNode is empty
        // and thus its own, subsequent m_idListTail is empty.
        if (m_id != null) {
            // Read a value from the user.
            Scanner input = new Scanner(System.in);
            double num = input.nextDouble();

            // Assign the user's value to the ID.
            progState.symTab().put(m_id, num);

            // Read this id-list-tail's subsequent id-list-tail.
            m_idListTail.read(progState);
        }
    }

    /**
     * Used to print a value from the symbol table to the
     * terminal. The value is retrieved from the program state's
     * symbol table using the identifier in this id-list-tail.
     *
     * If this id-list-tail is empty, the method does nothing.
     *
     * @param progState The current program state
     */
    public void print(ProgState progState)
            throws DCRuntimeErrorException
    {
        // If m_id is null, it means this IDListTailNode is empty
        // and thus its own, subsequent m_idListTail is empty.
        if (m_id != null) {
            if (!progState.symTab().containsKey(m_id)) {
                throw new DCRuntimeErrorException(
                        "Unrecognized variable name: " + m_id
                );
            }

            // Print the value on the console.
            System.out.println(progState.symTab().get(m_id));

            // Print this id-list-tail's subsequent id-list-tail.
            m_idListTail.print(progState);
        }
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, an id-list-tail node.
     *
     * An id-list-tail occurs in a PRINT or READ statement after
     * the id parameter to those keywords. It begins with a comma;
     * if no comma is found, the id-list-tail is empty.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed IDListTailNode that was parsed from
     *         the source code
     */
    public static IDListTailNode parseIDListTail(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;
        String id;
        IDListTailNode idListTail;

        // Eat up spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 21.
        //
        //      id-list-tail : , ID id-list-tail
        //

        if (token.getCode() == TokenCode.T_COMMA) {
            // Eat up spaces after READ.
            do {
                token = tokenReader.getToken();
            } while (token.getCode() == TokenCode.T_SPACE);

            // "token" should now be the first non-space token after
            // the COMMA, which should be ID.
            if (token.getCode() != TokenCode.T_ID) {
                throw new DCSyntaxErrorException(tokenReader,
                        "Expected identifier after ','.");
            }
            id = token.getText();

            // Read the id-list-tail.
            IDListTailNode nextIDListTail;
            nextIDListTail = IDListTailNode.parseIDListTail(tokenReader);

            // Build the IDListTailNode we just parsed.
            idListTail = new IDListTailNode(id, nextIDListTail);
        }

        //
        // GR 22.
        //
        //      id-list-tail :
        //

        else {
            // The IDListTailNode is empty.
            tokenReader.unread(token);
            idListTail = new IDListTailNode(null, null);
        }

        return idListTail;
    }

}
