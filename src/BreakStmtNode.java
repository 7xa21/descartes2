import java.io.IOException;
import java.util.Stack;


/**
 * A break statement with no ID provided will exit the inner most
 * loop that's executing; otherwise it will stop the named loop
 * from executing.
 *
 * <hr/>
 * <pre>
 *     6.  stmt : break-stmt
 *        ...
 *     15. break-stmt : BREAK id-option
 * </pre>
 */
public class BreakStmtNode extends StmtNode {

    //==================//
    // Member Variables //
    //==================//

    private IDOptionNode m_idOption;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new break statement node with the (optional)
     * loop ID.
     *
     * @param idOption An IDOptionNode (which is possibly empty)
     */
    public BreakStmtNode(IDOptionNode idOption) {
        m_idOption = idOption;
    }

    /**
     * Stops the inner-most loop from executing, or stops the
     * named loop from executing if a loop ID was provided.
     *
     * @param progState The current program state (symbol table,
     *                  etc.)
     *
     * @see IDOptionNode#popLoopID(ProgState)
     */
    public void execute(ProgState progState) {
        m_idOption.popLoopID(progState);
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Returns 'true' if a break statement appears next in the
     * input source code.
     *
     * This is used by StmtNode.parseStmt() which must detect any
     * of seven possible statements (including blank/empty
     * statements).
     *
     * The tokenReader is not advanced by this method; it only
     * detects whether a break statement appears next. If 'true'
     * is returned by this method, BreakStmtNode.parseAssignStmt()
     * should be called immediately after to parse the break
     * statement.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return 'true' if a break statement appears next in the
     *         input source code
     */
    public static boolean detectBreakStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        Stack<TokenDescriptor> toReplace = new Stack<TokenDescriptor>();
        TokenDescriptor token;
        boolean detected = false;

        // Eat leading spaces.
        do {
            token = tokenReader.getToken();
            toReplace.push(token);
        } while (token.getCode() == TokenCode.T_SPACE);

        // Check for BREAK keyword.
        if (token.getCode() == TokenCode.T_BREAK) {
            detected = true;
        }

        // Put back all the tokens we just read.
        while (!toReplace.isEmpty()) {
            tokenReader.unread(toReplace.pop());
        }


        return detected;
    }

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, a break statement.
     *
     * Break statements consist of the keyword "BREAK" followed by
     * an optional loop ID that will stop executing when this
     * statement is reached.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed BreakStmtNode that was parsed from
     *         the source code
     */
    public static BreakStmtNode parseBreakStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;

        // Eat leading spaces.
        do {
            token = tokenReader.getToken();
        } while(token.getCode() == TokenCode.T_SPACE);

        //
        // GR 15.
        //
        //      break-stmt : BREAK id-option
        //

        // Ensure token is BREAK keyword.
        assert(token.getCode() == TokenCode.T_BREAK);

        // Get the subsequent id-option.
        IDOptionNode idOption = IDOptionNode.parseIDOption(tokenReader);

        return new BreakStmtNode(idOption);
    }
}
