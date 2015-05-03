import java.io.IOException;
import java.util.Stack;


/**
 * A statement tail follows every statement in a Descartes 2
 * program.
 *
 * If a statement is followed by a semicolon, then it is also
 * followed by a statement and a subsequent statement tail. If no
 * semicolon is found, the statement tail is empty.
 *
 * Note that since both StmtNode and StmtTailNode instances may be
 * empty, which occurs when a semicolon appears but isn't followed
 * by any statement.
 *
 * <pre>
 *     2. stmt-tail : SEMICOLON stmt stmt-tail
 *     3. stmt-tail :
 * </pre>
 */
public class StmtTailNode {

    //==================//
    // Member Variables //
    //==================//

    private StmtNode m_stmt;
    private StmtTailNode m_stmtTail;


    //=========//
    // Methods //
    //=========//

    /**
     * Builds a new statement tail.
     *
     * If no semicolon is found, then no statement or subsequent
     * statement tail will follow this statement tail, and those
     * parameters should be null.
     *
     *
     * @param stmt The statement following the semicolon in this
     *             statement tail; may be null only if no semicolon
     * @param stmtTail The subsequent statement tail which follows
     *                 the
     */
    public StmtTailNode(StmtNode stmt, StmtTailNode stmtTail) {
        m_stmt = stmt;
        m_stmtTail = stmtTail;
    }

    /**
     * Executes the statement and subsequent statement tail in
     * this statement tail.
     *
     * @param progState The current program state
     *
     * @throws DCRuntimeErrorException Thrown in the event of a
     *         runtime error (for instance, if a division by zero
     *         occurs)
     */
    public void execute(ProgState progState)
            throws DCRuntimeErrorException
    {
        if (m_stmt != null && progState.breakName() == null) {
            // Execute child stmt.
            m_stmt.execute(progState);

            // Execute child stmt-tail unless the preceding
            // statement was a "BREAK", in which case the break
            // flag will be set.
            m_stmtTail.execute(progState);
        }
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into and returns a statement tail.
     *
     * Statement tails appear at the end of statement lists and at
     * the end of other (non-empty) statement tails. When this
     * method is called a statement tail is imminently expected.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed StmtTailNode that was parsed from
     *         the source code
     *
     * @throws IOException Thrown if an error occurs while reading
     *         the source code file
     * @throws DCSyntaxErrorException Thrown if a syntax error
     *         appears in the source code
     */
    public static StmtTailNode parseStmtTail(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        Stack<TokenDescriptor> toReplace = new Stack<TokenDescriptor>();
        StmtTailNode stmtTail;
        TokenDescriptor token;

        // Eat leading spaces.
        do {
            token = tokenReader.getToken();
            toReplace.push(token);
        } while (token.getCode() == TokenCode.T_SPACE);


        //
        // GR 2:
        //
        // 		stmt-tail : SEMICOLON stmt stmt-tail
        //

        // Look for semicolon.
        if (token.getCode() == TokenCode.T_SEMICOLON) {
            // After semicolon, get "stmt" and "stmt-tail".
            StmtNode stmt = StmtNode.parseStmt(tokenReader);
            StmtTailNode nextStmtTail =
                    StmtTailNode.parseStmtTail(tokenReader);

            stmtTail = new StmtTailNode(stmt, nextStmtTail);
        }

        //
        // GR 3:
        //
        // 		stmt-tail :
        //

        // If no semicolon, the statement tail is blank.
        else {
            // Unread all the tokens we just read.
            while (!toReplace.isEmpty()) {
                tokenReader.unread(toReplace.pop());
            }
            stmtTail = new StmtTailNode(null, null);
        }


        return stmtTail;
    }
}
