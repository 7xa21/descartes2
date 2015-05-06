import java.io.IOException;


/**
 * An else part is the second part of an if statement.
 *
 * All if statements have else parts; if no ELSE keyword appears
 * in the source code following an if clause, the else part is
 * empty. Otherwise the else part has its own statement list that
 * will be executed by its parent IfStmtNode when the IfStmtNode
 * is executed and its conditional expression is equal to 0.0.
 *
 * <hr/>
 * <pre>
 *         ...
 *     11. if-stmt : IF expr THEN stmt-list else-part
 *     12. else-part : ELSE stmt-list FI
 *     13. else-part : FI
 *         ...
 * </pre>
 */
public class ElsePartNode {

    //==================//
    // Member Variable //
    //==================//

    private StmtListNode m_stmtList;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new else-part with the given statement list.
     *
     * @param stmtList A StmtListNode instance
     */
    public ElsePartNode(StmtListNode stmtList) {
        m_stmtList = stmtList;
    }

    /**
     * Executes the else part's statement list.
     *
     * @param progState The current program state
     */
    public void execute(ProgState progState)
            throws DCRuntimeErrorException
    {
        if (m_stmtList != null) {
            m_stmtList.execute(progState);
        }
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, an else-part node.
     *
     * Else clauses terminate ALL if statements. An explicit else
     * clause begins with the "ELSE" keyword and subsequent
     * statement list.
     *
     * If no "ELSE" keyword (and subsequent statement list)
     * follows an if statement, it is empty.
     *
     * Either way, else clauses end with the "FI" keyword.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed ElseStmtNode that was parsed from
     *         the source code
     */
    public static ElsePartNode parseElse(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        ElsePartNode elsePart;
        TokenDescriptor token;

        // Eat up spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 12.
        //
        //      else-part : ELSE stmt-list FI
        //

        if (token.getCode() == TokenCode.T_ELSE) {
            // Read the statement list for this ELSE clause.
            StmtListNode stmtList = StmtListNode.parseStmtList(tokenReader);

            // Create the populated ElsePartNode.
            elsePart = new ElsePartNode(stmtList);
        }

        //
        // GR 13.
        //
        //      else-part : FI
        //

        else {
            // If the token wasn't an ELSE keyword, put it back.
            tokenReader.unread(token);

            // Create an empty ElsePartNode.
            elsePart = new ElsePartNode(null);
        }

        // Eat any spaces before the "FI" keyword.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // Ensure a "FI" keyword ends this else-part.
        assert(token.getCode() == TokenCode.T_FI);


        return elsePart;
    }

}
