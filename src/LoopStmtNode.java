import java.io.IOException;
import java.util.Stack;


/**
 * A loop statement repeats a statement list until broken.
 *
 * Loops are named, and nested loops are maintained with a stack
 * of loop names. They are broken when a BREAK statement is
 * executed directly within the statement list, or when a BREAK
 * statement that explicitly identifies a loop is executed within
 * a nested loop's statement list.
 *
 * <hr/>
 * <pre>
 *         ...
 *     5.  stmt : loop-stmt
 *         ...
 *     14. loop-stmt : LOOP ID COLON stmt-list REPEAT
 *         ...
 * </pre>
 */
public class LoopStmtNode extends StmtNode {

    //==================//
    // Member Variables //
    //==================//

    private String m_id;
    private StmtListNode m_stmtList;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new loop statement with the specified id and
     * statement list.
     *
     * @param id A String that will name the loop
     * @param stmtList A statement list that will be repeated by
     *                 the loop
     */
    public LoopStmtNode(String id, StmtListNode stmtList) {
        m_id = id;
        m_stmtList = stmtList;
    }

    /**
     * Repeatedly executes a loop's statement list until the loop
     * is broken.
     *
     * Before the statement list is initially executed, the loop's
     * name is pushed to the program state's loop ID stack. The
     * loop will continue executing until its ID is no longer at
     * the top of the loop ID stack.
     *
     * @param progState The current program state
     */
    public void execute(ProgState progState)
            throws DCRuntimeErrorException
    {
        // Push this loop's ID to the stack.
        progState.loopIDStack().push(m_id);

        // Repeatedly execute the loop's stmt-list until this
        // loop's ID is no longer at the top of the loop stack.
        do {
            m_stmtList.execute(progState);
        } while (!progState.loopIDStack().isEmpty() &&
                  progState.loopIDStack().peek().equals(m_id));

        //
        // When a break statement is executed, the "break name" is
        // provided after the BREAK keyword. If no name is given,
        // the inner-most loop's name (found at the top of the
        // loop stack) is used.
        //
        // A stmt-list will only continue to execute if no break
        // name is set. At this point we've broken out of the
        // loop - but we only clear the break name if we're
        // breaking *this* loop, and not a higher one.
        //

        if (progState.breakName().equals(m_id))
            progState.setBreakName(null);
    }


    //================//
    // Static Methods //
    //================//


    /**
     * Returns 'true' if a loop statement appears next in the
     * input source code.
     *
     * This is used by StmtNode.parseStmt() which must detect any
     * of seven possible statements (including blank/empty
     * statements).
     *
     * The tokenReader is not advanced by this method; it only
     * detects whether a loop statement appears next. If 'true' is
     * returned by this method, LoopStmtNode.parseLoopStmt()
     * should be called immediately after to parse the loop
     * statement.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return 'true' if a loop statement appears next in the
     *         input source code
     */
    public static boolean detectLoopStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;
        Stack<TokenDescriptor> toReplace = new Stack<TokenDescriptor>();
        boolean detected = false;

        // Eat up leading spaces.
        do {
            token = tokenReader.getToken();
            toReplace.push(token);
        } while (token.getCode() == TokenCode.T_SPACE);

        // Look for "LOOP" keyword.
        if (token.getCode() == TokenCode.T_LOOP) {
            detected = true;
        }

        // Unread all the tokens we just read so the parse method
        // can read them.
        while (!toReplace.isEmpty()) {
            tokenReader.unread(toReplace.pop());
        }


        return detected;
    }

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, a loop statement node.
     *
     * A loop statement begins with the keyword "LOOP" followed by
     * an identifier token which names the loop and a COLON. A
     * statement list follows the colon and is terminated by the
     * REPEAT keyword.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed LoopStmtNode that was parsed from
     *         the source code
     */
    public static LoopStmtNode parseLoopStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;


        //
        // GR 14.
        //
        //      loop-stmt : LOOP ID COLON stmt-list REPEAT
        //

        // Eat up leading spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // Look for "LOOP" keyword.
        assert(token.getCode() == TokenCode.T_LOOP);

        // Whitespace must follow the loop keyword.
        token = tokenReader.getToken();
        if (token.getCode() != TokenCode.T_SPACE) {
            throw new DCSyntaxErrorException(
                    tokenReader,
                    "Expected identifier after 'LOOP'.");
        }

        // The loop ID follows.
        token = tokenReader.getToken();
        if (token.getCode() != TokenCode.T_ID) {
            throw new DCSyntaxErrorException(
                    tokenReader,
                    "Expected identifier after 'LOOP'.");
        }
        String id = token.getText();

        // Eat any spaces between the ID string and the colon.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // Look for the colon after the ID.
        if (token.getCode() != TokenCode.T_COLON) {
            throw new DCSyntaxErrorException(
                    tokenReader,
                    "Expected ':' after loop identifier.");
        }

        // Parse the statement list.
        StmtListNode stmtList = StmtListNode.parseStmtList(tokenReader);

        // Eat up spaces between the statement list and the
        // "REPEAT" keyword.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // Look for "REPEAT" keyword.
        if (token.getCode() != TokenCode.T_REPEAT) {
            throw new DCSyntaxErrorException(
                    tokenReader,
                    "Expected 'REPEAT' after loop body.");
        }


        return new LoopStmtNode(id, stmtList);
    }

}
