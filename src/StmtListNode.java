import java.io.IOException;


/**
 * A statement list constitutes a descartes program (ProgNode),
 * if-then clauses (IfStmtNode), else clauses (ElsePartNode), and
 * loop bodies (LoopNode).
 *
 * <pre>
 *     1. stmt-list : stmt stmt-tail
 * </pre>
 */
public class StmtListNode {
    //==================//
    // Member Variables //
    //==================//

    private StmtNode m_stmt;
    private StmtTailNode m_stmtTail;


    //=========//
    // Methods //
    //=========//

    /**
     * Builds a new statement list.
     *
     * Statement lists consist of a statement and a statement
     * tail.
     *
     * @param stmt The first statement in the statement list
     * @param stmtTail Contains the statement that follows the
     *                 first statement in the statement list
     */
    public StmtListNode(StmtNode stmt, StmtTailNode stmtTail) {
        m_stmt = stmt;
        m_stmtTail = stmtTail;
    }

    /**
     * Executes the first statement in the statement list and its
     * statement tail (which executes the next statement, etc.)
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
        // Execute child stmt.
        m_stmt.execute(progState);

        // Execute child stmt-tail.
        m_stmtTail.execute(progState);
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into and returns a statement list.
     *
     * Statement lists appear in the bodies of programs, if-then
     * clauses, else clauses and loop bodies. When this method is
     * called a statement list is imminently expected.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed StmtListNode that was parsed from
     *         the source code
     *
     * @throws IOException Thrown if an error occurs while reading
     *         the source code file
     * @throws DCSyntaxErrorException Thrown if a syntax error
     *         appears in the source code
     */
    public static StmtListNode parseStmtList(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 1:
        //
        //      stmt-list : stmt stmt-tail
        //

        // Read "stmt" and "stmt-tail".
        StmtNode stmt = StmtNode.parseStmt(tokenReader);
        StmtTailNode stmtTail = StmtTailNode.parseStmtTail(tokenReader);


        return new StmtListNode(stmt, stmtTail);
    }
}

// (eof)
