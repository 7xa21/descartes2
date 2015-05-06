import java.io.IOException;
import java.util.Stack;


/**
 * A print statement is used to print the value of a variable on
 * the console.
 *
 * <hr/>
 * <pre>
 *         ...
 *     9.  stmt : print-stmt
 *         ...
 *     19. print-stmt : PRINT ID id-list-tail
 *         ...
 * </pre>
 */
public class PrintStmtNode extends StmtNode{


    //================//
    //Member Variables//
    //================//

    private String m_id;
    private IDListTailNode m_idListTail;


    //========//
    //Methods//
    //=======//

    /**
     * Constructs a new print statement using the provided
     * variable identifier and id-list-tail.
     *
     * @param id A String identifying a variable in the program
     *           symbol table
     * @param idListTail An IDListTailNode instance
     */
    public PrintStmtNode (String id, IDListTailNode idListTail){
        m_id = id;
        m_idListTail = idListTail;
    }

    /**
     *
     * @param progState The current program state (symbol table,
     * @throws DCRuntimeErrorException
     */
    public void execute(ProgState progState)
            throws DCRuntimeErrorException
    {
        if (!progState.symTab().containsKey(m_id)) {
            throw new DCRuntimeErrorException(
                    "Unrecognized variable name: " + m_id
            );
        }
        System.out.println(progState.symTab().get(m_id));
        m_idListTail.print(progState);
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Returns 'true' if a print statement appears next in the
     * input source code.
     *
     * This is used by StmtNode.parseStmt() which must detect any
     * of seven possible statements (including blank/empty
     * statements).
     *
     * The tokenReader is not advanced by this method; it only
     * detects whether a print statement appears next. If 'true'
     * is returned by this method, PrintStmtNode.parsePrintStmt()
     * should be called immediately after to parse the print
     * statement.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return 'true' if a print statement appears next in the
     *         input source code
     */
    public static boolean detectPrintStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        // Keep a stack of tokens read. If we DON'T find a
        // PRINT, we unread() all the tokens back to the
        // TokenReader.
        Stack<TokenDescriptor> toReplace = new Stack<TokenDescriptor>();
        boolean detected = false;
        TokenDescriptor token;

        // Eat up spaces.
        do {
            token = tokenReader.getToken();
            toReplace.push(token);
        } while (token.getCode() == TokenCode.T_SPACE);

        // Look for a PRINT token.
        if (token.getCode() == TokenCode.T_PRINT) {
            detected = true;
        }

        // unread() all the tokens we just read.
        while (!toReplace.empty()) {
            tokenReader.unread(toReplace.pop());
        }

        return detected;
    }

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, a print statement node.
     *
     * A print statement consists of the keyword "PRINT" followed
     * by a comma-delimited list of one or more variable
     * identifiers.
     *
     * When this method is called, a read statement is imminently
     * expected to appear in the input source code.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed PrintStmtNode that was parsed from
     *         the source code
     */
    public static PrintStmtNode parsePrintStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 19.
        //
        //      print-stmt : PRINT ID id-list-tail
        //
        //

        TokenDescriptor token;
        String id;


        // Eat up spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        assert(token.getCode() == TokenCode.T_PRINT);

        // Eat up spaces after PRINT.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // "token" should now be the first non-space token after
        // PRINT, which should be ID.
        if (token.getCode() != TokenCode.T_ID) {
            throw new DCSyntaxErrorException(tokenReader,
                    "Expected identifier after 'PRINT'.");
        }
        id = token.getText();

        // Read the id-list-tail.
        IDListTailNode idListTail =
                IDListTailNode.parseIDListTail(tokenReader);


        return new PrintStmtNode(id, idListTail);
    }

}
