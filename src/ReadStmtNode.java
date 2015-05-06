import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;


/**
 * Read statements are used to collect numeric literals from the
 * user on the console.
 *
 * <hr/>
 * <pre>
 *         ...
 *     8.  stmt : read-stmt
 *         ...
 *     20. read-stmt : READ ID id-list-tail
 *         ...
 * </pre>
 */
public class ReadStmtNode extends StmtNode {

    //==================//
    // Member Variables //
    //==================//

    private String m_id;
    private IDListTailNode m_idListTail;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new read statement node with the specified
     * variable identifier and (optionally) subsequent variable
     * identifiers.
     *
     * @param id The first variable identifier that will be read
     *           from the user
     * @param idListTail A list of zero or more additional
     *                   variable identifiers that will be read
     *                   after 'id'
     */
    public ReadStmtNode(String id, IDListTailNode idListTail) {
        m_id = id;
        m_idListTail = idListTail;
    }

    /**
     * For each identifier specified, reads a numeric literal from
     * the user on the console and updates the value of the
     * variable in the program's symbol table.
     *
     * If the variable isn't already in the symbol table it will
     * be added.
     *
     * @param progState The current program state
     */
    public void execute(ProgState progState) {
        Scanner input = new Scanner(System.in);
        double num = input.nextDouble();

        // Assign the user's value to the ID.
        progState.symTab().put(m_id, num);

        m_idListTail.read(progState);
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Returns 'true' if a read statement appears next in the
     * input source code.
     *
     * This is used by StmtNode.parseStmt() which must detect any
     * of seven possible statements (including blank/empty
     * statements).
     *
     * The tokenReader is not advanced by this method; it only
     * detects whether a read statement appears next. If 'true' is
     * returned by this method, ReadStmtNode.parseReadStmt()
     * should be called immediately after to parse the read
     * statement.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return 'true' if a read statement appears next in the
     *         input source code
     */
    public static boolean detectReadStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        // Keep a stack of tokens read. If we DON'T find
        // READ, we unread() all the tokens back
        // to the TokenReader.
        Stack<TokenDescriptor> toReplace = new Stack<TokenDescriptor>();
        boolean detected = false;
        TokenDescriptor token;

        // Eat up spaces.
        do {
            token = tokenReader.getToken();
            toReplace.push(token);
        } while (token.getCode() == TokenCode.T_SPACE);

        // Look for a READ token.
        if (token.getCode() == TokenCode.T_READ) {
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
     * into, and returns, a read statement node.
     *
     * A read statement consists of the keyword "READ" followed by
     * a comma-delimited list of one or more variable identifiers.
     *
     * When this method is called, a read statement is imminently
     * expected to appear in the input source code.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed ReadStmtNode that was parsed from
     *         the source code
     */
    public static ReadStmtNode parseReadStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 20.
        //
        //		read-stmt : READ ID id-list-tail
        //

        TokenDescriptor token;
        String id;

        // Eat up spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        assert(token.getCode() == TokenCode.T_READ);

        // Eat up spaces after READ.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // "token" should now be the first non-space token after
        // the ID, which should be ID.
        if (token.getCode() != TokenCode.T_ID) {
            throw new DCSyntaxErrorException(tokenReader,
                    "Expected identifier after 'READ'.");
        }
        id = token.getText();

        // Read the id-list-tail.
        IDListTailNode idListTail =
                IDListTailNode.parseIDListTail(tokenReader);


        return new ReadStmtNode(id, idListTail);
    }

}
